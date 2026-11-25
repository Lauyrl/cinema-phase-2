package vn.edu.uet.cinema.persistence.audit;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import vn.edu.uet.cinema.business.PageResult;
import vn.edu.uet.cinema.business.audit.AuditLog;
import vn.edu.uet.cinema.business.audit.AuditLogRepository;

@Repository
class JpaAuditLogRepository implements AuditLogRepository {

    private final AuditLogEntityRepository jpa;

    JpaAuditLogRepository(AuditLogEntityRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public AuditLog save(AuditLog log) {
        return jpa.save(AuditLogEntity.from(log)).toAuditLog();
    }

    @Override
    public PageResult<AuditLog> findAll(int page, int size) {
        int zeroBasedPage = Math.max(page, 1) - 1;
        Pageable pageable = PageRequest.of(
                zeroBasedPage,
                size,
                Sort.by(Sort.Order.desc("createdAt"), Sort.Order.asc("id")));
        Page<AuditLogEntity> result = jpa.findAll(pageable);
        List<AuditLog> items = result.getContent().stream().map(AuditLogEntity::toAuditLog).toList();
        return PageResult.of(items, page, size, result.getTotalElements());
    }
}
