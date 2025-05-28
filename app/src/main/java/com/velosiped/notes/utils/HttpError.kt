package com.velosiped.notes.utils

import com.velosiped.notes.R
import retrofit2.HttpException

sealed class HttpError(val code: Int, val messageId: Int) {
    data object BadRequest: HttpError(code = 400, messageId = R.string.bad_request)
    data object Forbidden: HttpError(code = 403, messageId = R.string.forbidden)
    data object NotFound: HttpError(code = 404, messageId = R.string.not_found)
    data object TooManyRequests: HttpError(code = 429, messageId = R.string.too_many_requests)
    data object InternalServerError: HttpError(code = 500, messageId = R.string.server_error)
    data object BadGateway: HttpError(code = 502, messageId = R.string.bad_gateway)
    data object ServiceUnavailable: HttpError(code = 503, messageId = R.string.server_unavailable)
    data class UnknownError(val unknownErrorCode: Int): HttpError(code = unknownErrorCode, messageId = R.string.unknown_http_error)
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
        else -> HttpError.UnknownError(this.code())
    }
}