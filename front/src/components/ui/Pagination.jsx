import React from 'react';
import MaterialSymbol from './MaterialSymbol';

const Pagination = ({ links = [], currentPage, hasNextPage, onPageChange }) => { 
    
    const isPrevDisabled = currentPage === 0;
    // 🚨 확인: isNextDisabled는 hasNextPage를 기준으로 판단합니다.
    const isNextDisabled = !hasNextPage;

    return (
        <div className="mt-6 flex items-center justify-between">
            {/* Previous Button */}
            <button 
                onClick={() => onPageChange(currentPage - 1)} 
                disabled={isPrevDisabled}
                className={`inline-flex items-center px-4 py-2 border border-gray-300 dark:border-gray-600 text-sm font-medium rounded-md transition-colors 
                    ${isPrevDisabled 
                        ? 'text-gray-400 dark:text-gray-500 bg-gray-100 dark:bg-gray-700 cursor-not-allowed'
                        : 'text-gray-700 dark:text-gray-300 bg-white dark:bg-gray-800 hover:bg-gray-50 dark:hover:bg-gray-700'
                    }`}
            >
                <MaterialSymbol name="arrow_back" className="text-base mr-2" />
                이전
            </button>

            {/* Page Numbers */}
            <nav aria-label="Pagination" className="hidden sm:flex items-center space-x-2">
                {links.map((link, index) => (
                    link.disabled ? (
                        <span key={index} className="px-3 py-1 text-sm font-medium text-gray-500 dark:text-gray-400">
                            {link.label}
                        </span>
                    ) : (
                        <button
                            key={index}
                            onClick={link.onClick}
                            className={`px-3 py-1 text-sm font-medium rounded-md transition-colors 
                                ${link.current
                                    ? 'bg-primary/10 text-primary dark:bg-primary/20 dark:text-white font-bold'
                                    : 'text-gray-500 dark:text-gray-400 hover:bg-gray-100 dark:hover:bg-gray-700'
                                }`}
                            aria-current={link.current ? 'page' : undefined}
                        >
                            {link.label}
                        </button>
                    )
                ))}
            </nav>

            {/* Next Button */}
            <button 
                onClick={() => onPageChange(currentPage + 1)} 
                disabled={isNextDisabled}
                className={`inline-flex items-center px-4 py-2 border border-gray-300 dark:border-gray-600 text-sm font-medium rounded-md transition-colors 
                    ${isNextDisabled 
                        ? 'text-gray-400 dark:text-gray-500 bg-gray-100 dark:bg-gray-700 cursor-not-allowed'
                        : 'text-gray-700 dark:text-gray-300 bg-white dark:bg-gray-800 hover:bg-gray-50 dark:hover:bg-gray-700'
                    }`}
            >
                다음
                <MaterialSymbol name="arrow_forward" className="text-base ml-2" />
            </button>
        </div>
    );
};

export default Pagination;