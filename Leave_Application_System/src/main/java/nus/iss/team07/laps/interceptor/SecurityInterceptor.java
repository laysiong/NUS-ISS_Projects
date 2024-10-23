package nus.iss.team07.laps.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import nus.iss.team07.laps.model.Employee;
import nus.iss.team07.laps.model.Role;
import nus.iss.team07.laps.model.UserSession;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
public class SecurityInterceptor implements HandlerInterceptor  {

    @Override
    public boolean preHandle(HttpServletRequest request, 
                             HttpServletResponse response, Object handler) 
                             throws IOException {
        
        
        
        // take note on what case where it does not require authentication for user
        String uri = request.getRequestURI();
        if (uri.startsWith("/css/") || uri.startsWith("/image/") || uri.equalsIgnoreCase("/") 
            || uri.equalsIgnoreCase("/login/authenticate") || uri.equalsIgnoreCase("/api/holiday") || uri.equals("/error")) {
            return true;
        }
        
        //System.out.println("Intercepting " + request.getRequestURI());
        
        HttpSession session = request.getSession();
        UserSession userSession = (UserSession) session.getAttribute("usession");
        

        if (userSession == null) {
          response.sendRedirect("/");
          return false;
        }
        
       
        String userRoles = userSession.getRoles();
        
    
        
        // Role-based access control
//        if (uri.startsWith("/a") && !userRoles.contains("Admin")) {
//            response.sendRedirect("/unauthorized");
//            return false;
//        }
//
//        if (uri.startsWith("/m") && !userRoles.contains("Manager")) {
//            response.sendRedirect("/unauthorized");
//            return false;
//        }
        
        
        // Define a map of paths and required roles
        Map<String, List<String>> accessControlMap = new HashMap<>();
        accessControlMap.put("/pendingrecords", Arrays.asList("Manager"));
        accessControlMap.put("/pendingrecords/**", Arrays.asList("Manager"));
        accessControlMap.put("/leaverecords", Arrays.asList("Manager"));
        accessControlMap.put("/leaverecords/**", Arrays.asList("Manager"));
        accessControlMap.put("/compensation_applicaiton", Arrays.asList("Manager"));
        accessControlMap.put("/compensation_applicaiton/**", Arrays.asList("Manager"));
        accessControlMap.put("/Manager", Arrays.asList("Manager"));
        accessControlMap.put("/Manager/**", Arrays.asList("Manager"));
        
        accessControlMap.put("/leave-types", Arrays.asList("Admin"));
        accessControlMap.put("/leave-types/**", Arrays.asList("Admin"));
        accessControlMap.put("/leave-entitlements", Arrays.asList("Admin"));
        accessControlMap.put("/leave-entitlements/**", Arrays.asList("Admin"));
        accessControlMap.put("/employee", Arrays.asList("Admin"));
        accessControlMap.put("/employee/**", Arrays.asList("Admin"));
        accessControlMap.put("/roles", Arrays.asList("Admin"));
        accessControlMap.put("/roles/**", Arrays.asList("Admin"));
        accessControlMap.put("/departments", Arrays.asList("Admin"));
        accessControlMap.put("/departments/**", Arrays.asList("Admin"));
        accessControlMap.put("/holidays", Arrays.asList("Admin"));
        accessControlMap.put("/holidays/**", Arrays.asList("Admin"));
        accessControlMap.put("/Admin", Arrays.asList("Admin"));
        accessControlMap.put("/Admin/**", Arrays.asList("Admin"));
        // Add more paths and required roles as needed
     

        for (Map.Entry<String, List<String>> entry : accessControlMap.entrySet()) {
            if (uri.startsWith(entry.getKey())) {
                boolean hasRequiredRole = entry.getValue().stream().anyMatch(userRoles::contains);
                if (!hasRequiredRole) {
                    System.out.println("Unauthorized access attempt to " + uri + " by user with roles: " + userRoles);
                    response.sendRedirect("/unauthorized");
                    return false;
                }
            }
        }
        
        

	        
        return true;
    }
}