import axios from "axios";

export async function getIssueList() {
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