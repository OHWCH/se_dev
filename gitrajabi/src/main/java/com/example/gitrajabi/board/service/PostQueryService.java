package com.example.gitrajabi.board.service;

import com.example.gitrajabi.board.domain.Post;
import com.example.gitrajabi.board.repository.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional(readOnly = true)
public class PostQueryService {

    private final PostRepository postRepository;

    public PostQueryService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    // Use Case #17: 게시글 목록 조회
    public List<Post> getPostList(String type, int page) {

        // 1. 정렬 기준 설정: type이 "popular"면 viewCount, 아니면 createdAt (최신순)
        Sort sort = Sort.by(type.equals("popular") ? "viewCount" : "createdAt").descending();

        // 2. PageRequest 생성: 페이지 번호(page), 페이지 크기 10, 정렬 기준 적용
        PageRequest pageable = PageRequest.of(page, 10, sort);

        // 3. Repository 호출 및 Page 결과를 List로 변환 (Controller 타입 맞춤)
        Page<Post> postPage = postRepository.findAll(pageable);

        return postPage.getContent();
    }

    // Use Case #18: 게시글 상세 조회 (조회수 증가 포함)
    @Transactional
    public Post getPostDetail(Long postId) { // throws Throwable 제거

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NoSuchElementException("게시글을 찾을 수 없습니다. Post ID: " + postId));

        // 조회수 증가
        post.increaseViewCount();
        // save를 명시적으로 호출하지 않아도 @Transactional에 의해 dirty checking으로 업데이트 됩니다.

        return post;
    }
}