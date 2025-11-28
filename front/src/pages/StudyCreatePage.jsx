import React, { useState } from 'react';
import Header from '../components/ui/Header';
import { useStudyForm } from '../hooks/useStudyForm';
import { mockStudies, mockCategories } from '../data/studyData'; // 카테고리 재사용

const addStudyToMockData = (newStudy) => {
    mockStudies.push(newStudy);
    console.log("새 스터디가 추가되었습니다:", newStudy);
    console.log("현재 Mock 스터디 목록:", mockStudies);
};

const StudyCreatePage = () => {

    const { 
        formData, 
        handleChange, 
        handleSubmit, 
        isSubmitting 
    } = useStudyForm();



    return (
        <div className="flex flex-col min-h-screen bg-background-light dark:bg-background-dark font-display text-text-light-primary dark:text-text-dark-primary antialiased">
            <Header /> 
            
            <main className="flex-grow container mx-auto px-4 sm:px-6 lg:px-8 py-8">
                {/* 폼 컨테이너: max-w-3xl */}
                <div className="max-w-3xl mx-auto">
                    <h1 className="text-3xl font-bold tracking-tight mb-6">스터디 생성</h1>

                    {/* 폼 영역 */}
                    <form 
                        className="bg-surface-light dark:bg-surface-dark rounded-lg shadow-xl border border-border-light dark:border-border-dark p-6 sm:p-8 space-y-6"
                        onSubmit={handleSubmit} // 핸들러 연결
                    >
                        
                        {/* 1. 스터디 제목 */}
                        <div>
                            <label className="block text-sm font-medium text-text-light-secondary dark:text-text-dark-secondary" htmlFor="study-title">스터디 제목</label>
                            <div className="mt-1">
                                <input 
                                    className="block w-full rounded-md border-border-light dark:border-border-dark shadow-sm focus:ring-primary focus:border-primary sm:text-sm bg-background-light dark:bg-background-dark text-text-light-primary dark:text-text-dark-primary placeholder-text-light-secondary" 
                                    id="study-title" 
                                    name="title" 
                                    placeholder="ex) 실전! React & Next.js 포트폴리오 스터디"
                                    type="text"
                                    value={formData.title}
                                    onChange={handleChange}
                                />
                            </div>
                        </div>

                        {/* 2. 최대 인원 수 및 태그 */}
                        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                            
                            {/* 최대 인원 수 */}
                            <div>
                                <label className="block text-sm font-medium text-text-light-secondary dark:text-text-dark-secondary" htmlFor="max-members">최대 인원 수</label>
                                <div className="mt-1">
                                    <input 
                                        className="block w-full rounded-md border-border-light dark:border-border-dark shadow-sm focus:ring-primary focus:border-primary sm:text-sm bg-background-light dark:bg-background-dark text-text-light-primary dark:text-text-dark-primary" 
                                        id="max-members" 
                                        name="maxMembers" 
                                        type="number"
                                        min="2"
                                        value={formData.maxMembers}
                                        onChange={handleChange}
                                    />
                                </div>
                            </div>
                        </div>

                        {/* 3. 스터디 카테고리 */}
                        <div>
                            <fieldset>
                                <legend className="text-base font-medium text-text-light-secondary dark:text-text-dark-secondary">스터디 카테고리</legend>
                                <div className="mt-4 grid grid-cols-2 sm:grid-cols-3 gap-y-3 gap-x-4">
                                    {mockCategories.filter(c => c).map(category => ( 
                                        <div key={category} className="flex items-center">
                                            <input 
                                                className="h-4 w-4 rounded border-border-light dark:border-border-dark text-primary focus:ring-primary bg-background-light dark:bg-background-dark" 
                                                id={`cat-${category}`} 
                                                name="category" 
                                                type="radio"
                                                value={category}
                                                checked={formData.category === category}
                                                onChange={handleChange}
                                            />
                                            <label className="ml-2 block text-sm text-text-light-primary dark:text-text-dark-primary cursor-pointer" htmlFor={`cat-${category}`}>{category}</label>
                                        </div>
                                    ))}
                                </div>
                            </fieldset>
                        </div>

                        {/* 4. 스터디 설명 */}
                        <div>
                            <label className="block text-sm font-medium text-text-light-secondary dark:text-text-dark-secondary" htmlFor="study-description">스터디 설명</label>
                            <div className="mt-1">
                                <textarea 
                                    className="block w-full rounded-md border-border-light dark:border-border-dark shadow-sm focus:ring-primary focus:border-primary sm:text-sm bg-background-light dark:bg-background-dark text-text-light-primary dark:text-text-dark-primary placeholder-text-light-secondary" 
                                    id="study-description" 
                                    name="description" 
                                    placeholder="스터디의 목표, 진행 방식, 기간 등을 자세히 설명해주세요." 
                                    rows="8"
                                    value={formData.description}
                                    onChange={handleChange}
                                />
                            </div>
                        </div>

                        {/* 폼 버튼 */}
                        <div className="flex justify-end space-x-3 pt-4">
                            <button 
                                type="button" 
                                className="px-6 py-2 text-sm font-semibold border border-border-light dark:border-border-dark rounded-md text-text-light-secondary dark:text-text-dark-secondary hover:bg-gray-50 dark:hover:bg-gray-700 transition-colors"
                            >
                                취소
                            </button>
                            <button 
                                type="submit" 
                                className="bg-primary text-white px-6 py-2 rounded-md text-sm font-semibold shadow-sm hover:opacity-90 transition-colors"
                            >
                                스터디 생성
                            </button>
                        </div>
                    </form>
                </div>
            </main>
        </div>
    );
};

export default StudyCreatePage;