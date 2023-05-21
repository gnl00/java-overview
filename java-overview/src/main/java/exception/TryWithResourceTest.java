package exception;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * TODO
 *
 * @author gnl
 * @since 2023/2/20
 */
public class TryWithResourceTest {
    public static void main(String[] args) {
        File file = new File("./test.txt");

        // Java 7 新增 try-with-resource 语法
        try(
                // 资源会自动关闭，不需要在编写 finally 代码块
                // 前提是使用到的资源都实现了 AutoCloseable 接口
                FileOutputStream fos = new FileOutputStream(file);
                FileInputStream fis = new FileInputStream(file);
            ) {
            String content = "hello world";
            fos.write(content.getBytes());

//            // 方式一
//            int available = fis.available();
//            byte[] buffer = new byte[available];
//            fis.read(buffer, 0, available);
//            String s = new String(buffer);
//            System.out.println(s);
            // 方式二
//            int read = fis.read();
//            String s = "";
//            while (read != -1) {
//                s += String.valueOf((char)read);
//                read = fis.read();
//            }
//            System.out.println(s);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
