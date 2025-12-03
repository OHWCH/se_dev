import React from 'react';
import MaterialSymbol from './MaterialSymbol';

const Pagination = ({ currentPage, totalPages, onPageChange }) => {
  const isPrevDisabled = currentPage === 0;
  const isNextDisabled = currentPage >= totalPages - 1;

  return (
    <div className="mt-6 flex items-center justify-between">
      {/* 이전 버튼 */}
      <button
        onClick={() => onPageChange(currentPage - 1)}
        disabled={isPrevDisabled}
        className={`inline-flex items-center px-4 py-2 border text-sm font-medium rounded-md transition-colors
          ${isPrevDisabled 
            ? 'text-gray-400 bg-gray-100 cursor-not-allowed dark:text-gray-500 dark:bg-gray-700'
            : 'text-gray-700 bg-white hover:bg-gray-50 dark:text-gray-300 dark:bg-gray-800 dark:hover:bg-gray-700'
          }`}
      >
        <MaterialSymbol name="arrow_back" className="text-base mr-2" />
        이전
      </button>

      {/* 페이지 번호 */}
      <nav aria-label="Pagination" className="hidden sm:flex items-center space-x-2">
        {Array.from({ length: totalPages }, (_, i) => (
          <button
            key={i}
            onClick={() => onPageChange(i)}
            className={`px-3 py-1 text-sm font-medium rounded-md transition-colors
              ${i === currentPage
                ? 'bg-primary/10 text-primary font-bold dark:bg-primary/20 dark:text-white'
                : 'text-gray-500 hover:bg-gray-100 dark:text-gray-400 dark:hover:bg-gray-700'
              }`}
            aria-current={i === currentPage ? 'page' : undefined}
          >
            {i + 1}
          </button>
        ))}
      </nav>

      {/* 다음 버튼 */}
      <button
        onClick={() => onPageChange(currentPage + 1)}
        disabled={isNextDisabled}
        className={`inline-flex items-center px-4 py-2 border text-sm font-medium rounded-md transition-colors
          ${isNextDisabled 
            ? 'text-gray-400 bg-gray-100 cursor-not-allowed dark:text-gray-500 dark:bg-gray-700'
            : 'text-gray-700 bg-white hover:bg-gray-50 dark:text-gray-300 dark:bg-gray-800 dark:hover:bg-gray-700'
          }`}
      >
        다음
        <MaterialSymbol name="arrow_forward" className="text-base ml-2" />
      </button>
    </div>
  );
};

export default Pagination;
