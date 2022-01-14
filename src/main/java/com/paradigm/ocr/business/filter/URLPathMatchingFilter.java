package com.paradigm.ocr.business.filter;

import com.paradigm.ocr.business.exception.BusinessException;
import com.paradigm.ocr.system.utils.SpringContextUtils;
import com.paradigm.ocr.business.service.PermissionService;
import lombok.extern.log4j.Log4j2;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.PathMatchingFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Log4j2
public class URLPathMatchingFilter extends PathMatchingFilter {
    @Autowired
    PermissionService permissionService;

    @Override
    protected boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws BusinessException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        String requestAPI = getPathWithinApplication(request);
        log.info("访问接口：" + requestAPI);

        if (HttpMethod.OPTIONS.toString().equals(httpServletRequest.getMethod())) {
            httpServletResponse.setStatus(HttpStatus.NO_CONTENT.value());
            return true;
        }
        if (null == permissionService) {
            permissionService = SpringContextUtils.getContext().getBean(PermissionService.class);
        }

        Subject subject = SecurityUtils.getSubject();

        boolean isAuthenticated = subject.isAuthenticated();
        boolean isRemembered = subject.isRemembered();

        if (!subject.isAuthenticated()) {
            log.info( "用户 [ " + subject.getPrincipals().toString() +  " ] 访问未授权接口：" + requestAPI);
            return false;
        }

        // 判断访问接口是否需要过滤（数据库中是否有对应信息）
        boolean needFilter = permissionService.needFilter(requestAPI);
        log.info("接口 [ " + requestAPI + " ] 需要过滤: " + needFilter);
        if(!needFilter) {
            return true;
        } else {
            // 判断当前用户是否有相应权限
            boolean hasPermission = false;
            String username = subject.getPrincipal().toString();
            List<String> permissionAPIs = permissionService.listPermissionUrlByUserName(username);
            for(String api: permissionAPIs) {
                // 匹配前缀
                if(requestAPI.startsWith(api)){
                    hasPermission = true;
                    break;
                }
            }

            if (hasPermission) {
                log.info("用户 [ " + username + " ] 访问授权接口：" + requestAPI);
                return true;
            } else {
                log.warn( "用户 [ " + username + " ] 访问未授权接口：" + requestAPI);
                return false;
            }
        }
    }
}
