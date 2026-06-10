package app.bambushain.api

import app.bambushain.model.CreateGrove
import app.bambushain.model.Grove
import app.bambushain.model.GroveUser
import app.bambushain.model.JoinGrove
import app.bambushain.model.JoinGroveStatus
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface GrovesApi {
    /**
     * PUT api/grove/{grove_id}/user/{user_id}/ban
     * Ban user from grove (requires moderator permissions)
     * 
     * Responses:
     *  - 204: User banned successfully
     *  - 401: Unauthorized
     *  - 403: Insufficient permissions
     *  - 404: Grove or user not found
     *
     * @param groveId
     * @param userId
     * @return [Unit]
     */
    @PUT("api/grove/{grove_id}/user/{user_id}/ban")
    suspend fun banUser(
        @Path("grove_id") groveId: String,
        @Path("user_id") userId: String
    ): Response<Unit>

    /**
     * GET api/grove/{grove_id}/join
     * Check join status for a grove
     * 
     * Responses:
     *  - 200: Join status information
     *  - 401: Unauthorized
     *  - 404: Grove not found
     *
     * @param groveId
     * @return [JoinGroveStatus]
     */
    @GET("api/grove/{grove_id}/join")
    suspend fun checkJoinStatus(@Path("grove_id") groveId: String): Response<JoinGroveStatus>

    /**
     * POST api/grove
     * Create a new grove
     * 
     * Responses:
     *  - 201: Grove created successfully
     *  - 400: Invalid request body
     *  - 401: Unauthorized
     *
     * @param createGrove
     * @return [Grove]
     */
    @POST("api/grove")
    suspend fun createGrove(@Body createGrove: CreateGrove): Response<Grove>

    /**
     * DELETE api/grove/{grove_id}
     * Delete grove (requires moderator permissions)
     * 
     * Responses:
     *  - 204: Grove deleted successfully
     *  - 401: Unauthorized
     *  - 403: Insufficient permissions
     *  - 404: Grove not found
     *
     * @param groveId
     * @return [Unit]
     */
    @DELETE("api/grove/{grove_id}")
    suspend fun deleteGrove(@Path("grove_id") groveId: String): Response<Unit>

    /**
     * DELETE api/grove/{grove_id}/invite
     * Disable grove invites (requires moderator permissions)
     * 
     * Responses:
     *  - 204: Invites disabled successfully
     *  - 401: Unauthorized
     *  - 403: Insufficient permissions
     *  - 404: Grove not found
     *
     * @param groveId
     * @return [Unit]
     */
    @DELETE("api/grove/{grove_id}/invite")
    suspend fun disableInvite(@Path("grove_id") groveId: String): Response<Unit>

    /**
     * PUT api/grove/{grove_id}/invite
     * Enable grove invites (requires moderator permissions)
     * 
     * Responses:
     *  - 204: Invites enabled successfully
     *  - 401: Unauthorized
     *  - 403: Insufficient permissions
     *  - 404: Grove not found
     *
     * @param groveId
     * @return [Unit]
     */
    @PUT("api/grove/{grove_id}/invite")
    suspend fun enableInvite(@Path("grove_id") groveId: String): Response<Unit>

    /**
     * GET api/grove/{grove_id}
     * Get a specific grove
     * 
     * Responses:
     *  - 200: Grove details
     *  - 401: Unauthorized
     *  - 404: Grove not found
     *
     * @param groveId
     * @return [Grove]
     */
    @GET("api/grove/{grove_id}")
    suspend fun getGrove(@Path("grove_id") groveId: String): Response<Grove>

    /**
     * GET api/grove/{grove_id}/user
     * Get users of a grove
     * 
     * Responses:
     *  - 200: Grove details
     *  - 401: Unauthorized
     *  - 404: Grove not found
     *
     * @param groveId
     * @return [kotlin.collections.List<GroveUser>]
     */
    @GET("api/grove/{grove_id}/user")
    suspend fun getGroveUsers(@Path("grove_id") groveId: String): Response<List<GroveUser>>

    /**
     * GET api/grove
     * Get all groves for authenticated user
     * 
     * Responses:
     *  - 200: List of groves
     *  - 401: Unauthorized
     *
     * @return [kotlin.collections.List<Grove>]
     */
    @GET("api/grove")
    suspend fun getGroves(): Response<List<Grove>>

    /**
     * POST api/grove/{grove_id}/join
     * Join a grove
     * 
     * Responses:
     *  - 204: Successfully joined grove
     *  - 400: Invalid request body or invite secret
     *  - 401: Unauthorized
     *  - 403: Not allowed to join (banned or invites disabled)
     *  - 404: Grove not found
     *
     * @param groveId
     * @param joinGrove
     * @return [Unit]
     */
    @POST("api/grove/{grove_id}/join")
    suspend fun joinGrove(
        @Path("grove_id") groveId: String,
        @Body joinGrove: JoinGrove
    ): Response<Unit>

    /**
     * DELETE api/grove/{grove_id}/user/{user_id}/ban
     * Unban user from grove (requires moderator permissions)
     * 
     * Responses:
     *  - 204: User unbanned successfully
     *  - 401: Unauthorized
     *  - 403: Insufficient permissions
     *  - 404: Grove or user not found
     *
     * @param groveId
     * @param userId
     * @return [Unit]
     */
    @DELETE("api/grove/{grove_id}/user/{user_id}/ban")
    suspend fun unbanUser(
        @Path("grove_id") groveId: String,
        @Path("user_id") userId: String
    ): Response<Unit>

    /**
     * PUT api/grove/{grove_id}
     * Update grove (requires moderator permissions)
     * 
     * Responses:
     *  - 204: Grove updated successfully
     *  - 400: Invalid request body
     *  - 401: Unauthorized
     *  - 403: Insufficient permissions
     *  - 404: Grove not found
     *
     * @param groveId
     * @param grove
     * @return [Unit]
     */
    @PUT("api/grove/{grove_id}")
    suspend fun updateGrove(
        @Path("grove_id") groveId: String,
        @Body grove: Grove
    ): Response<Unit>

    /**
     * PUT api/grove/{grove_id}/mods
     * Update grove moderators (requires moderator permissions)
     * 
     * Responses:
     *  - 204: Moderators updated successfully
     *  - 400: Invalid request body
     *  - 401: Unauthorized
     *  - 403: Insufficient permissions
     *  - 404: Grove not found
     *
     * @param groveId
     * @param requestBody
     * @return [Unit]
     */
    @PUT("api/grove/{grove_id}/mods")
    suspend fun updateGroveMods(
        @Path("grove_id") groveId: String,
        @Body requestBody: List<Int>
    ): Response<Unit>

}
