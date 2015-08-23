package com.ozay.service;

import com.ozay.model.OrganizationPermission;
import com.ozay.repository.OrganizationPermissionRepository;
import com.ozay.web.rest.dto.OrganizationUserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class OrganizationService {

    private final Logger log = LoggerFactory.getLogger(RoleService.class);

    @Inject
    OrganizationPermissionRepository organizationPermissionRepository;

    @Transactional
    public void updateOrganizationPermission(OrganizationUserDTO organizationUser) {
        OrganizationPermission organizationPermission = new OrganizationPermission();
        organizationPermission.setOrganizationId(organizationUser.getOrganizationId());
        organizationPermission.setUserId(organizationUser.getUserId());
        //Delete ALL Organization Permission
        organizationPermissionRepository.deleteALL(organizationPermission);
        //Update Organization Permission
        for(String orgPermission : organizationUser.getRoles()){
            organizationPermission.setName(orgPermission);
            organizationPermissionRepository.create(organizationPermission);
        }
    }
}
