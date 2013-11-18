package org.ci4j.web.annotation;

import static java.lang.annotation.ElementType.*;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;

@Target({METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented  
public @interface Path {
	String value();
}
