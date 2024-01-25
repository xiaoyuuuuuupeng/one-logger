package fun.pxyc.onelogger.bizlog;

import java.lang.reflect.Method;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.Advisor;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.framework.adapter.AdvisorAdapter;

public class BizAdvice extends BizServiceInterceptor
        implements MethodBeforeAdvice, AfterReturningAdvice, AdvisorAdapter {

    @Override
    public void before(Method method, Object[] args, Object target) throws Throwable {}

    @Override
    public void afterReturning(Object o, Method method, Object[] objects, Object o1) throws Throwable {}

    @Override
    public boolean supportsAdvice(Advice advice) {
        return true;
    }

    @Override
    public MethodInterceptor getInterceptor(Advisor advisor) {
        return null;
    }
}
