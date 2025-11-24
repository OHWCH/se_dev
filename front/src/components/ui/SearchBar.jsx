import React from 'react';
import MaterialSymbol from './MaterialSymbol';

const SearchBar = () => {
    return (
        <div className="flex flex-col sm:flex-row items-center justify-between gap-4 mb-8">
            <div className="relative w-full"> 
                <MaterialSymbol name="search" className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400 dark:text-gray-500" />
                <input 
                    className="w-full pl-10 pr-4 py-2 border border-gray-300 dark:border-gray-600 bg-white dark:bg-gray-700 rounded-md focus:ring-primary focus:border-primary text-gray-900 dark:text-gray-100 placeholder-gray-400 dark:placeholder-gray-500" 
                    placeholder="Search for issues..." 
                    type="search"
                />
            </div>
        </div>
    );
};

export default SearchBar;