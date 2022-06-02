package com.example.pantauhidroponik.Model;

public class TableContentItem {
    private final String TDS;
    private final String kelembaban;
    private final String namaNode;
    private final String pH;
    private String status;
    private final String suhuAir;
    private final String suhuUdara;
    private final String waktu;
    private final String lokasi;



    public TableContentItem(String namaNode2, String waktu2, String status2,
                            String suhuUdara2, String suhuAir2, String pH2,
                            String kelembaban2, String TDS2, String lokasi) {
        this.namaNode = namaNode2;
        this.waktu = waktu2;
        this.status = status2;
        this.suhuUdara = suhuUdara2;
        this.suhuAir = suhuAir2;
        this.pH = pH2;
        this.kelembaban = kelembaban2;
        this.TDS = TDS2;
        this.lokasi = lokasi;
    }

    public void setStatus(String status2) {
        if (status2.equalsIgnoreCase("0")) {
            this.status = "Mati";
        } else {
            this.status = "Hidup";
        }
    }

    public String getNamaNode() {
        return this.namaNode;
    }

    public String getWaktu() {
        return this.waktu;
    }

    public String getStatus() {
        return this.status;
    }

    public String getSuhuUdara() {
        return this.suhuUdara;
    }

    public String getSuhuAir() {
        return this.suhuAir;
    }

    public String getpH() {
        return this.pH;
    }

    public String getKelembaban() {
        return this.kelembaban;
    }

    public String getTDS() {
        return this.TDS;
    }

    public String getLokasi() {
        return lokasi;
    }
}
