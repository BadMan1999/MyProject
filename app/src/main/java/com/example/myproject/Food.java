package com.example.myproject;

public class Food {

        String name;
        String category;
        String calory;
        String image;


    public Food(String name, String category, String calory, String image) {
        this.name = name;
        this.category = category;
        this.calory = calory;
        this.image = image;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCalory() {
        return calory;
    }

    public void setCalory(String calory) {
        this.calory = calory;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
