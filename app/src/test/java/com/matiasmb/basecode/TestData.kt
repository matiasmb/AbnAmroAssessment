package com.matiasmb.basecode

import com.matiasmb.basecode.data.dto.Location
import com.matiasmb.basecode.data.dto.Photo
import com.matiasmb.basecode.data.dto.Place
import com.matiasmb.basecode.data.dto.PlaceSearchResponse
import com.matiasmb.basecode.data.service.ResponseType
import com.matiasmb.basecode.domain.model.ItemPlaceView
import com.matiasmb.basecode.domain.model.PlaceView
import kotlinx.coroutines.flow.flow

object TestData {

    val location = Location(
        address = "Coolsingel 105",
        postcode = "3012 AG",
        locality = "Rotterdam"
    )

    val placeSearch = Place(
        id = "5b6a9a644ac28a002cf131d0",
        location = location,
        name = "De Bijenkorf",
        email = null,
        tel = null,
        photos = null,
        description = null,
        website = null,
        rating = null
    )

    val photoListDto = listOf(
        Photo(
            prefix = "https://fastly.4sqi.net/img/general/",
            suffix = "/82624_qd27oVHlkCg3gayZWMAbQV3SoeIM4u4rMgUZlKWLs2U.jpg"
        ),
        Photo(
            prefix = "https://fastly.4sqi.net/img/general/",
            suffix = "/35749755_0SLSYr-Un7IqWlg9Q0e6iCwq8iVvhUkfxrOVvyRL90k.jpg"
        )
    )

    val placeDetailSearch = Place(
        id = "5b6a9a644ac28a002cf131d0",
        location = location,
        name = "De Bijenkorf",
        email = "testEmail",
        tel = "010 282 3700",
        photos = photoListDto,
        description = "Swatch, launched in 1983 by Nicolas G. Hayek, is a leading Swiss watch maker and one of the world's most popular brands",
        website = "http://www.debijenkorf.nl",
        rating = 9.5F
    )

    val dataPlaceSearchResponseEmpty = PlaceSearchResponse(
        emptyList()
    )

    val dataPlaceSearchResponse = PlaceSearchResponse(
        listOf(placeSearch)
    )

    val serviceSuccessSearchPlacesResponse =
        flow { emit(ResponseType.Success(dataPlaceSearchResponse)) }

    val serviceSuccessDetailPlaceResponse = flow { emit(ResponseType.Success(placeDetailSearch)) }

    val serviceFailureResponse = flow { emit(ResponseType.Failure) }

    val placeViewListFlow = flow {
        emit(
            listOf(
                ItemPlaceView(
                    name = "De Bijenkorf",
                    formattedAddress = "Address: Coolsingel 105\n" +
                            "Postal code: 3012 AG\n" +
                            "City: Rotterdam",
                    id = "5b6a9a644ac28a002cf131d0"
                )
            )
        )
    }

    val placeViewFlow = flow {
        emit(
            PlaceView(
                name = "De Bijenkorf",
                formattedAddress = "Address: Coolsingel 105\n" +
                        "Postal code: 3012 AG\n" +
                        "City: Rotterdam",
                description = "Swatch, launched in 1983 by Nicolas G. Hayek, is a leading Swiss watch maker and one of the world's most popular brands",
                imageUrl = "https://fastly.4sqi.net/img/general/original//82624_qd27oVHlkCg3gayZWMAbQV3SoeIM4u4rMgUZlKWLs2U.jpg",
                contactInformation = "Telephone: 010 282 3700\n" +
                        "Email: testEmail\n" +
                        "Website: http://www.debijenkorf.nl",
                rating = 8.3F
            )
        )
    }

    val flowNull = flow { emit(null) }
}
