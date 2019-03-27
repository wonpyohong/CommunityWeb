package com.web.domain

import com.web.domain.enums.BoardType
import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.*

data class User(
        @Id
        @Column
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private var idx: Long = 0,

        @Column
        private val name: String,

        @Column
        private val password: String,

        @Column
        private val email: String,

        @Column
        private val createdDate: LocalDateTime,

        @Column
        private val updatedDate: LocalDateTime
): Serializable