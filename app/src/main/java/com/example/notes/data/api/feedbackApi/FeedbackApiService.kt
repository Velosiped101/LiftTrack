package com.example.notes.data.api.feedbackApi

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface FeedbackApiService {
    @FormUrlEncoded
    @POST(FeedbackApiConstants.BASE_URL_ENDPOINT)
    suspend fun postFeedback(
        @Field("type") type: String,
        @Field("content") content: String,
        @Field("systemInfo") systemInfo: String,
    ): Response<Unit>
}