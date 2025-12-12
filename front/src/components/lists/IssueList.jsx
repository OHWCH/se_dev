import React from 'react';

const IssueList = ({ title, issues }) => {
    return (
        <section>
            <h2 className="text-xl font-semibold mb-4 text-gray-900 dark:text-white">
                <span className="text-primary mr-2 font-mono">#</span>
                {title}
            </h2>
            <div className="bg-white dark:bg-gray-800 rounded-lg shadow-sm overflow-hidden border border-gray-200 dark:border-gray-700">
                <ul className="divide-y divide-gray-200 dark:divide-gray-700" role="list">
                    {issues.map((issue, index) => (
                        <li key={index} className="p-4 hover:bg-gray-50 dark:hover:bg-gray-700/50 transition-colors duration-150">
                            <div className="flex items-center justify-between">
                                <p className="text-sm font-medium text-gray-900 dark:text-white truncate">{issue.title}</p>
                                <span className="text-xs text-gray-500 dark:text-gray-400 ml-4">by {issue.author}</span>
                            </div>
                        </li>
                    ))}
                </ul>
            </div>
        </section>
    );
};

export default IssueList;