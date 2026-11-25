package vn.edu.uet.cinema.api.audit;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.Instant;
import java.util.UUID;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.uet.cinema.api.PageResponse;
import vn.edu.uet.cinema.business.audit.AuditAction;
import vn.edu.uet.cinema.business.audit.AuditLog;
import vn.edu.uet.cinema.business.audit.AuditLogService;
import vn.edu.uet.cinema.business.audit.AuditTargetType;
import vn.edu.uet.cinema.config.OpenApiConfig;

@RestController
@Tag(name = "Admin Audit Logs", description = "Administrative audit trail retrieval.")
class AuditLogController {

    private final AuditLogService auditLogs;

    AuditLogController(AuditLogService auditLogs) {
        this.auditLogs = auditLogs;
    }

    @GetMapping("/api/admin/audit-logs")
    @Operation(summary = "List admin audit logs", description = "Returns a paginated list of administrative audit log entries, "
            + "newest first. Requires ADMIN.")
    @SecurityRequirement(name = OpenApiConfig.BEARER_AUTH)
    PageResponse<AuditLogResponse> list(
            @Parameter(description = "1-based page number (defaults to 1).", example = "1")
            @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Page size (defaults to 20, max 100).", example = "20")
            @RequestParam(defaultValue = "20") int size) {
        return PageResponse.from(auditLogs.list(page, size).map(AuditLogResponse::from));
    }

    record AuditLogResponse(
            @Schema(description = "Audit log entry identifier.", example = "550e8400-e29b-41d4-a716-446655440050") UUID id,
            @Schema(description = "Admin account identifier who performed the action.",
                    example = "550e8400-e29b-41d4-a716-446655440000") UUID actorId,
            @Schema(description = "Administrative action performed.", example = "CREATE_MOVIE") AuditAction action,
            @Schema(description = "Type of resource affected.", example = "MOVIE") AuditTargetType targetType,
            @Schema(description = "Identifier of the affected resource.",
                    example = "550e8400-e29b-41d4-a716-446655440002") UUID targetId,
            @Schema(description = "Human-readable summary of the change.", example = "Created movie: The Example") String summary,
            @Schema(description = "Timestamp when the action occurred.", example = "2026-05-01T12:00:00Z") Instant createdAt) {

        static AuditLogResponse from(AuditLog log) {
            return new AuditLogResponse(log.id(), log.actorId(), log.action(), log.targetType(), log.targetId(),
                    log.summary(), log.createdAt());
        }
    }
}
