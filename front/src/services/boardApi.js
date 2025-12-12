import axios from "axios";
import { showToast } from "../utils/toast";

const BOARD_API_URL = 'http://3.35.246.234:8080/api/posts';

// ✅ 헬퍼 함수: 깃허브 아이디 파라미터 생성
const getAuthParams = () => {
    const githubId = localStorage.getItem("currentGithubId");
    return githubId ? { githubId } : {};
};

// ==================== 게시글 관련 API ====================

// 1. 게시글 작성
export async function createPost(postData) {
    const token = localStorage.getItem("accessToken");
    if (!token) throw new Error("토큰 없음");

    try {
        const res = await axios.post(`${BOARD_API_URL}`, postData, {
            params: getAuthParams(), // ✅ githubId 전달
            headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer ${token}`
            }
        });
        showToast.success("게시글이 작성되었습니다!");
        return res.data;
    } catch (e) {
        console.error("게시글 작성 실패:", e);
        showToast.error("게시글 작성 실패");
        throw e;
    }
}

// 2. 게시글 목록 조회
export async function getPostList(page = 0, type = "createdAt") {
    try {
        const res = await axios.get(`${BOARD_API_URL}`, {
            params: { page, type }
        });
        return res.data;
    } catch (e) {
        console.error(e);
        throw e;
    }
}

// 3. 게시글 상세 조회
export async function getPostDetail(postId) {
    try {
        const res = await axios.get(`${BOARD_API_URL}/${postId}`);
        return res.data;
    } catch (e) {
        console.error(e);
        throw e;
    }
}

// 4. 게시글 수정 
export async function updatePost(postId, postData) {
    const token = localStorage.getItem("accessToken");
    
    try {
        // ✅ params에 githubId를 꼭 넣어줘야 400 에러가 안 납니다.
        const res = await axios.put(`${BOARD_API_URL}/${postId}`, postData, {
            params: getAuthParams(), 
            headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer ${token}`
            }
        });
        showToast.success("게시글이 수정되었습니다");
        return res.data;
    } catch (e) {
        console.error("게시글 수정 실패:", e);
        showToast.error("게시글 수정 실패");
        throw e;
    }
}

// 5. 게시글 삭제 
export async function deletePost(postId) {
    const token = localStorage.getItem("accessToken");

    try {
        // ✅ delete는 두 번째 인자가 config입니다. params를 여기에 넣습니다.
        await axios.delete(`${BOARD_API_URL}/${postId}`, {
            params: getAuthParams(),
            headers: { Authorization: `Bearer ${token}` }
        });
        showToast.success("게시글이 삭제되었습니다");
    } catch (e) {
        console.error("게시글 삭제 실패:", e);
        showToast.error("게시글 삭제 실패");
        throw e;
    }
}

// ==================== 댓글 관련 API ====================

// 6. 댓글 작성 
export async function createComment(postId, content) {
    const token = localStorage.getItem("accessToken");
    if (!token) throw new Error("로그인 필요");

    try {
        const res = await axios.post(`${BOARD_API_URL}/${postId}/comments`, 
            { content },
            {
                params: getAuthParams(), // ✅ githubId 전달
                headers: {
                    "Content-Type": "application/json",
                    Authorization: `Bearer ${token}`
                }
            }
        );
        showToast.success("댓글이 작성되었습니다");
        return res.data;
    } catch (e) {
        console.error("댓글 작성 실패:", e);
        showToast.error("댓글 작성 실패");
        throw e;
    }
}

// 7. 댓글 삭제 
export async function deleteComment(postId, commentId) {
    const token = localStorage.getItem("accessToken");

    try {
        await axios.delete(`${BOARD_API_URL}/${postId}/comments/${commentId}`, {
            params: getAuthParams(), // ✅ githubId 전달
            headers: { Authorization: `Bearer ${token}` }
        });
        showToast.success("댓글이 삭제되었습니다");
    } catch (e) {
        console.error("댓글 삭제 실패:", e);
        showToast.error("댓글 삭제 실패");
        throw e;
    }
}