package app.bambushain.api

import app.bambushain.model.FirebaseLogin
import app.bambushain.model.ForgotPassword
import app.bambushain.model.Login
import app.bambushain.model.LoginResult
import app.bambushain.model.ResetPassword
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.HEAD
import retrofit2.http.POST

interface AuthenticationApi {
    /**
     * HEAD api/login
     * Check if token is valid
     * 
     * Responses:
     *  - 204: Token is valid
     *  - 401: Token is invalid
     *
     * @return [Unit]
     */
    @HEAD("api/login")
    suspend fun checkToken(): Response<Unit>

    /**
     * POST api/forgot-password
     * Request password reset
     * 
     * Responses:
     *  - 204: Password reset email sent
     *
     * @param forgotPassword
     * @return [Unit]
     */
    @POST("api/forgot-password")
    suspend fun forgotPassword(@Body forgotPassword: ForgotPassword): Response<Unit>

    /**
     * POST api/login
     * Login to the application
     * 
     * Responses:
     *  - 200: Successful login
     *  - 204: Two-factor authentication required
     *  - 401: Invalid login credentials
     *
     * @param login
     * @return [LoginResult]
     */
    @POST("api/login")
    suspend fun login(@Body login: Login): Response<LoginResult>

    /**
     * DELETE api/login
     * Logout from the application
     * 
     * Responses:
     *  - 204: Successfully logged out
     *  - 401: Unauthorized
     *
     * @return [Unit]
     */
    @DELETE("api/login")
    suspend fun logout(): Response<Unit>

    /**
     * POST api/reset-password
     * Reset password using token
     * 
     * Responses:
     *  - 204: Password reset successful
     *  - 400: Invalid token or request
     *
     * @param resetPassword
     * @return [Unit]
     */
    @POST("api/reset-password")
    suspend fun resetPassword(@Body resetPassword: ResetPassword): Response<Unit>

    /**
     * POST api/firebase/login
     * Register the firebase token
     *
     * Responses:
     *  - 204: Successful login
     *  - 401: Invalid token
     *
     * @param firebaseLogin
     * @return [Unit]
     */
    @POST("api/firebase/login")
    suspend fun firebaseLogin(@Body firebaseLogin: FirebaseLogin): Response<Unit>
}
