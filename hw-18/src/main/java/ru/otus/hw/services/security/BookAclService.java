package ru.otus.hw.services.security;

import org.springframework.security.acls.model.Permission;

public interface BookAclService {

    void createPermission(Object object, Permission permission);

    void deletePermissions(Object object);
}
