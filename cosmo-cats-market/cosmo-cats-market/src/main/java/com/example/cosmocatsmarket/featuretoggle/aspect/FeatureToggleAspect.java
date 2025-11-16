package com.example.cosmocatsmarket.featuretoggle.aspect;

import com.example.cosmocatsmarket.featuretoggle.FeatureToggleService;
import com.example.cosmocatsmarket.featuretoggle.annotation.FeatureToggle;
import com.example.cosmocatsmarket.featuretoggle.exception.FeatureNotAvailableException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class FeatureToggleAspect {

    private final FeatureToggleService toggleService;

    @Around("@annotation(com.example.cosmocatsmarket.featuretoggle.annotation.FeatureToggle)")
    public Object enforceFeatureAvailability(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        FeatureToggle toggleAnnotation = methodSignature.getMethod().getDeclaredAnnotation(FeatureToggle.class);

        if (toggleAnnotation == null) {
            return pjp.proceed();
        }

        String featureName = toggleAnnotation.value();
        if (!toggleService.isEnabled(featureName)) {
            log.warn("Feature '{}' is DISABLED - blocking method {}",
                    featureName, methodSignature.getMethod().getName());
            throw new FeatureNotAvailableException(featureName);
        }

        log.debug("Feature '{}' is enabled - executing {}",
                featureName, methodSignature.getMethod().getName());
        return pjp.proceed();
    }
}
