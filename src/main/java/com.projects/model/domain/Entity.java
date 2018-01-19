package com.projects.model.domain;

public abstract class Entity {
    private Long id;

    public Entity() {
    }

    public Entity(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
