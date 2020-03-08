#Chat With Spring Websocket
## 1. Scenario
* User đăng nhập vào hệ thống bằng 1 trong các tài khoản sau **Username = $or (user1, user2, user3), Password = 123**
* User tạo room bằng cách nhập vào tên room và click nút _Create_
    * Hệ thống tạo room và trả về cho trình duyệt tên room và id.
    * User chọn room trong phần _select_, nhấn vào nút _copy to clipboard_ để copy id room.
    * Chia sẽ id room với user khác.
* User join vào 1 room bằng cách nhập id room vào input text _Name Room_ sau đó nhấn nút _Join_
* Chọn room ở phần _select_ để vào room.
  
## 2. Note
1. Những user trong cùng 1 room mới nhìn thấy được tin nhắn của nhau.
2. Tên room là không trùng nhau cho cùng 1 user. **ex:** user1 có đã tạo hoặc join room với tên ABC sẽ không thể tạo room tên ABC và cũng không thể join đến room đó nữa. User chưa tạo hoặc join room có thể tạo room có cùng tên với room của user khác đã tạo.

## 3. Technology
**Backend**
* Spring (boot, websocket, data mongodb, security).

**Frontend**
* Javascript and jquery (lib sockjs and stomp)

## 4. References
[Tài liệu 1 - Tiếng Anh](https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html#websocket-stomp-authentication)

[Tài liệu 2 - Tiếng Anh](https://docs.spring.io/spring-framework/docs/4.1.4.RELEASE/spring-framework-reference/html/websocket.html)

## 5. Idea
1. Trang cho phép tạo room bằng cách nhập tên room vào 1 input text. Hệ thống tạo room và trả về mã room. Join người tạo vào room.
2. User khác join room bằng cách nhập mã room vào 1 input text. Hệ thống lưu username của user đó vào csdl sau đó trả về kết quả cho client, nếu thành công Js tại client sẽ tiến hành  subcribe tại /topic/room/{mã room}.
3. Khi 1 ai đó nhắn tin vào room. Hệ thống tiến hành lưu message vào csdl dạng {content, author, date} sau đó tiếp tục lưu message vào room {id, name, messages, users}. Tiếp theo gửi message đến room tương ứng ( xem tiếp [6.1] )
4. Thông tin user lưu trữ bao gồm {urlAvatar, fullName, username, password, role}.
5. Ban đầu chưa join room hoặc chưa tạo room nào thì sẽ không có danh sách room, nếu join room thì danh sách room hiện ra. Js tiến hành subcribe tất cả các room đó.
6. Bấm vào 1 room, hệ thống sẽ tiến hành tải messages của room đó ( vào room find({id: ?id}) ). [6.1] Sau khi nhận được messages tại browser, trong function subcribe js sẽ tiến hành kiểm tra xem user có đang active room tương ứng không? ( dựa vào id room), nếu có sẽ show message đó lên.
7. Ban đầu tải trang, cập nhật số lượng room của 1 user và subcribe. Khi có hành động làm mới trang, disconntect websocket