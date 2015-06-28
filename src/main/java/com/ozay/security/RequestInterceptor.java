package com.ozay.security;

    import javax.servlet.http.HttpServletRequest;
    import javax.servlet.http.HttpServletResponse;
    import java.util.*;
    import javax.sql.DataSource;

    import com.ozay.domain.User;
    import com.ozay.model.Building;
    import com.ozay.model.Organization;
    import com.ozay.rowmapper.BuildingRowMapper;

    import com.ozay.rowmapper.OrganizationMapper;
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

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private DataSource dataSource;

    @SuppressWarnings("unchecked")
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        SecurityContext securityContext = SecurityContextHolder.getContext();

        if (securityContext.getAuthentication() ==null) return true;

        String userName = SecurityUtils.getCurrentLogin();
        Authentication authentication = securityContext.getAuthentication();

        if(authentication == null) return true;
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        //### 2) CHECK AUTHORITY
        for (GrantedAuthority auth : authorities){
          if (auth.getAuthority().equals("ROLE_ADMIN")) return true;
        }

        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);

        //### 3)Organization Role check ####
//        if (request.getServletPath().contains("group/")){
//            log.debug("Intercepting: " + request.getServletPath());
//            if(!isAccessibleGroup(request,userName )){
//                response.sendRedirect(request.getServerName()+"/#/error");
//                return false;
//            }
//        }

        //### 4) Building Access Check ###
        if (request.getServletPath().contains("building/")){
            log.debug("Intercepting: " + request.getServletPath());
            if(!isAccessibleBrd(request, userName)) {
                response.sendRedirect(request.getServerName()+"/#/error");
                return false;
            }
        }
        return true;
    }

//    private boolean isAccessibleGroup(HttpServletRequest request, String login) {
//        Integer key_no = getKeyNo(request, "group");
//        log.debug("Group Access: Group No.:[" + key_no.toString() + "]/User No:[" + login +"]");
//        List<Organization> bld = this.getOrgUserCanAccess(login, key_no);
//        if(bld.size() > 0) return true;
//        return false;
//    }

    private boolean isAccessibleBrd(HttpServletRequest request,String login) {
        Integer key_no = getKeyNo(request, "building");
        log.debug("Building Access: Building No.:[" + key_no.toString() + "]/User No:[" + login +"]");
       List<Building> bld = this.getBuildingsUserCanAccess(login,key_no);
        if(bld.size() > 0) return true;
        return false;
    }

    private Integer getKeyNo(HttpServletRequest request, String split_wd) {
        Integer rtnInt = -1;
        try{
            String[] tests = request.getServletPath().split(split_wd);
            String[] tests2 = tests[1].split("/");
            if (tests2.length>1) rtnInt= Integer.parseInt(tests2[1]);
        }catch(Exception e){
            // return -1
        }
        return rtnInt;
    }

//    public List<Organization> getOrgUserCanAccess(String login,Integer org_id){
//        String query = "SELECT o.* FROM " +
//            "  organization o ON o.id = b.organization_id " +
//            " LEFT JOIN organization_user ou ON ou.organization_id = o.id " +
//            " LEFT JOIN t_user u1 ON ou.user_id= u1.id " +
//            " LEFT JOIN subscription s ON s.id = o.subscription_id " +
//            " LEFT JOIN t_user u2 ON s.user_id= u2.id " +
//            " WHERE (u1.login = :Login OR u2.login = :Login )" +
//            " AND ou.organization_id =:Org_id";
//        MapSqlParameterSource params = new MapSqlParameterSource();
//        params.addValue("Login", login);
//        params.addValue("Org_id", org_id);
//        return namedParameterJdbcTemplate.query(query, params, new OrganizationMapper(){
//        });
//    }

    public List<Building> getBuildingsUserCanAccess(String login,Integer build_id){
        String query = "SELECT b.* FROM building b " +
            " LEFT JOIN organization o ON o.id = b.organization_id " +
            " LEFT JOIN organization_user ou ON ou.organization_id = o.id " +
            " LEFT JOIN t_user u1 ON ou.user_id = u1.id " +
            " LEFT JOIN member m ON b.id = m.building_id " +
            " LEFT JOIN subscription s ON s.id = o.subscription_id " +
            " LEFT JOIN t_user u2 ON s.user_id= u2.id " +
            " WHERE (m.login = :Login OR u2.login = :Login OR u1.login = :Login )" +
            " AND b.id =:Build_id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("Login", login);
        params.addValue("Build_id", build_id);
        return namedParameterJdbcTemplate.query(query, params, new BuildingRowMapper(){
        });
    }

    public void set(DataSource pDataSource) {
        this.dataSource = pDataSource;
    }

}
