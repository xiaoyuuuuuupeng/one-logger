package fun.pxyc.onelogger.db;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class JdbcAuditLogAspect {
    public JdbcAuditLogAspect() {}

    @Pointcut(
            "execution(* *..JdbcTemplate.query(..))||execution(* *..JdbcTemplate.queryForList(..))||execution(* *..JdbcTemplate.queryForObject(..))||execution(* *..JdbcTemplate.queryForRowSet(..))||execution(* *..JdbcTemplate.queryForMap(..))||execution(* *..JdbcTemplate.execute(..))||execution(* *..JdbcTemplate.update(..))||execution(* *..JdbcTemplate.batchUpdate(..))")
    public void jdbcAuditLogPointCut() {}

    @Around("jdbcAuditLogPointCut()")
    public Object jdbcAuditLogAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName().toLowerCase();
        String msgName = "jdbc_" + methodName;
        DbMsgNameHolder.msgName.set(msgName);
        return joinPoint.proceed();
    }
}
