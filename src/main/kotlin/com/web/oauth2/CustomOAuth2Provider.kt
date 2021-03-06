package com.web.oauth2

import org.springframework.security.oauth2.client.registration.ClientRegistration
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.ClientAuthenticationMethod

enum class CustomOAuth2Provider {
    KAKAO {
        override fun getBuilder(registrationId: String): ClientRegistration.Builder {
            val builder = getBuilder(registrationId, ClientAuthenticationMethod.POST, DEFAULT_LOGIN_REDIRECT_URL)
            builder.scope("profile")
            builder.authorizationUri("https://kauth.kakao.com/oauth/authorize")
            builder.tokenUri("https://kauth.kakao.com/oauth/token")
            builder.userInfoUri("https://kapi.kakao.com/v1/user/me")
            builder.userNameAttributeName("id")
            builder.clientName("Kakao")

            return builder
        }
    };

    val DEFAULT_LOGIN_REDIRECT_URL = "{baseUrl}/login/oauth2/code/{registrationId}"

    abstract fun getBuilder(registrationId: String): ClientRegistration.Builder

    protected fun getBuilder(registrationId: String, method: ClientAuthenticationMethod, redirectUri: String): ClientRegistration.Builder {
        val builder = ClientRegistration.withRegistrationId(registrationId)
        builder.clientAuthenticationMethod(method)
        builder.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)           // 책에서 설명한 승인 타입 4가지 중 하나
        builder.redirectUriTemplate(redirectUri)
        return builder
    }
}