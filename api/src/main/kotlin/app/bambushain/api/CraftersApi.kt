package app.bambushain.api

import app.bambushain.model.Crafter
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface CraftersApi {
    /**
     * POST api/final-fantasy/character/{character_id}/crafter
     * Create a new crafter for a character
     * 
     * Responses:
     *  - 201: Crafter created successfully
     *  - 401: Unauthorized
     *  - 404: Character not found
     *
     * @param characterId
     * @param crafter
     * @return [Crafter]
     */
    @POST("api/final-fantasy/character/{character_id}/crafter")
    suspend fun createCrafter(
        @Path("character_id") characterId: Int,
        @Body crafter: Crafter
    ): Response<Crafter>

    /**
     * DELETE api/final-fantasy/character/{character_id}/crafter/{crafter_id}
     * Delete a crafter for a character
     * 
     * Responses:
     *  - 204: Crafter deleted successfully
     *  - 401: Unauthorized
     *  - 404: Crafter not found
     *
     * @param characterId
     * @param crafterId
     * @return [Unit]
     */
    @DELETE("api/final-fantasy/character/{character_id}/crafter/{crafter_id}")
    suspend fun deleteCrafter(
        @Path("character_id") characterId: Int,
        @Path("crafter_id") crafterId: Int
    ): Response<Unit>

    /**
     * GET api/final-fantasy/character/{character_id}/crafter/{crafter_id}
     * Get a specific crafter for a character
     * 
     * Responses:
     *  - 200: Crafter details
     *  - 401: Unauthorized
     *  - 404: Crafter not found
     *
     * @param characterId
     * @param crafterId
     * @return [Crafter]
     */
    @GET("api/final-fantasy/character/{character_id}/crafter/{crafter_id}")
    suspend fun getCrafter(
        @Path("character_id") characterId: Int,
        @Path("crafter_id") crafterId: Int
    ): Response<Crafter>

    /**
     * GET api/final-fantasy/character/{character_id}/crafter
     * Get all crafters for a character
     * 
     * Responses:
     *  - 200: List of crafters
     *  - 401: Unauthorized
     *  - 404: Character not found
     *
     * @param characterId
     * @return [kotlin.collections.List<Crafter>]
     */
    @GET("api/final-fantasy/character/{character_id}/crafter")
    suspend fun getCrafters(@Path("character_id") characterId: Int): Response<List<Crafter>>

    /**
     * PUT api/final-fantasy/character/{character_id}/crafter/{crafter_id}
     * Update a crafter for a character
     * 
     * Responses:
     *  - 204: Crafter updated successfully
     *  - 401: Unauthorized
     *  - 404: Crafter not found
     *
     * @param characterId
     * @param crafterId
     * @param crafter
     * @return [Unit]
     */
    @PUT("api/final-fantasy/character/{character_id}/crafter/{crafter_id}")
    suspend fun updateCrafter(
        @Path("character_id") characterId: Int,
        @Path("crafter_id") crafterId: Int,
        @Body crafter: Crafter
    ): Response<Unit>

}
