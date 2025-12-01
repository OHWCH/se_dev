import React from 'react';
import Header from '../components/ui/Header';
import SearchBar from '../components/ui/SearchBar';
import IssueList from '../components/lists/IssueList';
import Pagination from '../components/ui/Pagination';
import { getMyProfile } from '../services/userApi';

// ====================================================================
// Mock Data (실제 데이터는 API 통신 등으로 대체됩니다)
// ====================================================================

const mockIssues = [
    { title: "Fix: Navbar alignment issue on mobile", author: "@janesmith", href: "#" },
    { title: "Update documentation for API endpoints", author: "@alexdev", href: "#" },
    { title: "Add unit tests for the user model", author: "@chrisw", href: "#" },
    { title: "Feature: Implement dark mode toggle", author: "@frontendguru", href: "#" },
    { title: "Refactor authentication service", author: "@backendwiz", href: "#" },
    { title: "Bug: User profile picture not updating", author: "@johndoe", href: "#" },
    { title: "Optimize database queries for dashboard", author: "@dbmaster", href: "#" },
    { title: "Enhancement: Add search filters", author: "@ux-expert", href: "#" },
];

const mockPaginationLinks = [
    { label: '1', href: '#', current: false },
    { label: '2', href: '#', current: true },
    { label: '3', 'href': '#', current: false },
    { label: '...', 'href': '#', current: false, disabled: true },
    { label: '8', 'href': '#', current: false },
];

// ====================================================================
// Homepage Component
// ====================================================================

const Homepage = () => {

    const myProfile = getMyProfile();
    return (
        // 전역 스타일링: 배경색(light/dark), 폰트(display), 기본 글자색 설정
        <div className="min-h-screen bg-background-light dark:bg-background-dark font-display text-gray-800 dark:text-gray-200 antialiased">
            <Header />
            
            {/* Main Content Area: 최대 너비 7xl, 중앙 정렬 */}
            <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
                <SearchBar />
                <div className="space-y-12">
                    {/* 이슈 섹션 */}
                    <IssueList 
                        title="good-first-issue" 
                        issues={mockIssues} 
                    />
                    
                    {/* 페이지네이션 */}
                    <Pagination links={mockPaginationLinks} />
                </div>
            </main>
        </div>
    );
};

export default Homepage;