import React from 'react';
import Router from './routes/Router';
import { Toaster } from 'sonner';

function App() {
  return (
    <>
      <Router />
      <Toaster
        position="top-center"
        richColors
        closeButton
        duration={4000}
        toastOptions={{
          style: { fontSize: '15px' },
        }}
      />
    </>
  );
}

export default App;