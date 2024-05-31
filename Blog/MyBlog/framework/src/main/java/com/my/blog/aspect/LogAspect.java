package com.my.blog.aspect;

import com.my.blog.annotation.SystemLog;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.protocol.RequestContent;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
@Slf4j
public class LogAspect {

    @Pointcut("@annotation(com.my.blog.annotation.SystemLog)")
    public void pointcut() {

    }

    @Around("pointcut()")
    public Object pringLog(ProceedingJoinPoint joinPoint) throws Throwable{
        Object ret ;
        try {
            handleBefore(joinPoint);
            ret = joinPoint.proceed();
            handleAfter(ret);
        } finally {

        }
        return ret;
    }

    private void handleAfter(Object ret) {
        // 打印出参
        log.info("Response : {}", ret.toString() );
// 结束后换行
        log.info("=======End=======" + System.lineSeparator());
    }

    private void handleBefore(ProceedingJoinPoint joinPoint) {

        ServletRequestAttributes requestAttributes =(ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        MethodSignature signature =(MethodSignature) joinPoint.getSignature();
        log.info("=======Start=======");
// 打印请求 URL
        log.info("URL : {}",request.getRequestURL());
// 打印描述信息
        log.info("BusinessName : {}", signature.getMethod().getAnnotation(SystemLog.class).BusinessName());
// 打印 Http method
        log.info("HTTP Method : {}", request.getMethod());
// 打印调用 controller 的全路径以及执行方法
        log.info("Class Method : {}.{}", signature.getDeclaringTypeName(),signature.getName());
// 打印请求的 IP
        log.info("IP : {}",request.getRemoteHost());
// 打印请求入参
        log.info("Request Args : {}",joinPoint.getArgs());
    }

}
