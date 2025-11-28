package com.example.gitrajabi.board.dto;

import org.springframework.data.domain.Page;
import java.util.List;


 // 게시글 목록 (List)와 페이징 정보 (Pagination)를 담아 클라이언트에게 반환하는 DTO
public record PostPageResponse(
        List<PostResponse> posts,
        int currentPage,
        int totalPages,
        long totalElements,
        boolean hasNext,
        boolean hasPrevious
) {
    public static PostPageResponse from(Page<PostResponse> postPage) {
        return new PostPageResponse(
                postPage.getContent(),
                postPage.getNumber() + 1,
                postPage.getTotalPages(),
                postPage.getTotalElements(),
                postPage.hasNext(),
                postPage.hasPrevious()
        );
    }
}