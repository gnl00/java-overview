package annotation;

import java.lang.annotation.Annotation;
import java.util.Arrays;

/**
 * TODO
 *
 * @author gnl
 * @since 2023/2/22
 */
@MyAnno("annotation-value")
@MyAnno("annotation-value-repeat")
public class AnnotatedElementTest {
    public static void main(String[] args) {
        Class<AnnotatedElementTest> clz = AnnotatedElementTest.class;
        Annotation[] declaredAnnotations = clz.getDeclaredAnnotations();
        Arrays.stream(declaredAnnotations).forEach(System.out::println);
    }
}
