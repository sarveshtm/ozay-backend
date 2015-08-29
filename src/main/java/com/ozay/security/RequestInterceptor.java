package com.ozay.security;

import com.ozay.domain.User;
import com.ozay.model.AccountInformation;
import com.ozay.repository.AccountRepository;
import com.ozay.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class RequestInterceptor extends HandlerInterceptorAdapter {

    private final Logger log = LoggerFactory.getLogger(RequestInterceptor.class);

    @Inject
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Inject
    private UserRepository userRepository;

    @Inject
    private AccountRepository accountRepository;


    private static String NOTIFICATION_ARCHIVE = "NOTIFICATION_ARCHIVE";
    private static String NOTIFICATION_CREATE = "NOTIFICATION_CREATE";
    private static String DIRECTORY_LIST = "DIRECTORY_LIST";
    private static String DIRECTORY_EDIT = "DIRECTORY_EDIT";
    private static String DIRECTORY_DELETE = "DIRECTORY_DELETE";

    @SuppressWarnings("unchecked")
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("Interceptor Start");
        log.debug("Intercepting: " + request.getServletPath());

        User loginUser = userRepository.findOneByLogin(SecurityUtils.getCurrentLogin()).get();

        if(loginUser == null){
            log.debug("False Intercepting Login is null: " + request.getServletPath());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        //### 2) CHECK AUTHORITY
        if(SecurityUtils.isUserInRole("ROLE_ADMIN")){
            return true;
        }


        boolean result = this.validation(request, response, loginUser);
        if(result == true){
            log.debug("True Intercepting: " + request.getServletPath());
            log.debug("!!!!!!!!!!!!!Interceptor return true!!!!!!!!!!!");
            return true;
        } else {
            log.debug("???????????Interceptor return false???????????????");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return false;
        }
    }

    private boolean validation(HttpServletRequest request, HttpServletResponse response, User loginUser){

        String method = request.getMethod();

        Long buildingId = this.parseNumber(request.getParameter("building"));
        Long organizationId = this.parseNumber(request.getParameter("organization"));


         // temp
        if(organizationId != null){
            return true;
        }


        if((buildingId == null || buildingId == 0) && (organizationId == null || organizationId == 0 )){
            log.debug("False Intercepting BuildingID is null: " + request.getServletPath());
            return false;
        }

        System.out.println("BuildingId is : " + buildingId);
        System.out.println(method.toUpperCase());


        if(buildingId == -1){
            log.debug("False Intercepting Key is -1: " + request.getServletPath());
            return false;
        }

        AccountInformation accountInformation = accountRepository.getLoginUserInformation(loginUser, buildingId);

        // If null this user cannot access to the building
        if(accountInformation == null) {
            log.debug("False Intercepting AccountInformation is NULL: " + request.getServletPath());
            return false;
        } else {
            // check if user has access
            boolean roleAccessCheck = false;
            if(accountInformation.getSubscriberId() != null){
                roleAccessCheck = true;
            }

            if(roleAccessCheck == false && accountInformation.getAuthorities()!=null){
                roleAccessCheck = this.checkAccessRole(accountInformation, request);
            }

            if(roleAccessCheck == false){
                log.debug("False Intercepting Doesn't have role to access: " + request.getServletPath());
                return false;
            }
        }




        //### 3)Organization Role check ####


        //### 4) Building Access Check ###

        return true;

    }


    private boolean checkAccessRole(AccountInformation accountInformation, HttpServletRequest request) {
        if(request.getServletPath().contains("api/notifications")){
            if(request.getMethod().toUpperCase().equals("GET")== true){
                return this.findRoleAccess(accountInformation, RequestInterceptor.NOTIFICATION_ARCHIVE);
            } else {
                return this.findRoleAccess(accountInformation, RequestInterceptor.NOTIFICATION_CREATE);
            }
        } else if(request.getServletPath().contains("api/members")){
            if(request.getServletPath().contains("api/member/delete")){
                return this.findRoleAccess(accountInformation, RequestInterceptor.DIRECTORY_DELETE);
            }
            if(request.getMethod().toUpperCase().equals("GET")== true){
                return this.findRoleAccess(accountInformation, RequestInterceptor.DIRECTORY_LIST);
            } else {
                return this.findRoleAccess(accountInformation, RequestInterceptor.DIRECTORY_EDIT);
            }
        }
        return false;
    }

    private boolean findRoleAccess(AccountInformation accountInformation, String rolePermission){
        boolean result = false;
        for(String authority : accountInformation.getAuthorities()){
            if(authority.equals(rolePermission)){
                result = true;
                break;
            }
        }
        return result;
    }

    private Long parseNumber(String value){
        Long temp = null;
        try {
            temp = Long.parseLong(value);

        } catch(Exception e){

        }
        return temp;
    }

}
