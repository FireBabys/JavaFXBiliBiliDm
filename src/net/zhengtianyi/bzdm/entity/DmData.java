package net.zhengtianyi.bzdm.entity;

/**
 * 类名 ClassName  DmData
 * 项目 ProjectName  JavaFXBiliBiliDm
 * 作者 Author  郑添翼 Taky.Zheng
 * 邮箱 E-mail 275158188@qq.com
 * 时间 Date  2019-05-31 11:07 ＞ω＜
 * 描述 Description TODO
 */
public class DmData {

    private String nickname;    //用户昵称
    private String text;        //用户信息
    private String timeline;    //发送时间

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTimeline() {
        return timeline;
    }

    public void setTimeline(String timeline) {
        this.timeline = timeline;
    }
}
