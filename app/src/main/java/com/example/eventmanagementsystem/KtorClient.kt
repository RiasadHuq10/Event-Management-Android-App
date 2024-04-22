import android.util.Log
import com.example.eventmanagementsystem.Event
import com.example.eventmanagementsystem.EventResponse
import com.example.eventmanagementsystem.HttpBinResponse
import com.example.eventmanagementsystem.JoinEventResponse
import com.example.eventmanagementsystem.Volunteer
import com.example.eventmanagementsystem.VolunteerEventResponse
import com.example.eventmanagementsystem.VolunteerLogin
import com.example.eventmanagementsystem.VolunteerLoginResponse
import com.example.eventmanagementsystem.VolunteerRegister
import com.example.eventmanagementsystem.userId
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.serialization.kotlinx.serialization
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject

object KtorClient {
    private var token: String = ""
    val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
                allowSpecialFloatingPointValues = true
                allowStructuredMapKeys = true
            })
        }
        install(Logging)
        defaultRequest {
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
            header("Authorization", "Bearer " + token)
        }

        expectSuccess = true
    }

    suspend fun getEvents(params: List<String>?): EventResponse {
        try {
            var url: String = "https://comp4107-spring2024.azurewebsites.net/api/events/"
            if (!params.isNullOrEmpty()) {
                url += "?"
                for (i in params.indices) {
                    url += params[i]
                    if (i != params.size - 1) url += "&"
                }

            }
            Log.d("URL", url)
            return httpClient.get(url).body<EventResponse>()
        } catch (e: Exception) {
            return EventResponse(
                listOf(
                    Event(
                        "Error",
                        e.toString(),
                        "Error",
                        e.toString(),
                        " error",
                        "Error",
                        "Error",
                        -1,
                        false,
                        "Error",
                        "Error",
                        null

                    )
                ), -1, -1, -1
            )

        }
    }


    suspend fun getEvent(id: String?): Event {
        return try {
            val url: String = "https://comp4107-spring2024.azurewebsites.net/api/events/$id"
            //var argument:String = "https://comp4107-spring2024.azurewebsites.net/api/events/652cfa83a2d76d2bfe59c4b0"

            httpClient.get(url).body()
        } catch (e: Exception) {

            Event(
                "Error",
                e.toString(),
                "Error",
                e.toString(),
                " error",
                "Error",
                "Error",
                -1,
                false,
                "Error",
                "Error",
                null

            )

        }
    }

    suspend fun login(email: String, password: String): String? {
        try {
            val response: VolunteerLoginResponse =
                httpClient.post("https://comp4107-spring2024.azurewebsites.net/api/login/") {
                    setBody(VolunteerLogin(email, password))
                }.body()
            Log.d("login body", response.toString())

            token = response.token.toString()
            return response.token.toString()
        } catch (e: Exception) {
            Log.d("login error", e.toString())
            return null
        }
    }

    suspend fun register(volunteerRegister: VolunteerRegister): String? {
        try {
            val response =
                httpClient.post("https://comp4107-spring2024.azurewebsites.net/api/volunteers/") {
                    setBody(volunteerRegister)
                }
            Log.d("register body", response.toString())
            return "Success"
        } catch (e: Exception) {
            Log.d("register error", e.toString())
            return null
        }
    }

    suspend fun joinEvent(eventId: String): String? {
        try {
            val response: JoinEventResponse =
                httpClient.post("https://comp4107-spring2024.azurewebsites.net/api/events/$eventId/volunteers") {}
                    .body()

            return response.message.toString()
        } catch (e: Exception) {
            Log.d("Join event error", e.toString())

            return null
        }

    }

    suspend fun unregisterEvent(eventId: String): String? {
        try {
            val response: JoinEventResponse =
                httpClient.delete("https://comp4107-spring2024.azurewebsites.net/api/events/$eventId/volunteers") {}
                    .body()

            return response.message.toString()
        } catch (e: Exception) {
            Log.d("Join event error", e.toString())

            return null
        }
    }

    suspend fun getVolunteerEvents(): Volunteer {
        try {
            Log.d("", "https://comp4107-spring2024.azurewebsites.net/api/volunteers/$userId")
            var url: String = "https://comp4107-spring2024.azurewebsites.net/api/volunteers/$userId"
            return httpClient.get(url).body<Volunteer>()
        } catch (e: Exception) {
            Log.d("Volunteer event error", "getVolunteerEvents: " + e.toString())
            return Volunteer("", "", "", "", "", "", false, "", "", false, listOf(""))
        }
    }

    suspend fun getVolunteerEvents(params: List<String>?): VolunteerEventResponse {
        try {
            var url: String =
                "https://comp4107-spring2024.azurewebsites.net/api/volunteers/$userId/events"

            Log.d("URL", url)
            if (!params.isNullOrEmpty()) {
                url += "?"
                for (i in params.indices) {
                    url += params[i]
                    if (i != params.size - 1) url += "&"
                }

            }
            return httpClient.get(url).body<VolunteerEventResponse>()
        } catch (e: Exception) {
            return VolunteerEventResponse(
                listOf(
                    Event(
                        "Error",
                        e.toString(),
                        "Error",
                        e.toString(),
                        " error",
                        "Error",
                        "Error",
                        -1,
                        false,
                        "Error",
                        "Error",
                        null

                    )
                ),
                -1,

                )

        }
    }


}