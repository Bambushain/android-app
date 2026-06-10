package app.bambushain.api

import app.bambushain.model.Gatherer
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface GatherersApi {
    /**
     * POST api/final-fantasy/character/{character_id}/gatherer
     * Create a new gatherer for a character
     * 
     * Responses:
     *  - 201: Gatherer created successfully
     *  - 401: Unauthorized
     *  - 404: Character not found
     *
     * @param characterId
     * @param gatherer
     * @return [Gatherer]
     */
    @POST("api/final-fantasy/character/{character_id}/gatherer")
    suspend fun createGatherer(
        @Path("character_id") characterId: Int,
        @Body gatherer: Gatherer
    ): Response<Gatherer>

    /**
     * DELETE api/final-fantasy/character/{character_id}/gatherer/{gatherer_id}
     * Delete a gatherer for a character
     * 
     * Responses:
     *  - 204: Gatherer deleted successfully
     *  - 401: Unauthorized
     *  - 404: Gatherer not found
     *
     * @param characterId
     * @param gathererId
     * @return [Unit]
     */
    @DELETE("api/final-fantasy/character/{character_id}/gatherer/{gatherer_id}")
    suspend fun deleteGatherer(
        @Path("character_id") characterId: Int,
        @Path("gatherer_id") gathererId: Int
    ): Response<Unit>

    /**
     * GET api/final-fantasy/character/{character_id}/gatherer/{gatherer_id}
     * Get a specific gatherer for a character
     * 
     * Responses:
     *  - 200: Gatherer details
     *  - 401: Unauthorized
     *  - 404: Gatherer not found
     *
     * @param characterId
     * @param gathererId
     * @return [Gatherer]
     */
    @GET("api/final-fantasy/character/{character_id}/gatherer/{gatherer_id}")
    suspend fun getGatherer(
        @Path("character_id") characterId: Int,
        @Path("gatherer_id") gathererId: Int
    ): Response<Gatherer>

    /**
     * GET api/final-fantasy/character/{character_id}/gatherer
     * Get all gatherers for a character
     * 
     * Responses:
     *  - 200: List of gatherers
     *  - 401: Unauthorized
     *  - 404: Character not found
     *
     * @param characterId
     * @return [kotlin.collections.List<Gatherer>]
     */
    @GET("api/final-fantasy/character/{character_id}/gatherer")
    suspend fun getGatherers(@Path("character_id") characterId: Int): Response<List<Gatherer>>

    /**
     * PUT api/final-fantasy/character/{character_id}/gatherer/{gatherer_id}
     * Update a gatherer for a character
     * 
     * Responses:
     *  - 204: Gatherer updated successfully
     *  - 401: Unauthorized
     *  - 404: Gatherer not found
     *
     * @param characterId
     * @param gathererId
     * @param gatherer
     * @return [Unit]
     */
    @PUT("api/final-fantasy/character/{character_id}/gatherer/{gatherer_id}")
    suspend fun updateGatherer(
        @Path("character_id") characterId: Int,
        @Path("gatherer_id") gathererId: Int,
        @Body gatherer: Gatherer
    ): Response<Unit>

}
