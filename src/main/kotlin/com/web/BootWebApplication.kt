package com.web

import com.web.domain.Board
import com.web.domain.User
import com.web.domain.enums.BoardType
import com.web.repository.BoardRepository
import com.web.repository.UserRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import java.time.LocalDateTime

@SpringBootApplication
class BootWebApplication {
    @Bean
    fun runner(userRepository: UserRepository, boardRepository: BoardRepository): CommandLineRunner {
        return CommandLineRunner{ args ->
            val user = userRepository.save(
                    User(0,
                            "havi",
                            "test",
                            "email",
                            LocalDateTime.now(),
                            LocalDateTime.now()
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
    runApplication<BootWebApplication>(*args)
}
