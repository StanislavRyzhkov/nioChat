package company.ryzhkov.server.util;

import company.ryzhkov.server.core.Watch;
import lombok.Getter;
import lombok.extern.java.Log;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Log
@Component
public class WatchHandlerBeanPostProcessor implements BeanPostProcessor {

    @Getter
    private Map<String, Class> map = new HashMap<>();

    @Value("${bar.profiling}")
    private String profiling;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        boolean isPresent = Arrays
                .stream(beanClass.getDeclaredMethods())
                .anyMatch(method -> method.isAnnotationPresent(Watch.class));
        if (isPresent) map.put(beanName, beanClass);
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = map.get(beanName);
        if (beanClass != null) {
            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(beanClass);
            enhancer.setCallback((MethodInterceptor) (obj, method, args, proxy) -> {
                if (method.isAnnotationPresent(Watch.class)) {
                    long start = System.nanoTime();
                    Object o = method.invoke(bean, args);
                    long end = System.nanoTime() - start;
                    log.info(String.format("Benchmark on %s: %d", beanName, end));
                    return o;
                } else {
                    return method.invoke(bean, args);
                }
            });
            return enhancer.create();
        }
        return bean;
    }
}
