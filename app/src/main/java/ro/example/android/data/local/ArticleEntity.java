package ro.example.android.data.local;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.sql.Date;

@Entity
public class ArticleEntity {
    public final String title;
    public final String description;
    public final String author;
    @PrimaryKey
    @NonNull
    public final String url;
    public final String urlToImage;
    //public final Date publishedAt;

    public ArticleEntity(String title, String description, String author, String url, String urlToImage) {
        this.title = title;
        this.description = description;
        this.author = author;
        this.url = url;
        this.urlToImage = urlToImage;
    }
}
