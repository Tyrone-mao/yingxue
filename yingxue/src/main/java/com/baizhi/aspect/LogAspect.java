package com.baizhi.aspect;

import com.baizhi.annotation.AddLog;
import com.baizhi.dao.LogMapper;
import com.baizhi.entity.Admin;
import com.baizhi.entity.Log;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.UUID;

@Aspect//这是一个切面类
@Configuration//将该类交给Spring工厂管理
public class LogAspect {
    @Autowired
    private HttpSession session;
    @Resource
    private LogMapper logMapper;

    @Around("@annotation(com.baizhi.annotation.AddLog)")
    public Object addLog(ProceedingJoinPoint proceedingJoinPoint){
        System.out.println("===进入环绕通知===");

        //谁   时间    操作(哪个方法)  是否成功
        Admin admin = (Admin) session.getAttribute("admin");

        //获取管理员名
        String username = admin.getUsername();

        //获取操作的当前时间
        Date date = new Date();

        //获取操作的方法名
        String methodName = proceedingJoinPoint.getSignature().getName();

        //System.out.println("方法名:"+methodName);  //update

        //获取方法的签名
        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        //获取方法
        Method method = signature.getMethod();
        //获取方法上的指定注解
        AddLog addLog = method.getAnnotation(AddLog.class);
        //获取注解上的属性值
        String value = addLog.value();
        String message=null;
        Object returnResult =null;
        //环绕通知放行   执行目标方法
        try {
            returnResult = proceedingJoinPoint.proceed();
            message="success(执行成功)";
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            message="error(执行失败)";
        }

        Log log = new Log(UUID.randomUUID().toString(), username,  methodName+"( "+value+" )",date, message);

        //数据入库
        logMapper.insert(log);

        return returnResult;
    }


}
