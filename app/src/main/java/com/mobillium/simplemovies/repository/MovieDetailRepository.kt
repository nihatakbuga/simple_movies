package com.mobillium.simplemovies.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mobillium.simplemovies.api.ApiClient
import com.mobillium.simplemovies.api.ApiService
import com.mobillium.simplemovies.response.detail.MovieDetailResponse


class MovieDetailRepository(var client: ApiClient) {

    private val apiClient: ApiService? = ApiClient.getApiService()


    val liveDataMovieDetail: MutableLiveData<MovieDetailResponse> = MutableLiveData()
    private var movieDetailResponse: MovieDetailResponse? = null

    private val _showProgress = MutableLiveData<Boolean>()
    val showProgress: LiveData<Boolean>
        get() = _showProgress


    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage


    fun getMovieDetailList(movieId: String): MutableLiveData<MovieDetailResponse> {
        _showProgress.postValue(true)
        val call: retrofit2.Call<MovieDetailResponse> =
            apiClient!!.getMoveDetail(movie_id = movieId)
        call.enqueue(object : retrofit2.Callback<MovieDetailResponse> {
            override fun onResponse(
                call: retrofit2.Call<MovieDetailResponse>,
                response: retrofit2.Response<MovieDetailResponse>
            ) {
                if (response.isSuccessful) {
                    try {
                        if (response.code() == 200) {
                            movieDetailResponse = response.body()
                            _showProgress.postValue(false)
                        } else {
                            _showProgress.postValue(false)
                            _errorMessage.postValue(response.message())
                        }
                        movieDetailResponse.let { liveDataMovieDetail.postValue(it) }
                    } catch (e: Exception) {

                    }
                } else {
                    response.message().let {
                        if (it.isNotEmpty())
                            _errorMessage.postValue(response.message())
                        else
                            _errorMessage.postValue("Beklenmedik bir hata oluştu! Lütfen daha sonra tekrar deneyiniz.")
                    }
                }
            }

            override fun onFailure(call: retrofit2.Call<MovieDetailResponse>, t: Throwable) {
                _showProgress.postValue(false)
                _errorMessage.postValue(t.message)
            }
        })
        return liveDataMovieDetail
    }
}