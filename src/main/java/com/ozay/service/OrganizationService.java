package com.ozay.service;

import com.ozay.model.*;
import com.ozay.repository.MemberRepository;
import com.ozay.repository.OrganizationAccessRepository;
import com.ozay.repository.OrganizationRepository;
import com.ozay.repository.RoleRepository;
import com.ozay.web.rest.dto.MemberListDTO;
import com.ozay.web.rest.dto.OrganizationUserDTO;
import com.ozay.web.rest.dto.OrganizationUserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class OrganizationService {

    private final Logger log = LoggerFactory.getLogger(RoleService.class);

    @Inject
    OrganizationAccessRepository organizationAccessRepository;

    //@Inject
    //private OrganizationPermission organizationPermission;
    @Transactional
    public void updateOrganizationPermission(OrganizationUserDTO organizationUser) {
        OrganizationPermission organizationPermission = new OrganizationPermission();
        organizationPermission.setOrganizationId(organizationUser.getOrganizationId());
        organizationPermission.setUserId(organizationUser.getUserId());
        //Delete ALL Organization Permission
        organizationAccessRepository.deleteALL(organizationPermission);
        //Update Organization Permission
        for(String orgPermission : organizationUser.getRoles()){
            organizationPermission.setName(orgPermission);
            organizationAccessRepository.create(organizationPermission);
        }
    }
}
