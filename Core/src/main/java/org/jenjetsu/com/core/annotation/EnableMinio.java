package org.jenjetsu.com.core.annotation;

import org.jenjetsu.com.core.config.MinioConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(MinioConfiguration.class)
public @interface EnableMinio {
}
