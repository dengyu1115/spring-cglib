package com.nature.proxy.factory;

import com.nature.demo.function.DemoFunction;
import com.nature.proxy.util.ContextHolder;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 构建NatureService标记组件实例的工厂类
 * @author nature
 * @version 1.0.0
 * @since 2021/7/3 21:24
 */
public class NatureFactoryBean implements FactoryBean<Object> {

    /**
     * 被代理的类
     */
    private final Class<Object> clz;

    /**
     * 通过扫描时指定构建参数，调用次构建函数传入实际需要实例化的类
     * @param clz 类
     */
    public NatureFactoryBean(Class<Object> clz) {
        this.clz = clz;
    }

    @Override
    public Object getObject() {
        return new NatureProxy().getObject(clz);
    }

    @Override
    public Class<?> getObjectType() {
        return clz;
    }

    private static class NatureProxy implements MethodInterceptor {

        /**
         * 获取代理实例
         * @param clz 类
         * @return 实例
         */
        public Object getObject(Class<Object> clz) {
            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(clz);
            enhancer.setCallback(this);
            Object o = enhancer.create();
            this.doAutoWire(clz, o);
            return o;
        }

        /**
         * 实例成员自动装配处理
         * @param clz 类
         * @param o   实例
         */
        private void doAutoWire(Class<Object> clz, Object o) {
            if (o == null) {
                return;
            }
            Class<? super Object> sc = clz.getSuperclass();
            if (!sc.equals(Object.class)) {
                this.doAutoWire(sc, o);
            }
            Field[] fields = clz.getDeclaredFields();
            for (Field field : fields) {
                if (!field.isAnnotationPresent(Autowired.class)) {
                    continue;
                }
                field.setAccessible(true);
                try {
                    field.set(o, ContextHolder.getBean(field.getType()));
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        /**
         * 代理拦截逻辑
         * @param o           代理对象实例
         * @param method      被拦截方法
         * @param objects     参数列表
         * @param methodProxy 代理方法
         * @return 代理方法返回值
         * @throws Throwable 异常
         */
        @Override
        public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
            Class<?> clz = method.getDeclaringClass();
            if (clz.equals(DemoFunction.class)) {
                return "this the function message";
            }
            return methodProxy.invokeSuper(o, objects);
        }
    }
}
