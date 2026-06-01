package app.bambushain.api

import app.bambushain.model.CustomField
import app.bambushain.model.CustomFieldOption
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface CustomFieldsApi {
    /**
     * POST api/final-fantasy/character/custom-field
     * Create a new custom field
     * 
     * Responses:
     *  - 201: Custom field created successfully
     *  - 401: Unauthorized
     *
     * @param customField
     * @return [CustomField]
     */
    @POST("api/final-fantasy/character/custom-field")
    suspend fun createCustomField(@Body customField: CustomField): Response<CustomField>

    /**
     * POST api/final-fantasy/character/custom-field/{field_id}/option
     * Create a new option for a custom field
     * 
     * Responses:
     *  - 201: Custom field option created successfully
     *  - 401: Unauthorized
     *  - 404: Custom field not found
     *
     * @param fieldId
     * @param body
     * @return [CustomFieldOption]
     */
    @POST("api/final-fantasy/character/custom-field/{field_id}/option")
    suspend fun createCustomFieldOption(
        @Path("field_id") fieldId: Int,
        @Body body: String
    ): Response<CustomFieldOption>

    /**
     * DELETE api/final-fantasy/character/custom-field/{field_id}
     * Delete a custom field
     * 
     * Responses:
     *  - 204: Custom field deleted successfully
     *  - 401: Unauthorized
     *  - 404: Custom field not found
     *
     * @param fieldId
     * @return [Unit]
     */
    @DELETE("api/final-fantasy/character/custom-field/{field_id}")
    suspend fun deleteCustomField(@Path("field_id") fieldId: Int): Response<Unit>

    /**
     * DELETE api/final-fantasy/character/custom-field/{field_id}/option/{option_id}
     * Delete an option for a custom field
     * 
     * Responses:
     *  - 204: Custom field option deleted successfully
     *  - 401: Unauthorized
     *  - 404: Custom field option not found
     *
     * @param fieldId
     * @param optionId
     * @return [Unit]
     */
    @DELETE("api/final-fantasy/character/custom-field/{field_id}/option/{option_id}")
    suspend fun deleteCustomFieldOption(
        @Path("field_id") fieldId: Int,
        @Path("option_id") optionId: Int
    ): Response<Unit>

    /**
     * GET api/final-fantasy/character/custom-field/{field_id}
     * Get a specific custom field
     * 
     * Responses:
     *  - 200: Custom field details
     *  - 401: Unauthorized
     *  - 404: Custom field not found
     *
     * @param fieldId
     * @return [CustomField]
     */
    @GET("api/final-fantasy/character/custom-field/{field_id}")
    suspend fun getCustomField(@Path("field_id") fieldId: Int): Response<CustomField>

    /**
     * GET api/final-fantasy/character/custom-field/{field_id}/option
     * Get all options for a custom field
     * 
     * Responses:
     *  - 200: List of custom field options
     *  - 401: Unauthorized
     *  - 404: Custom field not found
     *
     * @param fieldId
     * @return [kotlin.collections.List<CustomFieldOption>]
     */
    @GET("api/final-fantasy/character/custom-field/{field_id}/option")
    suspend fun getCustomFieldOptions(@Path("field_id") fieldId: Int): Response<List<CustomFieldOption>>

    /**
     * GET api/final-fantasy/character/custom-field
     * Get all custom fields
     * 
     * Responses:
     *  - 200: List of custom fields
     *  - 401: Unauthorized
     *
     * @return [kotlin.collections.List<CustomField>]
     */
    @GET("api/final-fantasy/character/custom-field")
    suspend fun getCustomFields(): Response<List<CustomField>>

    /**
     * PUT api/final-fantasy/character/custom-field/{field_id}/{position}
     * Move a custom field to a new position
     * 
     * Responses:
     *  - 204: Custom field moved successfully
     *  - 401: Unauthorized
     *  - 404: Custom field not found
     *
     * @param fieldId
     * @param position
     * @return [Unit]
     */
    @PUT("api/final-fantasy/character/custom-field/{field_id}/{position}")
    suspend fun moveCustomField(
        @Path("field_id") fieldId: Int,
        @Path("position") position: Int
    ): Response<Unit>

    /**
     * PUT api/final-fantasy/character/custom-field/{field_id}
     * Update a custom field
     * 
     * Responses:
     *  - 204: Custom field updated successfully
     *  - 401: Unauthorized
     *  - 404: Custom field not found
     *
     * @param fieldId
     * @param customField
     * @return [Unit]
     */
    @PUT("api/final-fantasy/character/custom-field/{field_id}")
    suspend fun updateCustomField(
        @Path("field_id") fieldId: Int,
        @Body customField: CustomField
    ): Response<Unit>

    /**
     * PUT api/final-fantasy/character/custom-field/{field_id}/option/{option_id}
     * Update an option for a custom field
     * 
     * Responses:
     *  - 204: Custom field option updated successfully
     *  - 401: Unauthorized
     *  - 404: Custom field option not found
     *
     * @param fieldId
     * @param optionId
     * @param body
     * @return [Unit]
     */
    @PUT("api/final-fantasy/character/custom-field/{field_id}/option/{option_id}")
    suspend fun updateCustomFieldOption(
        @Path("field_id") fieldId: Int,
        @Path("option_id") optionId: Int,
        @Body body: String
    ): Response<Unit>

}
