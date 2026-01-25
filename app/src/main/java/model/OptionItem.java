package model;

public class OptionItem {
    private String id;
    private String name;

    public OptionItem(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() { return id; }
    public String getName() { return name; }

    // Ini penting! Spinner akan memanggil toString() untuk menampilkan teks
    @Override
    public String toString() {
        return name;
    }
}