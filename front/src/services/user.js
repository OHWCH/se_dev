import axios from "axios";

//토큰
export async function login(email, password) {
    const response = await axios.post("http://localhost:8080/login", {
        email,
        password
    });

    // ⭐ JWT 토큰 저장
    localStorage.setItem("accessToken", response.data.accessToken);

    return response.data;
}