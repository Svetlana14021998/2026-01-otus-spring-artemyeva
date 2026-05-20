package ru.otus.hw.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.acls.AclPermissionCacheOptimizer;
import org.springframework.security.acls.AclPermissionEvaluator;
import org.springframework.security.acls.domain.AclAuthorizationStrategy;
import org.springframework.security.acls.domain.AclAuthorizationStrategyImpl;
import org.springframework.security.acls.domain.ConsoleAuditLogger;
import org.springframework.security.acls.domain.DefaultPermissionGrantingStrategy;
import org.springframework.security.acls.domain.SpringCacheBasedAclCache;
import org.springframework.security.acls.jdbc.BasicLookupStrategy;
import org.springframework.security.acls.jdbc.JdbcMutableAclService;
import org.springframework.security.acls.jdbc.LookupStrategy;
import org.springframework.security.acls.model.AclCache;
import org.springframework.security.acls.model.PermissionGrantingStrategy;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.sql.DataSource;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class AclConfig {

    @Autowired
    private DataSource dataSource;

    @Bean
    public CacheManager cacheManager() {
        ConcurrentMapCacheManager cacheManager = new ConcurrentMapCacheManager();
        cacheManager.setAllowNullValues(false);
        return cacheManager;
    }

    @Bean
    public AclCache aclCache(CacheManager cacheManager) {
        org.springframework.cache.Cache springCache = cacheManager.getCache("aclCache");
        if (springCache == null) {
            springCache = new ConcurrentMapCacheManager("aclCache").getCache("aclCache");
        }
        return new SpringCacheBasedAclCache(
            springCache,
            permissionGrantingStrategy(),
            aclAuthorizationStrategy());
    }

    @Bean
    public PermissionGrantingStrategy permissionGrantingStrategy() {
        return new DefaultPermissionGrantingStrategy(new ConsoleAuditLogger());
    }

    @Bean
    public AclAuthorizationStrategy aclAuthorizationStrategy() {
        return new AclAuthorizationStrategyImpl(new SimpleGrantedAuthority("ROLE_ADMIN_ACL"));
    }

    @Bean
    public MethodSecurityExpressionHandler defaultMethodSecurityExpressionHandler() {
        AclMethodSecurityExpressionHandler expressionHandler = new AclMethodSecurityExpressionHandler();
        AclPermissionEvaluator permissionEvaluator = new AclPermissionEvaluator(aclService());
        expressionHandler.setPermissionEvaluator(permissionEvaluator);
        expressionHandler.setPermissionCacheOptimizer(customAclPermissionCacheOptimizer());
        return expressionHandler;
    }

    @Bean
    public AclPermissionCacheOptimizer customAclPermissionCacheOptimizer() {
        return new CustomAclPermissionCacheOptimizer(aclService());
    }

    @Bean
    public LookupStrategy lookupStrategy() {
        return new BasicLookupStrategy(dataSource, aclCache(cacheManager()),
            aclAuthorizationStrategy(), new ConsoleAuditLogger());
    }

    @Bean
    public JdbcMutableAclService aclService() {
        JdbcMutableAclService aclService = new JdbcMutableAclService(
            dataSource,
            lookupStrategy(),
            aclCache(cacheManager())
        );

        aclService.setSidIdentityQuery("SELECT TOP 1 ID FROM ACL_SID ORDER BY ID DESC");
        aclService.setClassIdentityQuery("SELECT TOP 1 ID FROM ACL_CLASS ORDER BY ID DESC");

        return aclService;
    }
}
