package com.panyukovnn.linkshortener.beanpostprocessor;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@ConditionalOnProperty(
	value = "link-shortener.logging.enable-log-execution-time",
	havingValue = "true",
	matchIfMissing = true
)
public @interface ConditionalLogExecution {
}
