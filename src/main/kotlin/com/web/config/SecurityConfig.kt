package com.web.config

import com.web.domain.enums.SocialType
import com.web.oauth2.CustomOAuth2Provider
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider
import org.springframework.security.oauth2.client.registration.ClientRegistration
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint
import org.springframework.security.web.csrf.CsrfFilter
import org.springframework.web.filter.CharacterEncodingFilter
import java.util.*

@Configuration
@EnableWebSecurity          // 웹에서 시큐리티 기능을 사용하겠다는 어노테이션
class SecurityConfig: WebSecurityConfigurerAdapter() {      // 원하는 형식의 시큐리티 설정을 위해 상속 (override configure를 위해)
    override fun configure(http: HttpSecurity) {
        val filter = CharacterEncodingFilter()
        http.authorizeRequests()
                .antMatchers("/", "/oauth2/**", "/login/**", "/css/**", "/images/**", "/js/**", "/console/**").permitAll()      // login.html에서 버튼 누르면 시작
                .antMatchers("/facebook").hasAuthority(SocialType.FACEBOOK.getRoleType())
                .antMatchers("/google").hasAuthority(SocialType.GOOGLE.getRoleType())
                .antMatchers("/kakao").hasAuthority(SocialType.KAKAO.getRoleType())
                .anyRequest()           // 설정한 요청 이외의 리퀘스트 요청
                .authenticated()        // 해당 요청은 인증된 사용자만 가능
            .and()
                .oauth2Login()
                .defaultSuccessUrl("/loginSuccess")
                .failureUrl("/loginFailure")
            .and()
                .headers().frameOptions().disable()     // XFrameOptionsHeaderWriter의 최적화 설정을 허용하지 않음
            .and()
                .exceptionHandling()
                .authenticationEntryPoint(LoginUrlAuthenticationEntryPoint("/login"))       // 인증의 진입 지점. 인증 안되어있으면 여기로 보낸다
            .and()
                .formLogin()
                .successForwardUrl("/board/list")       // 로그인 성공 시 여기로 보낸다
            .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true)
            .and()
                .addFilterBefore(filter, CsrfFilter::class.java)        // 문자 인코딩 필터(filter)보다 CsrfFilter를 먼저 실행하도록 설정
                .csrf().disable()           // csrf: Cross-site request forgery (사이트 간 요청 위조)
    }

    @Bean
    fun clientRegistrationRepository(oAuth2ClientProperties: OAuth2ClientProperties,
                                     @Value("\${custom.oauth2.kakao.client-id}") kakaoClientId: String)
    : ClientRegistrationRepository {
        val registrations = oAuth2ClientProperties.registration.keys
                .map { client -> getRegistration(oAuth2ClientProperties, client) }
                .filter(Objects::nonNull).toMutableList()

        registrations.add(CustomOAuth2Provider.KAKAO.getBuilder("kakao")
                .clientId(kakaoClientId)
                .clientSecret("test")
                .jwkSetUri("test")
                .build())

        return InMemoryClientRegistrationRepository(registrations)
    }

    // 스프링 부트에서 제공되는 기본 설정(CommonOAuth2Provider)에 커스텀 설정을 더한다
    fun getRegistration(clientProperties: OAuth2ClientProperties, client: String): ClientRegistration? {
        if (client == "google") {
            val registration = clientProperties.registration["google"]!!

            return CommonOAuth2Provider.GOOGLE.getBuilder(client)
                    .clientId(registration.clientId)
                    .clientSecret(registration.clientSecret)
                    .scope("email", "profile")
                    .build()
        }

        if (client == "facebook") {
            val registration = clientProperties.registration["facebook"]!!

            return CommonOAuth2Provider.FACEBOOK.getBuilder(client)
                    .clientId(registration.clientId)
                    .clientSecret(registration.clientSecret)
                    .userInfoUri("https://graph.facebook.com/me?fields=id,name,email,link")
                    .scope("email")
                    .build()
        }

        return null
    }
}