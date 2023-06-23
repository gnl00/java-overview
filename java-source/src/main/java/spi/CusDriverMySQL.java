package spi;

/**
 * TODO
 *
 * @author gnl
 * @since 2023/2/25
 */
public class CusDriverMySQL implements CusDriver {
    @Override
    public void load() {
        System.out.println("MySQL driver loaded");
    }
}
