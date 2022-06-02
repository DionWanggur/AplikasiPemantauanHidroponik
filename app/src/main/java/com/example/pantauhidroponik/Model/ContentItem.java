package com.example.pantauhidroponik.Model;

public class ContentItem {
    private String lastCheck;
    private String location;
    private String parameterValue;
    private String title;
    private float value;
    private String warning;
    private boolean warningColor;

    public ContentItem(String title2, String location2, String paramaterValue, String lastCheck2) {
        this.title = title2;
        this.location = location2;
        this.parameterValue = paramaterValue;
        this.lastCheck = lastCheck2;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title2) {
        this.title = title2;
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location2) {
        this.location = location2;
    }

    public String getParameterValue() {
        return this.parameterValue;
    }

    public void setParameterValue(String parameterValue2) {
        this.parameterValue = parameterValue2;
    }

    public String getWarning() {
        return this.warning;
    }

    public boolean isWarningColor() {
        return this.warningColor;
    }

    public void setWarning(String batasAtas, String batasBawah, String paramaterValue) {
        this.warning = "Aman";
        this.warningColor = true;
        float btsBawah = Float.parseFloat(batasBawah);
        float btsAtas = Float.parseFloat(batasAtas);
        float value2 = Float.parseFloat(paramaterValue);
        if (value2 < btsBawah) {
            this.warning = "Kurang dari Ambang Batas " + batasBawah;
            this.warningColor = false;
        } else if (value2 > btsAtas) {
            this.warning = "Melebihi Ambang Batas " + batasAtas;
            this.warningColor = false;
        }
    }

    public String getLastCheck() {
        return this.lastCheck;
    }

    public void setLastCheck(String lastCheck2) {
        this.lastCheck = lastCheck2;
    }

    public float getValue() {
        return this.value;
    }

    public void setValue(String paramaterValue, String title2) {
        float value2 = Float.parseFloat(paramaterValue);
        if (title2.equalsIgnoreCase("Suhu Udara") || title2.equalsIgnoreCase("Suhu Air") || title2.equalsIgnoreCase("Kelembaban")) {
            this.value = (value2 / 100) * 270;
        } else if (title2.equalsIgnoreCase("pH")) {
            this.value = (value2 / 14) * 270;
        } else if (title2.equalsIgnoreCase("TDS")) {
            this.value = (value2 / 2000) * 270;
        }
    }
}
