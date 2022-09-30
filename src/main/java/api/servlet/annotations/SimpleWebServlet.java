package api.servlet.annotations;

import javax.servlet.annotation.WebInitParam;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface SimpleWebServlet {
    String name() default "";

    String[] value() default {};

    String[] urlPatterns() default {};
}
