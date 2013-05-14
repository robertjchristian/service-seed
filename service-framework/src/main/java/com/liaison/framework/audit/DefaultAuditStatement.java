package com.liaison.framework.audit;

public class DefaultAuditStatement implements AuditStatement {

    private final AuditStandardsRequirement requirement;
    private final Status status;
    private final String message;

    public DefaultAuditStatement(AuditStandardsRequirement requirement, Status status, String message) {
        this.requirement = requirement;
        this.status = status;
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    public Status getStatus() {
        return this.status;
    }

    public AuditStandardsRequirement getAuditStandardsRequirement() {
        return this.getAuditStandardsRequirement();
    }

}
