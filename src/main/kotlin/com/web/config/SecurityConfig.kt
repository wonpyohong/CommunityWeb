package com.web.config

import com.web.domain.enums.SocialType
import com.web.oauth.ClientResources
import com.web.oauth.UserTokenService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.oauth2.client.OAuth2ClientContext
import org.springframework.security.oauth2.client.OAuth2RestTemplate
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.security.web.csrf.CsrfFilter
import org.springframework.web.filter.CharacterEncodingFilter
import org.springframework.web.filter.CompositeFilter
import javax.servlet.Filter

@Configuration
@EnableWebSecurity          // 웹에서 시큐리티 기능을 사용하겠다는 어노테이션
@EnableOAuth2Client
class SecurityConfig: WebSecurityConfigurerAdapter() {      // 원하는 형식의 시큐리티 설정을 위해 (override configure를 위해)
    @Autowired
    lateinit var oAuth2ClientContext: OAuth2ClientContext

    override fun configure(http: HttpSecurity) {
        val filter = CharacterEncodingFilter()
        http.authorizeRequests()
                .antMatchers("/", "/login/**", "/css/**", "/images/**", "/js/**", "/console/**").permitAll()
                .anyRequest()           // 설정한 요청 이외의 리퀘스트 요청
                .authenticated()        // 해당 요청은 인증된 사용자만 가능
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
                .addFilterBefore(oauth2Filter(), BasicAuthenticationFilter::class.java)
    }

    @Bean
    fun oauth2ClientFilterRegistration(filter: OAuth2ClientContextFilter): FilterRegistrationBean {
        val registration = FilterRegistrationBean()
        registration.filter = filter
        registration.order = -100
        return registration
    }

    fun oauth2Filter(): Filter {
        val filter = CompositeFilter()
        val filters = mutableListOf<Filter>()
        filters.add(oauth2Filter(facebook(), "/login/facebook", SocialType.FACEBOOK))
        filters.add(oauth2Filter(google(), "/login/google", SocialType.GOOGLE))
        filters.add(oauth2Filter(kakao(), "/login/kakao", SocialType.KAKAO))
        filter.setFilters(filters)
        return filter
    }

    fun oauth2Filter(client: ClientResources, path: String, socialType: SocialType): Filter {
        val filter = OAuth2ClientAuthenticationProcessingFilter(path)
        val template = OAuth2RestTemplate(client.client, oAuth2ClientContext)
        filter.setRestTemplate(template)
        filter.setTokenServices(UserTokenService(client, socialType))
        filter.setAuthenticationSuccessHandler { resquest, response, authenication ->
            response.sendRedirect("/" + socialType.value + "/complete")
        }
        filter.setAuthenticationFailureHandler {resquest, response, exception ->
            response.sendRedirect("/error")
        }

        return filter
    }

    @Bean
    @ConfigurationProperties("facebook")            // 프로퍼티의 접두사(facebook:)
    fun facebook() = ClientResources()

    @Bean
    @ConfigurationProperties("google")
    fun google() = ClientResources()

    @Bean
    @ConfigurationProperties("kakao")
    fun kakao() = ClientResources()
}