package com.projects.helpers;

import com.projects.model.domain.constant.*;
import com.projects.model.domain.dto.*;

import java.time.LocalDateTime;
import java.util.Collections;

public class Dummies {

    public static Check getDummyCheck(long id, long employeeId) {
        return new Check.Builder()
                .id(id)
                .date(LocalDateTime.now())
                .employee(getDummyEmployee(employeeId))
                .products(Collections.emptyList())
                .status(CheckStatus.CLOSED)
                .sum(500.45)
                .build();
    }

    public static Employee getDummyEmployee(long id) {
        return new Employee.Builder()
                .id(id)
                .firstName("First name")
                .lastName("Last name")
                .email("test@email.com")
                .position(Position.CASHIER)
                .salary(350.)
                .build();
    }

    public static User getDummyUser(long id, long employeeId) {
        return new User(id, "userLogin", "userPass", Role.USER, getDummyEmployee(employeeId));
    }

    public static Report getDummyReport(long id) {
        return new Report.Builder()
                .id(id)
                .since(LocalDateTime.now())
                .until(LocalDateTime.now())
                .checks(Collections.emptyList())
                .totalSum(1500.)
                .type(ReportType.MIXED_CHECKS)
                .creationDate(LocalDateTime.now())
                .build();
    }

    public static Product getDummyProduct(long id) {
        return new Product.Builder()
                .id(id)
                .title("someTitle")
                .code(57759L)
                .price(2.5)
                .quantityType(QuantityType.GRAM)
                .quantityOnStock(5000)
                .boughtQuantity(3)
                .image(new byte[0])
                .build();
    }
}
