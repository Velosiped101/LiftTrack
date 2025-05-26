package com.example.notes.utils

import java.io.IOException
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

sealed class IOError(val message: String) {
    data object UnknownHost: IOError("No internet connection")
    data object SocketTimeout: IOError("Server timeout")
    data object ConnectionError: IOError("Connection failed")
    data object ConnectionLost: IOError("Connection lost")
    data object Unknown: IOError("Unknown connection error")
}

fun IOException.toIOError(): IOError {
    return when (this) {
        is UnknownHostException -> IOError.UnknownHost
        is SocketTimeoutException -> IOError.SocketTimeout
        is ConnectException -> IOError.ConnectionError
        is SocketException -> IOError.ConnectionLost
        else -> IOError.Unknown
    }
}