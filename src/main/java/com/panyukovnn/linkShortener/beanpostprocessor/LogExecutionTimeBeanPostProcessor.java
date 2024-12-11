package com.panyukovnn.linkshortener.beanpostprocessor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;


@Component
@ConditionalLogExecution
public class LogExecutionTimeBeanPostProcessor implements BeanPostProcessor {

	private static final Logger log = LoggerFactory.getLogger(LogExecutionTimeBeanPostProcessor.class);

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		Set<Method> annotatedMethods = Arrays.stream(bean.getClass().getMethods())
			.filter(method -> method.isAnnotationPresent(LogExecutionTime.class))
			.collect(Collectors.toSet());

		if (annotatedMethods.isEmpty()) {
			return bean;
		}

		MethodInterceptor methodInterceptor = (obj, method, args, proxy) -> {
			boolean isAnnotated = annotatedMethods.stream()
				.anyMatch(method::equals);

			if (isAnnotated) {
				long start = System.currentTimeMillis();
				try {
					return method.invoke(bean, args);
				} catch (Exception e) {
					throw e.getCause();
				} finally {
					log.info("Время выполнения метода {}: {}", method.getName(), System.currentTimeMillis() - start);
				}
			}

			try {
				return method.invoke(bean, args);
			} catch (Exception e) {
				throw e.getCause();
			}
		};

		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(bean.getClass());
		enhancer.setCallback(methodInterceptor);

		return enhancer.create();
	}
}
