package com.example.fixcarapp.TrungTam.DanhSachTrungTam;

import java.io.Serializable;

public class Item_Center implements Serializable {
    String tenCenter,diachiCenter,email,mota,sdt;
    String logo;
    String centerId;
    public Item_Center() {
    }
    public Item_Center(String centerId,String tenCenter, String email, String logo, String diachiCenter, String sdt, String mota) {
        this.centerId = centerId;
        this.tenCenter = tenCenter;
        this.email = email;
        this.logo = logo;
        this.diachiCenter = diachiCenter;
        this.sdt = sdt;
        this.mota = mota;
    }

    public String getCenterId() {
        return centerId;
    }

    public void setCenterId(String centerId) {
        this.centerId = centerId;
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

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
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
