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

    // --- CLASS ITEM  ---
    public static class Item implements Serializable {

        @SerializedName("id_detail")
        private String idDetail;

        @SerializedName("produk")
        private String namaProduk;

        @SerializedName("size")
        private String namaUkuran;

        @SerializedName("target")
        private int target;

        @SerializedName("progress_total")
        private int progressTotal;

        @SerializedName("sisa_qty")
        private int sisaQty;

        @SerializedName("sisa_label")
        private String sisaLabel;

        // Getter
        public String getIdDetail() { return idDetail; }
        public String getNamaProduk() { return namaProduk; }
        public String getNamaUkuran() { return namaUkuran; }
        public int getTarget() { return target; }
        public int getProgressTotal() { return progressTotal; }
        public int getSisaQty() { return sisaQty; }
        public String getSisaLabel() { return sisaLabel; }

        // Setter untuk update realtime di Android
        public void setSisaQty(int sisaBaru) {
            this.sisaQty = sisaBaru;
        }
        public void setSisaLabel(String labelBaru) {
            this.sisaLabel = labelBaru;
        }
    }


}