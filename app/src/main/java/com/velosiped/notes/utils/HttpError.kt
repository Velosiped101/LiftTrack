package com.velosiped.notes.utils

import retrofit2.HttpException

sealed class HttpError(val code: Int, val message: String) {
    data object BadRequest: HttpError(code = 400, message = "Bad request")
    data object Forbidden: HttpError(code = 403, message = "Forbidden")
    data object NotFound: HttpError(code = 404, message = "Not found")
    data object TooManyRequests: HttpError(code = 429, message = "Too many requests")
    data object InternalServerError: HttpError(code = 500, message = "Server error")
    data object BadGateway: HttpError(code = 502, message = "Bad gateway")
    data object ServiceUnavailable: HttpError(code = 503, message = "Server unavailable")
    data class UnknownError(val unknownErrorCode: Int, val unknownErrorMessage: String): HttpError(code = unknownErrorCode, message = unknownErrorMessage)
}

fun HttpException.toHttpError(): HttpError {
    return when (this.code()) {
        400 -> HttpError.BadRequest
        403 -> HttpError.Forbidden
        404 -> HttpError.NotFound
        429 -> HttpError.TooManyRequests
        500 -> HttpError.InternalServerError
        502 -> HttpError.BadGateway
        503 -> HttpError.ServiceUnavailable
        else -> HttpError.UnknownError(this.code(), this.message.toString())
    }
}