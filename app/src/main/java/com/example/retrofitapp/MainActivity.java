package com.example.retrofitapp;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    private JsonPlaceholderApi api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.contentTextView);

        Gson gson = new GsonBuilder().serializeNulls().create();

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        Request newRequest = request.newBuilder()
                                .header("Intercepted-Header", "567")
                                .build();
                        return chain.proceed(newRequest);
                    }
                })
                .addInterceptor(loggingInterceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        api = retrofit.create(JsonPlaceholderApi.class);

        //getPosts();
        //getComments();
        createPost();
        //updatePost();
        //deletePost();
    }

    void getPosts() {
        Call<List<Post>> call = api.getPosts();

        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (!response.isSuccessful()) {
                    String text = "Code: " + response.code();
                    textView.setText(text);
                }

                String content = "";
                List<Post> posts = response.body();
                for (Post post : posts) {
                    content += "User ID: " + post.getUserId()
                            + "\nID: " + post.getId()
                            + "\nTitle: " + post.getTitle()
                            + "\nText: " + post.getText()
                            + "\n\n";
                }

                textView.setText(content);
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                textView.setText(t.getMessage());
            }
        });
    }

    void getComments() {
        Call<List<Comment>> call = api.getComments(null, "id", "desc");

        call.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                if (!response.isSuccessful()) {
                    String text = "Code: " + response.code();
                    textView.setText(text);
                }

                String content = "";
                List<Comment> comments = response.body();
                for (Comment comment : comments) {
                    content += "Post ID: " + comment.getPostId()
                            + "\nID: " + comment.getId()
                            + "\nEmail: " + comment.getEmail()
                            + "\nTitle: " + comment.getTitle()
                            + "\nText: " + comment.getText()
                            + "\n\n";
                }

                textView.setText(content);
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                textView.setText(t.getMessage());
            }
        });
    }

    void createPost() {
        Post post = new Post(123, "How to make post request using Retrofit?", "Please help!");

        Call<Post> call = api.createPost("123", post);

        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if (!response.isSuccessful()) {
                    String text = "Code: " + response.code();
                    textView.setText(text);
                }

                String content = "";
                Post post = response.body();
                content += "User ID: " + post.getUserId()
                        + "\nID: " + post.getId()
                        + "\nTitle: " + post.getTitle()
                        + "\nText: " + post.getText()
                        + "\n\n";

                textView.setText(content);
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                textView.setText(t.getMessage());
            }
        });
    }

    void updatePost() {
        Post post = new Post(123, null, "Hi there!");
        post.setId(5);

        Call<Post> call = api.putPost(5, post);

        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if (!response.isSuccessful()) {
                    String text = "Code: " + response.code();
                    textView.setText(text);
                }

                String content = "";
                Post post = response.body();
                content += "User ID: " + post.getUserId()
                        + "\nID: " + post.getId()
                        + "\nTitle: " + post.getTitle()
                        + "\nText: " + post.getText()
                        + "\nCode: " + response.code()
                        + "\n\n";

                textView.setText(content);
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                textView.setText(t.getMessage());
            }
        });
    }

    void deletePost() {
        Call<Void> call = api.deletePost(5);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                String text = "Code: " + response.code();
                textView.setText(text);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                textView.setText(t.getMessage());
            }
        });
    }

}