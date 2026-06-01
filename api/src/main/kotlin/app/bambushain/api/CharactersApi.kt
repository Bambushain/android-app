package app.bambushain.api

import app.bambushain.model.Character
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface CharactersApi {
    /**
     * POST api/final-fantasy/character
     * Create a new character
     * 
     * Responses:
     *  - 201: Character created successfully
     *  - 401: Unauthorized
     *
     * @param character
     * @return [Character]
     */
    @POST("api/final-fantasy/character")
    suspend fun createCharacter(@Body character: Character): Response<Character>

    /**
     * DELETE api/final-fantasy/character/{character_id}
     * Delete a character
     * 
     * Responses:
     *  - 204: Character deleted successfully
     *  - 401: Unauthorized
     *  - 404: Character not found
     *
     * @param characterId
     * @return [Unit]
     */
    @DELETE("api/final-fantasy/character/{character_id}")
    suspend fun deleteCharacter(@Path("character_id") characterId: Int): Response<Unit>

    /**
     * GET api/final-fantasy/character/{character_id}
     * Get a specific character
     * 
     * Responses:
     *  - 200: Character details
     *  - 401: Unauthorized
     *  - 404: Character not found
     *
     * @param characterId
     * @return [Character]
     */
    @GET("api/final-fantasy/character/{character_id}")
    suspend fun getCharacter(@Path("character_id") characterId: Int): Response<Character>

    /**
     * GET api/final-fantasy/character
     * Get all characters
     * 
     * Responses:
     *  - 200: List of characters
     *  - 401: Unauthorized
     *
     * @return [kotlin.collections.List<Character>]
     */
    @GET("api/final-fantasy/character")
    suspend fun getCharacters(): Response<List<Character>>

    /**
     * PUT api/final-fantasy/character/{character_id}
     * Update a character
     * 
     * Responses:
     *  - 204: Character updated successfully
     *  - 401: Unauthorized
     *  - 404: Character not found
     *
     * @param characterId
     * @param character
     * @return [Unit]
     */
    @PUT("api/final-fantasy/character/{character_id}")
    suspend fun updateCharacter(
        @Path("character_id") characterId: Int,
        @Body character: Character
    ): Response<Unit>

}
