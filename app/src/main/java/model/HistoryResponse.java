package model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class HistoryResponse {
    @SerializedName("success")
    private boolean success;
    @SerializedName("data")
    private List<HistoryItem> data;

    public List<HistoryItem> getData() { return data; }

    public static class HistoryItem {
        @SerializedName("nama_barang")
        private String namaBarang;

        @SerializedName("ukuran")
        private String ukuran;

        @SerializedName("jumlah")
        private int jumlah;

        @SerializedName("type")
        private String type;

        @SerializedName("tanggal")
        private String tanggal;

        public String getNamaBarang() { return namaBarang; }
        public String getUkuran() { return ukuran; }
        public int getJumlah() { return jumlah; }
        public String getType() { return type; }
        public String getTanggal() { return tanggal; }
    }
}