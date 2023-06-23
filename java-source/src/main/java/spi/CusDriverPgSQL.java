package spi;

/**
 * TODO
 *
 * @author gnl
 * @since 2023/2/25
 */
public class CusDriverPgSQL implements CusDriver{
    @Override
    public void load() {
        System.out.println("PgSQL driver loaded");
    }
}
