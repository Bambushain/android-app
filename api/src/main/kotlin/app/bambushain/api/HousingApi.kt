package app.bambushain.api

import app.bambushain.model.CharacterHousing
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface HousingApi {
    /**
     * POST api/final-fantasy/character/{character_id}/housing
     * Create a new housing for a character
     * 
     * Responses:
     *  - 201: Character housing created successfully
     *  - 401: Unauthorized
     *  - 404: Character not found
     *
     * @param characterId
     * @param characterHousing
     * @return [CharacterHousing]
     */
    @POST("api/final-fantasy/character/{character_id}/housing")
    suspend fun createCharacterHousing(
        @Path("character_id") characterId: Int,
        @Body characterHousing: CharacterHousing
    ): Response<CharacterHousing>

    /**
     * DELETE api/final-fantasy/character/{character_id}/housing/{character_housing_id}
     * Delete a housing for a character
     * 
     * Responses:
     *  - 204: Character housing deleted successfully
     *  - 401: Unauthorized
     *  - 404: Character housing not found
     *
     * @param characterId
     * @param characterHousingId
     * @return [Unit]
     */
    @DELETE("api/final-fantasy/character/{character_id}/housing/{character_housing_id}")
    suspend fun deleteCharacterHousing(
        @Path("character_id") characterId: Int,
        @Path("character_housing_id") characterHousingId: Int
    ): Response<Unit>

    /**
     * GET api/final-fantasy/character/{character_id}/housing/{character_housing_id}
     * Get a specific housing for a character
     * 
     * Responses:
     *  - 200: Character housing details
     *  - 401: Unauthorized
     *  - 404: Character housing not found
     *
     * @param characterId
     * @param characterHousingId
     * @return [CharacterHousing]
     */
    @GET("api/final-fantasy/character/{character_id}/housing/{character_housing_id}")
    suspend fun getCharacterHousing(
        @Path("character_id") characterId: Int,
        @Path("character_housing_id") characterHousingId: Int
    ): Response<CharacterHousing>

    /**
     * GET api/final-fantasy/character/{character_id}/housing
     * Get all housings for a character
     * 
     * Responses:
     *  - 200: List of character housings
     *  - 401: Unauthorized
     *  - 404: Character not found
     *
     * @param characterId
     * @return [kotlin.collections.List<CharacterHousing>]
     */
    @GET("api/final-fantasy/character/{character_id}/housing")
    suspend fun getCharacterHousings(@Path("character_id") characterId: Int): Response<List<CharacterHousing>>

    /**
     * PUT api/final-fantasy/character/{character_id}/housing/{character_housing_id}
     * Update a housing for a character
     * 
     * Responses:
     *  - 204: Character housing updated successfully
     *  - 401: Unauthorized
     *  - 404: Character housing not found
     *
     * @param characterId
     * @param characterHousingId
     * @param characterHousing
     * @return [Unit]
     */
    @PUT("api/final-fantasy/character/{character_id}/housing/{character_housing_id}")
    suspend fun updateCharacterHousing(
        @Path("character_id") characterId: Int,
        @Path("character_housing_id") characterHousingId: Int,
        @Body characterHousing: CharacterHousing
    ): Response<Unit>

}
