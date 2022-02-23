package com.matiasmb.basecode.data

import com.matiasmb.basecode.data.dto.Place
import com.matiasmb.basecode.data.dto.PlaceSearchResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface FoursquareApiClient {

    /**
     * List products for the specified query.
     *
     * @param near - location to find places around.
     * @return a list of products.
     */
    @GET("search?limit=10")
    suspend fun searchPlaces(
        @Query("near") near: String
    ): PlaceSearchResponse

    /**
     * List products for the specified query.
     *
     * @param placeId - foursquare place identifier.
     * @return a place object.
     */
    @GET("{placeId}?fields=fsq_id,name,location,description,photos,tel,email,rating,website")
    suspend fun getPlaceDetails(
        @Path("placeId") placeId: String
    ): Place
}
