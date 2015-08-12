package com.ozay.security;

    import javax.inject.Inject;
    import javax.servlet.http.HttpServletRequest;
    import javax.servlet.http.HttpServletResponse;
    import java.io.BufferedReader;
    import java.util.*;
    import javax.sql.DataSource;

    import com.ozay.domain.User;
    import com.ozay.model.AccountInformation;
    import com.ozay.model.Building;
    import com.ozay.model.Organization;
    import com.ozay.repository.AccountRepository;
    import com.ozay.repository.UserRepository;
    import com.ozay.rowmapper.BuildingRowMapper;

    import com.ozay.rowmapper.OrganizationMapper;
    import org.json.JSONObject;
    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;

    import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
    import org.springframework.security.core.Authentication;
    import org.springframework.security.core.GrantedAuthority;
    import org.springframework.security.core.context.SecurityContext;
    import org.springframework.security.core.context.SecurityContextHolder;
    import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
    import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;


public class RequestInterceptor extends HandlerInterceptorAdapter {

    private final Logger log = LoggerFactory.getLogger(RequestInterceptor.class);

    @Inject
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Inject
    private UserRepository userRepository;

    @Inject
    private AccountRepository accountRepository;

    @SuppressWarnings("unchecked")
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("Interceptor Start");
        log.debug("Intercepting: " + request.getServletPath());
        if(request.getServletPath().equals("/api/building")){
            return true;
        }

        if (SecurityUtils.getCurrentLogin() ==null) return true;

        User loginUser = userRepository.findOneByLogin(SecurityUtils.getCurrentLogin()).get();

        if(loginUser == null) return false;


        String method = request.getMethod();

        //### 2) CHECK AUTHORITY
        if(SecurityUtils.isUserInRole("ROLE_ADMIN")) return true;

        Long key_no = getKeyNo(request, "building");

        System.out.println("KeyNO is : " + key_no);
        System.out.println(method.toUpperCase());


        if(key_no == -1){
            return true;
        }



        AccountInformation accountInformation = accountRepository.getLoginUserInformation(loginUser, key_no);

        // If null this user cannot access to the building
        if(accountInformation == null) return false;



        //### 3)Organization Role check ####


        //### 4) Building Access Check ###

        log.debug("Interceptor return true");
        return true;
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
