package vn.edu.uet.cinema.business.audit;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public record AuditLog(
        UUID id,
        UUID actorId,
        AuditAction action,
        AuditTargetType targetType,
        UUID targetId,
        String summary,
        Instant createdAt) {

    public AuditLog {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(actorId, "actorId");
        Objects.requireNonNull(action, "action");
        Objects.requireNonNull(targetType, "targetType");
        Objects.requireNonNull(targetId, "targetId");
        Objects.requireNonNull(summary, "summary");
        Objects.requireNonNull(createdAt, "createdAt");
    }
}
