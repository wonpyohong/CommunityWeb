package com.web.domain

import com.web.domain.enums.BoardType
import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table
data class User(
        @Id
        @Column
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var idx: Long = 0,

        @Column
        val name: String,

        @Column
        val password: String,

        @Column
        val email: String,

        @Column
        val createdDate: LocalDateTime,

        @Column
        val updatedDate: LocalDateTime = LocalDateTime.now()
): Serializable