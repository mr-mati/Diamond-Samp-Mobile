package com.mati.launcher.other;

import com.mati.launcher.model.Update;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface Interface {

    @GET("http://62.3.14.22:1212/api/version")
    @Headers("Token: P7p]j+AeL479")
    Call<Update> getUpdate();

    /*@GET("http://wh3606.web3.maze-host.ru/zakazi/35/servers.json")
    Call<List<Servers>> getServers();*/

    /*@GET("http://wh3606.web3.maze-host.ru/zakazi/35/stories.json")
    Call<List<News>> getNews();*/

    /*@GET("http://wh3606.web3.maze-host.ru/donate.json")
    Call<List<Actions>> getActions();*/
}
