package com.ozay.service;

import com.ozay.domain.Authority;
import com.ozay.domain.PersistentToken;
import com.ozay.domain.User;
import com.ozay.model.UserDetail;
import com.ozay.repository.AuthorityRepository;
import com.ozay.repository.PersistentTokenRepository;
import com.ozay.repository.UserDetailRepository;
import com.ozay.repository.UserRepository;
import com.ozay.security.SecurityUtils;
import com.ozay.service.util.RandomUtil;
import com.ozay.web.rest.dto.UserDetailListDTO;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.*;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class UserDetailService {

    private final Logger log = LoggerFactory.getLogger(UserDetailService.class);

    @Inject
    private UserDetailRepository userDetailRepository;


    public List<UserDetailListDTO> createUserDetailListByRole(List<UserDetail> userDetailList){
        List<UserDetailListDTO> userDetailListDTOs = new ArrayList<UserDetailListDTO>();

        UserDetailListDTO managementList = new UserDetailListDTO("Management");
        UserDetailListDTO staffList = new UserDetailListDTO("Staff");
        UserDetailListDTO boardList = new UserDetailListDTO("Board");
        UserDetailListDTO residentList = new UserDetailListDTO("Resident");

        for(UserDetail userDetail : userDetailList){
            if(userDetail.isManagement() == true){
                managementList.addUserDetailToList(userDetail);
            } else if(userDetail.isStaff()){
                staffList.addUserDetailToList(userDetail);
            } else if(userDetail.isBoard() == true){
                boardList.addUserDetailToList(userDetail);
            } else if(userDetail.isResident()){
                residentList.addUserDetailToList(userDetail);
            }
        }

        userDetailListDTOs.add(managementList);
        userDetailListDTOs.add(staffList);
        userDetailListDTOs.add(boardList);
        userDetailListDTOs.add(residentList);

        return userDetailListDTOs;
    }

}
