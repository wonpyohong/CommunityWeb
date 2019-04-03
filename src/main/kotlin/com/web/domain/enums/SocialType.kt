package com.web.domain.enums

enum class SocialType(val value: String) {
    FACEBOOK("facebook"),
    GOOGLE("google"),
    KAKAO("kakao");

    val ROLE_PREFIX = "ROLE_"

    fun getRoleType() = ROLE_PREFIX + value.toUpperCase()

    fun isEquals(authority: String) = getRoleType() == authority
}