package com.example.androidapiclient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.androidapiclient.adapter.MoviesAdapter;
import com.example.androidapiclient.model.Movie;
import com.example.androidapiclient.webservice.ApiInterface;
import com.example.androidapiclient.webservice.ServiceGenerator;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    private List<Movie> movies = new ArrayList<>();
    private MoviesAdapter moviesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerview);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
        moviesAdapter = new MoviesAdapter(MainActivity.this, movies);
        recyclerView.setAdapter(moviesAdapter);

        getMovies();
    }

    private void getMovies() {
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<List<Movie>> call = apiInterface.getMovies();
        call.enqueue(new Callback<List<Movie>>() {
            @Override
            public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
                if (response.isSuccessful()){
                    for (Movie movie : response.body()){
                        movies.add(movie);
                    }
                    moviesAdapter.notifyDataSetChanged();
                }  else {
                    Log.e(TAG, response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Movie>> call, Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });
    }
}