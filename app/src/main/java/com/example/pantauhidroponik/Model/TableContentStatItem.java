package com.example.pantauhidroponik.Model;

public class TableContentStatItem implements Comparable<TableContentStatItem> {
    private final String parameter;
    private final String status;
    private final String value;
    private final String waktu;
    private String warning;
    private boolean warningColor;
    private final int xLabel;

    public TableContentStatItem(String status2, String parameter2, String value2, String waktu2, int xLabel2) {
        this.status = status2;
        this.parameter = parameter2;
        this.value = value2;
        this.waktu = waktu2;
        this.xLabel = xLabel2;
    }

    public String getStatus() {
        return this.status;
    }

    public String getParameter() {
        return this.parameter;
    }

    public String getValue() {
        return this.value;
    }

    public String getWarning() {
        return this.warning;
    }

    public String getWaktu() {
        return this.waktu;
    }

    public void setWarning(String batasAtas, String batasBawah, String paramaterValue) {
        this.warning = "Aman";
        this.warningColor = true;
        float btsBawah = Float.parseFloat(batasBawah);
        float btsAtas = Float.parseFloat(batasAtas);
        float value2 = Float.parseFloat(paramaterValue);
        if (value2 < btsBawah) {
            this.warning = "Kurang dari \nAmbang Batas " + batasBawah;
            this.warningColor = false;
        } else if (value2 > btsAtas) {
            this.warning = "Melebihi Ambang \nBatas " + batasAtas;
            this.warningColor = false;
        }
    }

    public int getxLabel() {
        return this.xLabel;
    }

    public boolean isWarningColor() {
        return this.warningColor;
    }

    public int compareTo(TableContentStatItem tableContentStatItem) {
        return this.xLabel - tableContentStatItem.getxLabel();
    }
}
