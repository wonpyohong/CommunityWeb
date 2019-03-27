package com.web.service

import com.web.domain.Board
import com.web.repository.BoardRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class BoardService(val boardRepository: BoardRepository) {
    fun findBoardList(pageable: Pageable): Page<Board> {
        val page = if (pageable.pageNumber <= 0) 0 else pageable.pageNumber - 1
        val newPageable = PageRequest.of(page, pageable.pageSize)

        return boardRepository.findAll(newPageable)
    }

    fun findBoardByIdx(idx: Long): Board {
        return boardRepository.findById(idx).orElse(Board())
    }
}