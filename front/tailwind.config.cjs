/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}", 
  ],
  darkMode: "class",
  
  theme: {
    extend: {
      colors: {
        // 마이페이지 시안의 색상을 기준으로 설정
        primary: "#135bec", // text-primary, bg-primary 등 생성
        "background-light": "#f6f6f8", 
        "background-dark": "#101622",  
      },
      fontFamily: {
        // font-display 클래스로 Inter 폰트 사용
        display: ["Inter", "sans-serif"], 
      },
      // ... (borderRadius 등 기타 설정)
    },
  },
  plugins: [],
}

