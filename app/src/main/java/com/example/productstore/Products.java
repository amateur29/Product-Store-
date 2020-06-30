package com.example.productstore;

public class Products {

    private String product_name, description, category, rating, image1, image2, image3, pid;

    public Products()
    {

    }

    public Products(String product_name, String description, String category, String rating, String image1, String image2, String image3) {
        this.product_name = product_name;
        this.description = description;
        this.category = category;
        this.rating = rating;
        this.image1 = image1;
        this.image2 = image2;
        this.image3 = image3;
        this.pid = pid;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getDescription() {
        return description;
    }

    public Products(String pid) {
        this.pid = pid;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getImage3() {
        return image3;
    }

    public void setImage3(String image3) {
        this.image3 = image3;
    }
}
