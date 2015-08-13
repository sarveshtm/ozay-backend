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

    @SuppressWarnings("unchecked")
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("Interceptor Start");
        log.debug("Intercepting: " + request.getServletPath());

        String n1=request.getParameter("building");


        if (SecurityUtils.getCurrentLogin() ==null) return true;

        User loginUser = userRepository.findOneByLogin(SecurityUtils.getCurrentLogin()).get();

        if(loginUser == null) return false;


        String method = request.getMethod();

        //### 2) CHECK AUTHORITY
        if(SecurityUtils.isUserInRole("ROLE_ADMIN")) return true;

        //Long buildingId = getKeyNo(request, "building");
        Long buildingId = null;
        try {
            long temp = Long.parseLong(request.getParameter("building"));
            buildingId = temp;
        } catch(Exception e){

        }
        if(buildingId == 0 || buildingId == null){
            log.debug("False Intercepting BuildingID is null: " + request.getServletPath());
            log.debug("???????????Interceptor return false???????????????");
            return false;
        }

        System.out.println("BuildingId is : " + buildingId);
        System.out.println(method.toUpperCase());


        if(buildingId == -1){
            log.debug("False Intercepting Key is -1: " + request.getServletPath());
            log.debug("???????????Interceptor return false???????????????");
            return false;
        }

        AccountInformation accountInformation = accountRepository.getLoginUserInformation(loginUser, buildingId);

        // If null this user cannot access to the building
        if(accountInformation == null) {
            log.debug("False Intercepting AccountInformation is NULL: " + request.getServletPath());
            log.debug("???????????Interceptor return false???????????????");
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
                log.debug("???????????Interceptor return false???????????????");
                return false;
            }
        }




        //### 3)Organization Role check ####


        //### 4) Building Access Check ###
        log.debug("True Intercepting: " + request.getServletPath());
        log.debug("!!!!!!!!!!!!!Interceptor return true!!!!!!!!!!!");
        return true;
    }

    private boolean checkAccessRole(AccountInformation accountInformation, HttpServletRequest request) {
        if(request.getServletPath().contains("api/notifications")){
            if(request.getMethod().toUpperCase().equals("GET")== true){
                return this.findRoleAccess(accountInformation, RequestInterceptor.NOTIFICATION_ARCHIVE);
            } else {
                return this.findRoleAccess(accountInformation, RequestInterceptor.NOTIFICATION_CREATE);
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

    private Long getKeyNo(HttpServletRequest request, String split_wd) {
        long rtnInt = -1;
        try{
            String[] split_url = request.getServletPath().split("/");
            int buildingIndex = 0;
            for(int i =0; i<split_url.length;i++){
                if(split_url[i].equals(split_wd)){
                    buildingIndex = i + 1;
                    break;
                }
            }

            rtnInt = Integer.parseInt(split_url[buildingIndex]);



        }catch(Exception e){

        }
        return rtnInt;
    }



}
