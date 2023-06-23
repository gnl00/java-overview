package annotation;

import java.lang.annotation.*;

/**
 * TODO
 *
 * @author gnl
 * @since 2023/2/22
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(MyAnnos.class)
public @interface MyAnno {
    String value() default  "";
}
