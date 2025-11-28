export const mockStudies = [
    {
        id: 1,
        title: "실전! React & Next.js 프로젝트 (모집중)",
        description: "포트폴리오 완성을 목표로 주 2회 화상 스터디를 진행합니다. 초중급 환영합니다.",
        members: 5,
        maxMembers: 8,
        status: "open", // 'open' | 'closed'
        href: "/study/1",
    },
    {
        id: 2,
        title: "알고리즘 코딩 테스트 완벽 대비반",
        description: "매일 한 문제씩 풀고 코드 리뷰 진행합니다. 알고리즘 기본기 다지기.",
        members: 12,
        maxMembers: 15,
        status: "open",
        href: "/study/2",
    },
    {
        id: 3,
        title: "CS 지식 면접 대비 스터디 (마감)",
        description: "OS, 네트워크, 자료구조 등 핵심 CS 지식을 정리합니다.",
        members: 4,
        maxMembers: 4,
        status: "closed",
        href: "/study/3",
    },
    {
        id: 4,
        title: "Git & GitHub 마스터",
        description: "Git 고급 사용법, PR 리뷰 문화 익히기 스터디입니다.",
        members: 7,
        maxMembers: 10,
        status: "open",
        href: "/study/4",
    },
    {
        id: 5,
        title: "실전! React & Next.js 프로젝트 (모집중)",
        description: "포트폴리오 완성을 목표로 주 2회 화상 스터디를 진행합니다. 초중급 환영합니다.",
        members: 5,
        maxMembers: 8,
        status: "open", // 'open' | 'closed'
        href: "/study/1",
    },
    {
        id: 6,
        title: "알고리즘 코딩 테스트 완벽 대비반",
        description: "매일 한 문제씩 풀고 코드 리뷰 진행합니다. 알고리즘 기본기 다지기.",
        members: 12,
        maxMembers: 15,
        status: "open",
        href: "/study/2",
    },
];

export const mockStudyDetail = [
    {
        id: 1,
        title: "실전! React & Next.js 프로젝트",
        category: "프론트엔드",
        description: "포트폴리오 완성을 목표로 주 2회 화상 스터디를 진행합니다. 초중급 환영하며, 협업 툴(Jira, Notion) 사용법도 함께 익힙니다.",
        leader: "김개발",
        members: [
            { name: "김개발 (리더)", role: "leader" },
            { name: "박코딩", role: "member" },
            { name: "이알고", role: "member" },
            { name: "최프젝", role: "member" },
        ],
        progress: {
            totalTasks: 20,
            completedTasks: 12,
            completionRate: 60, // 12/20 * 100
        },
        upcomingTasks: [
            { id: 101, title: "Next.js 서버 컴포넌트 이해", dueDate: "2025.04.15" },
            { id: 102, title: "포트폴리오 기능 정의 회의", dueDate: "2025.04.17" },
             { id: 103, title: "Next.js 서버 컴포넌트 이해", dueDate: "2025.04.15" },
            { id: 104, title: "포트폴리오 기능 정의 회의", dueDate: "2025.04.17" },
        ]
    },
    {
        id: 2,
        title: "알고리즘 코딩 테스트 완벽 대비반",
        category: "프론트엔드",
        description: "매일 한 문제씩 풀고 코드 리뷰 진행합니다. 알고리즘 기본기 다지기.",
        leader: "홍길동",
        members: [
            { name: "홍길동 (리더)", role: "leader" },
            { name: "김두부", role: "member" },
            { name: "이계란", role: "member" },
        ],
        progress: {
            totalTasks: 20,
            completedTasks: 8,
            completionRate: 40, // 12/20 * 100
        },
        upcomingTasks: [
            { id: 101, title: "Next.js 서버 컴포넌트 이해", dueDate: "2025.04.15" },
            { id: 102, title: "포트폴리오 기능 정의 회의", dueDate: "2025.04.17" },
        ]
    },
]

export const mockApplications = [
    { id: 201, applicant: "유저123", date: "2025.04.05" },
    { id: 202, applicant: "유저455", date: "2025.04.03" },
];

export const mockCategories = ["전체", "알고리즘", "프론트엔드", "백엔드", "CS", "면접"];