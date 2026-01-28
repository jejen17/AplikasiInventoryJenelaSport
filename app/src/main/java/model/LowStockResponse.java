package model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class LowStockResponse {
    @SerializedName("status")
    private String status;

    @SerializedName("data")
    private List<StockItem> data;

    public List<StockItem> getData() { return data; }

    public static class StockItem {
        @SerializedName("id_stok")
        private int idStok;

        @SerializedName("nama_produk")
        private String namaProduk;

        @SerializedName("size")
        private String size;

        @SerializedName("stok_saat_ini")
        private int stokSaatIni;

        @SerializedName("min_stok")
        private int minStok;

        @SerializedName("kekurangan")
        private int kekurangan;

        public String getNamaProduk() { return namaProduk; }
        public String getSize() { return size; }
        public int getStokSaatIni() { return stokSaatIni; }
        public int getMinStok() { return minStok; }
        public int getKekurangan() { return kekurangan; }
    }
}