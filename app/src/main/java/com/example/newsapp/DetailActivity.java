package com.example.newsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar; // Assure-toi d'utiliser le bon import pour Toolbar

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem; // Import pour les options de menu
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    private ImageView newsImage;
    private TextView newsTitle, newsDescription;
    private Button openArticleButton;

    // Méthode statique pour démarrer l'activité avec des données
    public static void start(Context context, Article article) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra("title", article.getTitle());
        intent.putExtra("description", article.getDescription());
        intent.putExtra("imageUrl", article.getUrlToImage());
        intent.putExtra("url", article.getUrl());
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar); // Assure-toi de définir le Toolbar comme ActionBar

        newsImage = findViewById(R.id.news_image);
        newsTitle = findViewById(R.id.news_title);
        newsDescription = findViewById(R.id.news_description);
        openArticleButton = findViewById(R.id.open_article_button);

        String title = getIntent().getStringExtra("title");
        String description = getIntent().getStringExtra("description");
        String imageUrl = getIntent().getStringExtra("imageUrl");
        String url = getIntent().getStringExtra("url");

        newsTitle.setText(title);
        newsDescription.setText(description);
        Picasso.get().load(imageUrl).into(newsImage);

        openArticleButton.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(browserIntent);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_news_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_share) {
            String url = getIntent().getStringExtra("url");

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Regarde cet article : " + url);

            startActivity(Intent.createChooser(shareIntent, "Partager via"));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
