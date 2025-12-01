import React from 'react';
import MaterialSymbol from '../ui/MaterialSymbol';
import { Link } from 'react-router-dom';

/**
 * 이슈 목록의 개별 항목을 표시하는 컴포넌트입니다.
 */
const IssueItem = ({ issue }) => (
    // Link to를 사용하여 실제 이슈 상세 페이지로 이동하도록 설정
    <Link 
        to={issue.html_url} 
        className="flex items-center justify-between p-4 hover:bg-gray-100 dark:hover:bg-gray-700 rounded-lg transition-colors border-b dark:border-gray-700 last:border-b-0"
    >
        <div className="flex-1 min-w-0">
            {/* 이슈 제목 */}
            <p className="text-base font-medium text-text-light-primary dark:text-text-dark-primary truncate">
                {issue.title}
            </p>
            {/* 작성자 정보 */}
            <p className="text-sm text-text-light-secondary dark:text-text-dark-secondary mt-1">
                작성자: <span className="font-semibold text-primary-dark dark:text-primary">{issue.user.login}</span>
            </p>
        </div>
        <MaterialSymbol name="chevron_right" className="text-2xl text-gray-400 dark:text-gray-500 ml-4" />
    </Link>
);


/**
 * 이슈 목록 전체를 표시하는 메인 컴포넌트
 * @param {string} title - 목록의 제목 (예: "good-first-issue")
 * @param {Array<{title: string, author: string, href: string}>} issues - 표시할 이슈 데이터 배열
 */
const IssueList = ({ title, issues = [] }) => {
    return (
        <section className="space-y-4">
            {/* 제목: 대문자 변환 및 밑줄 스타일 적용 */}
            <h2 className="text-2xl font-bold capitalize text-gray-900 dark:text-white border-b-2 border-primary/50 pb-2">
                {title}
            </h2>
            
            {/* 이슈 목록 컨테이너 */}
            <div className="bg-surface-light dark:bg-surface-dark rounded-lg shadow-xl border border-border-light dark:border-border-dark divide-y divide-border-light dark:divide-border-dark">
                {issues.length > 0 ? (
                    issues.map((issue, index) => (
                        <IssueItem key={index} issue={issue} />
                    ))
                ) : (
                    <div className="p-6 text-center text-gray-500 dark:text-gray-400">
                        표시할 이슈가 없습니다.
                    </div>
                )}
            </div>
            
            {/* 더 보기 링크 */}
            <div className="mt-4 flex justify-end">
                <Link to="#" className="text-primary hover:text-primary-dark font-medium text-sm transition-colors">
                    더 보기 <MaterialSymbol name="open_in_new" className="text-base ml-1 inline-block align-text-bottom" />
                </Link>
            </div>
        </section>
    );
};

export default IssueList;