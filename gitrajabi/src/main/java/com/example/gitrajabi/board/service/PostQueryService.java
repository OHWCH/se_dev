package com.example.gitrajabi.board.service;
import com.example.gitrajabi.board.domain.Comment;
import com.example.gitrajabi.board.domain.Post;
import com.example.gitrajabi.board.dto.CommentResponse;
import com.example.gitrajabi.board.dto.PostDetailResponse; // 🌟 임포트
import com.example.gitrajabi.board.repository.CommentRepository;
import com.example.gitrajabi.board.repository.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.stream.Collectors;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional(readOnly = true)
public class PostQueryService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    public PostQueryService(PostRepository postRepository, CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    // Use Case #17: 게시글 목록 조회
    public List<Post> getPostList(String type, int page) {
        // ... (기존 로직 유지) ...
        Sort sort = Sort.by(type.equals("popular") ? "viewCount" : "createdAt").descending();
        PageRequest pageable = PageRequest.of(page, 10, sort);
        Page<Post> postPage = postRepository.findAll(pageable);

        return postPage.getContent();
    }

    // Use Case #18: 게시글 상세 조회 (조회수 증가 포함, 댓글 로딩)
    @Transactional
    public PostDetailResponse getPostDetailWithComments(Long postId) throws NoSuchElementException {

        // 1. 게시글 조회 및 조회수 증가 (이전 답변에서 Post 엔티티에 incrementViewCount()를 추가했다고 가정)
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NoSuchElementException("게시글을 찾을 수 없습니다. postId=" + postId));

        post.incrementViewCount(); // 조회수 증가
        List<Comment> comments = commentRepository.findByPostId(postId);
        // 2. 댓글 목록 로드 및 DTO 변환
        List<CommentResponse> commentResponses = comments.stream()
                .map(CommentResponse::from) // 💡 CommentResponse::from 메서드가 엔티티를 DTO로 변환한다고 가정
                .collect(Collectors.toList());

        // 3. PostDetailResponse 생성 및 반환
        return PostDetailResponse.from(post, commentResponses);
    }

    // ❌ public Post getPostDetail(Long postId) { } 메서드는 제거되었습니다.
}