package com.example.ncu_app;

public class ListItem {

    private String objectName;
    private String buildDate;

    private ListItem(){
        this.objectName = "objectName";
        this.buildDate = "buildDate";
    }

    public ListItem(String objectName, String buildDate){
        this.objectName = objectName;
        this.buildDate = buildDate;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public String getBuildDate() {
        return buildDate;
    }

    public void setBuildDate(String buildDate) {
        this.buildDate = buildDate;
    }


}
