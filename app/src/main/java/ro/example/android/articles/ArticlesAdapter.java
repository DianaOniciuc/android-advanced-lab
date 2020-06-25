package ro.example.android.articles;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.squareup.picasso.Picasso;

import java.util.List;

import ro.example.android.R;
import ro.example.android.data.local.ArticleEntity;
import ro.example.android.databinding.ItemLayoutWhiteBinding;
import timber.log.Timber;

public class ArticlesAdapter extends RecyclerView.Adapter<ArticlesAdapter.ArticleViewHolder> {

    private List<ArticleEntity> articleList;
    private Context context;

    public ArticlesAdapter(Context context) {
        this.context = context;
    }

    void setArticles(List<ArticleEntity> articles) {
        this.articleList = articles;
    }

    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ArticleViewHolder(ItemLayoutWhiteBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position) {
        ArticleEntity article = articleList.get(position);
        holder.bind(article);

        RequestOptions defaultOptions = new RequestOptions()
                .error(R.drawable.ic_launcher_background);

        //Glide.with().setDefaultRequestOptions(defaultOptions).load(articleList.get(position).urlToImage).into(holder.binding.image);

        Picasso.get().load(articleList.get(position).urlToImage).placeholder(R.drawable.ic_baseline_photo_24).resize(holder.binding.image.getMeasuredWidth(),470).into(holder.binding.image);
    }

    @Override
    public int getItemCount() {
        return articleList != null ? articleList.size() : 0;
    }

    private void onItemClicked(ArticleEntity article) {
        Timber.d("Clicked on article: %s", article);

        String url = article.url;
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        CustomTabsIntent customTabsIntent = builder.build();
        builder.enableUrlBarHiding();
        builder.setShowTitle(true);
        customTabsIntent.launchUrl(context, Uri.parse(url));
    }


    class ArticleViewHolder extends RecyclerView.ViewHolder{

        private ItemLayoutWhiteBinding binding;

        public ArticleViewHolder(ItemLayoutWhiteBinding binding){
            super(binding.getRoot());
            this.binding=binding;

            itemView.setOnClickListener(v -> {
                ArticleEntity clickedArticle = articleList.get(getAdapterPosition());
                onItemClicked(clickedArticle);
            });
        }

        public void bind(ArticleEntity articleEntity){
            binding.setArticle(articleEntity);
            binding.executePendingBindings();
        }
    }
}
