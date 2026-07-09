package ru.otus.hw.services.security;

import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.model.Sid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class BookAclServiceImpl implements BookAclService {

    private final MutableAclService mutableAclService;

    public BookAclServiceImpl(MutableAclService mutableAclService) {
        this.mutableAclService = mutableAclService;
    }

    @Override
    public void createPermission(Object object, Permission permission) {
        ObjectIdentity oid = new ObjectIdentityImpl(object);
        MutableAcl acl = mutableAclService.createAcl(oid);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        if (!(authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN")) ||
            authorities.contains(new SimpleGrantedAuthority("ROLE_MANAGER")))) {
            final Sid owner = new PrincipalSid(authentication);
            acl.insertAce(acl.getEntries().size(), permission, owner, true);
        }

        final Sid admin = new GrantedAuthoritySid("ROLE_ADMIN");
        final Sid manager = new GrantedAuthoritySid("ROLE_MANAGER");

        acl.insertAce(acl.getEntries().size(), permission, manager, true);
        acl.insertAce(acl.getEntries().size(), permission, admin, true);
        mutableAclService.updateAcl(acl);
    }

    @Override
    public void deletePermissions(Object object) {
        ObjectIdentity oid = new ObjectIdentityImpl(object);
        mutableAclService.deleteAcl(oid, false);
    }
}
