package com.vbl.distribution.server.controller.common;

import com.vbl.distribution.server.context.ConsumerContext;
import com.vbl.distribution.server.event.DistributionEvent;
import com.vbl.distirbution.model.DistributionProtocol;
import com.vbl.distirbution.model.Error;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;

@Aspect
public class ControllerAspect {

    @Pointcut("execution(* com.vbl.distribution.server.controller.*.handle*(..))")
    private void eventHandlerMethod() {}

    @Around("eventHandlerMethod() && args(event)")
    public Object sendLastMessage(ProceedingJoinPoint joinPoint, DistributionEvent event) throws Throwable {
        ConsumerContext consumerContext = event.getConsumerContext();
        DistributionProtocol message = event.getMessage();
        try {
            return joinPoint.proceed();
        } catch (Exception ex) {
            consumerContext.send(DistributionProtocol.newBuilder()
                    .setMessageType(DistributionProtocol.MessageType.ERROR)
                    .setToken(message.getToken())
                    .setCorrelationId(message.getCorrelationId())
                    .setErrorMessage(Error.newBuilder().setMessage(ex.getMessage()))
                    .build());
            throw ex;
        } finally {
            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            if (methodSignature.getMethod().isAnnotationPresent(LastMessage.class)) {
                consumerContext.send(DistributionProtocol.newBuilder()
                        .setMessageType(DistributionProtocol.MessageType.LAST)
                        .setToken(message.getToken())
                        .setCorrelationId(message.getCorrelationId())
                        .build());
            }
        }
    }

}
