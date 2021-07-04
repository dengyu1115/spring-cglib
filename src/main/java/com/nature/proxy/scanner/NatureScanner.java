package com.nature.proxy.scanner;


import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Set;

/**
 * 扫描器
 * @author nature
 * @version 1.0.0
 * @since 2021/7/3 21:17
 */
public class NatureScanner extends ClassPathBeanDefinitionScanner {

    public NatureScanner(BeanDefinitionRegistry registry, boolean useDefaultFilters) {
        super(registry, useDefaultFilters);
    }

    /**
     * 扫描包路径，通过include和exclude等过滤条件，返回符合条件的bean定义
     * 由于这个方法在父类是protected，所以只能通过继承类获取结果
     * @param basePackages 需要扫描的包路径
     */
    public Set<BeanDefinitionHolder> doScan(String... basePackages) {
        return super.doScan(basePackages);
    }

    /**
     * 判断注解标注的类是否可以作为候选组件
     * @param beanDefinition bean定义信息
     * @return boolean
     */
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        AnnotationMetadata metadata = beanDefinition.getMetadata();
        return (metadata.isIndependent() && (metadata.isConcrete() || metadata.isAbstract()));
    }
}
