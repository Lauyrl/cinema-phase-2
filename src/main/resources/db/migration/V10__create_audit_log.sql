CREATE TABLE audit_log (
    id          uuid         PRIMARY KEY,
    actor_id    uuid         NOT NULL CONSTRAINT fk_audit_log_actor REFERENCES account (id),
    action      varchar(40)  NOT NULL,
    target_type varchar(30)  NOT NULL,
    target_id   uuid         NOT NULL,
    summary     text         NOT NULL,
    created_at  timestamptz  NOT NULL DEFAULT now()
);

CREATE INDEX ix_audit_log_created_at ON audit_log (created_at DESC, id ASC);
CREATE INDEX ix_audit_log_actor_id ON audit_log (actor_id);
