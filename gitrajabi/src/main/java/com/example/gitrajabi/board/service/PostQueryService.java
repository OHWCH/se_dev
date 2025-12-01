package com.example.gitrajabi.board.service;
import com.example.gitrajabi.board.domain.Comment;
import com.example.gitrajabi.board.domain.Post;
import com.example.gitrajabi.board.dto.CommentResponse;
import com.example.gitrajabi.board.dto.PostDetailResponse; // 🌟 임포트
import com.example.gitrajabi.board.dto.PostResponse; // ✅ PostResponse 임포트 추가
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

    // Use Case #17: 게시글 목록 조회 (✅ PostResponse 리스트 반환으로 변경)
    public List<PostResponse> getPostList(String type, int page) {
        Sort sort = Sort.by(type.equals("popular") ? "viewCount" : "createdAt").descending();
        PageRequest pageable = PageRequest.of(page, 10, sort);
        Page<Post> postPage = postRepository.findAll(pageable);

        // ✅ 변경: Post 엔티티를 PostResponse DTO로 변환하면서 댓글 개수를 조회/주입
        return postPage.getContent().stream()
                .map(post -> {
                    // 💡 N+1 문제가 발생하지만, 현재 구조에서 댓글 개수를 가져오기 위한 가장 간단한 방법입니다.
                    int commentCount = (int) commentRepository.countByPostId(post.getPostId());
                    return PostResponse.from(post, commentCount);
                })
                .collect(Collectors.toList());
    }

    // Use Case #18: 게시글 상세 조회 (조회수 증가 포함, 댓글 로딩)
    @Transactional
    public PostDetailResponse getPostDetailWithComments(Long postId) throws NoSuchElementException {

        // 1. 게시글 조회 및 조회수 증가
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NoSuchElementException("게시글을 찾을 수 없습니다. postId=" + postId));

        post.incrementViewCount(); // 조회수 증가

        // 2. 댓글 목록 로드
        List<Comment> comments = commentRepository.findByPostId(postId);

        // 3. 댓글 개수 계산
        int commentCount = comments.size();

        // 4. 댓글 목록 DTO 변환
        List<CommentResponse> commentResponses = comments.stream()
                .map(CommentResponse::from)
                .collect(Collectors.toList());

        // 5. PostDetailResponse 반환 (댓글 개수 포함)
        return PostDetailResponse.from(post, commentCount, commentResponses);
    }
}