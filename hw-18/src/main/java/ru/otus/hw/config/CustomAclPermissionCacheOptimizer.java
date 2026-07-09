package ru.otus.hw.config;

import org.springframework.security.acls.AclPermissionCacheOptimizer;
import org.springframework.security.acls.model.AclService;
import org.springframework.security.core.Authentication;
import ru.otus.hw.dto.BookDto;

import java.util.Collection;
import java.util.stream.Collectors;

public class CustomAclPermissionCacheOptimizer extends AclPermissionCacheOptimizer {

    public CustomAclPermissionCacheOptimizer(AclService aclService) {
        super(aclService);
    }

    @Override
    public void cachePermissionsFor(Authentication authentication, Collection<?> objects) {
        Collection<?> filtered = objects.stream()
            .filter(obj -> !(obj instanceof BookDto))
            .collect(Collectors.toList());

        if (!filtered.isEmpty()) {
            super.cachePermissionsFor(authentication, filtered);
        }
    }
}
