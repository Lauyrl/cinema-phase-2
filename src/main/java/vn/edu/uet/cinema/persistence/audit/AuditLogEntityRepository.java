package vn.edu.uet.cinema.persistence.audit;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

interface AuditLogEntityRepository extends JpaRepository<AuditLogEntity, UUID> {
}
