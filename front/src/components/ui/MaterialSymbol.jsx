import React from 'react';

// TSX의 Props 인터페이스가 제거된 표준 JSX 함수 컴포넌트
export const MaterialSymbol = ({ name, className = '', style }) => (
    <span className={`material-symbols-outlined ${className}`} style={style}>
        {name}
    </span>
);

export default MaterialSymbol;