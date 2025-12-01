import { useState, useEffect } from 'react';
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
        studyName: '',
        maxMembers: 4,
        studyCategory: '', 
        studyDescription: '',
        ...initialData // 👈 전달받은 초기 데이터로 덮어쓰기
    });
    const [isSubmitting, setIsSubmitting] = useState(false); // 로딩 상태 추가
    const navigate = useNavigate();

    // 폼 입력 값 변경 핸들러 
    const handleChange = (e) => {
        const { name, value, type } = e.target;
        
        // 라디오 버튼 처리
        if (type === 'radio' && name === 'studyCategory') { 
            setFormData(prev => ({ ...prev, [name]: value }));
        }
        // 일반 입력 필드 처리
        else {
            setFormData(prev => ({ ...prev, [name]: value }));
        }
    };
    
    // 폼 데이터 유효성 검사 (간단한 예시)
    const validate = (data) => {
        if (!data.studyName || data.studyName.length < 5) return '스터디 제목은 5자 이상이어야 합니다.';
        if (!data.studyCategory) return '카테고리를 선택해주세요.';
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

export function patchStudyForm(initialData = null, studyId) { // initialData 기본값을 null로 설정
    
    // 1. 초기 상태 정의: API 로드 전에는 모든 필드를 빈 문자열/기본값으로 설정
    const [formData, setFormData] = useState({
        studyName: '',
        maxMembers: 4, 
        studyCategory: '', 
        studyDescription: '',
    });
    const [isSubmitting, setIsSubmitting] = useState(false); 
    const navigate = useNavigate();

    // 🌟 2. 핵심 해결 로직: initialData (기존 스터디 정보)가 로드될 때 폼 상태를 동기화
    // 이 useEffect가 '수정' 폼에 기존 데이터를 채워주는 역할을 합니다.
    useEffect(() => {
        // initialData가 유효한 객체이고, 그 안에 studyInfo가 있을 때만 실행
        if (initialData && initialData.studyInfo) { 
            const { studyInfo } = initialData;
            
            setFormData({
                // 🚨 API에서 받은 studyInfo의 필드를 폼의 필드에 매핑합니다.
                studyName: studyInfo.studyName || '',
                studyDescription: studyInfo.studyDescription || '',
                studyCategory: studyInfo.studyCategory || '',
                
                // maxMemberCount를 maxMembers로 매핑하고 문자열로 변환하여 폼에 입력합니다.
                maxMembers: String(studyInfo.maxMemberCount || 4), 
            });
        }
    }, [initialData]); // initialData(study prop)가 변경될 때만 실행

    
    // 3. 폼 입력 값 변경 핸들러 (사용자가 입력 시)
    const handleChange = (e) => {
        const { name, value, type } = e.target;
        
        if (type === 'radio' && name === 'studyCategory') { 
            setFormData(prev => ({ ...prev, [name]: value }));
        }
        else {
            setFormData(prev => ({ ...prev, [name]: value }));
        }
    };
    
    const validate = (data) => {
        // ... (유효성 검사 로직)
        if (!data.studyName || data.studyName.length < 5) return '스터디 제목은 5자 이상이어야 합니다.';
        if (!data.studyCategory) return '카테고리를 선택해주세요.';
        if (Number(data.maxMembers) < 2) return '최소 인원 수는 2명 이상이어야 합니다.';
        return null; 
    };

    // 4. 폼 제출 핸들러 (수정 버튼 클릭 시)
    const handleSubmit = async (e) => {
        e.preventDefault();
        
        const validationError = validate(formData);
        if (validationError) {
            alert(validationError);
            return;
        }

        setIsSubmitting(true);

        // 🚨 최종 Payload 생성: 백엔드 API 형식(maxMemberCount)에 맞춤
        const payload = {
            ...formData, 
            maxMemberCount: Number(formData.maxMembers), 
        };
        delete payload.maxMembers; // 프론트엔드용 필드는 삭제

        try {
            // 실제 수정 API 호출
            await putStudyDetail(studyId, payload); 
            navigate(`/study/${studyId}`); 

        } catch (error) {
            console.error("수정 오류:", error);
        } finally {
            setIsSubmitting(false);
        }
    };
    
    return {
        formData,
        handleChange,
        handleSubmit,
        isSubmitting
    };
}