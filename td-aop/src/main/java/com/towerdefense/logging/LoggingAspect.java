// Exemple complet d'un mécanisme AOP de logging pour tracer automatiquement
// les entrées et sorties de chaque méthode métier

package com.towerdefense.logging;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    // Pointcut : toutes les classes de service du domaine
    @Pointcut("within(com.towerdefense..*)")
    public void applicationPackagePointcut() {}

    // Log avant chaque appel
    @Before("applicationPackagePointcut()")
    public void logMethodEntry(JoinPoint joinPoint) {
        log.info("→ Entrée : {} avec paramètres {}", joinPoint.getSignature(), joinPoint.getArgs());
    }

    // Log après retour de la méthode
    @AfterReturning(pointcut = "applicationPackagePointcut()", returning = "result")
    public void logMethodExit(JoinPoint joinPoint, Object result) {
        log.info("← Sortie : {} avec résultat {}", joinPoint.getSignature(), result);
    }

    // Enrobage complet permettant de mesurer la durée et capturer exceptions
    @Around("applicationPackagePointcut()")
    public Object traceExecution(ProceedingJoinPoint pjp) throws Throwable {
        long start = System.currentTimeMillis();
        try {
            Object result = pjp.proceed();
            long duration = System.currentTimeMillis() - start;
            log.debug("⏱ Durée {}ms pour {}", duration, pjp.getSignature());
            return result;
        } catch (Throwable t) {
            log.error("✖ Exception dans {} : {}", pjp.getSignature(), t.getMessage(), t);
            throw t;
        }
    }
}

// Exemple d'activation pour Spring Boot
// @SpringBootApplication
// @EnableAspectJAutoProxy
// public class TowerDefenseApplication {
//     public static void main(String[] args) {
//         SpringApplication.run(TowerDefenseApplication.class, args);
//     }
// }
