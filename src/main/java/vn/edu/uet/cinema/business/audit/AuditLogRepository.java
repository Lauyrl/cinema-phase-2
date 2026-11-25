package vn.edu.uet.cinema.business.audit;

import vn.edu.uet.cinema.business.PageResult;

public interface AuditLogRepository {

    AuditLog save(AuditLog log);

    PageResult<AuditLog> findAll(int page, int size);
}
