export const mockPosts = [
    { 
        id: 1, 
        title: "Tailwind CSS로 다크모드 구현하는 방법 질문입니다.", 
        author: "김개발", 
        date: "2024. 05. 21", 
        comments: 12, 
        href: "#" 
    },
    { 
        id: 2, 
        title: "useEffect 훅의 dependency array 관련해서 궁금한 점이 있습니다.", 
        author: "박코딩", 
        date: "2024. 05. 20", 
        comments: 5, 
        href: "#" 
    },
    { 
        id: 3, 
        title: "자바스크립트 비동기 처리, async/await 완벽 정리", 
        author: "이알고", 
        date: "2024. 05. 20", 
        comments: 28, 
        href: "#" 
    },
    { 
        id: 4, 
        title: "사이드 프로젝트 같이 하실 분 구합니다. (React, Node.js)", 
        author: "최프젝", 
        date: "2024. 05. 19", 
        comments: 3, 
        href: "#" 
    },
    { 
        id: 5, 
        title: "프론트엔드 개발자 취업 준비 로드맵 공유", 
        author: "정로드", 
        date: "2024. 05. 18", 
        comments: 0, 
        href: "#" 
    },
];

// 페이지네이션 데이터는 Homepage의 Mock Pagination을 재사용합니다.
// (현재 페이지 1, 총 10페이지 가정)
export const mockPaginationLinks = [
    { label: '1', href: '#', current: true },
    { label: '2', href: '#', current: false },
    { label: '3', 'href': '#', current: false },
    { label: '...', 'href': '#', current: false, disabled: true },
    { label: '10', 'href': '#', current: false },
];