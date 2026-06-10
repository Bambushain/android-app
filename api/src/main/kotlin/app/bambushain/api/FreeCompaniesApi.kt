package app.bambushain.api

import app.bambushain.model.FreeCompany
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface FreeCompaniesApi {
    /**
     * POST api/final-fantasy/free-company
     * Create a new free company
     * 
     * Responses:
     *  - 201: Free company created successfully
     *  - 401: Unauthorized
     *
     * @param freeCompany
     * @return [FreeCompany]
     */
    @POST("api/final-fantasy/free-company")
    suspend fun createFreeCompany(@Body freeCompany: FreeCompany): Response<FreeCompany>

    /**
     * DELETE api/final-fantasy/free-company/{free_company_id}
     * Delete a free company
     * 
     * Responses:
     *  - 204: Free company deleted successfully
     *  - 401: Unauthorized
     *  - 404: Free company not found
     *
     * @param freeCompanyId
     * @return [Unit]
     */
    @DELETE("api/final-fantasy/free-company/{free_company_id}")
    suspend fun deleteFreeCompany(@Path("free_company_id") freeCompanyId: Int): Response<Unit>

    /**
     * GET api/final-fantasy/free-company
     * Get all free companies
     * 
     * Responses:
     *  - 200: List of free companies
     *  - 401: Unauthorized
     *
     * @return [kotlin.collections.List<FreeCompany>]
     */
    @GET("api/final-fantasy/free-company")
    suspend fun getFreeCompanies(): Response<List<FreeCompany>>

    /**
     * GET api/final-fantasy/free-company/{free_company_id}
     * Get a specific free company
     * 
     * Responses:
     *  - 200: Free company details
     *  - 401: Unauthorized
     *  - 404: Free company not found
     *
     * @param freeCompanyId
     * @return [FreeCompany]
     */
    @GET("api/final-fantasy/free-company/{free_company_id}")
    suspend fun getFreeCompany(@Path("free_company_id") freeCompanyId: Int): Response<FreeCompany>

    /**
     * PUT api/final-fantasy/free-company/{free_company_id}
     * Update a free company
     * 
     * Responses:
     *  - 204: Free company updated successfully
     *  - 401: Unauthorized
     *  - 404: Free company not found
     *
     * @param freeCompanyId
     * @param freeCompany
     * @return [Unit]
     */
    @PUT("api/final-fantasy/free-company/{free_company_id}")
    suspend fun updateFreeCompany(
        @Path("free_company_id") freeCompanyId: Int,
        @Body freeCompany: FreeCompany
    ): Response<Unit>

}
