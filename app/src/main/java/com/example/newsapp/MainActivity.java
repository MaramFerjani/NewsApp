package com.example.newsapp;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsapp.NewsAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Spinner categorySpinner;
    private NewsAdapter newsAdapter;
    private List<Article> articleList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_view);
        categorySpinner = findViewById(R.id.category_spinner);

        List<String> categories = new ArrayList<>();
        categories.add("All News");
        categories.add("Business");
        categories.add("Entertainment");
        categories.add("Health");
        categories.add("Science");
        categories.add("Sports");
        categories.add("Technology");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        newsAdapter = new NewsAdapter(this, articleList, article -> {
            DetailActivity.start(MainActivity.this, article);
        });
        recyclerView.setAdapter(newsAdapter);

        loadAllNews();

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedCategory = categories.get(position);
                if ("All News".equals(selectedCategory)) {
                    loadAllNews();
                } else {
                    loadNewsByCategory(selectedCategory);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
    }

    private void loadAllNews() {
        NewsApiService apiService = ApiClient.getRetrofitInstance().create(NewsApiService.class);

        Call<NewsResponse> call = apiService.getNews("tesla", "b3e75e036086465488b84229f9e65d5e");

        call.enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    articleList.clear();
                    articleList.addAll(response.body().getArticles());
                    newsAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
            }
        });
    }

    private void loadNewsByCategory(String category) {
        NewsApiService apiService = ApiClient.getRetrofitInstance().create(NewsApiService.class);

        Call<NewsResponse> call = apiService.getNews(category, "b3e75e036086465488b84229f9e65d5e");

        call.enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    articleList.clear();
                    articleList.addAll(response.body().getArticles());
                    newsAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
            }
        });
    }
}