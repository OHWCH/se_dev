import axios from "axios";
import { mockStudies } from "../data/studyData";
// 실제 백엔드 연동을 위한 POST 요청을 담당하는 함수
// fetch 또는 axios를 사용합니다. (여기서는 fetch 사용 예시)

const STUDY_API_URL = '/api/studies'; // 백엔드 스터디 생성 엔드포인트

/**
 * 새로운 스터디 데이터를 백엔드 서버에 생성 요청합니다.
 * @param {object} studyData - 폼에서 넘어온 가공된 스터디 데이터
 * @returns {Promise<object>} - 생성된 스터디 객체 (서버 응답)
 */

export async function getStudyList() {
    /*try {
        const response = await axios.get(`${API_BASE_URL}/api/studies`);
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
        return mockStudies;
}


export async function createStudy(studyData) {
    // 백엔드 연동 후
    /*try {
        const res = await axios.post(STUDY_API_URL, newStudy);
    } catch (e) {
        consol.log(e.response); 
    }*/
    
    // Mock 데이터 처리 (임시)
    return new Promise(resolve => {
        setTimeout(() => {
            // 1. 새 스터디 객체 생성
            const newStudy = {  
                ...studyData
            };
            
            // 2. 🎯 mockStudyList 배열에 새 객체 삽입 (핵심)
            mockStudies.push(newStudy); 
            
            console.log("Mock API: 새 스터디가 데이터 목록에 삽입되었습니다.", newStudy);
            console.log("현재 총 스터디 개수:", mockStudies.length);
            
            // 3. 삽입된 객체 반환
            resolve(newStudy);
        }, 500); // 0.5초 지연
    });
}

export async function joinStudy(studyId) {  //가입신청
    try {
        const response = await axios.post(`http://localhost:8080/studies//${studyId}/apply`,{ } ,{
            headers: {
                Authorization: `Bearer ${localStorage.getItem("accessToken")}`  //사용자 토근 포함
            }
        });
    } catch (e) {
        console.log(e.response);
    }
}

export async function getStudyDetail(studyId) { //스터디 상세정보
    try {
        const response = await axios.get(`http://localhost:8080/studies/${studyId}/manage`, {
            headers: {
                 Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
            }
        })
        return response.data;
    } catch (e) {
        console.log(e.response);
    }
}

export async function getStudyMember(studyId) { //스터디 멤버조회
    try {
        const response = await axios.get(`http://localhost:8080/studies/${studyId}/members`, {
            headers: {
                 Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
            }
        })
        return response.data;
    } catch (e) {
        console.log(e.response);
    }
}

export async function putStudyDetail(studyId, studyData) { //스터디 상세정보
    /*try {
        const res = await axios.put(`http://localhost:8080/studies/${studyId}`, newStudy);
    } catch (e) {
        consol.log(e.res); 
    }*/
   console.log('수정완료');
}

export async function approveApplicant(studyId, applicantionId) {
    /*try {
        const res = await axios.post(`http://localhost:8080/studies/${studyId}/approve/${applicantionId}`);
    } catch (e) {
        console.log(e.res)
    }*/
   console.log('수락')
}

export async function rejectApplicant(studyId, applicantionId) {
    /*try {
        const res = await axios.post(`http://localhost:8080/studies/${studyId}/reject/${applicantionId}`);
    } catch (e) {
        console.log(e.res)
    }*/
   console.log(`${studyId}에서 ${applicantionId}거절`)
}

export async function deleteMember(studyId, memberId) {
    /*try {
        const res = await axios.delete(`http://localhost:8080/studies/${studyId}/members/${memberId}`);
    } catch (e) {
        console.log(e.res)
    }*/
   console.log(`${studyId}에서 ${memberId}삭제`)
}

//내 스터디 목록 조회
export async function getMyStudy() {
    try {
        const response = await axios.get(`http://localhost:8080/studies/me`, {
            headers: {
                 Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
            }
        })
        return response.data;
    } catch (e) {
        console.log(e.response);
    }
}