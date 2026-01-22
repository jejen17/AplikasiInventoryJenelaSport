package model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

public class SpkResponse {

    @SerializedName("success")
    private boolean success;

    @SerializedName("data")
    private List<Spk> data;

    public boolean isSuccess() { return success; }
    public List<Spk> getData() { return data; }

    // --- CLASS SPK (INDUK) ---
    public static class Spk implements Serializable {


        @SerializedName("id_spk")
        private String idSpk;


        @SerializedName("target_date")
        private String targetDate;

        @SerializedName("pelanggan")
        private String namaPelanggan;

        @SerializedName("catatan")
        private String catatan;


        @SerializedName("items")
        private List<Item> items;

        // Getter
        public String getIdSpk() { return idSpk; }
        public String getTargetDate() { return targetDate; }
        public String getNamaPelanggan() { return namaPelanggan; }
        public String getCatatan() { return catatan; }
        public List<Item> getItems() { return items; }

        public int getTotalItems() {
            if (items == null) return 0;
            return items.size();
        }
    }

    // --- CLASS ITEM (Lebih Sederhana) ---
    public static class Item implements Serializable {

        @SerializedName("id_detail")
        private String idDetail;

        // Temanmu sudah mengirim ini sebagai String gabungan (Nama + Warna)
        // Jadi kita tidak butuh object Produk lagi!
        @SerializedName("produk")
        private String namaProduk;

        // Temanmu sudah mengirim ini sebagai String (misal: "L", "XL")
        // Tidak butuh object Size lagi!
        @SerializedName("size")
        private String namaUkuran;

        @SerializedName("target")
        private String target; // String jaga-jaga, nanti kita tampilkan langsung

        @SerializedName("selesai")
        private String selesai;

        @SerializedName("sisa")
        private String sisa;

        // Getter
        public String getIdDetail() { return idDetail; }
        public String getNamaProduk() { return namaProduk; }
        public String getNamaUkuran() { return namaUkuran; }
        public String getTarget() { return target; }
        public String getSelesai() { return selesai; }
        public String getSisa() { return sisa; }
    }


}