package com.example.smartwallet;

public class Businesscard {


    public String firstName,lastName,companyName,companyAddress,companyPhone,email;

    public Businesscard(){


    }

    public Businesscard( String companyName, String companyAddress, String companyNumber){

        this.companyName = companyName;
        this.companyAddress = companyAddress;
        this.companyPhone = companyNumber;
    }


    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getCompanyPhone() {
        return companyPhone;
    }

    public void setCompanyPhone(String ompanyPhone) {
        this.companyPhone = ompanyPhone;
    }


}
