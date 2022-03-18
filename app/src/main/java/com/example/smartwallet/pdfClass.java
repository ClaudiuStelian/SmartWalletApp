package com.example.smartwallet;

public class pdfClass {
    public String pdfname, url;


    public pdfClass(){


    }
    public pdfClass(String pdfname, String url) {
        this.pdfname = pdfname;
        this.url = url;
    }

    public String getPdfname() {
        return pdfname;
    }

    public void setPdfname(String pdfname) {
        this.pdfname = pdfname;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
