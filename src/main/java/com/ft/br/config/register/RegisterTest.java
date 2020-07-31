package com.ft.br.config.register;

import com.ft.br.model.mdo.City;
import com.ft.util.*;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Stream;

/**
 * RegisterTest
 *
 * @author shichunyang
 * Created by shichunyang on 2020/7/31.
 */
@Component
public class RegisterTest implements BeanDefinitionRegistryPostProcessor {

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
        Stream.of(beanDefinitionRegistry.getBeanDefinitionNames()).forEach(beanDefinitionName -> {
            BeanDefinition beanDefinition = beanDefinitionRegistry.getBeanDefinition(beanDefinitionName);
            String beanClassName = beanDefinition.getBeanClassName();
            if (StringUtil.isNull(beanClassName)) {
                return;
            }
            Class<?> clazz = ClassUtil.resolveClassName(beanClassName, ClassUtil.getDefaultClassLoader());
            ReflectionsUtil.doWithFields(clazz, field -> {
                Register register = ClassUtil.findAnnotation(field, Register.class);
                BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(Acomponent.class, () -> {
                    Acomponent acomponent = new Acomponent();
                    acomponent.setAge(register.age());
                    acomponent.setName(register.name());
                    return acomponent;
                });
                builder.setInitMethodName("init");
                builder.setDestroyMethodName("destroy");
                builder.addPropertyValue("createdAt", DateUtil.getDayStartTime(DateUtil.getCurrentDate()));

                BeanDefinitionBuilder builder2 = BeanDefinitionBuilder.genericBeanDefinition(Bcomponent.class, Bcomponent::new);
                builder2.setInitMethodName("init");
                builder2.setDestroyMethodName("destroy");
                builder2.getBeanDefinition().getPropertyValues().add("goodsService", new RuntimeBeanReference("com.ft.br.service.impl.GoodsServiceImpl"));
                beanDefinitionRegistry.registerBeanDefinition("bCommontReference", builder2.getBeanDefinition());

                builder.addPropertyReference("bcomponent", "bCommontReference");
                beanDefinitionRegistry.registerBeanDefinition("aCommontReference", builder.getBeanDefinition());

                BeanDefinitionBuilder builder3 = BeanDefinitionBuilder.genericBeanDefinition(Ccomponent.class, Ccomponent::new);
                builder3.setInitMethodName("init");
                builder3.setDestroyMethodName("destroy");
                beanDefinitionRegistry.registerBeanDefinition("cCommontReference", builder3.getBeanDefinition());

                Set<String> bDependBeans = Sets.newHashSet();
                String[] bDependsOn = builder2.getBeanDefinition().getDependsOn();
                if (!ArrayUtils.isEmpty(bDependsOn)) {
                    bDependBeans.addAll(Arrays.asList(bDependsOn));
                }
                bDependBeans.add("cCommontReference");
                builder2.getBeanDefinition().setDependsOn(bDependBeans.toArray(new String[]{}));

            }, field -> !Objects.isNull(ClassUtil.findAnnotation(field, Register.class)));
        });

        List<City> cities = Binder.get(SpringContextUtil.getEnvironment()).bind(City.PREFIX, Bindable.listOf(City.class)).orElse(Collections.emptyList());
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
    }
}
