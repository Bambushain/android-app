package app.bambushain.api

import app.bambushain.model.ChangePassword
import app.bambushain.model.TotpQrCode
import app.bambushain.model.UpdateProfile
import app.bambushain.model.User
import app.bambushain.model.ValidateTotp
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface MyProfileApi {
    /**
     * PUT api/my/password
     * Change current user password
     * 
     * Responses:
     *  - 204: Password changed successfully
     *  - 401: Unauthorized
     *
     * @param changePassword
     * @return [Unit]
     */
    @PUT("api/my/password")
    suspend fun changePassword(@Body changePassword: ChangePassword): Response<Unit>

    /**
     * DELETE api/my/totp
     * Disable totp
     * 
     * Responses:
     *  - 204: Totp disabled successfully
     *  - 401: Unauthorized
     *
     * @return [Unit]
     */
    @DELETE("api/my/totp")
    suspend fun disableTotp(): Response<Unit>

    /**
     * POST api/my/totp
     * Enable totp
     * 
     * Responses:
     *  - 204: Totp enablement request send successfully
     *  - 401: Unauthorized
     *
     * @param totpQrCode
     * @return [Unit]
     */
    @POST("api/my/totp")
    suspend fun enableTotp(@Body totpQrCode: TotpQrCode): Response<Unit>

    /**
     * GET api/my/profile
     * Get current user profile
     * 
     * Responses:
     *  - 200: User profile
     *  - 401: Unauthorized
     *
     * @return [User]
     */
    @GET("api/my/profile")
    suspend fun getProfile(): Response<User>

    /**
     * PUT api/my/profile
     * Update current user profile
     * 
     * Responses:
     *  - 204: Profile updated successfully
     *  - 401: Unauthorized
     *
     * @param updateProfile
     * @return [Unit]
     */
    @PUT("api/my/profile")
    suspend fun updateProfile(@Body updateProfile: UpdateProfile): Response<Unit>

    /**
     * PUT api/my/totp/validate
     * Validate the totp registration
     * 
     * Responses:
     *  - 204: Totp validated successfully
     *  - 401: Unauthorized
     *
     * @param validateTotp
     * @return [Unit]
     */
    @PUT("api/my/totp/validate")
    suspend fun validateTotp(@Body validateTotp: ValidateTotp): Response<Unit>

}
