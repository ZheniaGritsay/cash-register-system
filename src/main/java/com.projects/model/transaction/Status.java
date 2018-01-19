package com.projects.model.transaction;

public enum  Status {
    NO_TRANSACTION, ACTIVE, COMMITTING,
    COMMITTED, ROLLING_BACK, ROLLED_BACK
}
