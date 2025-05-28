package com.velosiped.notes.utils

import com.velosiped.notes.R
import java.io.IOException
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

sealed class IOError(val messageId: Int) {
    data object UnknownHost: IOError(R.string.unknown_host)
    data object SocketTimeout: IOError(R.string.socket_timeout)
    data object ConnectionError: IOError(R.string.connection_error)
    data object ConnectionLost: IOError(R.string.connection_lost)
    data object Unknown: IOError(R.string.unknown_connection_error)
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