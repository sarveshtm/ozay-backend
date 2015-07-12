package com.ozay.service;

import com.ozay.model.Member;
import com.ozay.model.Role;
import com.ozay.model.RolePermission;
import com.ozay.repository.MemberRepository;
import com.ozay.repository.RolePermissionRepository;
import com.ozay.repository.RoleRepository;
import com.ozay.web.rest.dto.MemberListDTO;
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
public class RoleService {

    private final Logger log = LoggerFactory.getLogger(RoleService.class);

    @Inject
    RoleRepository roleRepository;

    @Inject
    RolePermissionRepository rolePermissionRepository;

    @Transactional
    public void create(Role role){
        Long id = roleRepository.create(role);
        for(RolePermission rolePermission : role.getRolePermissions()){
            rolePermission.setRoleId(id);
            rolePermissionRepository.create(rolePermission);
        }
    }

    @Transactional
    public void update(Role role){
        roleRepository.update(role);
        List<RolePermission> rolePermissions = rolePermissionRepository.findAllByRoleId(role.getId());

        for(RolePermission currentRolePermission : rolePermissions){
            rolePermissionRepository.delete(currentRolePermission);
        }
        for(RolePermission rolePermission : role.getRolePermissions()){
                rolePermission.setRoleId(role.getId());
                rolePermissionRepository.create(rolePermission);
        }
    }

    @Transactional
    public void multiDelete(List<Role> roles){
        for(Role role : roles){
            roleRepository.delete(role);
        }
    }
}
