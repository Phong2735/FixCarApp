package com.example.fixcarapp.TaiKhoan;

public class User {
    public String userId;
    public String name;
    public String email;
    public String avatar;
    public String licensePlate;
    public String phone;
    public String role;

    public User(){}

    public User(String userId, String name, String email, String avatar, String licensePlate, String phone, String role) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.avatar = avatar;
        this.licensePlate = licensePlate;
        this.phone = phone;
        this.role = role;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
