import axios from "axios";
import { mockStudies } from "../data/studyData";
import { showToast } from "../utils/toast"; 

// 실제 백엔드 연동을 위한 POST 요청을 담당하는 함수
// fetch 또는 axios를 사용합니다. (여기서는 fetch 사용 예시)

const STUDY_API_URL = 'http://localhost:8080/studies'; // 백엔드 스터디 생성 엔드포인트

/**
 * 새로운 스터디 데이터를 백엔드 서버에 생성 요청합니다.
 * @param {object} studyData - 폼에서 넘어온 가공된 스터디 데이터
 * @returns {Promise<object>} - 생성된 스터디 객체 (서버 응답)
 */

export async function getStudyList(page = 0) { 
    try {
        const response = await axios.get(`${STUDY_API_URL}?page=${page}`, { 
            headers: {
                Authorization: `Bearer ${localStorage.getItem("accessToken")}` 
            }
        });
        
        // 응답 데이터는 스터디 목록 배열 (content: [...])
        return response.data; 
    } catch (error) {
        console.error("스터디 목록 조회 실패:", error);
        throw error;
    }
}

export async function getStudyMain(studyId) {
    try {
        const res = await axios.get(`${STUDY_API_URL}/${studyId}/main`, {
            headers: {
                // 🌟 Authorization 헤더에 토큰을 "Bearer " 형식으로 추가
                Authorization: `Bearer ${localStorage.getItem("accessToken")}` 
                
            }
        });
        console.log(JSON.stringify(res.data, null, 2));
        return res.data;
    } catch (e) {
        throw e;
    }
}

export async function getMyStudy() {
    try {
        const res = await axios.get(`${STUDY_API_URL}/me`, {
            headers: {
                Authorization: `Bearer ${localStorage.getItem("accessToken")}` 
            }
        })
        console.log(JSON.stringify(res.data, null, 2));
        return res.data;
    } catch (e) {
        // alert(`${e.response}`);  
        showToast.error("내 스터디 목록을 불러오지 못했습니다");
        throw e;
    }
}


export async function createStudy(studyData) {
    const token = localStorage.getItem("accessToken");

    if (!token) {
        console.error("스터디 생성 실패: Access Token이 없습니다. 로그인 상태를 확인하세요.");
        // alert(`스터디 생성 실패: Access Token이 없습니다. 로그인 상태를 확인하세요.`);  
        showToast.error("로그인이 필요합니다");
        throw new Error("인증 토큰이 누락되었습니다.");
        
    }

    try {
        const res = await axios.post(`${STUDY_API_URL}`, studyData, {
            headers: {
                // Authorization 헤더에 토큰을 "Bearer " 형식으로 추가
                Authorization: `Bearer ${token}` 
            }
        });

        // const responseData = res.data.message;
        // alert(`${responseData}`); // alert 팝업 표시  
        showToast.success("스터디가 생성되었습니다!");
        return res.data;

    } catch (e) {
        console.error("스터디 생성 실패:", e.response);
        // 400 Bad Request의 상세 원인 콘솔 확인 가능
        showToast.error("스터디 생성에 실패했습니다");
        throw e; // 오류 재발생
    }
}

export async function quitStudy(studyId) {
    const token = localStorage.getItem("accessToken");

    try{
        const res = await axios.delete(`${STUDY_API_URL}/${studyId}/members/me`, {
            headers: {
                Authorization: `Bearer ${token}` 
            }
        })

        // alert(`${res.data.message}`);
        showToast.success("스터디에서 탈퇴했습니다");
        return res;

    } catch (e) {
        console.error("스터디 탈퇴 실패:", e.response);
        showToast.error("스터디 탈퇴 실패");
        throw e;
    }
}

export async function deleteStudy(studyId) {
    //스터디 삭제 미구현
}

export async function joinStudy(studyId) {  //가입신청
    const token = localStorage.getItem("accessToken");

    if (!token) {
        // alert(`스터디 참가요청 실패: Access Token이 없습니다. 로그인 상태를 확인하세요.`);
        showToast.error("로그인이 필요합니다");
        throw new Error("인증 토큰이 누락되었습니다.");
        
    }

    try {
        const response = await axios.post(`${STUDY_API_URL}/${studyId}/apply`,{ } ,{
            headers: {
                Authorization: `Bearer ${token}`  //사용자 토큰 포함
            }
        });

        // alert(`${response.data.message}`)
        showToast.success("가입 신청이 완료되었습니다!");
        return response;

    } catch (e) {
        console.log(e.response);
        showToast.error("가입 신청 실패");
        throw e;
    }
}

