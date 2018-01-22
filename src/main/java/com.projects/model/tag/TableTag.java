package com.projects.model.tag;

import com.projects.controller.util.i18n.Internationalization;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TableTag extends SimpleTagSupport {
    private List<Object> entities;
    private List<String> excludeFields;

    @Override
    public void doTag() throws JspException, IOException {
        JspWriter jspWriter = getJspContext().getOut();

        List<Field> fields = getFields(entities.get(0));
        jspWriter.write("<table class=\"table table-striped\">");

        jspWriter.write("<thead>");
        jspWriter.write("<tr>");
        fields.forEach(f -> {
            if (!excludeFields.isEmpty() && excludeFields.contains(f.getName()))
                return;

            try {
                jspWriter.write("<th>" + Internationalization.getText(getFieldLabel(f.getName())) + "</th>");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        jspWriter.write("</thead>");

        jspWriter.write("<tbody>");
        for (Object entity : entities) {
            fields = getFields(entity);
            jspWriter.write("<tr>");
            fields.forEach(f -> {
                if (!excludeFields.isEmpty() && excludeFields.contains(f.getName()))
                    return;

                try {
                    f.setAccessible(true);
                    jspWriter.write("<td>" + f.get(entity).toString() + "</td>");
                    f.setAccessible(false);
                } catch (IOException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            });
            jspWriter.write("</tr>");
        }
        jspWriter.write("</tbody>");

        jspWriter.write("</table>");
    }

    public void setEntities(List<Object> entities) {
        this.entities = entities;
    }

    public void setExcludeFields(List<String> excludeFields) {
        this.excludeFields = excludeFields;
    }

    private List<Field> getFields(Object entity) {
        return Arrays.asList(entity.getClass().getDeclaredFields());
    }

    private String getFieldLabel(String fieldName) {
        String label = fieldName;
        Pattern pattern = Pattern.compile("(?<=[a-z])[A-Z](?!=[a-z])");
        Matcher matcher = pattern.matcher(fieldName);

        List<Integer> indexes = new ArrayList<>();
        int index = 0;
        while (matcher.find(index)) {
            indexes.add(matcher.start());
            index = matcher.end();
        }

        for (int i = 0; i < indexes.size(); i++) {
            int offset = indexes.get(i) + i;
            String tmp = label.substring(0, offset);
            label = tmp + "." + label.substring(offset).toLowerCase();
        }

        return "label." + label;
    }
}
