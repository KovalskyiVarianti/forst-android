package com.example.forst_android.account.data

import com.example.forst_android.account.domain.SaveAvatarResult
import com.example.forst_android.account.domain.SaveAvatarUseCase
import com.example.forst_android.common.data.Routes
import com.example.forst_android.common.domain.service.UserService
import io.ktor.client.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import javax.inject.Inject

class KtorSaveAvatarUseCase @Inject constructor(
    private val httpClient: HttpClient,
    private val userService: UserService,
) : SaveAvatarUseCase {
    override suspend fun saveAvatar(byteArray: ByteArray) : SaveAvatarResult{
        val token = userService.getIdToken() ?: return SaveAvatarResult.Fail
        return httpClient.submitFormWithBinaryData(
            url = "${Routes.BASE_URL}/${Routes.AVATARS}/${Routes.UPLOAD}",
            formData = formData {
                append(
                    "image",
                    byteArray,
                    Headers.build {
                        append(HttpHeaders.Authorization, token)
                        append(HttpHeaders.ContentType, "image/*")
                        append(HttpHeaders.ContentDisposition, "filename=avatar")
                    }
                )
            }
        ).let {httpResponse ->
            if (httpResponse.status == HttpStatusCode.OK) {
                SaveAvatarResult.Success
            } else {
                SaveAvatarResult.Fail
            }
        }
    }
}