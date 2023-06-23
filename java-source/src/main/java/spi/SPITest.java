package spi;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * TODO
 *
 * @author gnl
 * @since 2023/2/25
 */
public class SPITest {
    public static void main(String[] args) {
        ServiceLoader<CusDriver> serviceLoader = ServiceLoader.load(CusDriver.class);
        Iterator<CusDriver> iterator = serviceLoader.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }
}
