import React, { useEffect, useState } from 'react';
import MaterialSymbol from './MaterialSymbol.jsx';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { supabase } from "../../supabaseClient.js";  

const Header = () => {
    const location = useLocation();
    const navigate = useNavigate();
    const [user, setUser] = useState(null);

    // 로그인 상태 실시간 감지
    useEffect(() => {
        // 초기 세션 확인
        supabase.auth.getSession().then(({ data: { session } }) => {
            setUser(session?.user ?? null);
        });

        // 실시간 변화 감지
        const { data: listener } = supabase.auth.onAuthStateChange((_event, session) => {
            setUser(session?.user ?? null);
        });

        return () => listener.subscription.unsubscribe();
    }, []);

    const handleLogout = async () => {
        await supabase.auth.signOut();
        navigate("/");
    };

    const getActiveLinkName = (pathname) => {
        if (pathname === '/') return 'home';
        if (pathname.startsWith('/community')) return 'community';
        if (pathname.startsWith('/studylist') || pathname.startsWith('/study')) return 'study';
        if (pathname.startsWith('/challenge')) return 'challenge';
        return '';
    };

    const currentActiveLink = getActiveLinkName(location.pathname);

    const getLinkClass = (linkName) => {
        const baseClass = "px-3 py-2 rounded-md text-sm font-medium transition-colors duration-150";
        if (currentActiveLink === linkName) {
            return `${baseClass} bg-gray-100 dark:bg-gray-700 text-gray-900 dark:text-white`;
        }
        return `${baseClass} text-gray-500 dark:text-gray-400 hover:bg-gray-100 dark:hover:bg-gray-700 hover:text-gray-900 dark:hover:text-white`;
    };

    return (
        <header className="bg-white dark:bg-gray-800 shadow-sm sticky top-0 z-10">
            <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                <div className="flex h-16 items-center justify-between">
                    <div className="flex items-center space-x-8">
                        <h1 className="text-xl font-bold text-primary cursor-pointer" onClick={() => navigate("/")}>
                            Git-ra-jab-i
                        </h1>
                        <nav className="hidden md:flex items-center space-x-4">
                            <Link to="/" className={getLinkClass('home')}>Home</Link>
                            <Link to="/community" className={getLinkClass('community')}>Community</Link>
                            <Link to="/studylist" className={getLinkClass('study')}>Study</Link>
                            <Link to="/challenge" className={getLinkClass('challenge')}>Challenge</Link>
                        </nav>
                    </div>

                    {/* 오른쪽 영역: 로그인 상태에 따라 다르게 표시 */}
                    <div className="flex items-center space-x-4">
                        {user ? (
                            // 로그인된 경우
                            <div className="flex items-center space-x-3">
                                <span className="text-sm text-gray-700 dark:text-gray-300 hidden sm:block">
                                    {user.user_metadata?.name || user.email?.split('@')[0]}
                                </span>
                                <button
                                    onClick={handleLogout}
                                    className="px-4 py-2 text-sm bg-red-600 text-white rounded-lg hover:bg-red-700 transition"
                                >
                                    로그아웃
                                </button>
                                <Link to="/mypage" className="w-8 h-8 rounded-full bg-gradient-to-br from-blue-500 to-purple-600 flex items-center justify-center hover:opacity-90 transition">
                                    <MaterialSymbol name="person" className="text-white text-lg" />
                                </Link>
                            </div>
                        ) : (
                            // 로그인 안 된 경우
                            <div className="flex items-center space-x-3">
                                <button
                                    onClick={() => navigate("/login")}
                                    className="px-5 py-2 bg-black text-white rounded-lg hover:bg-gray-800 transition flex items-center gap-2 text-sm font-medium"
                                >
                                    <svg className="w-5 h-5" fill="currentColor" viewBox="0 0 24 24">
                                        <path d="M12 0c-6.626 0-12 5.373-12 12 0 5.302 3.438 9.8 8.207 11.387.599.111.793-.261.793-.577v-2.234c-3.338.726-4.033-1.416-4.033-1.416-.546-1.387-1.333-1.756-1.333-1.756-1.089-.745.083-.729.083-.729 1.205.084 1.839 1.237 1.237 1.237 1.07 1.834 2.807 1.304 3.492.997.107-.775.418-1.305.762-1.604-2.665-.305-5.467-1.334-5.467-5.931 0-1.311.469-2.381 1.236-3.221-.124-.303-.535-1.524.117-3.176 0 0 1.008-.327 3.301 1.23.957-.266 1.983-.399 3.003-.404 1.02.005 2.047.138 3.006.404 2.291-1.552 3.297-1.23 3.297-1.23.653 1.653.242 2.874.118 3.176.77.84 1.235 1.911 1.235 3.221 0 4.609-2.807 5.624-5.479 5.921.43.372.823 1.102.823 2.222v3.293c0 .319.192.694.801.576 4.765-1.589 8.199-6.086 8.199-11.386 0-6.627-5.373-12-12-12z"/>
                                    </svg>
                                    로그인
                                </button>
                                <Link to="/mypage" className="w-8 h-8 rounded-full bg-gray-200 dark:bg-gray-600 flex items-center justify-center hover:opacity-80 transition">
                                    <MaterialSymbol name="person" className="text-gray-500 dark:text-gray-300" />
                                </Link>
                            </div>
                        )}
                    </div>
                </div>
            </div>
        </header>
    );
};

export default Header;