package net.zhengtianyi.bzdm;

import com.alibaba.fastjson.JSONArray;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import net.zhengtianyi.bzdm.entity.DmData;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 类名 ClassName  BzCon
 * 项目 ProjectName  JavaFXBiliBiliDm
 * 作者 Author  郑添翼 Taky.Zheng
 * 邮箱 E-mail 275158188@qq.com
 * 时间 Date  2019-05-31 11:10 ＞ω＜
 * 描述 Description TODO
 */
public class BzCon {


    private String roomid;  //房间ID
    private String csrf_token = "2d91f3896f81bc23c052715e67508590";
    private String csrf = csrf_token;
    private String visit_id = "";

    private Boolean isCon; //是否连接成功
    private HttpURLConnection con;  //连接
    private OutputStreamWriter osw; //输入流
    private BufferedReader bfr; //输入流

    /**
     * 方法名 MethodName BzCon
     * 参数 Params [roomid]
     * 返回值 Return
     * 作者 Author 郑添翼 Taky.Zheng
     * 编写时间 Date 2019-05-31 13:24 ＞ω＜
     * 描述 Description TODO 构造中传入房间ID
     */
    public BzCon(String roomid){
        this.roomid = roomid;
    }

    /**
     * 方法名 MethodName con
     * 参数 Params []
     * 返回值 Return java.net.HttpURLConnection
     * 作者 Author 郑添翼 Taky.Zheng
     * 编写时间 Date 2019-05-31 13:24 ＞ω＜
     * 描述 Description TODO 连接弹幕服务器
     */
    public HttpURLConnection con(){
        try {
            URL url = new URL("https://api.live.bilibili.com/ajax/msg");
            con = (HttpURLConnection)url.openConnection();
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            con.setUseCaches(false);
            con.connect();
            this.isCon = true;
            return con;
        } catch (IOException e) {
            this.isCon = false;
            return null;
        }
    }

    /**
     * 方法名 MethodName getInfo
     * 参数 Params []
     * 返回值 Return javafx.collections.ObservableList<net.zhengtianyi.bzdm.entity.DmData>
     * 作者 Author 郑添翼 Taky.Zheng
     * 编写时间 Date 2019-05-31 13:23 ＞ω＜
     * 描述 Description TODO 获取信息
     */
    public ObservableList<DmData> getInfo() throws IOException {
        //获取输出流,发送数据
        osw = new OutputStreamWriter(con.getOutputStream());
        String conParam = "roomid=" + roomid + "&csrf_token=" + csrf_token + "&csrf=" + csrf + "&visit_id" + visit_id;
        osw.write(conParam);
        osw.flush();
        osw.close();
        //获取输入流,接收数据
        bfr = new BufferedReader(new InputStreamReader(con.getInputStream()));
        StringBuffer sb = new StringBuffer();
        String temp = "";
        while ((temp = bfr.readLine()) != null) {
            sb.append(temp + "\r\n");
        }
        ObservableList<DmData> dmData = msgHandler(sb.toString());
        bfr.close();
        return dmData;

    }

    /**
     * 方法名 MethodName msgHandler
     * 参数 Params [s]
     * 返回值 Return javafx.collections.ObservableList<net.zhengtianyi.bzdm.entity.DmData>
     * 作者 Author 郑添翼 Taky.Zheng
     * 编写时间 Date 2019-05-31 13:23 ＞ω＜
     * 描述 Description TODO 信息处理
     */
    public ObservableList<DmData> msgHandler(String s){
        //使用正则处理字符串
        Pattern pattern = Pattern.compile("\"room\":(.*)\"message\"");
        Matcher matcher = pattern.matcher(s);
        String group ="";
        while (matcher.find()) group = matcher.group();
        //截取字符串
        String substring = group.substring(7, group.length() - 11);
        List<DmData> dmData = JSONArray.parseArray(substring, DmData.class);
        ObservableList<DmData> dmData1 = FXCollections.observableArrayList(dmData);
        return dmData1;
    }



    /**
     * 方法名 MethodName closeCon
     * 参数 Params []
     * 返回值 Return void
     * 作者 Author 郑添翼 Taky.Zheng
     * 编写时间 Date 2019-05-31 13:23 ＞ω＜
     * 描述 Description TODO 关闭连接
     */
    public void closeCon(){
        try {
            bfr.close();
            osw.close();
            con.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public Boolean getCon() {
        return isCon;
    }
    public void setCon(Boolean con) {
        isCon = con;
    }
}
