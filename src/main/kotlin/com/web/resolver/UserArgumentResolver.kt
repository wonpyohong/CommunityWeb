package com.web.resolver

import com.web.annotation.SocialUser
import com.web.domain.User
import com.web.domain.enums.SocialType
import com.web.repository.UserRepository
import org.springframework.core.MethodParameter
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.provider.OAuth2Authentication
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import java.lang.ClassCastException
import java.time.LocalDateTime
import javax.servlet.http.HttpSession

@Component
class UserArgumentResolver(val userRepository: UserRepository): HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter?): Boolean {
        return parameter?.getParameterAnnotation(SocialUser::class.java) != null
        && parameter.parameterType == User::class.java
    }

    override fun resolveArgument(parameter: MethodParameter?,
                                 mavContainer: ModelAndViewContainer?,
                                 webRequest: NativeWebRequest?,
                                 binderFactory: WebDataBinderFactory?): Any? {
        val session = (RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes).request.session
        val user = (session.getAttribute("user") as User)
        return getUser(user, session)
    }

    private fun getUser(user: User?, session: HttpSession): User? {
        var userFromRepository: User? = null
        if (user == null) {
            try {
                val authentication = SecurityContextHolder.getContext().authentication as OAuth2Authentication
                val map = authentication.userAuthentication.details as Map<String, String>
                val convertUser = convertUser("${authentication.authorities.toTypedArray()[0]}", map)

                userFromRepository = userRepository.findByEmail(convertUser?.email ?: "")
                if (userFromRepository == null) {
                    userFromRepository = userRepository.save(convertUser)
                }

                setRoleIfNotSame(userFromRepository, authentication, map)
                session.setAttribute("user", userFromRepository)
            } catch (e: ClassCastException) {
                return userFromRepository
            }
        }

        return user
    }

    private fun convertUser(authority: String, map: Map<String, String>): User? {
        return when {
            SocialType.FACEBOOK.isEquals(authority) -> getModernUser(SocialType.FACEBOOK, map)
            SocialType.GOOGLE.isEquals(authority) -> getModernUser(SocialType.GOOGLE, map)
            SocialType.KAKAO.isEquals(authority) -> getKakaoUser(map)
            else -> null
        }
    }

    private fun getModernUser(socialType: SocialType, map: Map<String, String>): User {
        return User(name = map["name"]!!,
                email = map["email"]!!,
                principal = map["id"]!!,
                socialType = socialType,
                createdDate = LocalDateTime.now())
    }

    private fun getKakaoUser(map: Map<String, String>): User {
        val propertyMap = map["properties"] as HashMap<String, String>

        return User(name = propertyMap["nickname"]!!,
                email = map["kaccount_email"]!!,
                principal = "${map["id"]!!}",
                socialType = SocialType.KAKAO,
                createdDate = LocalDateTime.now())
    }

    private fun setRoleIfNotSame(user: User?, authentication: OAuth2Authentication, map: Map<String, String>) {
        if (user != null && !authentication.authorities.contains(SimpleGrantedAuthority(user.socialType.getRoleType()))) {
            SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(map, "N/A", AuthorityUtils.createAuthorityList(user.socialType.getRoleType()))
        }
    }
}