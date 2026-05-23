CREATE TABLE IF NOT EXISTS acl_sid (
                                       id BIGINT NOT NULL AUTO_INCREMENT,
                                       principal TINYINT NOT NULL,
                                       sid VARCHAR(100) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT unique_uk_1 UNIQUE (sid, principal)
    );

CREATE TABLE IF NOT EXISTS acl_class (
                                         id BIGINT NOT NULL AUTO_INCREMENT,
                                         class VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT unique_uk_2 UNIQUE (class)
    );

CREATE TABLE IF NOT EXISTS acl_object_identity (
                                                   id BIGINT NOT NULL AUTO_INCREMENT,
                                                   object_id_class BIGINT NOT NULL,
                                                   object_id_identity BIGINT NOT NULL,
                                                   parent_object BIGINT DEFAULT NULL,
                                                   owner_sid BIGINT DEFAULT NULL,
                                                   entries_inheriting TINYINT NOT NULL,
                                                   PRIMARY KEY (id),
    CONSTRAINT unique_uk_3 UNIQUE (object_id_class, object_id_identity)
    );

CREATE TABLE IF NOT EXISTS acl_entry (
                                         id BIGINT NOT NULL AUTO_INCREMENT,
                                         acl_object_identity BIGINT NOT NULL,
                                         ace_order INT NOT NULL,
                                         sid BIGINT NOT NULL,
                                         mask INT NOT NULL,
                                         granting TINYINT NOT NULL,
                                         audit_success TINYINT NOT NULL,
                                         audit_failure TINYINT NOT NULL,
                                         PRIMARY KEY (id),
    CONSTRAINT unique_uk_4 UNIQUE (acl_object_identity, ace_order)
    );

ALTER TABLE acl_entry
    ADD FOREIGN KEY (acl_object_identity)
        REFERENCES acl_object_identity(id) ON DELETE CASCADE;

ALTER TABLE acl_entry
    ADD FOREIGN KEY (sid)
        REFERENCES acl_sid(id) ON DELETE CASCADE;

ALTER TABLE acl_object_identity
    ADD FOREIGN KEY (parent_object)
        REFERENCES acl_object_identity(id);

ALTER TABLE acl_object_identity
    ADD FOREIGN KEY (object_id_class)
        REFERENCES acl_class(id);

ALTER TABLE acl_object_identity
    ADD FOREIGN KEY (owner_sid)
        REFERENCES acl_sid(id);