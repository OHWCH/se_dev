import axios from "axios";

export async function getContribution() { //스터디 상세정보
    try {
        const response = await axios.get(`http://localhost:8080/api/my/contributions`)
        return response.data;
    } catch (e) {
        console.log(e.response);
    }
}