package com.example.study;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.*;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class ConditionalTest {
    @Test
    void conditional() {
        //true
        ApplicationContextRunner contextRunner = new ApplicationContextRunner();
        contextRunner.withUserConfiguration(Config1.class).run(context -> {
            assertThat(context).hasSingleBean(MyBean.class); // MyBean이 등록되어 있는지 체크
            assertThat(context).hasSingleBean(Config1.class);
        });

        // false
        new ApplicationContextRunner().withUserConfiguration(Config2.class).run(context -> {
            assertThat(context).doesNotHaveBean(MyBean.class); // MyBean이 등록되어 있지 않은지 체크
            assertThat(context).doesNotHaveBean(Config1.class);
        });
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @Conditional(BooleanCondition.class)
    @interface BooleanConditional {
        boolean value();
    }


    @Configuration
    @BooleanConditional(true)
    static class Config1 {
        @Bean
        MyBean bean() {
            return new MyBean();
        }
    }

    @Configuration
    @BooleanConditional(false)
    static class Config2 {
        @Bean
        MyBean bean() {
            return new MyBean();
        }
    }

    static class MyBean {

    }


    private static class BooleanCondition implements Condition {
        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            // 모든 metadata의 모든 속성값을 가져옴.
            Map<String, Object> annotationAttributes = metadata.getAnnotationAttributes(BooleanConditional.class.getName());
            Boolean value = (Boolean) annotationAttributes.get("value");
            return value;
        }
    }
}

