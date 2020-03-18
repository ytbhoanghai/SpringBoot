<a href="https://imgur.com/irPHp0a"><img src="https://i.imgur.com/irPHp0a.png" align="right"/></a>
# SpringWebSocket.chat

**Table of Contents**


## Đôi lời
Đây là phần cuối cùng trong bản demo tìm hiểu và xây dựng ứng dụng chat với SSE và Websocket.

Mô tả: Phần này sẽ xây dựng ứng dụng cho phép thực hiện đăng nhập và chat với user khác (user to user). Giao diện sẽ như sau:

[![18-03-2020-1.png](https://i.postimg.cc/qvB7vP8F/18-03-2020-1.png)](https://postimg.cc/1fTSWY70)

Các tài liệu tham khảo chính bao gồm: [javascript](https://javascript.info/), [baeldung](https://www.baeldung.com/spring-websockets-send-message-to-user), [stackoverflow](https://stackoverflow.com/questions/22367223/sending-message-to-specific-user-on-spring-websocket), document of spring ([document-1](https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html#websocket-stomp-authentication), [document-1](https://docs.spring.io/spring-framework/docs/4.1.4.RELEASE/spring-framework-reference/html/websocket.html)) cùng một số nguồn khác.

## Nhiệm vụ của tôi là gì?

  * Thiết kế database (mongodb)
  * Xây dựng mã nguồn phía back-end
  * Xây dựng mã nguồn (javascript and bootstrap) phía front-end

## Tôi đã làm gì?

**database** 

Tôi sử dụng nosql (mongodb) vì tốc độ xử lý CRUD nhanh do không phải kiểm tra các ràng buộc quan hệ và nhiều thứ khác. Nó là 1 lựa chọn thích hợp hơn cho các ứng dụng realtime như app chat thay vì sql.

Vì tôi sử dụng Spring Data kết hợp với Spring boot nên không cần thực hiện nhiều bước thủ công để kết nối đến database mà chỉ cần extends interface MongoRepository, và đây là 1 ví dụ:

```java
@Repository
public interface ChatChannelRepository extends MongoRepository<ChatChannel, String> {

    @Query(value = "{userNames: {$all: ?0}}")
    Optional<ChatChannel> findByUserNames(List<String> userNames);

    @Query(value = "{_id: {$in: ?0}}")
    List<ChatChannel> findByListId(List<String> ids);

}
```

À quên, ánh xạ thực thể nữa chứ:

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document
@TypeAlias("chatChannel")
public class ChatChannel {

    @Id
    private String id;
    private Date createDate;
    private List<String> userNames;
    @DBRef
    private Message messageRecently;

}
```

**backend**

Tôi chỉ liệt kê 2 điểm đáng lưu ý ở phía backend đó là cấu hình MessageBroker sử dụng là *queue* (bỏ qua spring security, ...).

```java
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketMessageBrokerConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/listen").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/queue/reply");
        registry.setApplicationDestinationPrefixes("/app");
    }
}
```


> Nói làm sao ta ?: Chúng ta có topic để client subscribe và là nơi để server đẩy thông báo vào socket. (xem phần [Chat In Topic](https://github.com/ytbhoanghai/SpringBoot/tree/master/Chat%20With%20Websocket) và [Chat In Room](https://github.com/ytbhoanghai/SpringBoot/tree/master/Chat%20With%20Websocket/chat%20in%20room)), nhiều client cùng subscribe 1 topic để lắng nghe dữ liệu trong socket tương ứng topic đó. Vậy? làm sao để gửi dữ liệu đến 1 user cụ thể? đó là queue.


Điểm lưu ý thứ 2 không thuộc phần kỹ thuật lắm, nói chung là tôi sẽ tạo ra 1 Channel cho mỗi liên kết giữa user to user, đây là nơi sẽ chứa tất cả các thông điệp tin nhắn qua lại:

```bash
Microsoft Windows [Version 10.0.18363.720]
(c) 2019 Microsoft Corporation. All rights reserved.

C:\Users\youto>mongo
MongoDB shell version v4.2.3
connecting to: mongodb://127.0.0.1:27017/?compressors=disabled&gssapiServiceName=mongodb
Implicit session: session { "id" : UUID("ee7825fc-4010-464f-8912-d5aef4079017") }
MongoDB server version: 4.2.3
> use admin
switched to db admin
> db.auth("ytbhoanghai", "hoanghai1018")
1
> show dbs
admin                  0.000GB
config                 0.000GB
demo-application-chat  0.000GB
demoMongodb            0.013GB
local                  0.000GB
> use demo-application-chat
switched to db demo-application-chat
> show collections
chatChannel
message
user
>
```

Bạn có thể mở và xem database trực quan hơn với phần mềm robo3t để hiểu cách mà tôi xác định và lưu trữ dữ liệu cũng như cấu trúc các thực thể.


**front-end**

Mỗi user sau khi authentication (có thể là xác thực với form hoặc xác thực với jwt) sẽ subscribe *"/user/queue/..."*

```javascript
let webSocket = new SockJS('/listen');
let stompClient = Stomp.over(webSocket);
stompClient.connect({}, onConnected);

function onConnected() {
    stompClient.subscribe("/user/queue/reply", function (payload) {
        // Cái gì đó sẽ được thực hiện ở đây khi có dữ liệu đẩy vào
    })
}
```

Tại đây, để gửi dữ liệu đến server, client gọi function send với cú pháp ví dụ như sau:

```javascript
stompClient.send(`/app/sendMessage.to/${idChatChannel}`, {}, content);
```

## Tôi đã rút ra được gì?

  * Thiết kế batabase chưa tối ưu, (yêu cầu truy xuất còn nhiều).
  * Làm sao để xác định được số lượng tin nhắn đang đọc?
  * Làm sao để xác định 1 người là ĐANG nhập tin nhắn vào ô trả lời? (sự kiện thay đổi nội dung ô input có thể gửi message với status Typing...)
  * Tôi muốn gửi emoji thì sao nhỉ?
  * Hình như tôi nên cache avatar để nhanh hơn nhỉ?
  * Viết mã nguồn javascript chưa clear lắm.
  * Các chức năng về cơ bản là đã được hoàn thiện và cũng có thể vận hành.


