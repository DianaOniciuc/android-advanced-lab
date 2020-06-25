package ro.example.android.articles;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;

import java.util.List;

import ro.example.android.BR;
import ro.example.android.R;
import ro.example.android.core.BaseActivity;
import ro.example.android.data.local.ArticleEntity;
import ro.example.android.databinding.ActivityArticlesBinding;
import timber.log.Timber;

public class ArticleListActivity extends BaseActivity {

    private ActivityArticlesBinding articlesBinding;
    private ArticleListViewModel articleListViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articles);

       articlesBinding = DataBindingUtil.setContentView(this, R.layout.activity_articles);
       articlesBinding.setLifecycleOwner(this);

        articleListViewModel = new ViewModelProvider(this).get(ArticleListViewModel.class);
        articlesBinding.setVariable(BR.articlesViewModel,articleListViewModel);

        RecyclerView recyclerView = articlesBinding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        MaterialToolbar toolbar = articlesBinding.toolbar;

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.search:
                        SearchView searchView = (SearchView) item.getActionView();
                        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
                        searchView.setBackgroundColor(Color.WHITE);
                        searchView.setIconifiedByDefault(false);
                        searchView.setSubmitButtonEnabled(true);

                        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                            @Override
                            public boolean onQueryTextSubmit(String query) {
                                Timber.d("Searched: %s", query);
                                articleListViewModel.searchQuery(query);
                                return false;
                            }

                            @Override
                            public boolean onQueryTextChange(String newText) {
                                return false;
                            }
                        });

                        return true;
                }
                return false;
            }
        });




        ArticlesAdapter adapter = new ArticlesAdapter(this);
        recyclerView.setAdapter(adapter);
        //adapter.setArticles(ArticleKt.getTestArticles());
        adapter.setArticles(articleListViewModel.getArticles().getValue());  //ca parametru metoda creata in view model care aduce datele din repository

        articleListViewModel.getArticles().observe(this, new Observer<List<ArticleEntity>>() {
            @Override
            public void onChanged(List<ArticleEntity> articleEntities) {
                adapter.setArticles(articleEntities);
                adapter.notifyDataSetChanged();
            }
        });
    }




}
