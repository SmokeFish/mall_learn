import com.mmall.util.MD5Util;
import com.mmall.util.PropertiesUtil;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * Created by think9 on 19-12-16.
 */
public class MD5Test {
    @Test
    public void test1() {
        String md5ps = MD5Util.MD5EncodeUtf8("1234567890");
        System.out.println(md5ps);
    }
    @Test
    public void test2(){
       String psalt = PropertiesUtil.getProperty("password.salt", "");
        System.out.println(psalt);
    }
    @Test
    public void test3(){
        System.out.println(MD5Test.class);
        System.out.println(MD5Test.class.getClassLoader().getResource("TestDemo.class"));
    }
}
