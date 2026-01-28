package model;

import com.google.gson.annotations.SerializedName;

public class DashboardResponse {

    @SerializedName("status")
    private String status;

    @SerializedName("data")
    private Data data;

    public String getStatus() {
        return status;
    }

    public Data getData() {
        return data;
    }

    public static class Data {
        @SerializedName("total_produk")
        private int totalProduk;

        @SerializedName("stok_menipis")
        private int stokMenipis;

        @SerializedName("harus_ditambahkan")
        private int harusDitambahkan;

        public int getTotalProduk() {
            return totalProduk;
        }

        public int getStokMenipis() {
            return stokMenipis;
        }

        public int getHarusDitambahkan() {
            return harusDitambahkan;
        }
    }
}