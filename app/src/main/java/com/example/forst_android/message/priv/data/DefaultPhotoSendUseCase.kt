package com.example.forst_android.message.priv.data

import com.example.forst_android.common.data.Routes
import com.example.forst_android.common.domain.service.UserService
import com.example.forst_android.message.priv.domain.MessageType
import com.example.forst_android.message.priv.domain.PhotoSendUseCase
import io.ktor.client.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import java.io.InputStream
import java.util.*
import javax.inject.Inject

class DefaultPhotoSendUseCase @Inject constructor(
    private val messageRealtimeDatabase: MessageRealtimeDatabase,
    private val httpClient: HttpClient,
    private val userService: UserService,
) : PhotoSendUseCase {

    override suspend fun sendPhoto(
        clusterId: String,
        chatId: String,
        userId: String,
        interlocutorId: String,
        inputStream: InputStream,
    ) {
        val messageId = UUID.randomUUID().toString()

        val token = userService.getIdToken()!!
        httpClient.submitFormWithBinaryData(
            url = "${Routes.BASE_URL}/$clusterId/$chatId/${Routes.UPLOAD}",
            formData = formData {
                append(
                    "image",
                    inputStream.readBytes(),
                    Headers.build {
                        append(HttpHeaders.Authorization, token)
                        append(HttpHeaders.ContentType, "image/*")
                        append(HttpHeaders.ContentDisposition, "filename=$messageId")
                    }
                )
            }
        ).let { response ->
            if (response.status == HttpStatusCode.OK) {
                val messageEntity = MessageRealtimeEntity(
                    messageId,
                    userId,
                    "${Routes.BASE_URL}/$clusterId/$chatId/$messageId",
                    MessageType.IMAGE.name,
                    System.currentTimeMillis()
                )
                messageRealtimeDatabase.sendMessage(
                    clusterId,
                    chatId,
                    userId,
                    interlocutorId,
                    messageEntity
                )
            }
        }
    }
}