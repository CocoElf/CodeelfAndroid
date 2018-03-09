package cocoelf.codeelfandroid.util;

/**
 * Rest API url接口常量
 * Created by csy on 2017/9/7.
 */

public class RestAPI {

    public static final String PROTOCOL = "http";

    /**
     * 域名／ip地址
     * 需要修改为服务器ip
     * 模拟器默认把127.0.0.1和localhost当做本身了，在模拟器上可以用10.0.2.2代替127.0.0.1和localhost
     */
    public static final String IP = "10.0.2.2";
//    public static final String IP = "192.168.43.15";

    public static final int PORT = 8081;

//    public static final String CONTEXT_PATH = "api";
    public static final String CONTEXT_PATH = "";

    public static final String URL = PROTOCOL + "://" + IP + ":" + PORT ;

}
