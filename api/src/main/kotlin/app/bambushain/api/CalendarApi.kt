package app.bambushain.api

import app.bambushain.model.EventReminder
import app.bambushain.model.GroveEvent
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface CalendarApi {
    /**
     * DELETE api/bamboo-grove/event/{event_id}
     * Delete event
     * Delete a grove event
     * Responses:
     *  - 204: Event deleted successfully
     *  - 400: Invalid dates
     *  - 401: Unauthorized
     *  - 404: Not found
     *  - 500: Internal server error
     *
     * @param eventId ID of the event to delete
     * @return [Unit]
     */
    @DELETE("api/bamboo-grove/event/{event_id}")
    suspend fun deleteGroveEvent(@Path("event_id") eventId: Int): Response<Unit>

    /**
     * PUT api/bamboo-grove/event/{event_id}
     * Update event
     * Update an existing grove event
     * Responses:
     *  - 204: Event updated successfully
     *  - 400: Invalid dates
     *  - 401: Unauthorized
     *  - 404: Not found
     *  - 500: Internal server error
     *
     * @param eventId ID of the event to update
     * @param groveEvent
     * @return [Unit]
     */
    @PUT("api/bamboo-grove/event/{event_id}")
    suspend fun updateGroveEvent(
        @Path("event_id") eventId: Int,
        @Body groveEvent: GroveEvent
    ): Response<Unit>

    /**
     * GET api/bamboo-grove/event/{event_id}/reminder
     * Get reminder for event
     * Gets the reminder for the passed event id
     * Responses:
     *  - 200: Event reminder
     *  - 400: Invalid dates
     *  - 401: Unauthorized
     *  - 404: Not found
     *  - 500: Internal server error
     *
     * @param eventId ID of the event to update
     * @return [kotlin.collections.List<EventReminder>]
     */
    @GET("api/bamboo-grove/event/{event_id}/reminder")
    suspend fun getReminderForEvent(@Path("event_id") eventId: Int): Response<List<EventReminder>>

    /**
     * POST api/bamboo-grove/event/{event_id}/reminder
     * Create event reminder
     * Create a new grove event reminder
     * Responses:
     *  - 201: Event reminder created successfully
     *  - 400: Invalid dates
     *  - 401: Unauthorized
     *  - 500: Internal server error
     *
     * @param eventId ID of the event to update
     * @param eventReminder
     * @return [GroveEvent]
     */
    @POST("api/bamboo-grove/event/{event_id}/reminder")
    suspend fun addEventReminder(
        @Path("event_id") eventId: Int,
        @Body eventReminder: EventReminder
    ): Response<GroveEvent>

    /**
     * DELETE api/bamboo-grove/event/{event_id}/reminder/{reminder_id}
     * Delete event reminder
     * Delete a grove event reminder
     * Responses:
     *  - 204: Event deleted successfully
     *  - 400: Invalid dates
     *  - 401: Unauthorized
     *  - 404: Not found
     *  - 500: Internal server error
     *
     * @param eventId
     * @param reminderId
     * @return [Unit]
     */
    @DELETE("api/bamboo-grove/event/{event_id}/reminder/{reminder_id}")
    suspend fun deleteEventReminder(
        @Path("event_id") eventId: Int,
        @Path("reminder_id") reminderId: Int
    ): Response<Unit>

    /**
     * GET api/bamboo-grove/event
     * Get events
     * Retrieve events within a date range, optionally filtered by grove
     * Responses:
     *  - 200: List of events retrieved successfully
     *  - 400: Invalid dates
     *  - 401: Unauthorized
     *  - 500: Internal server error
     *
     * @param start Start date for event range
     * @param end End date for event range
     * @param grove Optional grove ID to filter events (optional)
     * @return [List<GroveEvent>]
     */
    @GET("api/bamboo-grove/event")
    suspend fun getGroveEvents(
        @Query("start") start: kotlinx.datetime.LocalDate,
        @Query("end") end: kotlinx.datetime.LocalDate,
        @Query("grove") grove: Int? = null
    ): Response<List<GroveEvent>>

    /**
     * POST api/bamboo-grove/event
     * Create event
     * Create a new grove event
     * Responses:
     *  - 201: Event created successfully
     *  - 400: Invalid dates
     *  - 401: Unauthorized
     *  - 500: Internal server error
     *
     * @param groveEvent
     * @return [GroveEvent]
     */
    @POST("api/bamboo-grove/event")
    suspend fun createGroveEvent(@Body groveEvent: GroveEvent): Response<GroveEvent>

}
