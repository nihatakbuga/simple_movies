package com.mobillium.simplemovies.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mobillium.simplemovies.api.ApiClient
import com.mobillium.simplemovies.api.ApiService
import com.mobillium.simplemovies.response.upcoming.UpcomingResponse


class UpcomingRepository(var client: ApiClient) {

    private val apiClient: ApiService? = ApiClient.getApiService()
    private val call: retrofit2.Call<UpcomingResponse> = apiClient!!.getUpComing()

    val liveDataUpcoming: MutableLiveData<UpcomingResponse> = MutableLiveData()
    private var upcomingResponse: UpcomingResponse? = null

    private val _showProgress = MutableLiveData<Boolean>()
    val showProgress: LiveData<Boolean>
        get() = _showProgress


    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage


    fun getUpcomingList(): MutableLiveData<UpcomingResponse> {
        _showProgress.postValue(true)
        call.clone().enqueue(object : retrofit2.Callback<UpcomingResponse> {
            override fun onResponse(
                call: retrofit2.Call<UpcomingResponse>,
                response: retrofit2.Response<UpcomingResponse>
            ) {
                if (response.isSuccessful) {
                    try {
                        if (response.code() == 200) {
                            upcomingResponse = response.body()

                        } else {
                            _showProgress.postValue(false)
                            _errorMessage.postValue(response.message())
                        }
                        _showProgress.postValue(false)
                        upcomingResponse.let { liveDataUpcoming.postValue(it) }
                    } catch (e: Exception) {
                        _showProgress.postValue(false)
                        _errorMessage.postValue(e.message)
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

            override fun onFailure(call: retrofit2.Call<UpcomingResponse>, t: Throwable) {
                _showProgress.postValue(false)
                _errorMessage.postValue(t.message)
            }
        })
        return liveDataUpcoming
    }
}