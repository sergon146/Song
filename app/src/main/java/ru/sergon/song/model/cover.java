package ru.sergon.song.model;

/**
 * Created by SerGon on 27.04.2016.
 */

//класс получения ссылок на маленькое и большое изображение
public class cover {
    private String small;
    private String big;
    public cover (String small, String big){
        this.small = small;
        this.big = big;
    }
    public cover(){

    }

    public String getBig() {
        return big;
    }

    public void setBig(String big) {
        this.big = big;
    }

    public String getSmall() {
        return small;
    }

    public void setSmall(String small) {
        this.small = small;
    }
}