export async function getStudyDetail(studyId) { //스터디 상세정보

    const token = localStorage.getItem("accessToken");

    if (!token) {
        // alert(`스터디 상세요청 실패: Access Token이 없습니다. 로그인 상태를 확인하세요.`);
        showToast.error("로그인이 필요합니다");
        throw new Error("인증 토큰이 누락되었습니다.");
        
    }

    try {
        const response = await axios.get(`${STUDY_API_URL}/${studyId}/manage`, {
            headers: {
                 Authorization: `Bearer ${token}`,
            }
        })

        console.log(JSON.stringify(response.data, null, 2));
        return response.data;

    } catch (e) {
        console.log(e.response);
        showToast.error("스터디 상세 정보를 불러오지 못했습니다");
        throw e;
    }
}

export async function getStudyMember(studyId) {
    try {
        const response = await axios.get(`${STUDY_API_URL}/${studyId}/members`, {
            headers: {
                 Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
            }
        })
        
        return response.data;
    } catch (e) {
        console.log(e.response);
        showToast.error("멤버 목록을 불러오지 못했습니다");
        throw e;
    }
}

export async function putStudyDetail(studyId, studyData) { //스터디 상세정보 수정
    try {
        const res = await axios.put(`${STUDY_API_URL}/${studyId}`, studyData, {
            headers: {
                 Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
            }
        });

        // alert(`${res.data.message}`);
        showToast.success("스터디 정보가 수정되었습니다");
        return res.data;
    } catch (e) {
        console.log(e.res); 
        showToast.error("스터디 수정 실패");
        throw e;
    }
}

export async function approveApplicant(studyId, applicantionId) {

    const token = localStorage.getItem("accessToken");

    if (!token) {
        // alert(`스터디 상세요청 실패: Access Token이 없습니다 로그인 상태를 확인하세요.`);
        showToast.error("로그인이 필요합니다");
        throw new Error("인증 토큰이 누락되었습니다.");
        
    }

    try {
        const res = await axios.post(`${STUDY_API_URL}/${studyId}/approve/${applicantionId}`, {}, {
            headers: {
                 Authorization: `Bearer ${token}`,
            }
        });

        // alert(`${res.data.message}`)
        showToast.success("신청을 승인했습니다");
        window.location.reload();
        return res;

    } catch (e) {
        console.log(e.res)
        showToast.error("승인 처리 실패");
        throw e;
    }
   console.log('수락')
}

export async function rejectApplicant(studyId, applicantionId) {
    try {
        const res = await axios.post(`${STUDY_API_URL}/${studyId}/reject/${applicantionId}`, {}, {
            headers: {
                 Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
            }
        });

        // alert(`${res.data.message}`);
        showToast.success("신청을 거절했습니다");
        window.location.reload();
        return res;
    } catch (e) {
        console.log(e.res)
        showToast.error("거절 처리 실패");
        throw e;
    }
   console.log(`${studyId}에서 ${applicantionId}거절`)
}

export async function deleteMember(studyId, memberId) {
    try {
        const res = await axios.delete(`${STUDY_API_URL}/${studyId}/members/${memberId}`, {
            headers: {
                 Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
            }
        });

        // alert(`${res.data.message}`);
        showToast.success("멤버를 내보냈습니다");
        return res;

    } catch (e) {
        console.log(e.res)
        showToast.error("멤버 내보내기 실패");
        throw e;
    }
}


export async function getStudySchedule(studyId) {
    try {
        const response = await axios.get(`${STUDY_API_URL}/${studyId}/schedules`)
        console.log(JSON.stringify(response.data, null, 2));
        return response.data;
    } catch (e) {
        console.log(e.response)
        showToast.error("일정을 불러오지 못했습니다");
        throw e;
    }
}

export async function createStudySchedule(studyId, scheduleData) {
    
    try {
        const res = await axios.post(
            `${STUDY_API_URL}/${studyId}/schedules`,
            scheduleData,
            {
                headers: {
                    Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
                },
            }
        );
        // alert(`${res.data.message}`)
        showToast.success("일정이 생성되었습니다");
        return res.data;
    } catch (e) {
        console.error("일정 생성 실패:", e.response ? e.response.data : e);
        showToast.error("일정 생성에 실패했습니다");
        throw new Error("일정 생성에 실패했습니다.");
    }
}

export async function joinStudySchedule(studyId, scheduleId, scheduleData) {  
    try{
        const res = await axios.post(`${STUDY_API_URL}/${studyId}/schedules/${scheduleId}/participate`, { scheduleData }, {
            headers: {
                    Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
                },
        })
        showToast.success("일정 참여 신청 완료");
        return res;
    } catch (e){
        showToast.error("일정 참여 실패");
        throw e;
    }
}