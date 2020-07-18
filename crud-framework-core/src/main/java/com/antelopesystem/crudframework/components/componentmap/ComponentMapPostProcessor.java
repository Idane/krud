package com.antelopesystem.crudframework.components.componentmap;

import com.antelopesystem.crudframework.utils.ReflectionUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.aop.TargetClassAware;
import org.springframework.aop.framework.Advised;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ComponentMapPostProcessor implements BeanPostProcessor, ApplicationContextAware {

	private ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(@NotNull ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Override
	public Object postProcessBeforeInitialization(@NotNull Object bean, @NotNull String beanName) throws BeansException {
		Object handler = bean;
		if(handler instanceof TargetClassAware) {
			try {
				handler = ((Advised) handler).getTargetSource().getTarget();
			} catch (Exception e) {
			}
		}

		List<Field> fields = ReflectionUtils.getFields(handler.getClass());

		for(Field field : fields) {
			if(field.isAnnotationPresent(ComponentMap.class) && field.getType() == Map.class) {
				ComponentMap mapped = field.getAnnotation(ComponentMap.class);
				try {
					Class<?> keyClazz = mapped.key();
					Class<?> valueClazz = mapped.value();

					if(keyClazz == void.class) {
						keyClazz = ((Class<?>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0]);
					}

					if(valueClazz == void.class) {
						valueClazz = ((Class<?>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[1]);
					}

					field.setAccessible(true);
					field.set(handler, initMap(keyClazz, valueClazz));
				} catch(Exception e) {
				}
			}
		}
		return bean;
	}

	private <T, E> Map<T, E> initMap(Class<T> key, Class<E> value) {
		Map<T, E> map = new LinkedHashMap<>();

		List<E> beans = new ArrayList<>(applicationContext.getBeansOfType(value).values());
		AnnotationAwareOrderComparator.sort(beans);
		for(E bean : beans) {
			List<Method> methods = ReflectionUtils.getMethods(bean.getClass());

			for(Method method : methods) {
				Annotation annotation = AnnotationUtils.findAnnotation(method, ComponentMapKey.class);
				if(annotation != null) {
					try {
						map.put((T) method.invoke(bean), bean);
					} catch(Exception e) {
					}
				}
			}
		}

		return map;
	}
}