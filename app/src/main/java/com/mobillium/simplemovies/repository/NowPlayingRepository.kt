package com.mobillium.simplemovies.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mobillium.simplemovies.api.ApiClient
import com.mobillium.simplemovies.api.ApiService
import com.mobillium.simplemovies.response.detail.MovieDetailResponse
import com.mobillium.simplemovies.response.playing.NowPlayingResponse


class NowPlayingRepository(var client: ApiClient) {

    private val apiClient: ApiService? = ApiClient.getApiService()
    private val call: retrofit2.Call<NowPlayingResponse> = apiClient!!.getNowPlaying()

    val liveDataNowPlaying: MutableLiveData<NowPlayingResponse> = MutableLiveData()
    private var nowPlayingResponse: NowPlayingResponse? = null


    private val _showProgress = MutableLiveData<Boolean>()
    val showProgress: LiveData<Boolean>
        get() = _showProgress


    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage


    fun getNowPlayingList(): MutableLiveData<NowPlayingResponse> { _showProgress.postValue(true)
        call.clone().enqueue(object : retrofit2.Callback<NowPlayingResponse> {
            override fun onResponse(
                call: retrofit2.Call<NowPlayingResponse>,
                response: retrofit2.Response<NowPlayingResponse>
            ) {
                if (response.isSuccessful) {

                    try {
                        if (response.code() == 200) {
                            nowPlayingResponse = response.body()
                        }
                        else {
                            _showProgress.postValue(false)
                            _errorMessage.postValue(response.message())
                        }
                        _showProgress.postValue(false)
                        nowPlayingResponse.let { liveDataNowPlaying.postValue(it) }
                    } catch (e: Exception) {
                        _showProgress.postValue(false)
                        _errorMessage.postValue(e.message)
                    }
                }
                else {
                    response.message().let {
                        if (it.isNotEmpty())
                            _errorMessage.postValue(response.message())
                        else
                            _errorMessage.postValue("Beklenmedik bir hata oluştu! Lütfen daha sonra tekrar deneyiniz.")
                    }
                }
            }

            override fun onFailure(call: retrofit2.Call<NowPlayingResponse>, t: Throwable) {
                _showProgress.postValue(false)
                _errorMessage.postValue(t.message)
            }
        })
        return liveDataNowPlaying
    }
}