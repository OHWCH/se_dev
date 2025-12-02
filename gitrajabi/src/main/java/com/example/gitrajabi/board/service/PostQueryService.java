package com.example.gitrajabi.board.service;

import com.example.gitrajabi.board.domain.Comment;
import com.example.gitrajabi.board.domain.Post;
import com.example.gitrajabi.board.dto.CommentResponse;
import com.example.gitrajabi.board.dto.PostDetailResponse;
import com.example.gitrajabi.board.dto.PostPageResponse; // ✅ PostPageResponse 추가
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

    // Use Case #17: GET /api/posts - 게시글 목록 조회 (페이징, 정렬)
    public PostPageResponse getPostList(int page, String sortType) {

        // 1. Pageable 객체 생성
        // page는 0부터 시작하지만, 클라이언트에게는 1부터 보여주기 위해 0 기반으로 변환하여 사용
        int pageSize = 10;
        Sort sort = Sort.by(Sort.Direction.DESC, sortType); // 예: createdAt, viewCount
        PageRequest pageable = PageRequest.of(page, pageSize, sort);

        // 2. 게시글 목록 조회 (Post 엔티티의 deleted_at = null 인 것만 조회됨)
        Page<Post> postPage = postRepository.findAll(pageable);
        List<Post> posts = postPage.getContent();

        // 3. N+1 문제 해결을 위한 사전 작업: 게시글 작성자 ID 및 댓글 수 조회에 필요한 ID 목록 확보
        List<Long> postIds = posts.stream().map(Post::getPostId).collect(Collectors.toList());
        List<Long> postAuthorIds = posts.stream().map(Post::getUserId).distinct().collect(Collectors.toList());

        // 4. ✅ 게시글 작성자 ID를 사용하여 GitHub ID 일괄 조회
        // key: userId, value: githubId
        Map<Long, String> userGithubIdMap = userRepository.findAllByUserIdIn(postAuthorIds).stream()
                .collect(Collectors.toMap(UserEntity::getUserId, UserEntity::getGithubId));

        // 5. ✅ 게시글 ID 목록을 사용하여 댓글 수 일괄 조회
        // 반환: List<Object[]> where Object[] = [postId (Long), commentCount (Long)]
        List<Object[]> commentCounts = commentRepository.countCommentsByPostIds(postIds);
        Map<Long, Integer> commentCountMap = commentCounts.stream()
                .collect(Collectors.toMap(
                        // Object[0] = postId (Long)
                        row -> (Long) row[0],
                        // Object[1] = commentCount (Long)
                        row -> ((Long) row[1]).intValue()
                ));

        // 6. Post 엔티티를 PostResponse DTO로 변환
        Page<PostResponse> responsePage = postPage.map(post -> {
            String githubId = userGithubIdMap.getOrDefault(post.getUserId(), "익명");
            int commentCount = commentCountMap.getOrDefault(post.getPostId(), 0);
            return PostResponse.from(post, githubId, commentCount);
        });

        // 7. 페이징 정보와 함께 응답 DTO로 변환하여 반환
        return PostPageResponse.from(responsePage);
    }

    // Use Case #18: GET /api/posts/{postId} - 게시글 상세 조회
    @Transactional // 조회수 증가 때문에 쓰기 트랜잭션 필요
    public PostDetailResponse getPostDetail(Long postId) {

        // 1. 게시글 조회 및 조회수 증가
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