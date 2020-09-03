package com.example.home.ui.contacts;

/** Simple class to store the details of each service **/
class ServiceDetails{
    String name;
    String url;
    String phoneNumber;
    String county;
    ServiceDetails(String name,  String phoneNumber, String url, String county){
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.url = url;
        this.county = county;
    }

}