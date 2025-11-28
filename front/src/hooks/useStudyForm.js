import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { createStudy } from '../services/studyApi'; // 👈 API 함수 가져오기
import { putStudyDetail } from '../services/studyApi';

/*const initialFormData = {
    title: '',
    maxMembers: 4,
    category: '', 
    description: '',
};*/

export function useStudyForm(initialData = {}) {
    const [formData, setFormData] = useState({
        // 기존 초기값(빈값)을 기본으로 설정하되,
        // initialData로 전달된 값이 있으면 그 값을 사용합니다.
        title: '',
        maxMembers: 4,
        category: '', 
        description: '',
        ...initialData // 👈 전달받은 초기 데이터로 덮어쓰기
    });
    const [isSubmitting, setIsSubmitting] = useState(false); // 로딩 상태 추가
    const navigate = useNavigate();

    // 폼 입력 값 변경 핸들러 
    const handleChange = (e) => {
        const { name, value, type } = e.target;
        
        // 라디오 버튼 처리
        if (type === 'radio' && name === 'category') {
            setFormData(prev => ({ ...prev, [name]: value }));
        } 
        // 일반 입력 필드 처리
        else {
            setFormData(prev => ({ ...prev, [name]: value }));
        }
    };
    
    // 폼 데이터 유효성 검사 (간단한 예시)
    const validate = (data) => {
        if (!data.title || data.title.length < 5) return '스터디 제목은 5자 이상이어야 합니다.';
        if (!data.category) return '카테고리를 선택해주세요.';
        if (Number(data.maxMembers) < 2) return '최소 인원 수는 2명 이상이어야 합니다.';
        return null; // 유효성 통과
    };

    // 폼 제출 핸들러
    const handleSubmit = async (e) => {
        e.preventDefault();
        
        const validationError = validate(formData);
        if (validationError) {
            alert(validationError);
            return;
        }

        setIsSubmitting(true);

        // 폼 데이터 가공 (API에 전달할 최종 형식)
        const payload = {
            ...formData,
            maxMembers: Number(formData.maxMembers), 
        };

        try {
            // API 호출
            await createStudy(payload); 

            alert(`'${payload.title}' 스터디가 성공적으로 생성되었습니다.`);
            
            // 성공 후 목록 페이지로 이동
            navigate('/studylist'); 

        } catch (error) {
            // studyApi.js에서 던져진 에러 처리
            alert(error.message || "스터디 생성 중 알 수 없는 오류가 발생했습니다.");
        } finally {
            setIsSubmitting(false); // 로딩 상태 해제
        }
    };

    // 컴포넌트에서 사용할 상태와 함수를 반환
    return {
        formData,
        handleChange,
        handleSubmit,
        isSubmitting
    };
}

export function patchStudyForm(initialData = {}) {
    const [formData, setFormData] = useState({
        // 기존 초기값(빈값)을 기본으로 설정하되,
        // initialData로 전달된 값이 있으면 그 값을 사용합니다.
        title: '',
        maxMembers: 4,
        category: '', 
        description: '',
        ...initialData // 👈 전달받은 초기 데이터로 덮어쓰기
    });
    const [isSubmitting, setIsSubmitting] = useState(false); // 로딩 상태 추가
    const navigate = useNavigate();

    // 폼 입력 값 변경 핸들러 
    const handleChange = (e) => {
        const { name, value, type } = e.target;
        
        // 라디오 버튼 처리
        if (type === 'radio' && name === 'category') {
            setFormData(prev => ({ ...prev, [name]: value }));
        } 
        // 일반 입력 필드 처리
        else {
            setFormData(prev => ({ ...prev, [name]: value }));
        }
    };
    
    // 폼 데이터 유효성 검사 (간단한 예시)
    const validate = (data) => {
        if (!data.title || data.title.length < 5) return '스터디 제목은 5자 이상이어야 합니다.';
        if (!data.category) return '카테고리를 선택해주세요.';
        if (Number(data.maxMembers) < 2) return '최소 인원 수는 2명 이상이어야 합니다.';
        return null; // 유효성 통과
    };

    // 폼 제출 핸들러
    const handleSubmit = async (e) => {
        e.preventDefault();
        
        const validationError = validate(formData);
        if (validationError) {
            alert(validationError);
            return;
        }

        setIsSubmitting(true);

        // 폼 데이터 가공 (API에 전달할 최종 형식)
        const payload = {
            ...formData,
            maxMembers: Number(formData.maxMembers), 
        };

        try {
            // API 호출
            await putStudyDetail(payload); 

            alert(`'${payload.title}' 스터디가 성공적으로 수정되었습니다.`);
            
            // 성공 후 목록 페이지로 이동
            navigate('/studylist'); 

        } catch (error) {
            // studyApi.js에서 던져진 에러 처리
            alert(error.message || "스터디 수정 중 알 수 없는 오류가 발생했습니다.");
        } finally {
            setIsSubmitting(false); // 로딩 상태 해제
        }
    };

    // 컴포넌트에서 사용할 상태와 함수를 반환
    return {
        formData,
        handleChange,
        handleSubmit,
        isSubmitting
    };
}
