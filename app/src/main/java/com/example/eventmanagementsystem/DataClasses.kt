package com.example.eventmanagementsystem

import kotlinx.serialization.Serializable

@Serializable
data class EventResponse(
    val events: List<Event>,
    val total: Int,
    val perPage: Int,
    val page: Int
)

@Serializable
data class VolunteerEventResponse(
    val events: List<Event>,
    val total: Int,
    /* val perPage: Int,
     val page: Int*/
)

@Serializable
data class Event(
    val _id: String,
    val title: String, val organiser: String,
    val description: String, val event_date: String, val location: String, val image: String,
    val quota: Int,
    val highlight: Boolean,
    val createdAt: String, val modifiedAt: String, val volunteers: List<String>? = null
) {
    companion object {
        val data = listOf(
            Event(
                "1",
                "Event1",
                "Organizer1",
                "description1",
                "12-12-2001",
                " Location1 ",
                "https://cdn.stocksnap.io/img-thumbs/960w/philadelphia-travel_LPDQBLM2A0.jpg",
                50,
                false,
                "Error",
                "Error",
                null
            ),
        )
    }
}

@Serializable
data class HttpBinResponse(
    val args: Map<String, String>,
    val data: String,
    val files: Map<String, String>,
    val form: Map<String, String>,
    val headers: Map<String, String>,
    val json: String?,
    val origin: String,
    val url: String
)

@Serializable
data class JoinEventResponse(
    val message: String
)

@Serializable
data class Volunteer(
    val _id: String,
    val email: String,
    //val password: String,
    val name: String,
    val contact: String,
    val age_group: String,
    val about: String,
    val terms: Boolean,
    val createdAt: String,
    val modifiedAt: String,
    val isAdmin: Boolean,
    val events: List<String>,

    )

@Serializable
data class VolunteerRegister(
    val email: String,
    val password: String,
    val name: String,
    val contact: String,
    val age_group: String,
    val about: String,
    val terms: Boolean,
)

@Serializable
data class VolunteerLogin(
    val email: String,
    val password: String
)

@Serializable
data class VolunteerLoginResponse(
    val token: String,

    )


public var loggedIn: Boolean = false
public var userId: String = ""