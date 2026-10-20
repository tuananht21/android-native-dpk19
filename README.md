# 🛒 LEGO Store - Ứng dụng Bán hàng LEGO trên Android Native

**LEGO Store** là ứng dụng di động Android Native được phát triển bằng ngôn ngữ Java, cho phép người dùng xem, tìm kiếm và đặt mua các bộ đồ chơi LEGO trực tuyến. Ứng dụng đồng thời tích hợp bảng điều khiển dành riêng cho Quản trị viên (Admin) để quản lý sản phẩm, duyệt đơn hàng và theo dõi thống kê doanh thu trực quan.

---

## 📌 📑 Mục lục

1. [Tính năng nổi bật](#-tính-năng-nổi-bật)
   - [Dành cho Khách hàng (User)](#1-dành-cho-khách-hàng-user)
   - [Dành cho Quản trị viên (Admin)](#2-dành-cho-quản-trị-viên-admin)
2. [Công nghệ & Thư viện sử dụng](#-công-nghệ--thư-viện-sử-dụng)
3. [Cấu trúc thư mục dự án](#-cấu-trúc-thư-mục-dự-án)
4. [Kiến trúc & Hệ thống Backend](#-kiến-trúc--hệ-thống-backend)
5. [Hướng dẫn Cài đặt & Chạy ứng dụng](#-hướng-dẫn-cài-đặt--chạy-ứng-dụng)
6. [Danh sách API Endpoints](#-danh-sách-api-endpoints)

---

## ✨ Tính năng nổi bật

### 1. Dành cho Khách hàng (User)
* 🔐 **Đăng ký & Đăng nhập:** Cho phép người dùng tạo tài khoản mới và đăng nhập an toàn. Quản lý phiên làm việc qua `UserSessionManager` (SharedPreferences).
* 🏠 **Trang chủ & Danh mục sản phẩm:** 
  * Lọc sản phẩm theo danh mục (LEGO Star Wars, Technic, City,...).
  * Tìm kiếm sản phẩm theo từ khóa trực tiếp.
  * Phân trang linh hoạt (Pagination) giúp tối ưu hiệu năng tải dữ liệu.
* 📦 **Chi tiết sản phẩm:** Xem hình ảnh, mô tả chi tiết, giá bán, số lượng tồn kho và thêm vào giỏ hàng.
* 🛒 **Quản lý Giỏ hàng:** Xem danh sách sản phẩm đã thêm, thay đổi số lượng, xóa mục giỏ hàng và tính tổng tiền tự động.
* 💳 **Đặt hàng (Checkout):** Nhập địa chỉ giao hàng, số điện thoại nhận hàng và ghi chú đơn hàng.
* 📋 **Lịch sử đơn hàng:** Theo dõi trạng thái đơn hàng (Đang chờ xử lý, Đang giao, Hoàn thành, Đã hủy) và hỗ trợ hủy đơn khi chưa giao.
* 👤 **Trang cá nhân:** Cập nhật thông tin cá nhân (số điện thoại, địa chỉ) và đổi mật khẩu.

### 2. Dành cho Quản trị viên (Admin)
* 📊 **Admin Dashboard & Thống kê:**
  * Thống kê tổng quan: Tổng doanh thu, Đơn hàng chờ xử lý, Tổng số sản phẩm và Tổng số khách hàng.
  * Biểu đồ doanh thu trực quan tích hợp thư viện **MPAndroidChart**.
* ➕ **Quản lý Sản phẩm:** Thêm sản phẩm mới kèm hình ảnh, danh mục, giá bán, mô tả và số lượng tồn kho.
* 📑 **Quản lý Đơn hàng:** Xem toàn bộ đơn hàng của hệ thống và cập nhật trạng thái đơn hàng (Duyệt đơn, Đang giao, Hủy,...).
* 🚪 **Điều hướng Admin:** Sidebar riêng cho Admin với chức năng chuyển đổi nhanh giữa chế độ Admin và Đăng xuất.

---

## 🛠 Công nghệ & Thư viện sử dụng

* **Ngôn ngữ lập trình:** Java (JDK 11)
* **Nền tảng phát triển:** Android SDK (Compile SDK 35, Min SDK 23)
* **Giao diện (UI):** AndroidX Material Components, ConstraintLayout, DrawerLayout, BottomNavigationView, RecyclerView, CardView
* **Kết nối Mạng & API (Networking):**
  * **Retrofit 2 (`2.9.0`)**: Xử lý HTTP Request / Response RESTful API
  * **Gson Converter (`2.9.0`)**: Parse dữ liệu JSON từ Server thành Java Object
* **Tải & Caching hình ảnh:**
  * **Glide (`4.16.0`)**: Tải hình ảnh sản phẩm từ Server XAMPP một cách mượt mà và tối ưu bộ nhớ
* **Biểu đồ & Thống kê:**
  * **MPAndroidChart (`v3.1.0`)**: Vẽ biểu đồ cột (BarChart) doanh thu trực quan
* **Backend Stack:**
  * PHP (RESTful API endpoints)
  * CSDL MySQL / MariaDB (chạy qua XAMPP hoặc Web Server)

---

## 📂 Cấu trúc thư mục dự án

```text
com.legostore
├── MainActivity.java                   # Màn hình chính (Trang chủ, danh sách sản phẩm, phân trang)
├── activities/
│   ├── SplashActivity.java             # Màn hình khởi động (Splash Screen)
│   ├── LoginActivity.java              # Màn hình Đăng nhập
│   ├── RegisterActivity.java           # Màn hình Đăng ký
│   ├── ProductDetailActivity.java      # Màn hình Chi tiết sản phẩm
│   ├── CartActivity.java               # Màn hình Giỏ hàng
│   ├── CheckoutActivity.java           # Màn hình Thanh toán / Đặt hàng
│   ├── OrderListActivity.java          # Màn hình Lịch sử đơn hàng của User
│   ├── ProfileActivity.java            # Màn hình Thông tin cá nhân & Đổi mật khẩu
│   ├── AdminDashboardActivity.java     # Bảng điều khiển Admin & Báo cáo thống kê
│   ├── AdminAddProductActivity.java    # Màn hình Admin thêm sản phẩm mới
│   └── AdminOrderManageActivity.java   # Màn hình Admin quản lý & duyệt đơn hàng
├── adapters/
│   ├── DanhMucAdapter.java             # Adapter danh mục sản phẩm
│   ├── SanPhamAdapter.java             # Adapter danh sách sản phẩm
│   ├── GioHangAdapter.java             # Adapter giỏ hàng
│   └── DonHangAdapter.java             # Adapter danh sách đơn hàng
├── api/
│   ├── ApiService.java                 # Định nghĩa các phương thức Retrofit REST API
│   └── RetrofitClient.java             # Khởi tạo Singleton Retrofit instance & Base URL
├── models/
│   ├── DanhMuc.java                    # Model Danh mục
│   ├── SanPham.java                    # Model Sản phẩm
│   ├── MucGioHang.java                 # Model Mục giỏ hàng
│   ├── DonHang.java                    # Model Đơn hàng
│   ├── NguoiDung.java                  # Model Người dùng
│   └── PhanHoiApi.java                 # Model Response chuẩn từ API PHP (success, message, data)
└── utils/
    └── UserSessionManager.java         # Quản lý phiên đăng nhập qua SharedPreferences
```

---

## 🏗 Kiến trúc & Hệ thống Backend

Dự án áp dụng mô hình **Client-Server**:
1. **Android Client (App):** Gửi các yêu cầu HTTP (GET, POST Form-UrlEncoded) qua Retrofit 2 đến PHP API backend.
2. **PHP Backend API (`legostore_api`):** Nhận request, xử lý logic, truy vấn CSDL MySQL và trả về dữ liệu định dạng **JSON**.
3. **Cấu trúc JSON Response mẫu (`PhanHoiApi`):**

```json
{
  "thanh_cong": true,
  "thong_bao": "Thành công",
  "du_lieu": [...]
}
```

---

## 🚀 Hướng dẫn Cài đặt & Chạy ứng dụng

### 1. Chuẩn bị Backend (XAMPP / PHP + MySQL)
1. Cài đặt [XAMPP](https://www.apachefriends.org/) (Apache + MySQL).
2. Tạo CSDL MySQL đặt tên là `legostore_db`.
3. Import các bảng dữ liệu (`nguoi_dung`, `danh_muc`, `san_pham`, `gio_hang`, `don_hang`, `chi_tiet_don_hang`).
4. Copy thư mục chứa các file API PHP (`legostore_api`) vào thư mục `htdocs` của XAMPP (Ví dụ: `C:/xampp/htdocs/legostore_api/`).

### 2. Cấu hình IP Server trên Android App
1. Kiểm tra địa chỉ IP LAN của máy tính đang chạy XAMPP (ví dụ: `192.168.1.x` hoặc `192.168.43.130`).
2. Mở file [`RetrofitClient.java`](file:///D:/legostore/app/src/main/java/com/legostore/api/RetrofitClient.java) và thay đổi `BASE_URL`:

```java
public class RetrofitClient {
    public static final String BASE_URL = "http://<IP_MAY_TINH_CUA_BAN>/legostore_api/";
    ...
}
```

> **Lưu ý:** Nếu chạy ứng dụng trên Android Emulator, bạn có thể dùng `http://10.0.2.2/legostore_api/`. Nếu chạy trên thiết bị thật, máy tính và điện thoại phải kết nối cùng mạng Wi-Fi.

### 3. Mở & Biên dịch Dự án trên Android Studio
1. Mở **Android Studio** -> Chọn **Open** -> Chọn thư mục `legostore`.
2. Đợi Gradle Sync hoàn tất.
3. Nhấn **Run (Shift + F10)** để cài đặt và chạy ứng dụng trên Emulator hoặc thiết bị thật.

---

## 📡 Danh sách API Endpoints chính

| Phương thức | API Endpoint | Mô tả |
| :--- | :--- | :--- |
| `POST` | `dang_ky.php` | Đăng ký tài khoản người dùng mới |
| `POST` | `dang_nhap.php` | Đăng nhập tài khoản |
| `GET` | `lay_danh_muc.php` | Lấy danh sách danh mục LEGO |
| `GET` | `lay_san_pham.php` | Lấy danh sách sản phẩm (có hỗ trợ lọc, tìm kiếm, phân trang) |
| `POST` | `them_gio_hang.php` | Thêm sản phẩm vào giỏ hàng |
| `GET` | `lay_gio_hang.php` | Lấy danh sách sản phẩm trong giỏ hàng |
| `POST` | `cap_nhat_gio_hang.php` | Cập nhật số lượng sản phẩm trong giỏ |
| `POST` | `xoa_gio_hang.php` | Xóa sản phẩm khỏi giỏ hàng |
| `POST` | `tao_don_hang.php` | Đặt hàng |
| `GET` | `lay_don_hang.php` | Lấy lịch sử đơn hàng của người dùng |
| `POST` | `huy_don_hang.php` | Hủy đơn hàng |
| `POST` | `doi_mat_khau.php` | Đổi mật khẩu người dùng |
| `POST` | `cap_nhat_thong_tin.php` | Cập nhật thông tin cá nhân |
| `POST` | `admin/them_san_pham.php` | *(Admin)* Thêm sản phẩm mới |
| `POST` | `admin/cap_nhat_don_hang.php` | *(Admin)* Cập nhật trạng thái đơn hàng |
| `GET` | `admin/thong_ke_doanh_thu.php` | *(Admin)* Lấy dữ liệu thống kê doanh thu |

---

## 📝 Giấy phép (License) & Tác giả

Dự án được xây dựng cho mục đích học tập / đồ án ứng dụng di động Android.
