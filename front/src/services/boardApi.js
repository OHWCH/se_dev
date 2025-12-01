import axios from "axios";
import { showToast } from "../utils/toast";


const BOARD_API_URL = 'http://localhost:8080/api/posts';

// ==================== 게시글 관련 API ====================

// 1. 게시글 작성 (POST /api/posts)
export async function createPost(postData) {
    const token = localStorage.getItem("accessToken");

    if (!token) {
        showToast.error("로그인이 필요합니다");
        throw new Error("인증 토큰이 누락되었습니다.");
    }

    try {
        const res = await axios.post(`${BOARD_API_URL}`, postData, {
            headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer ${token}`
            }
        });

        showToast.success("게시글이 작성되었습니다!");
        return res.data;

    } catch (e) {
        console.error("게시글 작성 실패:", e.response?.data);
        showToast.error("게시글 작성에 실패했습니다");
        throw e;
    }
}

// 2. 게시글 목록 조회 (GET /api/posts)
export async function getPostList(page = 0, type = "createdAt") {
    try {
        const res = await axios.get(`${BOARD_API_URL}`, {
            params: { page, type }
        });
        return res.data;
    } catch (e) {
        console.error("게시글 목록 조회 실패:", e.response);
        showToast.error("게시글 목록을 불러오지 못했습니다");
        throw e;
    }
}

// 3. 게시글 상세 조회 (GET /api/posts/{postId})
export async function getPostDetail(postId) {
    try {
        const res = await axios.get(`${BOARD_API_URL}/${postId}`);
        return res.data;
    } catch (e) {
        console.error("게시글 상세 조회 실패:", e.response);
        showToast.error("게시글을 불러오지 못했습니다");
        throw e;
    }
}

// 4. 게시글 수정 (PUT /api/posts/{postId})
export async function updatePost(postId, postData) {
    const token = localStorage.getItem("accessToken");
    if (!token) {
        showToast.error("로그인이 필요합니다");
        throw new Error("인증 토큰이 누락되었습니다.");
    }

    try {
        const res = await axios.put(`${BOARD_API_URL}/${postId}`, postData, {
            headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer ${token}`
            }
        });
        showToast.success("게시글이 수정되었습니다");
        return res.data;
    } catch (e) {
        console.error("게시글 수정 실패:", e.response);
        showToast.error("게시글 수정에 실패했습니다");
        throw e;
    }
}

// 5. 게시글 삭제 (DELETE /api/posts/{postId})
export async function deletePost(postId) {
    const token = localStorage.getItem("accessToken");
    if (!token) {
        showToast.error("로그인이 필요합니다");
        throw new Error("인증 토큰이 누락되었습니다.");
    }

    try {
        await axios.delete(`${BOARD_API_URL}/${postId}`, {
            headers: { Authorization: `Bearer ${token}` }
        });
        showToast.success("게시글이 삭제되었습니다");
    } catch (e) {
        console.error("게시글 삭제 실패:", e.response);
        showToast.error("게시글 삭제 실패");
        throw e;
    }
}

// ==================== 댓글 관련 API ====================

// 6. 댓글 작성 (POST /api/posts/{postId}/comments)
export async function createComment(postId, content) {
    const token = localStorage.getItem("accessToken");
    if (!token) {
        showToast.error("로그인이 필요합니다");
        throw new Error("인증 토큰이 누락되었습니다.");
    }

    try {
        const res = await axios.post(`${BOARD_API_URL}/${postId}/comments`, 
            { content },
            {
                headers: {
                    "Content-Type": "application/json",
                    Authorization: `Bearer ${token}`
                }
            }
        );
        showToast.success("댓글이 작성되었습니다");
        return res.data;
    } catch (e) {
        console.error("댓글 작성 실패:", e.response);
        showToast.error("댓글 작성에 실패했습니다");
        throw e;
    }
}

// 7. 댓글 삭제 (DELETE /api/posts/{postId}/comments/{commentId})
export async function deleteComment(postId, commentId) {
    const token = localStorage.getItem("accessToken");
    if (!token) {
        showToast.error("로그인이 필요합니다");
        throw new Error("인증 토큰이 누락되었습니다.");
    }

    try {
        await axios.delete(`${BOARD_API_URL}/${postId}/comments/${commentId}`, {
            headers: { Authorization: `Bearer ${token}` }
        });
        showToast.success("댓글이 삭제되었습니다");
    } catch (e) {
        console.error("댓글 삭제 실패:", e.response);
        showToast.error("댓글 삭제 실패");
        throw e;
    }
}