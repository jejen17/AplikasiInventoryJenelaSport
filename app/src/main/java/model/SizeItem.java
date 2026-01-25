package model;

import com.google.gson.annotations.SerializedName;

public class SizeItem {
    @SerializedName("id")
    private String id;

    @SerializedName("tipe") // Sesuai JSON temanmu
    private String tipe;

    public String getId() { return id; }
    public String getTipe() { return tipe; }
}