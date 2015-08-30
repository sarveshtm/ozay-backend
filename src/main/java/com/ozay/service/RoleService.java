package com.ozay.service;

import com.ozay.domain.User;
import com.ozay.model.Member;
import com.ozay.model.Role;
import com.ozay.model.RolePermission;
import com.ozay.repository.MemberRepository;
import com.ozay.repository.RoleMemberRepository;
import com.ozay.repository.RolePermissionRepository;
import com.ozay.repository.RoleRepository;
import com.ozay.web.rest.dto.OrganizationUserRoleDTO;
import com.ozay.web.rest.dto.form.RoleFormDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
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

    @Inject
    MemberRepository memberRepository;

    @Inject
    private RoleMemberRepository roleMemberRepository;

    @Transactional
    public void create(RoleFormDTO roleFormDTO){
        Long id = roleRepository.create(roleFormDTO.getRole());
        this.createRollPermissions(roleFormDTO.getRole());
        this.organizationUserUpdate(roleFormDTO);
    }

    @Transactional
    public void update(RoleFormDTO roleFormDTO){
        roleRepository.update(roleFormDTO.getRole());
        List<RolePermission> rolePermissions = rolePermissionRepository.findAllByRoleId(roleFormDTO.getRole().getId());

        for(RolePermission currentRolePermission : rolePermissions){
            rolePermissionRepository.delete(currentRolePermission);
        }
        this.createRollPermissions(roleFormDTO.getRole());

        this.organizationUserUpdate(roleFormDTO);
    }

    private void createRollPermissions(Role role){
        for(RolePermission rolePermission : role.getRolePermissions()){
            rolePermission.setRoleId(role.getId());
            rolePermissionRepository.create(rolePermission);
        }
    }

    private void organizationUserUpdate(RoleFormDTO roleFormDTO){
        Role role = roleFormDTO.getRole();
        List<OrganizationUserRoleDTO> organizationUserRoleDTOs = roleFormDTO.getOrganizationUserRoleDTOs();


        for(OrganizationUserRoleDTO organizationUserRoleDTO : organizationUserRoleDTOs){
            Member member = memberRepository.findOneByUserIdAndBuildingId(organizationUserRoleDTO.getUserId(), role.getBuildingId());
            if(organizationUserRoleDTO.isAssigned() == true){
                if(member == null){ // If null there is no member record
                    member = new Member();
                    member.setUserId(organizationUserRoleDTO.getUserId());
                    member.setFirstName(organizationUserRoleDTO.getFirstName());
                    member.setLastName(organizationUserRoleDTO.getLastName());
                    member.setBuildingId(role.getBuildingId());
                    memberRepository.create(member);
                } else {
                    if(member.isDeleted() == true){
                        member.setDeleted(false);
                        memberRepository.update(member);
                    }
                }

                if(roleMemberRepository.hasRole(role.getId(), member.getId()) == false){
                    roleMemberRepository.create(role.getId(), member.getId());
                }
            } else { // assigned = false
                if(member != null){
                    boolean hasOtherRoles = false;
                    for(Role tempRole: member.getRoles()){
                        if(tempRole.getId() != role.getId()){
                            hasOtherRoles = true;
                        }
                    }
                    if(hasOtherRoles == false){
                        member.setDeleted(true);
                        memberRepository.update(member);
                    }

                    roleMemberRepository.delete(role.getId(), member.getId());
                }
            }
        }

    }

    @Transactional
    public void multiDelete(List<Role> roles){
        for(Role role : roles){
            roleRepository.delete(role);
        }
    }
}
