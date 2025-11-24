import React from 'react';
import MaterialSymbol from '../ui/MaterialSymbol';
import { Link } from 'react-router-dom';

const StudyListItem = ({ study }) => {
    const isClosed = study.status === 'closed';

    const buttonClass = isClosed
        ? "bg-gray-200 dark:bg-gray-700 text-gray-500 cursor-not-allowed"
        : "bg-primary text-white hover:opacity-90";
    
    const statusText = isClosed ? "마감" : "참여 신청";

    return (
        <div className="bg-surface-light dark:bg-surface-dark p-6 rounded-lg shadow-sm border border-border-light dark:border-border-dark flex flex-col justify-between transition-shadow hover:shadow-md">
            <div className="flex-grow">
                <Link 
                    to={`/study/${study.id}`} 
                    className="flex justify-between items-start mb-3 cursor-pointer hover:opacity-80 transition-opacity" 
                >
                    <h3 className="text-lg font-bold text-text-light-primary dark:text-text-dark-primary">{study.title}</h3>
                </Link>

                <p className="text-text-light-secondary dark:text-text-dark-secondary text-sm mb-4 line-clamp-2">{study.description}</p>
                
                <div className="flex flex-wrap gap-2 mb-4">
                    {study.tags.map(tag => (
                        <span key={tag} className="text-xs font-medium bg-primary/10 text-primary dark:bg-primary/20 dark:text-white px-2 py-1 rounded-full">
                            {tag}
                        </span>
                    ))}
                </div>
            </div>

            <div className="pt-4 border-t border-border-light dark:border-border-dark flex items-center justify-between">
                {/* 인원수 표시 */}
                <div className="flex items-center text-text-light-secondary dark:text-text-dark-secondary text-sm">
                    <MaterialSymbol name="group" className="mr-1 text-base" style={{ fontSize: '1rem' }} />
                    {study.members} / {study.maxMembers} 명
                </div>
                
                {/* 참여/마감 버튼 */}
                <Link 
                    to={study.href}
                    className={`px-4 py-2 text-sm font-semibold rounded-md transition-colors ${buttonClass}`}
                    aria-disabled={isClosed}
                    onClick={(e) => isClosed && e.preventDefault()}
                >
                    {statusText}
                </Link>
            </div>
        </div>
    );
};

export default StudyListItem;