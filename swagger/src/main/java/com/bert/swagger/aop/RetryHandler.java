package com.bert.swagger.aop;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

/**
 * @author yangbo
 * @date 2018/6/12
 */
@Aspect
@Component
public class RetryHandler {

    private static final Logger log = LoggerFactory.getLogger(RetryHandler.class);

    @Around("@annotation(com.bert.swagger.aop.Retry)")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Signature signature = joinPoint.getSignature();
        if (!(signature instanceof MethodSignature)) {
            throw new RuntimeException("This joinPoint's signature must be methodSignature!");
        }
        MethodSignature methodSignature = (MethodSignature) signature;
        Retry retry = methodSignature.getMethod().getAnnotation(Retry.class);
        if (null != retry && retry.times() <= 1) {
            return joinPoint.proceed();
        }
        String baseUrl = retry.url();
        if (StringUtils.isNotEmpty(baseUrl)) {
            ExpressionParser parser = new SpelExpressionParser();
            ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();
            StandardEvaluationContext context = new MethodBasedEvaluationContext(null, methodSignature.getMethod(),
                    joinPoint.getArgs(), parameterNameDiscoverer);
            Expression expression = parser.parseExpression(baseUrl);
            Object value = expression.getValue(context);
            if (null == value) {
                throw new RuntimeException("@Retry中的SpEL要获取的值在参数列表中无法获取");
            }
            baseUrl = (String) value;
        }
        int retryCount = 0;
        while (retry.times() - retryCount > 0) {
            try {
                retryCount++;
                return joinPoint.proceed();
            } catch (Exception e) {
                log.error("rest请求error,请求次数:{},baseUrl:{}->error:{}", retryCount, baseUrl,
                        ExceptionUtils.getRootCauseMessage(e));
            }
        }
        return null;
    }

}
