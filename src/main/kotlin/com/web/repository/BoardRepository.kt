package com.web.repository

import com.web.domain.Board
import com.web.domain.User
import org.springframework.data.jpa.repository.JpaRepository

interface BoardRepository: JpaRepository<Board, Long> {
    fun findByUser(user: User): Board
}