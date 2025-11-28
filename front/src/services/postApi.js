import axios from "axios";
import { mockPosts } from "../data/communityData";

const STUDY_API_URL = '/api/posts';

export async function getPostList() {
    /*try {
        const response = await axios.get(`${API_BASE_URL}/api/posts`, 
            headers: {
                Authorization: `Bearer ${localStorage.getItem("accessToken")}`  //사용자 토근 포함
            });
        return response.data;
    } catch (error) {
        console.error("스터디 목록 조회 실패:", error);

        // 에러 응답이 있다면 (예: 404, 500)
        if (error.response) {
            console.error("서버 응답 데이터:", error.response.data);
            throw new Error(`목록 조회 서버 오류: ${error.response.status}`);
        } 
        // 네트워크 에러라면
        else if (error.request) {
            throw new Error("네트워크 연결 오류 또는 서버 응답 없음.");
        } else {
            throw new Error("요청 설정 중 오류 발생.");
        }
    }*/

        // MOCK 데이터 임시로 
        return mockPosts;
}

export async function createPost(postData) {
    // 백엔드 연동 후
    /*try {
        const res = await axios.post(STUDY_API_URL, newPost);
    } catch (e) {
        consol.log(e.response); 
    }*/
    
    // Mock 데이터 처리 (임시)
    return new Promise(resolve => {
        setTimeout(() => {
            // 1. 새 게시글 객체 생성
            const newPost = {  
                ...postData
            };
            
            // 2. 🎯 mockStudyList 배열에 새 객체 삽입 (핵심)
            mockPosts.push(newPost); 
            
            console.log("Mock API: 새 스터디가 데이터 목록에 삽입되었습니다.", newPost);
            console.log("현재 총 스터디 개수:", mockPosts.length);
            
            // 3. 삽입된 객체 반환
            resolve(newPost);
        }, 500); // 0.5초 지연
    });
}

//게시글 삭제
export async function deletePost(postId) {
    try {
        // DELETE 요청: /api/posts/{postId} 엔드포인트에 요청을 보냄
        const response = await axios.delete(`${API_BASE_URL}/api/posts/${postId}`);
        
        // 백엔드가 204 No Content 또는 200 OK를 반환할 수 있음
        // 성공 시 별도의 데이터를 반환하지 않을 가능성이 높음
        return response.status; 

    } catch (error) {
        console.error(`게시글 ${postId} 삭제 실패:`, error);
        
        // 오류 처리 로직 (네트워크 오류, 403 Forbidden 등)
        if (error.response) {
            throw new Error(`삭제 서버 오류: ${error.response.status}`);
        } else {
            throw new Error("네트워크 연결 오류.");
        }
    }
}