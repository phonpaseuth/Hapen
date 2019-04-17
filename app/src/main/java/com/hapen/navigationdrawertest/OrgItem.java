package com.hapen.navigationdrawertest;

public class OrgItem {

    String organizationName, Logo, id;

    public OrgItem(){}

    public OrgItem(String organizationName, String Logo, String id){
        this.organizationName = organizationName;
        //this.search = search;
        this.Logo = Logo;
        this.id = id;
        //.cover = cover;


    }

    public String getName1() {
        return organizationName;
    }

    public void setName1(String organizationName){
        this.organizationName = organizationName;
    }



    public String getImage() {
        return Logo;
    }

    public void setImage(String Logo){
        this.Logo = Logo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id){
        this.id = id;
    }


    /**
    public String getSearch() {
        return search;
    }

    public void setSearch(String search){
        this.search = search;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover){
        this.cover = cover;
    }
     **/


}
