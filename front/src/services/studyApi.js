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
    try {
        const response = await axios.get(`http://localhost:8080/studies`);
        console.log(response.data);
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
    }
}


export async function createStudy(studyData) {
    // 🌟 이 부분을 수정합니다 🌟
    const token = localStorage.getItem("accessToken");

    if (!token) {
        console.error("스터디 생성 실패: Access Token이 없습니다. 로그인 상태를 확인하세요.");
        throw new Error("인증 토큰이 누락되었습니다.");
    }

    try {
        const res = await axios.post(`http://localhost:8080/studies`, studyData, {
            headers: {
                // 🌟 Authorization 헤더에 토큰을 "Bearer " 형식으로 추가
                Authorization: `Bearer ${token}` 
            }
        });
        return res.data; // 성공 시 응답 데이터 반환

    } catch (e) {
        console.error("스터디 생성 실패:", e.response);
        // 400 Bad Request의 상세 원인을 콘솔에서 확인 가능합니다.
        throw e; // 오류 재발생
    }
}

export async function deleteStudy(studyId) {
    //스터디 삭제 미구현
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

export async function putStudyDetail(studyId, studyData) { //스터디 상세정보 수정
    try {
        const res = await axios.put(`http://localhost:8080/studies/${studyId}`, studyData, {
            headers: {
                 Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
            }
        });
    } catch (e) {
        console.log(e.res); 
    }
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

export async function getStudySchedule(studyId) {
    try {
        const response = await axios.get(`http://localhost:8080/studies/${studyId}/schedules`)
        return response.data;
    } catch (e) {
        console.log(e.response)
    }
}

export async function createStudySchedule(studyId, scheduleData) {
    try {
        const res = await axios.post(
            `http://localhost:8080/studies/${studyId}/schedules`,
            scheduleData,
            {
                headers: {
                    Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
                },
            }
        );
        return res.data;
    } catch (e) {
        console.error("일정 생성 실패:", e.response ? e.response.data : e);
        throw new Error("일정 생성에 실패했습니다.");
    }
}
