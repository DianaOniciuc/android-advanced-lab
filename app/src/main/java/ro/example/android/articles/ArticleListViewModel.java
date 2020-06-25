package ro.example.android.articles;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import ro.example.android.App;
import ro.example.android.data.NewsRepository;
import ro.example.android.data.local.ArticleEntity;
import timber.log.Timber;

public class ArticleListViewModel  extends ViewModel {

    private NewsRepository newsRepository;
    Disposable listenDisposable;
    Disposable loadDataDisposable;
    Disposable searchDisposable;
    Disposable deleteDisposable;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    private MutableLiveData<List<ArticleEntity>> articlesDB = new MutableLiveData<List<ArticleEntity>>();

    public ArticleListViewModel() {
        newsRepository = App.get().getRepository();

        defaultNews();
        //getArticlesWeb("Bitcoin");
        // Start listening to article updates - we will be notified with a new list of articles
        // every time something is written into the database
        listenDisposable = newsRepository.listenToAllArticles()
                // When the results come back, make sure we switch to main thread to handle them
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(articles -> {
                    Timber.d("Received response: %s", articles.size());

                    // Just keep a list of article names
                    articlesDB.setValue(articles);

                }, throwable -> {
                    // Handle the error
                    Timber.e(throwable, "Received error while fetching articles:");
                });
        compositeDisposable.add(listenDisposable);
    }

    public LiveData<List<ArticleEntity>> getArticles(){
        Timber.d("Called getArticles()");
        return articlesDB;
    }

    public void getArticlesWeb(String query){
      loadDataDisposable = newsRepository.fetchArticles(query)
                       .observeOn(AndroidSchedulers.mainThread())
                       .subscribe(() -> {
                           Timber.d("Data fetch completed");
                       }, throwable -> {
                           Timber.e(throwable, "Data fetch failed");
                       });

      compositeDisposable.add(loadDataDisposable);
    }

    public void getTopNews(){
        loadDataDisposable = newsRepository.fetchTopNews()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    Timber.d("Data fetch completed");
                }, throwable -> {
                    Timber.e(throwable, "Data fetch failed");
                });

        compositeDisposable.add(loadDataDisposable);
    }

    public void searchQuery(String query){

       searchDisposable = newsRepository.deleteAllArticles().observeOn(AndroidSchedulers.mainThread())
               .subscribe(()->{
                   Timber.d("Data deletion completed");
                   getArticlesWeb(query);
               }, throwable -> {
                   Timber.e(throwable, "Data deletion failed");
               });
       compositeDisposable.add(searchDisposable);
    }

    public void defaultNews(){

        deleteDisposable = newsRepository.deleteAllArticles().observeOn(AndroidSchedulers.mainThread())
                .subscribe(()->{
                    Timber.d("Data deletion completed");
                    getTopNews();
                }, throwable -> {
                    Timber.e(throwable, "Data deletion failed");
                });
        compositeDisposable.add(deleteDisposable);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        // Make sure that we cancel any subscription / operation so it doesn't run after the view
        // model is not visible and has been cleared
        // TODO: Replace with a single `CompositeDisposable`
        compositeDisposable.clear();
    }
}
