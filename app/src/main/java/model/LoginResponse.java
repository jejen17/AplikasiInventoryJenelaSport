package model;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private Data data;

    public boolean isSuccess() {
        return success;
    }
    public String getMessage() {
        return message;
    }
    public Data getData() {
        return data;
    }


    public static class Data {
        @SerializedName("user")
        private User user;

        @SerializedName("token")
        private String token;

        public User getUser() {
            return user;
        }
        public String getToken() {
            return token;
        }
    }


    public static class User {
        @SerializedName("id")
        private String id;

        @SerializedName("nama")
        private String nama;

        @SerializedName("role")
        private String role;

        public String getId() { return id; }
        public String getNama() { return nama; }
        public String getRole() { return role; }
    }
}