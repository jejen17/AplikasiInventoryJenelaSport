package model;

import com.google.gson.annotations.SerializedName;

public class ProdukItem {
    @SerializedName("id")
    private String id;

    @SerializedName("nama_tampil") // Sesuai JSON temanmu
    private String namaTampil;

    public String getId() { return id; }
    public String getNamaTampil() { return namaTampil; }
}