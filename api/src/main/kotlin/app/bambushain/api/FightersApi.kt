package app.bambushain.api

import app.bambushain.model.Fighter
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface FightersApi {
    /**
     * POST api/final-fantasy/character/{character_id}/fighter
     * Create a new fighter for a character
     * 
     * Responses:
     *  - 201: Fighter created successfully
     *  - 401: Unauthorized
     *  - 404: Character not found
     *
     * @param characterId
     * @param fighter
     * @return [Fighter]
     */
    @POST("api/final-fantasy/character/{character_id}/fighter")
    suspend fun createFighter(
        @Path("character_id") characterId: Int,
        @Body fighter: Fighter
    ): Response<Fighter>

    /**
     * DELETE api/final-fantasy/character/{character_id}/fighter/{fighter_id}
     * Delete a fighter for a character
     * 
     * Responses:
     *  - 204: Fighter deleted successfully
     *  - 401: Unauthorized
     *  - 404: Fighter not found
     *
     * @param characterId
     * @param fighterId
     * @return [Unit]
     */
    @DELETE("api/final-fantasy/character/{character_id}/fighter/{fighter_id}")
    suspend fun deleteFighter(
        @Path("character_id") characterId: Int,
        @Path("fighter_id") fighterId: Int
    ): Response<Unit>

    /**
     * GET api/final-fantasy/character/{character_id}/fighter/{fighter_id}
     * Get a specific fighter for a character
     * 
     * Responses:
     *  - 200: Fighter details
     *  - 401: Unauthorized
     *  - 404: Fighter not found
     *
     * @param characterId
     * @param fighterId
     * @return [Fighter]
     */
    @GET("api/final-fantasy/character/{character_id}/fighter/{fighter_id}")
    suspend fun getFighter(
        @Path("character_id") characterId: Int,
        @Path("fighter_id") fighterId: Int
    ): Response<Fighter>

    /**
     * GET api/final-fantasy/character/{character_id}/fighter
     * Get all fighters for a character
     * 
     * Responses:
     *  - 200: List of fighters
     *  - 401: Unauthorized
     *  - 404: Character not found
     *
     * @param characterId
     * @return [kotlin.collections.List<Fighter>]
     */
    @GET("api/final-fantasy/character/{character_id}/fighter")
    suspend fun getFighters(@Path("character_id") characterId: Int): Response<List<Fighter>>

    /**
     * PUT api/final-fantasy/character/{character_id}/fighter/{fighter_id}
     * Update a fighter for a character
     * 
     * Responses:
     *  - 204: Fighter updated successfully
     *  - 401: Unauthorized
     *  - 404: Fighter not found
     *
     * @param characterId
     * @param fighterId
     * @param fighter
     * @return [Unit]
     */
    @PUT("api/final-fantasy/character/{character_id}/fighter/{fighter_id}")
    suspend fun updateFighter(
        @Path("character_id") characterId: Int,
        @Path("fighter_id") fighterId: Int,
        @Body fighter: Fighter
    ): Response<Unit>

}
