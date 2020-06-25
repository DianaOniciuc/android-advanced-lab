package ro.example.android.data.remote;

import com.google.gson.annotations.SerializedName;

import org.threeten.bp.format.DateTimeFormatter;

import java.sql.Date;

import ro.example.android.data.local.ArticleEntity;

public class ArticleDto {
    public final String title;
    @SerializedName("description")
    public final String description;
    public final String author;
    public final String url;
    public final Source source;
    public final String urlToImage;
    //public final Date publishedAt;

    private static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    public ArticleDto(String title, String description, String author, String url, Source source, String urlToImage) {
        this.title = title;
        this.description = description;
        this.author = author;
        this.url = url;
        this.source = source;
        this.urlToImage = urlToImage;
    }

    public static class Source {
        public final String id;
        public final String name;

        public Source(String id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    public ArticleEntity toEntity() {
        return new ArticleEntity(title, description, author, url, urlToImage);
    }
}
