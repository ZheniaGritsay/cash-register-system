package com.projects.model.domain.dto;

import com.projects.model.domain.Entity;
import com.projects.model.domain.constant.QuantityType;

public class Product extends Entity {
    private final String title;
    private final Long code;
    private final Double price;
    private final QuantityType quantityType;
    private final Integer boughtQuantity;
    private final Integer quantityOnStock;
    private byte[] image;

    public Product(Builder builder) {
        super(builder.id);
        this.title = builder.title;
        this.code = builder.code;
        this.price = builder.price;
        this.quantityType = builder.quantityType;
        this.boughtQuantity = builder.boughtQuantity;
        this.quantityOnStock = builder.quantityOnStock;
        this.image = builder.image;
    }

    public Product(Long id, String title, Long code, Double price, QuantityType quantityType, Integer boughtQuantity, Integer quantityOnStock, byte[] image) {
        super(id);
        this.title = title;
        this.code = code;
        this.price = price;
        this.quantityType = quantityType;
        this.boughtQuantity = boughtQuantity;
        this.quantityOnStock = quantityOnStock;
        this.image = image;
    }

    public static class Builder {
        private Long id;
        private String title;
        private Long code;
        private Double price;
        private QuantityType quantityType;
        private Integer boughtQuantity;
        private Integer quantityOnStock;
        private byte[] image;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder code(Long code) {
            this.code = code;
            return this;
        }

        public Builder price(Double price) {
            this.price = price;
            return this;
        }

        public Builder quantityType(QuantityType quantityType) {
            this.quantityType = quantityType;
            return this;
        }

        public Builder boughtQuantity(Integer boughtQuantity) {
            this.boughtQuantity = boughtQuantity;
            return this;
        }

        public Builder quantityOnStock(Integer quantityOnStock) {
            this.quantityOnStock = quantityOnStock;
            return this;
        }

        public Builder image(byte[] image) {
            this.image = image;
            return this;
        }

        public Product build() {
            return new Product(this);
        }
    }

    public String getTitle() {
        return title;
    }

    public Long getCode() {
        return code;
    }

    public Double getPrice() {
        return price;
    }

    public QuantityType getQuantityType() {
        return quantityType;
    }

    public Integer getBoughtQuantity() {
        return boughtQuantity;
    }

    public Integer getQuantityOnStock() {
        return quantityOnStock;
    }

    public byte[] getImage() {
        return image;
    }
}