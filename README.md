
## websocket-client为java客户端
  使用`Java-WebSocket`  提供错误重连
## 使用方法
其他客户端如果想要请求，则需要严格按照`Message`实体类传输 实体类中的 `path`对应注解的 `value`

#### 第三方引入说明 
mvn install 本项目到本地仓库
Message数据结构如下：
```json
{
  "fromUser": "bigscreen",
  "toUser": "test",
  "msgType": "0",
  "content": {
    "path": "/test",
    "msgQuery": {
      "queryContent": {
        "test": "sss"
      },
      "queryType": "test"
    },
    "socketResult": {
    }
  }
}
```
### 配置文件说明
```yaml
ws-client:
  reconnect-time: 5000
  reflection-path: 'cn.beichenhpy' #注解需要扫描的目录
  web-socket-server-uri: 'ws://localhost:9999/beichenhpy/ws/backend?req=ok' #websocketServerUri
```
### WsClient使用说明
本第三方jar包不提供WsClient的bean，由于能力问题，无法处理一些自定义，因此需要手动注册bean
```java
@Configuration
public class MyWsClient {
    //注入配置类
    private final WsClientProperties wsClientProperties;
    public MyWsClient(WsClientProperties wsClientProperties){
        this.wsClientProperties = wsClientProperties;
    }

    /**
     * 注入WsClient 可以进行一些生成token之类的
     * @return WsClient
     * @throws URISyntaxException URI转换异常
     */
    @Bean
    public WsClient wsClient() throws URISyntaxException {
        String webSocketServerUri = wsClientProperties.getWebSocketServerUri();
        String token = "?req=yes";
        String newUri = webSocketServerUri + token;
        return new WsClient(new URI(newUri),wsClientProperties.getReflectionPath(),wsClientProperties.getReconnectTime());
    }

}
```
