package com.taobao.easyweb.core.app.deploy;

import java.lang.annotation.*;

/**
 * User: jimmey/shantong
 * DateTime: 13-4-25 обнГ3:36
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Deployer {

    DeployPhase value();

}
