package com.web.domain

import com.web.domain.enums.BoardType
import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table
data class Board(
        @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val idx: Long = 0,

        @Column
    val title: String = "",

        @Column
    val subTitle: String = "",

        @Column
    val content: String = "",

        @Column
    @Enumerated(EnumType.STRING)
    // enum이 db에는 String으로 자동 변환되어 들어간다
    val boardType: BoardType = BoardType.free,

        @Column
    val createdDate: LocalDateTime = LocalDateTime.now(),

        @Column
    val updatedDate: LocalDateTime = LocalDateTime.now(),

        @OneToOne(fetch = FetchType.LAZY)
    // 실제 DB에는 User의 PK인 user_idx가 저장된다.
    // eager와 lazy 두 종류가 있는데, eager는 Board 도메인(?)
    // 조회 즉시 User 객체 조회,
    // lazy는 User 객체가 실제로 사용될 때 (User 객체 사용 시점 아님)
    val user: User? = null
): Serializable