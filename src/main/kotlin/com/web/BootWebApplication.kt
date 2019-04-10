package com.web

import com.web.domain.Board
import com.web.domain.User
import com.web.domain.enums.BoardType
import com.web.repository.BoardRepository
import com.web.repository.UserRepository
import com.web.resolver.UserArgumentResolver
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter
import java.time.LocalDateTime

@SpringBootApplication
class BootWebApplication: WebMvcConfigurerAdapter() {
    @Autowired
    lateinit var userArgumentResolver: UserArgumentResolver

    override fun addArgumentResolvers(argumentResolvers: MutableList<HandlerMethodArgumentResolver>?) {
        argumentResolvers?.add(userArgumentResolver)
    }

    @Bean
    fun runner(userRepository: UserRepository, boardRepository: BoardRepository): CommandLineRunner {
        return CommandLineRunner{ args ->
            val user = userRepository.save(
                    User(0,
                            "havi",
                            "test",
                            "email",
                            createdDate = LocalDateTime.now(),
                            updatedDate = LocalDateTime.now()
                    )
            )

            (1 .. 200).forEach {index ->
                boardRepository.save(
                        Board(0,
                                "게시글$index",
                                "순서$index",
                                "콘텐츠",
                                BoardType.free,
                                LocalDateTime.now(),
                                LocalDateTime.now(), user
                        )
                )
            }
        }
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(BootWebApplication::class.java, *args)
}
