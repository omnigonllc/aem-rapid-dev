package com.omnigon.aem.sling.mvp.model.presenter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Presenter {

    enum CachingStrategy {
        REQUEST, NONE
    }

    String[] resourceTypes() default {};

    boolean handleSubTypes() default true;

    CachingStrategy cachingStrategy() default CachingStrategy.NONE;
}

