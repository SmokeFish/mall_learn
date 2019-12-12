import com.mmall.util.PropertiesUtil;

import java.net.SocketPermission;

/**
 * Created by think9 on 19-12-10.
 */
public class TestDemo {
    public static void main(String[] args) {
        System.out.printf(PropertiesUtil.getProperty("ftp.server.http.prefix"));
    }
}
