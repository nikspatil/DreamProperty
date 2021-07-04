package com.example.dreamproperty.buyProperty;

import com.google.firebase.firestore.Exclude;

import java.util.List;

public class Note {
    private String documentId;
    private String title;
    private String description;
    private String propertyType;
    private String propertySubType;
    private String propertyLocation;
    private String propertyexpectedprice;
    private String ownermobilnumer;
    private String housepropertybathrooms;
    private String housepropertybedrooms;
    private String propertyarea;
    private String propertyLatLong;
    List<String> images;
    public Note() {
        //public no-arg constructor needed
    }
    public Note(String title, String description, String propertyType, String propertySubType, String propertyLocation, String propertyexpectedprice, String ownermobilnumer, String housepropertybathrooms, String housepropertybedrooms, List<String> images, String propertArea, String propertyLatLong) {
        this.title = title;
        this.description = description;
        this.propertyType = propertyType;
        this.propertySubType = propertySubType;
        this.propertyLocation = propertyLocation;
        this.propertyexpectedprice = propertyexpectedprice;
        this.ownermobilnumer = ownermobilnumer;
        this.housepropertybathrooms = housepropertybathrooms;
        this.housepropertybedrooms = housepropertybedrooms;
        this.propertyarea = propertArea;
        this.propertyLatLong = propertyLatLong;
        this.images = images;
    }

    @Exclude
    public String getDocumentId() {
        return documentId;
    }
    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }
    public Note(String title, String description) {
        this.title = title;
        this.description = description;
    }
    public String getTitle() {
        return title;
    }
    public String getDescription() {
        return description;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public String getPropertySubType() {
        return propertySubType;
    }

    public void setPropertySubType(String propertySubType) {
        this.propertySubType = propertySubType;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getPropertyLocation() {
        return propertyLocation;
    }

    public void setPropertyLocation(String propertyLocation) {
        this.propertyLocation = propertyLocation;
    }

    public String getPropertyexpectedprice() {
        return propertyexpectedprice;
    }

    public void setPropertyexpectedprice(String propertyexpectedprice) {
        this.propertyexpectedprice = propertyexpectedprice;
    }

    public String getPropertyarea() {
        return propertyarea;
    }

    public void setPropertyarea(String propertyarea) {
        this.propertyarea = propertyarea;
    }

    public String getOwnermobilnumer() {
        return ownermobilnumer;
    }

    public void setOwnermobilnumer(String ownermobilnumer) {
        this.ownermobilnumer = ownermobilnumer;
    }

    public String getHousepropertybathrooms() {
        return housepropertybathrooms;
    }

    public void setHousepropertybathrooms(String housepropertybathrooms) {
        this.housepropertybathrooms = housepropertybathrooms;
    }

    public String getHousepropertybedrooms() {
        return housepropertybedrooms;
    }

    public void setHousepropertybedrooms(String housepropertybedrooms) {
        this.housepropertybedrooms = housepropertybedrooms;
    }

    public String getPropertyLatLong() {
        return propertyLatLong;
    }

    public void setPropertyLatLong(String propertyLatLong) {
        this.propertyLatLong = propertyLatLong;
    }
}
