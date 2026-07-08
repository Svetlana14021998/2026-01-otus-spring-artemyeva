package ru.otus.hw.config;

import org.springframework.security.acls.domain.AclAuthorizationStrategy;
import org.springframework.security.acls.jdbc.BasicLookupStrategy;
import org.springframework.security.acls.model.AclCache;
import org.springframework.security.acls.model.PermissionGrantingStrategy;

import javax.sql.DataSource;

public class PostgresqlLookupStrategy extends BasicLookupStrategy {

    public PostgresqlLookupStrategy(
        DataSource dataSource,
        AclCache aclCache,
        AclAuthorizationStrategy aclAuthorizationStrategy,
        PermissionGrantingStrategy grantingStrategy) {
        super(dataSource, aclCache, aclAuthorizationStrategy, grantingStrategy);

        // Переопределяем WHERE клаузу с CAST
        this.setLookupObjectIdentitiesWhereClause(
            " (CAST(acl_object_identity.object_id_identity AS VARCHAR) = ? and acl_class.class = ?)"
        );
    }
}
