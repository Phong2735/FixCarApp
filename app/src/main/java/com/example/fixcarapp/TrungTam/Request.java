package com.example.fixcarapp.TrungTam;

public class Request {
    private String address;
    private String email;
    private int id;
    private String incident;
    private double latitude;
    private double longitude;
    private String problem;
    private String scenePhoto;
    int centerId;
    private String vehicle;

    private String status; // Thêm thuộc tính status
    private String phone;  // Thêm thuộc tính phone

    public Request(int id, String phone, String incident, String problem, double longitude, double latitude, String address, String vehicle, String scenePhoto, String status, int centerId, String email) {
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
    }


    // Constructor mặc định (bắt buộc cho Firebase)
    public Request() {}

    // Getter và Setter cho các thuộc tính mới
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    // email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    // Getter và Setter
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIncident() {
        return incident;
    }

    public void setIncident(String incident) {
        this.incident = incident;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getScenePhoto() {
        return scenePhoto;
    }

    public void setScenePhoto(String scenePhoto) {
        this.scenePhoto = scenePhoto;
    }

    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }
}

