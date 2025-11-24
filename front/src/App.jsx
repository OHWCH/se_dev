import React from 'react';
import Router from './routes/Router';
// MyPage 컴포넌트도 필요하다면 여기에 import 할 수 있습니다.

function App() {
  return (
    // 라우팅 시스템이 있다면 Homepage를 루트 경로에 연결합니다.
    <Router/>
  );
}

export default App;