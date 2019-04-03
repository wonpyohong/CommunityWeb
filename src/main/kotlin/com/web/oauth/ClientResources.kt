package com.web.oauth

import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties
import org.springframework.boot.context.properties.NestedConfigurationProperty
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails

class ClientResources {
    @NestedConfigurationProperty                            // 단일 값이 아닌 중복으로 바인딩된다는 의미
    val client = AuthorizationCodeResourceDetails()         // yaml에서 client 부분을 가져오는 부분

    @NestedConfigurationProperty
    val resource = ResourceServerProperties()
}