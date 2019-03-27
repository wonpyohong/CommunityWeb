package com.web.domain

import com.web.domain.enums.BoardType
import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.*

data class Board(
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private var idx: Long = 0,

    @Column
    private val title: String,

    @Column
    private val subTitle: String,

    @Column
    private val content: String,

    @Column
    @Enumerated(EnumType.STRING)
    // enum이 db에는 String으로 자동 변환되어 들어간다
    private val boardType: BoardType,

    @Column
    private val createdDate: LocalDateTime,

    @Column
    private val updatedDate: LocalDateTime,

    @OneToOne(fetch = FetchType.LAZY)
    // 실제 DB에는 User의 PK인 user_idx가 저장된다.
    // eager와 lazy 두 종류가 있는데, eager는 Board 도메인(?) 조회 즉시 User 객체 조회, lazy는 User 객체가 실제로 사용될 때 (User 객체 사용 시점 아님)
    private val user: User
): Serializable