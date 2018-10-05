package inc.trilokia.popularmovies_2.models;

import com.google.gson.annotations.SerializedName;

public class Genre {

    //Genre Variables
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Genre(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || obj.getClass() != getClass()) return false;

        Genre genre = (Genre) obj;
        return genre.getId() == this.getId() || genre.getName().equals(this.getName());
    }


}