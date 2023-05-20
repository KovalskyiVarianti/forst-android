package com.example.forst_android.message.group.data

import com.example.forst_android.common.data.Routes
import com.example.forst_android.common.domain.service.UserService
import com.example.forst_android.message.group.domain.PhotoGroupSendUseCase
import com.example.forst_android.message.priv.domain.MessageType
import io.ktor.client.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import java.io.InputStream
import java.util.*
import javax.inject.Inject

class DefaultPhotoGroupSendUseCase @Inject constructor(
    private val messageRealtimeDatabase: MessageGroupRealtimeDatabase,
    private val httpClient: HttpClient,
    private val userService: UserService,
) : PhotoGroupSendUseCase {

    override suspend fun sendPhoto(
        clusterId: String,
        groupId: String,
        userId: String,
        groupMembers: List<String>,
        inputStream: InputStream,
    ) {
        val messageId = UUID.randomUUID().toString()

        val token = userService.getIdToken()!!
        httpClient.submitFormWithBinaryData(
            url = "${Routes.BASE_URL}/$clusterId/$groupId/${Routes.UPLOAD}",
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
                val messageEntity = MessageGroupRealtimeEntity(
                    messageId,
                    userId,
                    "${Routes.BASE_URL}/$clusterId/$groupId/$messageId",
                    MessageType.IMAGE.name,
                    System.currentTimeMillis()
                )
                messageRealtimeDatabase.sendMessage(
                    clusterId,
                    groupId,
                    groupMembers,
                    messageEntity
                )
            }
        }
    }
}