package hw;

import java.util.*;

/**
 * 开发一个简单错误记录功能小模块，能够记录出错的代码所在的文件名称和行号。
 *
 * @author gnl
 * @since 2023/4/14
 */
public class HJ19 {

    private static List<File> list = new ArrayList<>();
    private static Map<String, File> map = new LinkedHashMap<>();

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLine()) {
            String str = sc.nextLine();
            record(str);
        }

        int fromIndex = map.entrySet().size() > 8 ? map.entrySet().size() - 8 : 0;
        int index = 0;
        Iterator<Map.Entry<String, File>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            if (index >= fromIndex) {
                Map.Entry<String, File> next = iterator.next();
                File err = next.getValue();
                System.out.println(err.name.substring(0, err.name.indexOf("-")) + " " + err.line + " " + err.count);
            } else {
                iterator.next();
                iterator.remove();
            }

            index++;
        }
    }

    public static void record(String str) {
        int lastIndex = str.lastIndexOf("\\");
        String line = str.substring(lastIndex + 1);
        String[] split = line.split(" ");
        String originKey = split[0];
        String filename = originKey.length() > 16 ? originKey.substring(originKey.length() - 16) : originKey;
        String le = split[1];
        String key = filename + "-" +le;
        if (!map.containsKey(key)) {
            map.put(key, new File(key, Integer.valueOf(le), 1));
        } else {
            int preCount = map.get(key).count;
            map.get(key).count = preCount + 1;
        }
    }
}

class File {
    String name;
    int line;
    int count;

    public File(String name, int line) {
        this.name = name;
        this.line = line;
    }

    public File(String name, int line, int count) {
        this.name = name;
        this.line = line;
        this.count = count;
    }

    @Override
    public int hashCode() {
        return name.hashCode() + line;
    }

    @Override
    public boolean equals(Object obj) {
        File f = (File) obj;
        return this.name == f.name && this.line == f.line;
    }
}
