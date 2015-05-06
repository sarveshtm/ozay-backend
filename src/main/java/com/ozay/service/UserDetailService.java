package com.ozay.service;

import com.ozay.domain.Authority;
import com.ozay.domain.PersistentToken;
import com.ozay.domain.User;
import com.ozay.repository.AuthorityRepository;
import com.ozay.repository.PersistentTokenRepository;
import com.ozay.repository.UserDetailRepository;
import com.ozay.repository.UserRepository;
import com.ozay.security.SecurityUtils;
import com.ozay.service.util.RandomUtil;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class UserDetailService {

    private final Logger log = LoggerFactory.getLogger(UserDetailService.class);

    @Inject
    private UserDetailRepository userDetailRepository;



}
