package com.example.fixcarapp.TaoYeuCau;

public class Request {
    int id;
    String phone;
    String incident;
    String problem;
    double longitude;
    double latitude;
    String address;
    String vehicle;
    String scenePhoto;
    String status;
    String centerId;
    String email;
    String time;

    public Request(){

    }


    public Request(int id, String phone, String incident, String problem, double longitude, double latitude, String address, String vehicle, String scenePhoto, String status, String centerId, String email, String time) {
        this.id = id;
        this.phone = phone;
        this.incident = incident;
        this.problem = problem;
        this.longitude = longitude;
        this.latitude = latitude;
        this.address = address;
        this.vehicle = vehicle;
        this.scenePhoto = scenePhoto;
        this.status = status;
        this.centerId = centerId;
        this.email = email;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIncident() {
        return incident;
    }

    public void setIncident(String incident) {
        this.incident = incident;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }

    public String getScenePhoto() {
        return scenePhoto;
    }

    public void setScenePhoto(String scenePhoto) {
        this.scenePhoto = scenePhoto;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCenterId() {
        return centerId;
    }

    public void setCenterId(String centerId) {
        this.centerId = centerId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


}