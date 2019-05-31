package net.zhengtianyi.bzdm;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import net.zhengtianyi.bzdm.entity.DmData;

import java.io.IOException;

/**
 * 类名 ClassName  Main
 * 项目 ProjectName  JavaFXBiliBiliDm
 * 作者 Author  郑添翼 Taky.Zheng
 * 邮箱 E-mail 275158188@qq.com
 * 时间 Date  2019-05-31 10:59 ＞ω＜
 * 描述 Description TODO B站直播弹幕抓取
 */
public class Main extends Application {

    private BzCon bzCon; //连接对象
    TableView<DmData> tableView = new TableView<>();    //列表
    MyService myService = new MyService();  //定时任务


    /**
     * 方法名 MethodName start
     * 参数 Params [primaryStage]
     * 返回值 Return void
     * 作者 Author 郑添翼 Taky.Zheng
     * 编写时间 Date 2019-05-31 13:29 ＞ω＜
     * 描述 Description TODO 界面初始化
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

        Button conBtn = new Button("连接");
        TextField roomIdtf = new TextField("56237");
        Label rid = new Label("房间号");
        roomIdtf.setPromptText("请输入房间ID");
        TextField reTime = new TextField();
        reTime.setPrefWidth(50);
        reTime.setPromptText("秒");
        Button disconBtn = new Button("断开");
        disconBtn.setDisable(true);
        Label reLb = new Label("刷新时间");
        Label label = new Label();
        HBox hBox = new HBox(10,rid,roomIdtf,reLb,reTime,conBtn,disconBtn,label);
        hBox.setAlignment(Pos.CENTER_LEFT);
        BorderPane.setMargin(hBox,new Insets(0,0,10,0));

        //初始化姓名列
        TableColumn<DmData, String> name = new TableColumn<>("姓名");
        name.setCellValueFactory(new PropertyValueFactory<>("nickname"));
        name.setCellFactory(TextFieldTableCell.forTableColumn());
        //初始化消息列
        TableColumn<DmData, String> msg = new TableColumn<>("消息");
        msg.setCellValueFactory(new PropertyValueFactory<>("text"));
        msg.setCellFactory(TextFieldTableCell.forTableColumn());
        //初始化发送时间列
        TableColumn<DmData, String> time = new TableColumn<>("发送时间");
        time.setCellValueFactory(new PropertyValueFactory<>("timeline"));
        time.setCellFactory(TextFieldTableCell.forTableColumn());
        //添加进列表
        tableView.getColumns().addAll(name, msg, time);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        BorderPane root = new BorderPane();
        root.setTop(hBox);
        root.setCenter(tableView);
        root.setPadding(new Insets(10));
        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("B站直播弹幕抓取");
        primaryStage.setWidth(600);
        primaryStage.setHeight(600);
        primaryStage.show();

        //连接按钮事件
        conBtn.setOnAction(p ->{
            String text = roomIdtf.getText();
            String text1 = reTime.getText();
            if (text.trim().length() == 0 || text1.trim().length() == 0){
                label.setText("房间号与刷新时间不能为空");
                return;
            }
            bzCon = new BzCon(text);
            bzCon.con();
            if (bzCon.getCon()) {
                label.setText("连接成功!");
                conBtn.setDisable(true);
                roomIdtf.setDisable(true);
                reTime.setDisable(true);
                disconBtn.setDisable(false);
                myService.setDelay(Duration.seconds(0));
                myService.setPeriod(Duration.seconds(Integer.valueOf(text1)));
                myService.start();
            }else{
                return;
            }
        });

        //断开连接事件
        disconBtn.setOnAction(p -> {
            bzCon.closeCon();
            label.setText("成功断开!");
            conBtn.setDisable(false);
            roomIdtf.setDisable(false);
            reTime.setDisable(false);
            disconBtn.setDisable(true);
            myService.cancel();
            myService.reset();
        });

        //监听定时任务值
        myService.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                tableView.setItems(newValue);
            }
        });

    }

    /**
     * 方法名 MethodName main
     * 参数 Params [args]
     * 返回值 Return void
     * 作者 Author 郑添翼 Taky.Zheng
     * 编写时间 Date 2019-05-31 13:28 ＞ω＜
     * 描述 Description TODO 主函数
     */
    public static void main(String[] args) {
        launch(args);
    }


    /**
     * 方法名 MethodName
     * 参数 Params
     * 返回值 Return
     * 作者 Author 郑添翼 Taky.Zheng
     * 编写时间 Date 2019-05-31 13:22 ＞ω＜
     * 描述 Description TODO 定时任务
     */
    class MyService extends ScheduledService<ObservableList<DmData>>{
        @Override
        protected Task<ObservableList<DmData>> createTask() {
            return new Task<ObservableList<DmData>>() {
                @Override
                protected ObservableList<DmData> call() throws Exception {
                    try {
                        bzCon.con();
                        ObservableList<DmData> info = bzCon.getInfo();
                        return info;
                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }
                }
            };
        }
    }

}
