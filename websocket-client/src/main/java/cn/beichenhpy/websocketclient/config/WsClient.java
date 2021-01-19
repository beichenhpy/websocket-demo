package cn.beichenhpy.websocketclient.config;

import cn.beichenhpy.websocketclient.pojo.Message;
import cn.beichenhpy.websocketclient.pojo.SocketResult;
import com.alibaba.fastjson.JSON;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URI;

/**
 * @author beichenhpy
 * @version 1.0
 * @description TODO websocket客户端 单例有问题，改为多例
 * @since 2021/1/15 15:28
 */
@Component
public class WsClient extends WebSocketClient {
    private static final Logger log = LoggerFactory.getLogger(WsClient.class);
    /**
     * 连接成功过一次后设置为true
     */
    public boolean wasConnected = false;
    @Autowired
    URI uri;

    public WsClient(URI uri) {
        super(uri);
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        log.info("[websocket] 连接成功");
        wasConnected = true;
    }

    @Override
    public void onMessage(String message) {
        //解析message
        Message messageJson = JSON.parseObject(message, Message.class);
        SocketResult<Object> test = SocketResult.broadcast(null, "test", "0", null);
        Message message1 = new Message();
        message1.setSocketResult(test);
        message1.setFromUser(messageJson.getToUser());
        message1.setToUser(messageJson.getFromUser());
        String s = JSON.toJSONString(message1);
        send(s);
        log.info("[websocket] 收到消息={}", message);

    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        log.info("[websocket] 退出连接，{}", reason);
    }

    @Override
    public void onError(Exception ex) {
        log.info("[websocket] 连接错误={}", ex.getMessage());
    }
}