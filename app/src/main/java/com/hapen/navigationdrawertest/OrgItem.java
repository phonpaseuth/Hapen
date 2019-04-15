package com.hapen.navigationdrawertest;

public class OrgItem {

    String organizationName, search, Logo, cover, uid;

    public OrgItem(){}

    public OrgItem(String organizationName, String search, String Logo, String cover){
        this.organizationName = organizationName;
        this.search = search;
        this.Logo = Logo;
        this.cover = cover;

    }

    public String getName1() {
        return organizationName;
    }

    public void setName1(String organizationName){
        this.organizationName = organizationName;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search){
        this.search = search;
    }

    public String getImage() {
        return Logo;
    }

    public void setImage(String Logo){
        this.Logo = Logo;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover){
        this.cover = cover;
    }

}
