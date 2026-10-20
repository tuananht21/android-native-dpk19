package com.legostore.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.legostore.models.NguoiDung;

public class UserSessionManager {
    private static final String PREF_NAME = "legostore_user_pref";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_FULL_NAME = "fullName";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_ROLE = "role";

    private SharedPreferences sharedPreferences;

    public UserSessionManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    // Lưu toàn bộ thông tin người dùng (dùng khi Đăng nhập)
    public void saveUser(NguoiDung user) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_USER_ID, user.getMaNguoiDung());
        editor.putString(KEY_FULL_NAME, user.getHoTen());
        editor.putString(KEY_EMAIL, user.getEmail());
        editor.putString(KEY_PHONE, user.getSoDienThoai());
        editor.putString(KEY_ADDRESS, user.getDiaChi());
        editor.putString(KEY_ROLE, user.getVaiTro());
        editor.apply();
    }

    // Cập nhật riêng Số điện thoại và Địa chỉ (dùng trong màn hình Profile)
    public void saveUserPhoneAndAddress(String phone, String address) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_PHONE, phone);
        editor.putString(KEY_ADDRESS, address);
        editor.apply();
    }

    public int getUserId() {
        return sharedPreferences.getInt(KEY_USER_ID, -1);
    }

    public String getFullName() {
        String name = sharedPreferences.getString(KEY_FULL_NAME, "");
        if (name != null && !name.trim().isEmpty()) {
            return name;
        }
        String email = getEmail();
        if (email != null && email.contains("@")) {
            name = email.split("@")[0];
            if (!name.isEmpty()) {
                return name.substring(0, 1).toUpperCase() + name.substring(1);
            }
        }
        return "Tuấn Anh";
    }

    public String getEmail() {
        return sharedPreferences.getString(KEY_EMAIL, "");
    }

    public String getPhone() {
        return sharedPreferences.getString(KEY_PHONE, "");
    }

    public String getAddress() {
        return sharedPreferences.getString(KEY_ADDRESS, "");
    }

    public String getRole() {
        return sharedPreferences.getString(KEY_ROLE, "USER");
    }

    public boolean isAdmin() {
        return "ADMIN".equalsIgnoreCase(getRole());
    }

    public boolean isLoggedIn() {
        return getUserId() != -1;
    }

    public void logout() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}