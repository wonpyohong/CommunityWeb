package com.web

import com.web.domain.Board
import com.web.domain.User
import com.web.domain.enums.BoardType
import com.web.domain.enums.SocialType
import com.web.repository.BoardRepository
import com.web.repository.UserRepository
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.junit4.SpringRunner
import java.time.LocalDateTime

@RunWith(SpringRunner::class)
@DataJpaTest
class JpaMappingTest {
    private val boardTestTitle = "테스트"
    private val email = "test@gmail.com"

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var boardRepository: BoardRepository

//    @Before
//    fun init() {
//        val user = userRepository.save(User(0, "havi", "test", email, "", SocialType.KAKAO, LocalDateTime.now(), LocalDateTime.now()))
//
//        val board = Board(0, boardTestTitle, "서브 타이틀", "콘텐츠", BoardType.free, LocalDateTime.now(), LocalDateTime.now(), user)
//        boardRepository.save(board)
//    }

//    @Test
//    fun 제대로_생성됐는지_테스트() {
//        val user = userRepository.findByEmail(email)
//        assertThat(user.name, `is`("havi"))
//        assertThat(user.password, `is`("test"))
//        assertThat(user.email, `is`(email))
//
//        val board = boardRepository.findByUser(user)
//        assertThat(board.title, `is`(boardTestTitle))
//        assertThat(board.subTitle, `is`("서브 타이틀"))
//        assertThat(board.boardType, `is`(BoardType.free))
//    }
}