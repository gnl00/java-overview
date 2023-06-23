package jndi;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * TODO
 *
 * @author gnl
 * @since 2023/2/24
 */
public class JNDITest {
    public static void main(String[] args) {
        Hashtable<String, String> env = new Hashtable<>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.dns.DnsContextFactory");
        env.put(Context.PROVIDER_URL, "dns://114.114.114.114");

        try {
            DirContext context = new InitialDirContext(env);
            DirContext lookup = (DirContext) context.lookup("douban.com");
            Attributes attributes = lookup.getAttributes("", new String[]{"A"});
            System.out.println(attributes);
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
    }
}
