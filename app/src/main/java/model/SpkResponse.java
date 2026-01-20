package model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

public class SpkResponse {

    @SerializedName("data")
    private List<Spk> data;

    public List<Spk> getData() { return data; }

    public static class Spk implements Serializable {

        @SerializedName("id")
        private String id;

        @SerializedName("created_at")
        private String tanggal;

        @SerializedName("pelanggan")
        private Pelanggan pelanggan;

        @SerializedName("details")
        private List<Detail> details;


        public String getId() {
            return id;
        }
        public String getTanggal() {
            return tanggal;
        }
        public Pelanggan getPelanggan() {
            return pelanggan;
        }
        public List<Detail> getDetails() {
            return details;
        }

        public int getTotalItems() {
            if (details == null) return 0;
            return details.size();
        }
    }


    public static class Pelanggan implements Serializable {
        @SerializedName("nama")
        private String nama;

        public String getNama() {
            return nama;
        }
    }


    public static class Detail implements Serializable {

        @SerializedName("id")
        private String id;

        @SerializedName("jumlah_target")
        private String jumlahTarget;

        public String getId() { return id; }
        public String getJumlahTarget() { return jumlahTarget; }
    }
}