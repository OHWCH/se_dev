import React from 'react';
import MaterialSymbol from './MaterialSymbol';

// Prop 구조는 Homepage.jsx에서 정의된 mockPaginationLinks를 기반으로 합니다.
const Pagination = ({ links = [] }) => {
    return (
        <div className="mt-6 flex items-center justify-between">
            {/* Previous Button */}
            <a className="inline-flex items-center px-4 py-2 border border-gray-300 dark:border-gray-600 text-sm font-medium rounded-md text-gray-700 dark:text-gray-300 bg-white dark:bg-gray-800 hover:bg-gray-50 dark:hover:bg-gray-700" href="#">
                <MaterialSymbol name="arrow_back" className="text-base mr-2" />
                Previous
            </a>

            {/* Page Numbers */}
            <nav aria-label="Pagination" className="hidden sm:flex items-center space-x-2">
                {links.map((link, index) => (
                    link.disabled ? (
                        <span key={index} className="px-3 py-1 text-sm font-medium text-gray-500 dark:text-gray-400">
                            {link.label}
                        </span>
                    ) : (
                        <a
                            key={index}
                            className={`px-3 py-1 text-sm font-medium rounded-md ${
                                link.current
                                    ? 'bg-primary/10 text-primary dark:bg-primary/20 dark:text-white'
                                    : 'text-gray-500 dark:text-gray-400 hover:bg-gray-100 dark:hover:bg-gray-700'
                            }`}
                            href={link.href}
                            aria-current={link.current ? 'page' : undefined}
                        >
                            {link.label}
                        </a>
                    )
                ))}
            </nav>

            {/* Next Button */}
            <a className="inline-flex items-center px-4 py-2 border border-gray-300 dark:border-gray-600 text-sm font-medium rounded-md text-gray-700 dark:text-gray-300 bg-white dark:bg-gray-800 hover:bg-gray-50 dark:hover:bg-gray-700" href="#">
                Next
                <MaterialSymbol name="arrow_forward" className="text-base ml-2" />
            </a>
        </div>
    );
};

export default Pagination;