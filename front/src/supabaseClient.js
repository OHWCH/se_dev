// src/supabaseClient.ts
import { createClient } from '@supabase/supabase-js'

// 환경변수에서 자동으로 읽어옴 (VITE_ 로 시작해야 브라우저에서 사용 가능)
const supabaseUrl = import.meta.env.VITE_SUPABASE_URL
const supabaseAnonKey = import.meta.env.VITE_SUPABASE_PUBLISHABLE_KEY

// 만약 .env가 없거나 키가 없는 경우
if (!supabaseUrl || !supabaseAnonKey) {
  console.error(
    'Supabase 설정 오류! .env 파일에 아래 두 줄을 추가해주세요:\n' +
    'VITE_SUPABASE_URL=당신의-project-url\n' +
    'VITE_SUPABASE_ANON_KEY=당신의-anon-key'
  )
}

// supabase 클라이언트 생성 (전역에서 재사용 가능)
export const supabase = createClient(supabaseUrl, supabaseAnonKey)

// 선택사항: 디버깅용 로그 
if (import.meta.env.DEV) {
  console.log('Supabase 연결 성공!', { supabaseUrl })
}