import React from 'react';
import MaterialSymbol from '../ui/MaterialSymbol';
import { studyList } from '../../data/mypagedata'; // Mock Data import

const StudyList = () => {
    return (
        // 시안: 별도의 컨테이너로 감싸져 있습니다
        <div className="mt-8 bg-white dark:bg-gray-800 p-6 rounded-lg shadow-md border border-gray-200 dark:border-gray-700">
            <h3 className="text-lg font-semibold text-gray-900 dark:text-white mb-4">내 스터디 목록</h3>
            <div className="space-y-4">
                {studyList.map((study, index) => (
                    <div key={index} className="flex items-center justify-between p-4 bg-gray-50 dark:bg-gray-700/50 rounded-md">
                        <div>
                            <p className="font-semibold text-gray-800 dark:text-gray-100">{study.name}</p>
                            <p className="text-sm text-gray-500 dark:text-gray-400">{study.description}</p>
                        </div>
                        <a 
                            className="inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md shadow-sm text-white bg-primary hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary transition-colors" 
                            href={study.link}
                        >
                            바로가기
                            <MaterialSymbol name="arrow_forward" className="text-lg ml-1" />
                        </a>
                    </div>
                ))}
            </div>
        </div>
    );
};

export default StudyList;