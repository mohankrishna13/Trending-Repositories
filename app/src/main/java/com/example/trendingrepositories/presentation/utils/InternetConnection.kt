package com.example.trendingrepositories.presentation.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi

class InternetConnection {
    @RequiresApi(Build.VERSION_CODES.M)
    public fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val connectivityType =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (connectivityType != null) {
                if (connectivityType.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    return true
                } else if (connectivityType.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    return true
                } else if (connectivityType.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    return true
                }
            }
        }
        return false
    }
}