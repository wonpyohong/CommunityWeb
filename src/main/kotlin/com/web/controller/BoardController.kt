package com.web.controller

import com.web.service.BoardService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
@RequestMapping("/board")
class BoardController {
    @Autowired
    lateinit var boardService: BoardService

    @GetMapping("", "/")        // 코틀린은 중괄호 필요 없다.
    fun board(
            // idx 파라미터를 필수로 받는다. 만약 바인딩할 값이 없으면 기본값 '0'으로 설정됨
            // findBoardByIdx(0) 으로 조회 시 board 값은 null 리턴
            @RequestParam(value = "idx", defaultValue = "0") idx: Long,
            model: Model
    ): String {
        model.addAttribute("board", boardService.findBoardByIdx(idx))

        return "/board/form"
    }

    @GetMapping("/list")
    fun list(
            // @PageableDefault 의 파라미터인 size, sort, direction 등을 이용하여 페이징 처리에 대한 규약을 정의할 수 있음
            @PageableDefault pageable: Pageable,
            model: Model
    ): String {
        model.addAttribute("boardList", boardService.findBoardList(pageable))
        return "/board/list"    // src/resources/template를 기준으로 데이터를 바인딩할 타깃의 뷰 경로를 지정
    }
}