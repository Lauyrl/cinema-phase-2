package vn.edu.uet.cinema.persistence.audit;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;
import vn.edu.uet.cinema.business.audit.AuditAction;
import vn.edu.uet.cinema.business.audit.AuditLog;
import vn.edu.uet.cinema.business.audit.AuditTargetType;

@Entity
@Table(name = "audit_log")
class AuditLogEntity {

    @Id
    private UUID id;

    @Column(name = "actor_id", nullable = false)
    private UUID actorId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private AuditAction action;

    @Enumerated(EnumType.STRING)
    @Column(name = "target_type", nullable = false, length = 30)
    private AuditTargetType targetType;

    @Column(name = "target_id", nullable = false)
    private UUID targetId;

    @Column(nullable = false, columnDefinition = "text")
    private String summary;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    protected AuditLogEntity() {
    }

    static AuditLogEntity from(AuditLog log) {
        AuditLogEntity entity = new AuditLogEntity();
        entity.id = log.id();
        entity.actorId = log.actorId();
        entity.action = log.action();
        entity.targetType = log.targetType();
        entity.targetId = log.targetId();
        entity.summary = log.summary();
        entity.createdAt = log.createdAt();
        return entity;
    }

    AuditLog toAuditLog() {
        return new AuditLog(id, actorId, action, targetType, targetId, summary, createdAt);
    }
}
