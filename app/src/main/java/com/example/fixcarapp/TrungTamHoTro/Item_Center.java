package com.example.fixcarapp.TrungTamHoTro;

import java.io.Serializable;

public class Item_Center implements Serializable {
    String tenCenter,diachiCenter,email,mota,sdt;
    byte[] Logo;

    public Item_Center() {
    }

    public byte[] getLogo() {
        return Logo;
    }

    public void setLogo(byte[] logo) {
        Logo = logo;
    }

    public String getDiachiCenter() {
        return diachiCenter;
    }

    public void setDiachiCenter(String diachiCenter) {
        this.diachiCenter = diachiCenter;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMota() {
        return mota;
    }

    public void setMota(String mota) {
        this.mota = mota;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getTenCenter() {
        return tenCenter;
    }

    public void setTenCenter(String tenCenter) {
        this.tenCenter = tenCenter;
    }
}
