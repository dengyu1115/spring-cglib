package com.nature.proxy.processor;

import com.nature.proxy.annotation.NatureService;
import com.nature.proxy.factory.NatureFactoryBean;
import com.nature.proxy.scanner.NatureScanner;
import com.nature.proxy.util.ContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * bean定义后置处理器，通过此类作为扫描自定义注解的入口
 * @author nature
 * @version 1.0.0
 * @since 2021/7/3 21:18
 */
@Component
public class NatureProcessor implements PriorityOrdered, BeanDefinitionRegistryPostProcessor, ApplicationContextAware {

    private final static Logger LOG = LoggerFactory.getLogger(NatureProcessor.class);

    /**
     * 搜索条件：BaseService注解
     */
    private final String annotation;
    /**
     * 搜索条件：扫描包路径数组
     */
    private final String[] basePackage;

    public NatureProcessor() {
        this(NatureService.class.getName(), new String[]{"com.nature.demo"});
    }

    public NatureProcessor(String annotation, String[] basePackage) {
        super();
        this.annotation = annotation;
        this.basePackage = basePackage;
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

    private Set<BeanDefinitionHolder> getBeanDefinitionHolderSet(String... basePackages) {
        BeanDefinitionRegistry registry = new SimpleBeanDefinitionRegistry();
        NatureScanner scanner = new NatureScanner(registry, false);
        //配置过滤条件,是用BaseService标注的类
        TypeFilter includeFilter = (reader, factory) -> reader.getAnnotationMetadata().hasAnnotation(this.annotation);
        scanner.addIncludeFilter(includeFilter);
        return scanner.doScan(basePackages);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        DefaultListableBeanFactory factory = (DefaultListableBeanFactory) beanFactory;
        Set<BeanDefinitionHolder> holders = this.getBeanDefinitionHolderSet(this.basePackage);
        for (BeanDefinitionHolder holder : holders) {
            // 获取bean的类型(经测试此时，bean可以操作)
            try {
                Class<?> cls = Class.forName(holder.getBeanDefinition().getBeanClassName());
                if (!cls.isAnnotationPresent(NatureService.class)) {
                    continue;
                }
                // 将全限定名注册为别名
                this.registerAlias(beanFactory, holder.getBeanName(), cls.getName());
                // 如果不做registerBeanDefinition，那么bean内的注解会无效(如Autowired)
                RootBeanDefinition rbd = new RootBeanDefinition(cls);
                rbd.getConstructorArgumentValues().addGenericArgumentValue(cls);
                rbd.setBeanClass(NatureFactoryBean.class);
                rbd.setAutowireCandidate(true);
                rbd.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
                factory.registerBeanDefinition(holder.getBeanName(), rbd);
            } catch (Exception e) {
                LOG.error("bean definition register error", e);
            }
        }
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("postProcessBeanDefinitionRegistry");
        }
    }

    /**
     * 为bean注册别名
     * 注意：如果别名与bean的ID冲突，放弃别名注册
     * @param factory ConfigurableListableBeanFactory
     * @param beanId  bean的ID
     * @param value   Interface的value
     */
    private void registerAlias(ConfigurableListableBeanFactory factory, String beanId, String value) {
        // 防止别名覆盖bean的ID
        if (!factory.containsBeanDefinition(value)) {
            factory.registerAlias(beanId, value);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ContextHolder.init(applicationContext);
    }
}
