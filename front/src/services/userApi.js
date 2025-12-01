import axios from "axios";

const BASE_URL = `http://localhost:8080/api`

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
        console.log(JSON.stringify(res.data, null, 2));
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
        console.log(JSON.stringify(response.data, null, 2));
        return response.data;
    } catch (e) {
        console.log(e.response);
    }
}

export async function getTodoList() { 
    const accessToken = localStorage.getItem("accessToken");

    if (!accessToken) {
        // 🌟 토큰이 없으면 호출하지 않고 예외 발생 (로그인 필요)
        throw new Error("로그인 토큰이 없어 할 일을 생성할 수 없습니다. 다시 로그인해 주세요.");
    }
    try {
        const response = await axios.get(`${BASE_URL}/todos?page=0&size=5`, {
            headers: {
                 Authorization: `Bearer ${accessToken}`,
            }
        })
        console.log(JSON.stringify(response.data, null, 2));
        return response.data;
    } catch (e) {
        console.log(e.response);
    }
}

export async function createTodo(content) {
    const accessToken = localStorage.getItem("accessToken");

    if (!accessToken) {
        // 🌟 토큰이 없으면 호출하지 않고 예외 발생 (로그인 필요)
        throw new Error("로그인 토큰이 없어 할 일을 생성할 수 없습니다. 다시 로그인해 주세요.");
    }
    try {
        const payload = { content };
        
        const response = await axios.post(
            `${BASE_URL}/todos`, 
            payload, 
            {
                headers: {
                    // 🌟 Authorization 헤더에 토큰을 "Bearer " 형식으로 추가
                    Authorization: `Bearer ${accessToken}`, 
                    'Content-Type': 'application/json',
                },
            }
        );
        
        // 응답: { id: 1, content: "...", isChecked: false }
        return response.data;
        
    } catch (error) {
        console.error("할 일 생성 실패:", error);
        // 오류 응답이 있다면 (예: 400, 500)
        if (error.response) {
            console.error("서버 응답 데이터:", error.response.data); // 500 응답 본문 로깅
            // 500 오류는 서버 내부 오류이므로, 프론트엔드에서 특정 에러 메시지를 표시하기 어렵습니다.
            // 하지만 백엔드에서 에러 메시지를 내려줬다면 그것을 사용합니다.
            const serverMessage = error.response.data.message || error.response.data.error || '내부 서버 오류';
            throw new Error(`할 일 생성 실패: ${serverMessage} (Status: ${error.response.status})`);
        }
        throw error;
    }
}

