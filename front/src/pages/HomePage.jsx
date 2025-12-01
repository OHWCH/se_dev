import { React, useState, useCallback, useEffect } from 'react';
import Header from '../components/ui/Header';
import SearchBar from '../components/ui/SearchBar';
import IssueList from '../components/community/IssueList';
import { getGoodFirstIssues } from '../services/issueApi';

const Homepage = () => {
    // 🌟 1. 이슈 목록 상태
    const [issues, setIssues] = useState([]);
    // 🌟 2. 로딩 상태
    const [loading, setLoading] = useState(true);
    // 🌟 3. 검색어 상태 (SearchBar와 공유)
    const [keyword, setKeyword] = useState('');
    // 🌟 4. 에러 상태
    const [error, setError] = useState(null);

    // 🌟 5. API 호출 로직 분리 및 useCallback으로 감싸기
    const fetchIssues = useCallback(async (searchKeyword) => {
        setLoading(true);
        setError(null);
        try {
            // GET /api/issues/good-first?keyword={검색어} 호출
            const fetchedIssues = await getGoodFirstIssues(searchKeyword);
            setIssues(fetchedIssues);
        } catch (err) {
            console.error("이슈 목록 로드 실패:", err);
            setError("이슈 목록을 불러오는 데 실패했습니다.");
            setIssues([]);
        } finally {
            setLoading(false);
        }
    }, []);

    // 6. 컴포넌트 마운트 시 초기 목록 로드 (keyword: '')
    useEffect(() => {
        fetchIssues('');
    }, [fetchIssues]);


    // 7. 검색 버튼 클릭 핸들러 (SearchBar에서 호출)
    const handleSearch = () => {
        fetchIssues(keyword); // 현재 입력된 keyword로 검색 실행
    };

    // 8. 검색어 변경 핸들러 (SearchBar에서 호출)
    const handleKeywordChange = (e) => {
        setKeyword(e.target.value);
    };

    // 9. 로딩/에러 표시
    let content;
    if (loading) {
        content = <div className="text-center py-10">로딩 중...</div>;
    } else if (error) {
        content = <div className="text-center py-10 text-red-500">{error}</div>;
    } else if (issues.length === 0) {
        content = <div className="text-center py-10 text-gray-500 dark:text-gray-400">
            검색 결과가 없습니다.
        </div>;
    } else {
        content = (
            <div className="space-y-12">
                <IssueList 
                    title="Good First Issue" 
                    issues={issues} 
                />
            </div>
        );
    }
    
    return (
        <div className="min-h-screen bg-background-light dark:bg-background-dark font-display text-gray-800 dark:text-gray-200 antialiased">
            <Header />
            
            <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
                {/* 🌟 SearchBar에 keyword, setKeyword, handleSearch 전달 */}
                <SearchBar 
                    keyword={keyword}
                    onKeywordChange={handleKeywordChange}
                    onSearch={handleSearch}
                />
                
                {content}
                
            </main>
        </div>
    );
};

export default Homepage;