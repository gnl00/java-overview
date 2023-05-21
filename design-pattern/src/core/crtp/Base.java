package core.crtp;

/**
 * Base
 *
 * @since 2023/2/15
 * @author gnl
 */
public class Base extends AbstractBase<Base> {
    @Override
    void method() {
        System.out.println("this is base method");
    }
}
