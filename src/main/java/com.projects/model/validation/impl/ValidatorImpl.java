package com.projects.model.validation.impl;

import com.projects.model.validation.Validator;
import com.projects.model.validation.Violation;
import com.projects.model.validation.annotation.*;
import com.projects.model.validation.exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class ValidatorImpl implements Validator {
    private static final Logger logger = LoggerFactory.getLogger(ValidatorImpl.class);
    private static final String METHOD_PREFIX_GET = "get";
    private static final Class[] validationAnnotations = new Class[]{
            AssertFalse.class, AssertTrue.class, DecimalMax.class, DecimalMin.class,
            Max.class, Min.class, NotNull.class, Pattern.class, Size.class
    };

    @Override
    public Set<Violation> validate(Object object) throws ValidationException {
        if (object == null) throw new IllegalArgumentException();

        Class clazz = object.getClass();
        Set<Violation> violations = new HashSet<>();

        Map<Field, Set<Annotation>> fields = parseFields(object);
        for (Map.Entry<Field, Set<Annotation>> entry : fields.entrySet()) {
            String methodName = getMethodName(entry.getKey());
            Method method;
            Object fieldVal;
            try {
                method = clazz.getMethod(methodName, (Class<?>[]) null);
                fieldVal = method.invoke(object, (Object[]) null);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                logger.error("failed to handle a method", e);
                throw new ValidationException("unable to handle method: " + e.getMessage());
            }

            for (Annotation a : entry.getValue()) {
                List<Method> attrs = getAnnotationAttributes(a);
                boolean conform = fitsToAttributes(a, attrs, fieldVal);
                if (!conform) {
                    String message = (String) getAttributeValue(a, "message");
                    String field = entry.getKey().getName();
                    violations.add(new ViolationImpl(field, message));
                }
            }
        }

        return violations;
    }

    private Object getAttributeValue(Annotation annotation, String attribute) throws ValidationException {
        Object retVal;
        Method method;
        try {
            method = annotation.annotationType().getMethod(attribute, (Class<?>[]) null);
            retVal = method.invoke(annotation, (Object[]) null);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            logger.error("failed to get an attribute value", e);
            throw new ValidationException("unable to get an attribute value: " + e.getMessage());
        }

        return retVal;
    }

    private List<Method> getAnnotationAttributes(Annotation annotation) {
        Class objectClass = Object.class;
        Method[] objectMethods = objectClass.getMethods();
        List<Method> annotationAttributes = new ArrayList<>();

        for (int i = 0; i < annotation.annotationType().getMethods().length; i++) {
            Method m = annotation.annotationType().getMethods()[i];
            boolean contains = false;
            for (Method objectMethod : objectMethods) {
                if (m.getName().equals(objectMethod.getName())) {
                    contains = true;
                    break;
                }
            }
            if (!contains)
                annotationAttributes.add(m);
        }

        annotationAttributes = annotationAttributes.stream()
                .filter(m -> !"message".equals(m.getName()) && !"annotationType".equals(m.getName()))
                .collect(Collectors.toList());

        return annotationAttributes;
    }

    private boolean fitsToAttributes(Annotation annotation, List<Method> attributes, Object value) throws ValidationException {
        if (attributes.isEmpty()) {
            if (NotNull.class == annotation.annotationType())
                return value == null;
            else if (AssertFalse.class == annotation.annotationType())
                return !((Boolean) value);
            else if (AssertTrue.class == annotation.annotationType())
                return (Boolean) value;
        }

        int counter = 0;
        for (Method m : attributes) {
            if (value instanceof Number) {
                if (handleNumeric(annotation, m, value))
                    counter++;
            } else if (value instanceof String) {
                if (handleString(annotation, m, value))
                    counter++;
            } else {
                throw new ValidationException("unsupported type: " + value.getClass().getTypeName());
            }

        }

        return counter == attributes.size();
    }

    private boolean handleNumeric(Annotation annotation, Method attribute, Object value) throws ValidationException {
        boolean result = false;
        Object attributeVal = getAttributeValue(annotation, attribute.getName());
        boolean numericDecimal = annotation.annotationType().getTypeName().contains("Decimal");
        boolean maxComparison = annotation.annotationType().getTypeName().contains("Max");

        if (maxComparison) {
            if (numericDecimal) {
                if (((Number) value).doubleValue() <= ((Number) attributeVal).doubleValue())
                    result = true;
            } else {
                if (((Number) value).longValue() <= ((Number) attributeVal).longValue())
                    result = true;
            }
        } else {
            if (numericDecimal) {
                if (((Number) value).doubleValue() >= ((Number) attributeVal).doubleValue())
                    result = true;
            } else {
                if (((Number) value).longValue() >= ((Number) attributeVal).longValue())
                    result = true;
            }
        }
        return result;
    }

    private boolean handleString(Annotation annotation, Method attribute, Object value) throws ValidationException {
        boolean result = false;
        Object attributeVal = getAttributeValue(annotation, attribute.getName());
        if ("max".equals(attribute.getName())) {
            if (((String) value).length() < ((Number) attributeVal).intValue())
                result = true;
        } else if ("min".equals(attribute.getName())) {
            if (((String) value).length() > ((Number) attributeVal).intValue())
                result = true;
        } else if ("regex".equals(attribute.getName())) {
            if (((String) value).matches((String) attributeVal))
                result = true;
        }
        return result;
    }

    private Map<Field, Set<Annotation>> parseFields(Object object) {
        Map<Field, Set<Annotation>> fields = new HashMap<>();
        Set<Annotation> annotations;
        for (Field field : object.getClass().getDeclaredFields()) {
            annotations = getValidationAnnotations(field);
            if (!annotations.isEmpty())
                fields.put(field, annotations);
        }
        return fields;
    }

    private Set<Annotation> getValidationAnnotations(Field field) {
        Set<Annotation> annotations = new HashSet<>();
        for (Annotation annotation : field.getAnnotations()) {
            if (isValidationAnnotation(annotation))
                annotations.add(annotation);
        }
        return annotations;
    }

    private boolean isValidationAnnotation(Annotation annotation) {
        for (Class c : validationAnnotations) {
            if (c == annotation.annotationType())
                return true;
        }
        return false;
    }

    private String getMethodName(Field field) {
        String retVal = field.getName();
        char first = retVal.charAt(0);
        retVal = METHOD_PREFIX_GET + String.valueOf(first).toUpperCase() + retVal.substring(1);
        return retVal;
    }
}
