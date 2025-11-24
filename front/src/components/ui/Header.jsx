import React from 'react';
import MaterialSymbol from './MaterialSymbol.jsx';
import { Link, useLocation } from 'react-router-dom';

const Header = () => {

    const location = useLocation();

    const getActiveLinkName = (pathname) => {
        if (pathname === '/') return 'home';
        if (pathname.startsWith('/community')) return 'community';
        if (pathname.startsWith('/study')) return 'study';
        if (pathname.startsWith('/challenge')) return 'challenge';
        // 마이페이지는 링크 활성화 목록에 없으므로 기본값 유지
        return ''; 
    };

    const currentActiveLink = getActiveLinkName(location.pathname);

    const getLinkClass = (linkName) => {
        const baseClass = "px-3 py-2 rounded-md text-sm font-medium transition-colors duration-150";
        // 🌟 activeLink 대신 currentActiveLink 사용
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
                        {/* primary 색상은 tailwind.config.js에서 설정된 #4F46E5 또는 #135bec를 사용 */}
                        <h1 className="text-xl font-bold text-primary">Git-ra-jab-i</h1>
                        <nav className="hidden md:flex items-center space-x-4">
                            {/* Home 링크 */}
                            <Link to="/" className={getLinkClass('home')}>Home</Link>
                            <Link to="/community" className={getLinkClass('community')}>Community</Link>
                            <Link to="/studylist" className={getLinkClass('study')}>Study</Link>
                            <Link to="/challenge" className={getLinkClass('challenge')}>Challenge</Link>
                        </nav>
                    </div>
                    <div className="flex items-center space-x-4">
                        <div className="w-8 h-8 rounded-full bg-gray-200 dark:bg-gray-600 flex items-center justify-center">
                            <Link to="/mypage" className="w-8 h-8 rounded-full bg-gray-200 dark:bg-gray-600 flex items-center justify-center hover:opacity-80 transition-opacity duration-150">
                                <MaterialSymbol name="person" className="text-gray-500 dark:text-gray-300" />
                            </Link>
                        </div>
                    </div>
                </div>
            </div>
        </header>
    );
};

export default Header;