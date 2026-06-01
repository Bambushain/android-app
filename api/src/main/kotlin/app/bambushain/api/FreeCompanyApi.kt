package app.bambushain.api

import app.bambushain.model.FreeCompanyHousing
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface FreeCompanyApi {
    /**
     * DELETE api/final-fantasy/free-company/{free_company_id}/housing
     * Delete the housing for a free company
     * 
     * Responses:
     *  - 204: Free company housing deleted successfully
     *  - 401: Unauthorized
     *  - 404: Free company housing not found
     *
     * @param freeCompanyId
     * @return [Unit]
     */
    @DELETE("api/final-fantasy/free-company/{free_company_id}/housing")
    suspend fun deleteFreeCompanyHousing(@Path("free_company_id") freeCompanyId: Int): Response<Unit>

    /**
     * GET api/final-fantasy/free-company/{free_company_id}/housing
     * Get the housing for the given free company
     * 
     * Responses:
     *  - 200: Free company housing details
     *  - 401: Unauthorized
     *  - 404: Free company housing not found
     *
     * @param freeCompanyId
     * @return [FreeCompanyHousing]
     */
    @GET("api/final-fantasy/free-company/{free_company_id}/housing")
    suspend fun getFreeCompanyHousing(@Path("free_company_id") freeCompanyId: Int): Response<FreeCompanyHousing>

    /**
     * PUT api/final-fantasy/free-company/{free_company_id}/housing
     * Set a housing for a free company
     * 
     * Responses:
     *  - 204: Free company housing set successfully
     *  - 401: Unauthorized
     *  - 404: Character housing not found
     *
     * @param freeCompanyId
     * @param freeCompanyHousing
     * @return [Unit]
     */
    @PUT("api/final-fantasy/free-company/{free_company_id}/housing")
    suspend fun setFreeCompanyHousing(
        @Path("free_company_id") freeCompanyId: Int,
        @Body freeCompanyHousing: FreeCompanyHousing
    ): Response<Unit>

}
