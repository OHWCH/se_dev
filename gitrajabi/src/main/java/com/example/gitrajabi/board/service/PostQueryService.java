package com.example.gitrajabi.board.service;
import com.example.gitrajabi.board.domain.Comment;
import com.example.gitrajabi.board.domain.Post;
import com.example.gitrajabi.board.dto.CommentResponse;
import com.example.gitrajabi.board.dto.PostDetailResponse; // 🌟 임포트
import com.example.gitrajabi.board.dto.PostResponse; // ✅ 추가: PostResponse 임포트
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
    // ✅ 반환 타입을 List<PostResponse>로 변경
    public List<PostResponse> getPostList(String type, int page) {
        // ... (기존 로직 유지) ...
        Sort sort = Sort.by(type.equals("popular") ? "viewCount" : "createdAt").descending();
        PageRequest pageable = PageRequest.of(page, 10, sort);
        Page<Post> postPage = postRepository.findAll(pageable);

        // ✅ Post 엔티티 리스트를 PostResponse DTO 리스트로 변환. PostResponse.from(Post post)를 사용.
        return postPage.getContent().stream()
                .map(PostResponse::from)
                .collect(Collectors.toList());
    }

    // Use Case #18: 게시글 상세 조회 (조회수 증가 포함, 댓글 로딩)
    // ✅ @Transactional 어노테이션을 Read/Write 트랜잭션으로 변경해야 조회수 증가(Update 쿼리)가 DB에 반영됩니다.
    @Transactional
    public PostDetailResponse getPostDetailWithComments(Long postId) throws NoSuchElementException {

        // 1. 게시글 조회 및 조회수 증가
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NoSuchElementException("게시글을 찾을 수 없습니다. postId=" + postId));

        // ✅ 조회수 증가 로직 호출 (Post.java에 구현됨)
        post.incrementViewCount();

        List<Comment> comments = commentRepository.findByPostId(postId);
        // 2. 댓글 목록 로드 및 DTO 변환
        List<CommentResponse> commentResponses = comments.stream()
                .map(CommentResponse::from) // 💡 CommentResponse::from 메서드가 엔티티를 DTO로 변환한다고 가정
                .collect(Collectors.toList());

        // 3. PostDetailResponse 반환
        // PostDetailResponse.from(Post post, List<CommentResponse> comments) 형태를 사용
        return PostDetailResponse.from(post, commentResponses);
    }
}