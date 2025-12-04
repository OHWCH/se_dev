import axios from "axios";

const BASE_URL = `http://3.35.246.234:8080/api`

export async function getMyProfile() {
    try {
        const res = await axios.get(`${BASE_URL}/users/me`, {
            headers: {
                 Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
            }
        });
        const githubId = res.data.githubId;
        if (githubId) {
            // 🚨 FIX: "currentUserId" -> "currentGithubId"로 키 이름 통일
            localStorage.setItem("currentGithubId", githubId); 
        }
        return res.data;
    } catch (e) {
        throw e;
    }
}
export async function getContribution() { //스터디 상세정보
    try {
        const response = await axios.get(`${BASE_URL}/my/contributions`, {
            headers: {
                 Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
            }
        })
        return response.data;
    } catch (e) {
        console.log(e.response);
    }
}

export async function getTodoList(page = 0) { 
    const accessToken = localStorage.getItem("accessToken");
    if (!accessToken) {
        throw new Error("Access Token is missing.");
    }
    try {
        const response = await axios.get(`${BASE_URL}/todos?page=0&size=50`, {
            headers: {
                 Authorization: `Bearer ${accessToken}`,
            }
        });
        return response.data;
    } catch (e) {
        console.error("할 일 목록 조회 실패:", e.response ? e.response.data : e);
        throw e;
    }
}

export async function createTodo(content) {
    const accessToken = localStorage.getItem("accessToken");
    if (!accessToken) {
        throw new Error("Access Token is missing.");
    }
    try {
        const payload = { content };
        
        const response = await axios.post(
            `${BASE_URL}/todos`, 
            payload, 
            {
                headers: {
                    Authorization: `Bearer ${accessToken}`, 
                    'Content-Type': 'application/json',
                },
            }
        );
        
        return response.data; 
        
    } catch (error) {
        console.error("할 일 생성 실패:", error.response ? error.response.data : error);
        throw error;
    }
}

export async function toggleTodoCheck(todoId) {
    const accessToken = localStorage.getItem("accessToken");
    if (!accessToken) {
        throw new Error("Access Token is missing.");
    }
    try {
        await axios.patch(
            `${BASE_URL}/todos/${todoId}/check`, 
            null, // 요청 본문(body) 없음
            {
                headers: {
                    Authorization: `Bearer ${accessToken}`, 
                },
            }
        );
        // 응답: 200 OK (본문 없음)
    } catch (error) {
        console.error(`할 일 ID ${todoId} 체크 토글 실패:`, error.response ? error.response.data : error);
        throw error;
    }
}


export async function batchDeleteTodos(todoIds) {
    const accessToken = localStorage.getItem("accessToken");
    if (!accessToken) {
        throw new Error("Access Token is missing.");
    }
    try {
        const payload = { todoIds };
        
        await axios.post(
            `${BASE_URL}/todos/batch-delete`, 
            payload, 
            {
                headers: {
                    Authorization: `Bearer ${accessToken}`, 
                    'Content-Type': 'application/json',
                },
            }
        );
        // 응답: 200 OK (본문 없음)
    } catch (error) {
        console.error("할 일 선택 삭제 실패:", error.response ? error.response.data : error);
        throw error;
    }
}

