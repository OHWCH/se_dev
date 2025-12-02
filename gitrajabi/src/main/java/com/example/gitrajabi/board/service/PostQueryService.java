package com.example.gitrajabi.board.service;
import com.example.gitrajabi.board.domain.Comment;
import com.example.gitrajabi.board.domain.Post;
import com.example.gitrajabi.board.dto.CommentResponse;
import com.example.gitrajabi.board.dto.PostDetailResponse;
import com.example.gitrajabi.board.dto.PostResponse;
import com.example.gitrajabi.board.repository.CommentRepository;
import com.example.gitrajabi.board.repository.PostRepository;
import com.example.gitrajabi.user.domain.entity.UserEntity;
import com.example.gitrajabi.user.domain.repository.UserRepository; // ✅ 추가
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
@Transactional(readOnly = true)
public class PostQueryService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository; // ✅ 추가

    public PostQueryService(PostRepository postRepository, CommentRepository commentRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
    }

    // Use Case #17: GET /api/posts - 게시글 목록 조회 (페이징, 정렬 포함)
    public Page<PostResponse> getPostList(int page, int size, String sortProperty) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, sortProperty));
        Page<Post> postPage = postRepository.findAll(pageRequest);

        List<Long> postIds = postPage.getContent().stream()
                .map(Post::getPostId)
                .collect(Collectors.toList());

        // 1. ✅ 게시글별 댓글 수 일괄 조회 (Map<postId, commentCount>)
        Map<Long, Long> commentCountMap = commentRepository.countCommentsByPostIds(postIds).stream()
                .collect(Collectors.toMap(
                        arr -> (Long) arr[0], // postId
                        arr -> (Long) arr[1]  // count
                ));

        // 2. ✅ 게시글 작성자 ID 추출 및 깃허브 아이디 일괄 조회 (Map<userId, githubId>)
        List<Long> postUserIds = postPage.getContent().stream()
                .map(Post::getUserId)
                .distinct()
                .collect(Collectors.toList());

        Map<Long, String> userGithubIdMap = userRepository.findAllByUserIdIn(postUserIds).stream() // ✅ findAllByUserIdIn 사용
                .collect(Collectors.toMap(UserEntity::getUserId, UserEntity::getGithubId)); // ✅ getUserId, getGithubId 사용

        // 3. Post -> PostResponse DTO 변환
        Page<PostResponse> responsePage = postPage.map(post -> {
            String githubId = userGithubIdMap.getOrDefault(post.getUserId(), "익명");
            int commentCount = commentCountMap.getOrDefault(post.getPostId(), 0L).intValue();
            return PostResponse.from(post, githubId, commentCount);
        });

        return responsePage;
    }

    // Use Case #18: GET /api/posts/{postId} - 게시글 상세 조회 (조회수 증가, 댓글 포함)
    @Transactional // 조회수 증가를 위해 readOnly = false
    public PostDetailResponse getPostDetail(Long postId) {
        // 1. 게시글 로드 및 조회수 증가
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NoSuchElementException("게시글을 찾을 수 없습니다. postId=" + postId));

        post.incrementViewCount(); // 조회수 증가

        List<Comment> comments = commentRepository.findByPostId(postId);

        // 2. ✅ 댓글 작성자 ID 추출 및 깃허브 아이디 일괄 조회
        List<Long> commentUserIds = comments.stream().map(Comment::getUserId).distinct().collect(Collectors.toList());
        // 게시글 작성자 ID도 포함
        if (!commentUserIds.contains(post.getUserId())) {
            commentUserIds.add(post.getUserId());
        }

        Map<Long, String> userGithubIdMap = userRepository.findAllByUserIdIn(commentUserIds).stream() // ✅ findAllByUserIdIn 사용
                .collect(Collectors.toMap(UserEntity::getUserId, UserEntity::getGithubId)); // ✅ getUserId, getGithubId 사용

        // 3. 댓글 목록 로드 및 DTO 변환 (깃허브 아이디 포함)
        List<CommentResponse> commentResponses = comments.stream()
                .map(comment -> {
                    String githubId = userGithubIdMap.getOrDefault(comment.getUserId(), "익명");
                    return CommentResponse.from(comment, githubId); // ✅ 깃허브 아이디 전달
                })
                .collect(Collectors.toList());

        // 4. 게시글 상세 DTO 생성 시 작성자 깃허브 아이디 추가
        String postAuthorGithubId = userGithubIdMap.getOrDefault(post.getUserId(), "익명");

        return PostDetailResponse.from(post, postAuthorGithubId, commentResponses);
    }
}