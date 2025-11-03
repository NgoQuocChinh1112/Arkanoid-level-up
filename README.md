# Arkanoid-level-up
-Link video :https://drive.google.com/drive/folders/1D-iQodEMBZZD5dM7hFMtvFy7KHQZ17bO?usp=sharing
*Thành viên
-Ngô Quốc Chính
-Lê Trịnh Quốc Quân
-Trần Đạt Khôi Nguyên
-Mai Quang Diệp
1. Giới thiệu chung
-Arkanoid là một trò chơi hành động – phản xạ thuộc thể loại Breakout cổ điển, nơi người chơi điều khiển paddle để đánh bóng (ball) phá các viên gạch (bricks).
Phiên bản Arkanoid này được phát triển mở rộng, bổ sung nhiều tính năng hiện đại và phong phú, bao gồm:
+10 màn chơi (10 levels) với độ khó tăng dần.
+2 loại gạch: gạch tĩnh (Static Brick) và động (Moving Brick).
+6 loại gạch với 5 màu và 5 độ cứng khác nhau, trong đó gạch tím (Purple Brick) là Unbreakable Brick – không thể phá.
+7 loại Power-Up đa dạng với thời gian hiệu lực khác nhau.
+Âm thanh chân thực gồm nhạc nền, âm va chạm và âm hiệu ứng Power-Up.
+Chế độ 1 người chơi,2 người cùng chơi và 2 người chơi cạnh tranh (Competitive Mode).
+Hệ thống điểm, mạng, và lưu Highest Score (điểm cao nhất).
2. Giao diện người dùng (User Interface)
2.1. Màn hình chính (Menu)
-Game có 5 nút chức năng chính:
+ ▶: Play Game – Bắt đầu chơi game.
+ ☰:Select Level – Chọn màn chơi từ 1 đến 10.
+ 👤 1 Player or 2 Players – Bắt đầu chế độ 1 người chơi hoặc 2 người cùng chơi
+ ⚔ :2 PlayersCompetitve – Bắt đầu chế độ đấu 2 người chơi.
+ 🞩: Exit – Thoát khỏi trò chơi.
2.2. Giao diện trong game
-Hiển thị điểm (Score), mạng (Lives)
-Khi 2 người chơi, giao diện có thêm:
+Timer (đếm ngược thời gian đấu).
+Điểm và mạng riêng cho từng người.
+ Nếu là 2 người cùng chơi thì có 2 paddle cùng nằm ở phía dưới nhưng khác độ cao (Paddle 2 cao hơn Paddle 1 một khoảng).
3. Cấu trúc màn chơi (Level Design)
-Gồm 10 màn chơi (10 levels).
-Mỗi màn có số lượng và vị trí gạch khác nhau, độ khó tăng dần.
4. Gạch (Bricks)
4.1. Phân loại
Static Brick – Gạch đứng yên, bố trí cố định.
Moving Brick – Gạch di chuyển ngang qua lại, tăng độ khó.
4.2. Các loại gạch
-Brick Trắng: Vỡ sau 1 lần chạm
-Brick Xanh Dương: Vỡ sau 2 lần chạm
-Brick Xanh lá: Vỡ sau 3 lần chạm
-Brick Vàng: Vỡsau 4 lần chạm
-Brick Đỏ: Vỡsau 5 lần chạm
-Brick Tím(Unbreakable Brick):không thể phá
4.3. Điểm số
-Mỗi viên gạch bị phá: +100 điểm.
-Gạch tím không thể bị phá nên không cho điểm.
5. Power-Up (Vật phẩm hỗ trợ)
-Game có 7 loại Power-Up, có xác suất 20% xuất hiện ngẫu nhiên 1 trong 7 loại khi có 1 gạch bị phá
+ExpandPaddle	EP	Mở rộng paddle,
+ShrinkPaddle	S	Thu nhỏ paddle
+FastBall	F	Tăng tốc độ bóng
+BigBall	B	Làm bóng to hơn
+ExplosiveBall	E	Bóng nổ, 
+ExtraLife	EX	Cộng thêm 1 mạng
+DoubleBall	D	Tạo thêm 1 bóng phụ, bóng phụ biến mất khi rơi xuống
-Mỗi Power-Up có hiệu lực trong 2–5 giây, riêng DoubleBall không có thời gian giới hạn, mà biến mất khi bóng phụ rơi khỏi màn hình.
6. Âm thanh (Sound System)
Game có 3 loại âm thanh chính được quản lý bởi lớp SoundEffect:
Loại âm thanh	Mô tả
🔊 Collision Sound	Phát khi bóng chạm tường, gạch, hoặc paddle
🎵 Background Music	Nhạc nền phát trong khi chơi, khác nhau theo từng level
✨ Power-Up Sound	Khi người chơi ăn hoặc kích hoạt vật phẩm hỗ trợ
7. Cơ chế gameplay
7.1. Cơ bản
-Người chơi điều khiển paddle để đánh bóng phá gạch.
-Bóng rơi khỏi màn hình → mất 1 mạng.
-Khi hết tất cả mạng → Game Over.
-Khi phá hết gạch có thể phá → qua màn kế tiếp.
7.2. Cơ chế điểm và mạng
-Người chơi khởi đầu với 3 mạng (Lives = 3).
-Game lưu lại Highest Score (điểm cao nhất đạt được).
8. Chế độ chơi
8.1. Chế độ 1 người (Single Player)
-Màn hình hiển thị:
+Paddle
+bóng
+gạch
+điểm
+số mạng
Mục tiêu: đạt điểm cao nhất và so sánh với Highest Score
8.2 Chế độ 2 người(Multiplayer)
-Màn hình hiển thị:
+ 2 Paddles
+ Bóng
+ Gạch
+ Điểm
+ Số mạng
8.2. Chế độ 2 người (Competitive Mode)
-Chia màn hình ra làm đôi
-Mỗi người có:
+Điểm, mạng và bóng riêng.
+Kết thúc khi 1 trong 2 người hết mạng trước
+Có timer (đếm thời gian) – khi hết giờ:
+Người có điểm cao hơn là người thắng.
+Bóng,gạch,paddle power-up hoạt động độc lập giữa hai người.
+Người chơi điều khiển bằng bộ phím riêng biệt ( A/D và ←/→).
9. Cấu trúc chương trình (Class Diagram Overview)
9.1. Các lớp chính
+GameObject:đại diện cho mọi đối tượng có vị trí, kích thước, hình ảnh
+MoveableObject:Kế thừa GameObject thêm vận tốc và hướng di chuyển
+Ball:Kế thừa MoveableObject	Quản lý vị trí, va chạm, tốc độ và hướng của bóng
+Brick:Kế thừa MoveableObject	Quản lý loại gạch, độ cứng
+Paddle	Thanh điều khiển của người chơi
+PowerUp	Lớp cha cho tất cả power-up 
+FastBallPowerUp	Tăng tốc bóng
+BigBallPowerUp	Làm bóng lớn hơn
+ExpandPaddlePowerUp	Mở rộng paddle
+ShrinkPaddlePowerUp	Thu nhỏ paddle
+ExplosivePowerUp	Cho bóng khả năng nổ
+DoubleBallPowerUp	Sinh thêm bóng phụ
+ExtraLifePowerUp	Cộng thêm 1 mạng
+Renderer	Vẽ các đối tượng, background, HUD
+SoundEffect	Phát âm thanh và nhạc nền
+Menu	Quản lý giao diện menu chính và các nút chức năng
+Level	Lưu thông tin bố cục gạch cho từng màn
+LevelPanel	Hiển thị giao diện chọn màn chơi
+GamePanel	Vòng lặp chính (update, render, input)
+GameManager	Điều khiển logic tổng thể: điểm, mạng, level, reset, pause
+Competitive kế thừa gameManager	Quản lý chế độ 2 người chơi (timer, điểm riêng, paddle riêng)
+Main	Điểm khởi động chương trình, khởi tạo các thành phần chính
10. Tổng kết
- 10 màn chơi với số lượng và loại gạch khác nhau
- 2 loại gạch: tĩnh và động
- 6 loại gạch – có 5 mức độ cứng, gạch tím không thể phá
- 7 Power-Up độc đáo (2–5s hiệu lực, DoubleBall đặc biệt)
- Âm thanh: va chạm, nhạc nền, power-up
- 3 mạng ban đầu, 100 điểm/gạch, Highest Score
- Chế độ 1 hoặc 2 người và 2 người cạnh tranh (competitive)
- Ở chế độ 2 người cạnh tranh: Màn hình chia đôi, mỗi người có điểm và mạng riêng
- Giao diện có 5 nút chính: Play, Exit, Select Level, 1 Player/2 Players, 2 Players Competitve
