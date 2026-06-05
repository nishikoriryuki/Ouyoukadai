package model;

import java.util.List;

public class Kondate {

    private int id;
    private String name;
    private int calorie;
    private int difficulty;
    private String imageUrl;
    
    private List<String> ingredients;

    public Kondate() {
    }
    
    //難易度紐づけ
    public String getDifficultyName() {
        switch (difficulty) {
            case 1:
                return "簡単";
            case 2:
                return "普通";
            case 3:
                return "難しい";
            case 4:
                return "激ムズ";
            default:
                return "不明";
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCalorie() {
        return calorie;
    }

    public void setCalorie(int calorie) {
        this.calorie = calorie;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }
    
    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }
    
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}