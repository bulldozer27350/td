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
/**
 * Aspect for logging execution of service and repository Spring components.
 */
public class LoggingAspect {

	private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

	// Pointcut : toutes les classes de service du domaine
	@Pointcut("within(com.towerdefense..*)")
	/**
	 * Defines a pointcut for all methods within the com.towerdefense package and
	 * its sub-packages
	 */
	public void applicationPackagePointcut() {
	}

	// Log avant chaque appel
	@Before("applicationPackagePointcut()")
	/**
	 * Logs method entry with parameters before execution
	 */
	public void logMethodEntry(JoinPoint joinPoint) {
		log.info("→ Entrée : {} avec paramètres {}", joinPoint.getSignature(), joinPoint.getArgs());
	}

	// Log après retour de la méthode
	@AfterReturning(pointcut = "applicationPackagePointcut()", returning = "result")
	/**
	 * Logs method exit with result after successful execution
	 */
	public void logMethodExit(JoinPoint joinPoint, Object result) {
		log.info("← Sortie : {} avec résultat {}", joinPoint.getSignature(), result);
	}

	// Enrobage complet permettant de mesurer la durée et capturer exceptions
	@Around("applicationPackagePointcut()")
	/**
	 * Traces method execution time and captures exceptions
	 */
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