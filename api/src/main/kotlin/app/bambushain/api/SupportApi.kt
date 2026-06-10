package app.bambushain.api

import app.bambushain.model.SupportRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface SupportApi {
    /**
     * POST api/support
     * Send a support request
     * 
     * Responses:
     *  - 204: Support request sent successfully
     *
     * @param supportRequest
     * @return [Unit]
     */
    @POST("api/support")
    suspend fun sendSupportRequest(@Body supportRequest: SupportRequest): Response<Unit>

}
