import { useNavigate } from 'react-router-dom';

export const useLogout = () => {
    const navigate = useNavigate();

    const handleLogout = () => {
        // 1. 로컬 스토리지에서 Access Token 삭제
        localStorage.removeItem('accessToken');
        navigate('/login', { replace: true });
    };
    
    return handleLogout;
};