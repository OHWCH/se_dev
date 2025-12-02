import axios from "axios";

const ISSUE_API_URL = 'http://localhost:8080/api/issues'; // API URL 확인

export async function getGoodFirstIssues(keyword = '', page = 3) {
    const accessToken = localStorage.getItem("accessToken");
    
    // 🚨 Access Token이 없으면 요청을 보내지 않거나, 에러를 발생시킵니다.
    if (!accessToken) {
        throw new Error("로그인이 필요합니다 (Access Token 누락).");
    }

    try {
        const response = await axios.get(`${ISSUE_API_URL}/good-first?keyword=${keyword}page=${page}`, {
            headers: {
                // 🌟 FIX: Authorization 헤더에 토큰을 'Bearer ' 형식으로 추가
                Authorization: `Bearer ${accessToken}`
            }
        });

        return response.data;

    } catch (error) {
        console.error("Good First Issue 조회 실패:", error);
        if (error.response) {
            console.error("서버 응답 데이터:", error.response.data);
            throw new Error(`이슈 조회 서버 오류: ${error.response.status}`);
        }
        throw error;
    }
}