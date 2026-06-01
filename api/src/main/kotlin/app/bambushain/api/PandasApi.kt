package app.bambushain.api

import app.bambushain.model.User
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PandasApi {
    /**
     * GET api/user/{user_id}/picture
     * Get a user\&quot;s profile picture
     * 
     * Responses:
     *  - 200: Profile picture
     *  - 401: Unauthorized
     *  - 404: User not found
     *
     * @param userId
     * @return [ResponseBody]
     */
    @GET("api/user/{user_id}/picture")
    suspend fun getProfilePicture(@Path("user_id") userId: Int): Response<ResponseBody>

    /**
     * GET api/user
     * Get all users
     * 
     * Responses:
     *  - 200: List of users
     *  - 401: Unauthorized
     *
     * @param grove  (optional)
     * @param banned  (optional, default to false)
     * @param all  (optional, default to false)
     * @return [kotlin.collections.List<User>]
     */
    @GET("api/user")
    suspend fun getUsers(
        @Query("grove") grove: Int? = null,
        @Query("banned") banned: Boolean? = false,
        @Query("all") all: Boolean? = false
    ): Response<List<User>>

}
