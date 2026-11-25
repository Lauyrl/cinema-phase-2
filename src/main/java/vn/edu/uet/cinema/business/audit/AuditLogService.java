package vn.edu.uet.cinema.business.audit;

import java.time.Instant;
import java.util.UUID;
import vn.edu.uet.cinema.business.PageResult;

public class AuditLogService {

    private final AuditLogRepository logs;

    public AuditLogService(AuditLogRepository logs) {
        this.logs = logs;
    }

    public AuditLog record(UUID actorId, AuditAction action, AuditTargetType targetType, UUID targetId, String summary) {
        AuditLog entry = new AuditLog(UUID.randomUUID(), actorId, action, targetType, targetId, summary, Instant.now());
        return logs.save(entry);
    }

    public PageResult<AuditLog> list(int page, int size) {
        return logs.findAll(PageResult.normalizePage(page), PageResult.normalizeSize(size));
    }
}
