package com.web.oauth

import com.web.domain.enums.SocialType
import org.springframework.boot.autoconfigure.security.oauth2.resource.AuthoritiesExtractor
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.AuthorityUtils

class UserTokenService(resources: ClientResources, socialType: SocialType): UserInfoTokenServices(resources.resource.userInfoUri, resources.client.clientId) {
    init {
        setAuthoritiesExtractor(OAuth2AuthoritiesExtractor(socialType))
    }

    class OAuth2AuthoritiesExtractor(socialType: SocialType) : AuthoritiesExtractor {
        var socialType: String = socialType.getRoleType()

        override fun extractAuthorities(map: MutableMap<String, Any>?): MutableList<GrantedAuthority> {
            return AuthorityUtils.createAuthorityList(this.socialType)
        }
    }
}