package com.mobillium.simplemovies.api

import com.mobillium.simplemovies.response.detail.MovieDetailResponse
import com.mobillium.simplemovies.response.playing.NowPlayingResponse
import com.mobillium.simplemovies.response.upcoming.UpcomingResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    //Movie Now Playing for slider
    @GET("movie/now_playing?api_key=26522534ad5699a4a6f53f3eacaaf340")
    fun getNowPlaying(): Call<NowPlayingResponse>

    //Movie Upcoming for list
    @GET("movie/upcoming/?api_key=26522534ad5699a4a6f53f3eacaaf340")
    fun getUpComing(): Call<UpcomingResponse>

    //Movie Upcoming for detail
    @GET("movie/{movie_id}?api_key=26522534ad5699a4a6f53f3eacaaf340")
    fun getMoveDetail(@Path("movie_id") movie_id: String): Call<MovieDetailResponse>

}