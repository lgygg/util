package com.lgy.util;

import android.content.Context;
import android.net.Proxy;
import android.os.Build;
import android.text.TextUtils;

import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;

import static android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH;

/**
 * author : LGY
 * time   : 2020/11/02
 * desc   : 代理检查
 * version: 1.0
 *
 * Android是提供单个接口访问不带代理的
 * URL url = new URL(urlStr);
 * urlConnection = (HttpURLConnection) url.openConnection(Proxy.NO_PROXY);
 *
 * okhttp也提供了不代理的方法：
 * OkHttpClient client = new OkHttpClient().newBuilder().proxy(Proxy.NO_PROXY).build（）;
 *
 * 下面是设置代理的例子：
 * import java.io.IOException;
 * import java.io.PrintStream;
 * import java.net.InetSocketAddress;
 * import java.net.MalformedURLException;
 * import java.net.Proxy;
 * import java.net.URL;
 * import java.net.URLConnection;
 * import java.util.Scanner;
 *
 * public class ProxyTest {
 *     final String PROXY_ADDR = "172.20.230.5";
 *     final int PROXY_PORT = 3128;
 *     String urlStr = "http://www.baidu.com";
 *     //String urlStr = "http://www.crazyit.org";
 *
 *     public void init() throws IOException {
 *         URL url = new URL(urlStr);
 *         Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(PROXY_ADDR, PROXY_PORT));
 *         //使用代理服务器打开链接
 *         URLConnection conn = url.openConnection(proxy);
 *         //URLConnection conn = url.openConnection();
 *         conn.setConnectTimeout(5000);
 *         try {
 *             Scanner scan = new Scanner(conn.getInputStream());
 *             PrintStream ps = new PrintStream("index.html");
 *             while (scan.hasNextLine()) {
 *                 String line = scan.nextLine();
 *                 System.out.println(line);
 *                 ps.println(line);
 *             }
 *         } catch (IOException e) {
 *             e.printStackTrace();
 *         }
 *     }
 *
 *     public static void main(String[] args) throws IOException {
 *         new ProxyTest().init();
 *     }
 * }
 */
public class ProxyCheckUtil {
    /**
     * 是否使用vpn
     * @return
     */
    public static boolean isVpnUsed() {
        try {
            Enumeration niList = NetworkInterface.getNetworkInterfaces();
            ArrayList<NetworkInterface> Alist = Collections.list(niList);
            if(Alist != null) {
                for (NetworkInterface intf :Alist) {
                    if(!intf.isUp() || intf.getInterfaceAddresses().size() == 0) {
                        continue;
                    }
                    if ("tun0".equals(intf.getName()) || "ppp0".equals(intf.getName())){
                        return true; // The VPN is up
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * wifi下是否使用代理
     * @param context
     * @return
     */
    public static boolean isWifiProxy(Context context) {
        String host;
        String port;
        if (Build.VERSION.SDK_INT >= ICE_CREAM_SANDWICH) {
            host = System.getProperty("http.proxyHost");
            port = System.getProperty("http.proxyPort");
        } else {
            host = Proxy.getHost(context);
            port = String.valueOf(Proxy.getPort(context));
        }
        return !TextUtils.isEmpty(host) && !TextUtils.isEmpty(port);
    }

}
