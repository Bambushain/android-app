package app.bambushain.api

import app.bambushain.model.License
import retrofit2.Response
import retrofit2.http.GET

interface LegalApi {
    /**
     * GET api/licenses
     * Get all licenses
     * 
     * Responses:
     *  - 200: List of licenses
     *
     * @return [kotlin.collections.List<License>]
     */
    @GET("api/licenses")
    suspend fun getLicenses(): Response<List<License>>

}
