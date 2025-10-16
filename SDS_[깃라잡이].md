# Software Design Specification (SDS)

## (Project Title)

(Logo) - option

**Team information:** Student No, Name, E-mail  

---

## Revision history

| Revision date | Version # | Description | Author |
|---------------|-----------|-------------|--------|
| MM/DD/YYYY    | 0.00      | Type brief description here | Author name |
| | | | |
| | | | |
| | | | |
| | | | |
---

## Contents
1. Introduction  -------------------
2. Use case analysis  --------------
3. Class diagram  ------------------
4. Sequence diagram  ---------------
5. State machine diagram  ----------
6. User interface prototype  -------
7. Implementation requirements  ----
8. Glossary  -----------------------
9. References  ---------------------

---

## Authors for each section

- Introduction – XXX, XXX, XXX  
- Use case analysis – XXX  
- Class diagram – XXX, XXX  
- Sequence diagram – XXX, XXX  
- State machine diagram – XXX, XXX  
- User interface prototype – XXX, XXX  
- Implementation requirements – XXX, XXX  
- Glossary – XXX, XXX  
- References – XXX, XXX  

---

## 1. Introduction
- Summarize the contents of this document.  
- Describe the important points of your design.  
- 12pt, 160%.  

---

## 2. Use case analysis
- Build a use case diagram.  
- Make detailed description for each use case (Use case description).  
- 12pt, 160%.  

### <Form of Use case description> 이건 예시
**Use case #2 : Log-in**

#### GENERAL CHARACTERISTICS
- **Summary**  
  자원봉사자와 사회복지기관이 VMS 사용을 위해 회원인증을 받기 위한 기능  

- **Scope**  
  VMS (Volunteer Management System)  

- **Level**  
  User level  

- **Author**  
  김철민  

- **Last Update**  
  2025. 10. 27.  

- **Status**  
  Analysis (Finalize)  

- **Primary Actor**  
  자원봉사자, 사회복지기관  

- **Preconditions**  
  봉사활동을 예약하려는 자원봉사자들과 사회복지기관이 VMS에 회원가입 상태여야 한다.  

- **Trigger**  
  VMS에 로그인하기 위해 아이디와 비밀번호를 입력한 후 회원 인증을 받으려고 할 때  

- **Success Post Condition**  
  자원봉사자와 사회복지기관은 VMS 사용 허가를 받는다. 이후 VMS의 모든 기능을 이용할 수 있다.  

- **Failed Post Condition**  
  자원봉사자와 사회복지기관은 VMS를 사용하기 위한 허가를 얻지 못한다. VMS에서 봉사활동검색 기능만 이용할 수 있다.  

---

#### MAIN SUCCESS SCENARIO
| Step | Action |
|------|--------|
| S | 회원이 VMS에 로그인한다. |
| 1 | 이 Use case는 회원이 VMS에 로그인할 때 시작된다. |
| 2 | 회원은 VMS에서 ID와 Password를 입력하고 로그인 버튼을 클릭한다. |
| 3 | VMS에 등록된 회원인지 체크해보고 등록된 회원이라면 로그인에 성공한다. |
| 4 | 이 Use case는 로그인이 성공하면 끝난다. |

---

#### EXTENSION SCENARIOS
| Step | Branching Action |
|------|------------------|
| 3 | 3a. 아이디나 비밀번호가 잘못되어 로그인에 실패한다. |
|    | …3a1. 아이디나 비밀번호가 잘못되었다는 메시지를 보여준다. |
|    | …3a2. 아이디와 비밀번호를 입력하는 단계로 돌아간다. (Use case #2-2) |

---

#### RELATED INFORMATION
- **Performance**: ≤ 2 seconds  
- **Frequency**: 회원당 하루에 평균 2번  
- **Concurrency**: 제한 없음  
- **Due Date**: 2025.11.03.

### 우리가 해야할 기능

## 회원 관리
- 회원가입
- 소셜 로그인
- 로그아웃
- 회원 탈퇴
- 프로필 조회
- 프로필 수정
- 알림 내역 조회
- vscode.dev열기기

## 스터디 관리
- 스터디 생성
- 스터디 삭제
- 스터디 목록 조회
- 스터디 상세 조회
- 스터디 참여 신청
- 스터디 참여 승인/거절
- 스터디 탈퇴
- 스터디 강퇴
- 스터디 일정 등록/수정/삭제
- 스터디 실시간 채팅
- 스터디 화상채팅방 생성
- 스터디 화상채팅방 종료
- 스터디 화상채팅방 입장
- 스터디 화상채팅방 퇴장

## 알림
- 알림 내역 조회

## 게시판
- 게시글 작성
- 게시글 수정
- 게시글 삭제 (작성자, 관리자)
- 게시글 목록 조회
- 게시글 상세 조회
- 댓글 작성
- 댓글 수정
- 댓글 삭제
- QnA 게시글 작성
- QnA 게시글 답변 등록

## 오픈소스 이슈 관리
- Good First Issue 이슈 목록 조회
- 이슈 북마크 저장
- 이슈 북마크 삭제
- 키워드 검색
- OSS 이슈 이동
- vscode.dev 열기

## 기여도 및 도전과제
- 도전과제 진행 상태 조회
- 도전과제 완료
- 오픈소스 기여 배지 획득/조회
- 오픈소스 기여도 랭킹 확인
- OSS 뉴스 목록 조회
- OSS 뉴스 페이지로 이동

---
# 여기서부터 명세서 작성

## 회원 관리
### **Use case #1 : 회원가입**

#### GENERAL CHARACTERISTICS
- **Summary**  
  신규 사용자가 깃라잡이를 이용하기 위해 깃허브 계정을 통해 회원으로 등록하는 기능

- **Scope**  
  깃라잡이

- **Level**  
  User level  

- **Author**  
  박솔

- **Last Update**  
  2025. 10. 16

- **Status**  
  Design

- **Primary Actor**  
  User

- **Preconditions**  
  깃라잡이에 접속한 상태여야 한다.
  로그인을 하지 않은 상태여야 한다.

- **Trigger**  
  사용자가 "회원가입" 버튼을 클릭했을 때 프로세스가 시작된다.

- **Success Post Condition**  
  GitHub 계정 정보가 OAuth를 통해 인증되고, 데이터베이스에 신규 사용자로 등록된다.
  이후 닉네임·선호분야·언어 설정 완료 후 프로필이 자동 생성된다.
  시스템은 회원가입 완료 메시지를 표시하고 메인페이지로 이동시킨다.

- **Failed Post Condition**  
  GitHub 인증에 실패하거나 사용자 데이터 저장 중 오류가 발생한 경우, 회원가입이 완료되지 않으며 오류 메시지가 출력된다.
  오류 유형에 따라 재시도 버튼 또는 관리자 문의 안내가 제공된다.


#### MAIN SUCCESS SCENARIO
| Step | Action                                       |
| ---- | -------------------------------------------- |
| S    | 사용자가 메인 페이지 또는 로그인 화면에서 "회원가입" 버튼을 클릭한다.                           |
| 1    | 시스템은 GitHub OAuth 인증 페이지로 리다이렉트 한다.                   |
| 2    | 사용자는 GitHub 계정으로 로그인 하고 깃라잡이 앱의 접근 권한을 승인한다.          |
| 3    | GitHub는 authorization code를 깃라잡이 서버로 전달한다. |
| 4    | 시스템은 code를 이용해 access token을 발급받는다.     |
| 5    | 시스템은 GitHub API를 통해 사용자 프로필 정보를 가져온다.         |
| 6    | 시스템은 해당 GitHub ID가 기존 사용자 계정과 일치하는지 확인한다.     |
| 7    | 등록된 계정이 없다면, 추가 정보 입력 페이지(닉네임, 기술 스택, 선호 언어)를 표시한다.        |
| 8    |사용자가 정보를 입력하고 “저장” 버튼을 클릭한다.             |
| 9    | 시스템은 입력받은 정보와 GitHub 정보를 통합하여 데이터베이스에 신규 사용자로 등록한다.                             |
| 10   | 프로필 생성이 완료되면, "회원가입이 완료되었습니다" 메시지를 표시하고 메인 페이지로 이동한다 |
| 11   | 프로세스가 종료된다.                         |


#### EXTENSION SCENARIOS
| Step | Branching Action |
|------|------------------|
|  2a  | 사용자가 GitHub 로그인 중 권한 승인을 거부하면, 회원가입이 중단되고 로그인 페이지로 복귀한다. |
|  4a  | GitHub 서버에서 토큰 발급이 실패할 경우, “인증 오류가 발생했습니다. 잠시 후 다시 시도해주세요.” 메시지를 표시한다. |
| 5a   | GitHub API 호출 실패 시, “사용자 정보를 불러오지 못했습니다.” 오류가 표시되고 재시도 버튼이 활성화된다. |
| 6a   | 이미 동일한 GitHub ID로 등록된 계정이 존재하면, “이미 가입된 계정입니다.” 메시지와 함께 로그인 페이지로 안내된다.|
|  8a  | 추가 정보 입력 시 필수 필드 누락 또는 형식 오류 발생 시, 입력값 검증 오류 메시지를 표시하고 재입력을 유도한다. |
|  9a  | 데이터베이스 저장 중 예외 발생 시, “회원가입 중 오류가 발생했습니다.” 메시지를 표시하고 프로세스를 종료한다. |



#### RELATED INFORMATION
- **Performance**: 회원가입 전체 과정은 평균 5초 이내에 완료되어야 하며, OAuth 인증 과정은 3초 이내 처리된다.
- **Frequency**: 신규 사용자 최초 1회 실행. (GitHub 계정으로만 가입 가능)
- **Concurrency**: 최대 500명 동시 가입 처리 가능하도록 서버 및 API 스케일링 설정.
- **Due Date**: 2025. 11. 01 (예정)

### **Use case # : 로그인**

  

#### GENERAL CHARACTERISTICS

- **Summary**    
  기존 사용자가 GitHub 계정을 통해 깃라잡이에 로그인하여 개인화된 서비스를 이용하는 기능이다.

- **Scope**  
  깃라잡이

  

- **Level**  
  User level  

  

- **Author**  
  박솔

  

- **Last Update**  
  2025. 10. 16

  

- **Status**  
  Design

  

- **Primary Actor**  
  User

  

- **Preconditions**  
  - 깃라잡이에 접속한 상탱여야 한다.
  - 사용자가 GitHub 계정을 보유하고 있어야 한다.
  - 깃라잡이에 해당 GitHub 계정으로 이미 회원가입되어 있어야 한다.
  

- **Trigger**  
  사용자가 “GitHub로 로그인” 버튼을 클릭했을 때 프로세스가 시작된다.
  
- **Success Post Condition**  
  - GitHub OAuth 인증을 통해 사용자가 본인임이 검증된다.
  - 사용자는 메인 페이지 (또는 이전에 요청했던 보호 페이지)로 이동한다.
  
- **Failed Post Condition** 
  - GitHub 인증에 실패하거나, 등록되지 않은 계정일 경우 로그인에 실패한다.
  - 오류 메시지와 함께 재시도 또는 회원가입 안내 메시지가 표시된다.
  
#### MAIN SUCCESS SCENARIO

| Step | Action |
| ---- | ------ |
| S    |   사용자가 “GitHub로 로그인” 버튼을 클릭한다.     |
| 1    |  시스템은 GitHub OAuth 인증 페이지로 리다이렉트한다.      |
| 2    |   사용자는 GitHub 계정으로 로그인하고, 깃라잡이 앱의 접근 권한을 승인한다.     |
| 3    |    GitHub는 authorization code를 깃라잡이 서버로 전달한다.    |
| 4    |   시스템은 code를 이용해 access token을 발급받는다.     |
| 5    |   시스템은 GitHub API를 통해 사용자 정보를 가져온다.     |
| 6    |   시스템은 해당 GitHub ID가 내부 데이터베이스에 등록되어 있는지 확인한다.     |
| 7    |     등록된 계정이면 사용자 세션 또는 JWT 토큰을 생성한다.   |
| 8    |    시스템은 로그인 성공 메시지를 표시하고 메인 페이지로 이동한다.    |
| 9    |   로그인 프로세스가 완료된다.     |

  
  

#### EXTENSION SCENARIOS

| Step | Branching Action |
| ---- | ---------------- |
|  2a    |     사용자가 GitHub 로그인 중 권한 승인을 거부하면 로그인 프로세스가 중단되고, 로그인 페이지로 복귀한다.             |
|   4a   |        GitHub 서버에서 토큰 발급이 실패하면 “인증 오류가 발생했습니다. 잠시 후 다시 시도해주세요.” 메시지가 표시된다.          |
|  5a   | GitHub API 호출 실패 시, “사용자 정보를 불러오지 못했습니다.” 오류 메시지가 표시된다.  |
|  6a   | 데이터베이스에 해당 GitHub ID가 존재하지 않으면 “등록되지 않은 계정입니다. 회원가입을 진행해주세요.” 메시지를 표시한다.  |
|  7a   | 세션 생성 중 서버 오류가 발생하면 “로그인 중 문제가 발생했습니다.” 메시지와 함께 재시도 버튼이 활성화된다.  |
|  8a   | 로그인 성공 후 원래 접근하려던 보호 페이지가 있었다면, 메인 페이지 대신 해당 페이지로 이동한다.  |
  
  
  

#### RELATED INFORMATION

- **Performance**:
  - GitHub 인증 포함 전체 로그인 프로세스는 평균 3초 이내에 완료되어야 한다.
  - 세션 또는 JWT 발급은 1초 이내에 처리되어야 한다.

- **Frequency**:용자는 필요 시 여러 번 로그인할 수 있다.
(자동 로그인 설정 시 로그인 빈도는 감소함)

- **Concurrency**:최대 5,000명 동시 로그인 요청을 처리할 수 있어야 한다.

- **Due Date**: 2025. 11. 01 (예정)




### **Use case # : 로그아웃**

  

#### GENERAL CHARACTERISTICS

- **Summary**    
  로그인한 사용자가 깃라잡이에서 자신의 세션을 종료하여 로그아웃하는 기능이다.

- **Scope**  
  깃라잡이

  

- **Level**  
  User level  

  

- **Author**  
  박솔

  

- **Last Update**  
  2025. 10. 16

  

- **Status**  
  Design

  

- **Primary Actor**  
  User

  

- **Preconditions**  
 - 사용자는 깃라잡이에 로그인된 상태여야 한다.
 - 활성화된 세션 또는 JWT토큰이 존재해야 한다.
  

- **Trigger**  
  사용자가 “로그아웃” 버튼을 클릭했을 때 프로세스가 시작된다.
  
- **Success Post Condition**  
  - 로그아웃 완료 후 로그인 페이지 또는 메인 랜딩 페이지로 이동한다.
  - 이후 사용자는 인증이 필요한 페이지에 접근할 수 없다.
  
- **Failed Post Condition** 
  - 네트워크 오류나 서버 내부 오류로 인해 세션 종료가 정상적으로 처리되지 않을 수 있다.
  - 오류 메시지가 표시되고, 사용자는 재시도할 수 있다.
  
#### MAIN SUCCESS SCENARIO

| Step | Action                            |
| ---- | --------------------------------- |
| S    | 사용자가 상단 메뉴 또는 프로필 메뉴에서 “로그아웃” 버튼을 클릭한다.    |
| 1    | 시스템은 사용자에게 “정말 로그아웃하시겠습니까?” 확인 팝업을 표시한다.         |
| 2    | 사용자가 확인을 선택하면 로그아웃 요청이 서버로 전송된다.            |
| 3    | 시스템은 서버 세션(또는 리프레시 토큰)을 무효화한다.. |
| 4    | 클라이언트 측(LocalStorage/Cookie)에 저장된 인증 정보를 삭제한다.           |
| 5    | 시스템은 “정상적으로 로그아웃되었습니다.” 메시지를 표시한다.   |
| 6    | 사용자를 로그인 페이지 또는 메인 랜딩 페이지로 리다이렉트한다.  |
| 7    | 로그아웃 프로세스가 완료된다.  |

  
  

#### EXTENSION SCENARIOS

| Step | Branching Action |
| ---- | ---------------- |
|  2a    |   사용자가 “취소”를 선택하면 로그아웃 요청이 취소되고, 현재 페이지에 머무른다.               |
|  3a    | 서버 연결 실패 또는 타임아웃 발생 시, “로그아웃 중 오류가 발생했습니다.” 메시지를 표시하고 재시도 버튼을 제공한다.                 |
|  4a    | 클라이언트 저장소 삭제 실패 시(브라우저 보안 제한 등), 사용자에게 수동 로그아웃 안내를 표시한다.   |
|  6a    | 다중 기기 세션이 존재할 경우, “모든 기기에서 로그아웃” 옵션을 추가로 제공한다.   |
  
  
  

#### RELATED INFORMATION

- **Performance**: 로그아웃 요청 처리 및 리다이렉트는 평균 1초 이내에 완료되어야 한다.

- **Frequency**:
  - 일반적으로 사용자는 세션 종료 시 1회 수행한다.
  - 자동 로그아웃(세션 만료)은 시스템에 의해 주기적으로 발생할 수 있다.
  

- **Concurrency**: 동시 로그아웃 요청 2,000 RPS까지 안정적으로 처리 가능해야 한다.

- **Due Date**: 2025. 11. 01 (예정)



### **Use case # : 회원 탈퇴**

  

#### GENERAL CHARACTERISTICS

- **Summary**    
  - 사용자가 깃라잡이에서 자신의 계정을 영구적으로 삭제하는 기능이다. 사용자는 탈퇴 시 모든 개인 데이터가 삭제되며, 이후 동일한 GitHub 계정으로 재가입이 필요하다.

- **Scope**  
  깃라잡이

  

- **Level**  
  User level  

  

- **Author**  
  박솔

  

- **Last Update**  
  2025. 10. 16

  

- **Status**  
  Design

  

- **Primary Actor**  
  User

  

- **Preconditions**  
  - 사용자가 깃라잡이에 로그인된 상태여야 한다.
  - 사용자 계정이 활성 상태여야 한다.
  - 탈퇴 시 삭제되는 데이터(프로필, 알림, 활동, 로그 등)에 대한 안내가 사전에 제공 되어야 한다.
  

- **Trigger**  
  사용자가 "회원 탈퇴" 버튼을 클릭할 때 프로세스가 시작된다.
  
- **Success Post Condition**  
  - 사용자의 계정 정보가 데이터베이스에서 삭제(또는 비활성화) 된다.
  - 로그인 세션 및 인증 토큰이 즉시 만료된다.
  - 탙퇴 완료 메시지가 표시되고 메인 페이지(비로그인 상태)로 이동한다.
  
- **Failed Post Condition** 
  - 서버 오류 또는 데이터 삭제 중 문제 발생 시, 탈퇴가 완료되지 않고 오류 메시지가 표시된다.
  - 시스템은 오류 로그를 남기고 사용자는 재시도할 수 있다.
  
#### MAIN SUCCESS SCENARIO

| Step | Action                                     |
| ---- | ------------------------------------------ |
| S    | 사용자가 마이페이지 또는 설정 화면에서 “회원 탈퇴” 버튼을 클릭한다.                    |
| 1    | 시스템은 “정말로 탈퇴하시겠습니까? 탈퇴 시 모든 데이터가 삭제됩니다.”라는 확인 팝업을 표시한다.                   |
| 2    | 사용자가 “예”를 클릭하여 탈퇴를 확정한다.         |
| 3    | 시스템은 사용자의 본인 인증(GitHub OAuth 재확인 또는 비밀번호 입력)을 요청한다.  |
| 4    | 사용자가 인증을 완료하면, 시스템은 관련 데이터 삭제 프로세스를 시작한다. |
| 5    | 시스템은 사용자의 프로필, 알림, 즐겨찾기, 활동 기록 등을 삭제하거나 익명화한다.|
| 6    | 시스템은 세션 및 JWT 토큰을 무효화한다.                     |
| 7    | 데이터베이스에서 사용자의 계정 상태를 “Deleted”로 변경하거나 완전 삭제한다.                   |
| 8    | 시스템은 “회원 탈퇴가 완료되었습니다.” 메시지를 표시한다.  |
| 9    | 사용자는 로그인 페이지 또는 메인 랜딩 페이지로 리다이렉트된다.  |
| 10    | 회원 탈퇴 프로세스가 종료된다.  |


  
  

#### EXTENSION SCENARIOS

| Step | Branching Action |
| ---- | ---------------- |
|  1a    |   사용자가 “아니오”를 클릭하면 탈퇴 절차가 중단되고 설정 페이지로 복귀한다.              |
|  3a    |  본인 인증 실패 시 “인증에 실패했습니다. 다시 시도해주세요.” 메시지를 표시한다.                |
|  5a    | 일부 데이터(법적 보존 데이터, 로그 등)는 정책상 일정 기간 보관 후 자동 삭제된다.   |
|  6a    | 세션 만료 요청 실패 시, 클라이언트 측에서 강제 로그아웃 처리를 수행한다.   |
|  7a    | DB 접근 오류나 트랜잭션 실패 발생 시 “탈퇴 중 오류가 발생했습니다.” 메시지와 함께 재시도 안내를 표시한다.   |
  
  
  

#### RELATED INFORMATION

- **Performance**:
  - 데이터 삭제 및 세션 종료눈 평균 3초 이내에 완료되어야 한다.
  - 대량 데이터 삭제는 백그라운드(비동기 큐) 처리로 전환 가능하다.

- **Frequency**: 낮음 (일반적으로 사용자당 1회 미만)

- **Concurrency**: 동시 탈퇴 요청 100 RPS까지 안정적으로 처리 가능해야 한다.

- **Due Date**: 2025. 11. 01 (예정)




### **Use case # : 프로필 조회**

  

#### GENERAL CHARACTERISTICS

- **Summary**    
  사용자가 자신의 프로필 정보를 조회하는 기능이다. 사용자는 닉네임, 기술 스택, 선호 언어, 활동 내역 등 기본 정보를 조회할 수 있다.

- **Scope**  
  깃라잡이

  

- **Level**  
  User level  

  

- **Author**  
  박솔

  

- **Last Update**  
  2025. 10. 16

  

- **Status**  
  Design

  

- **Primary Actor**  
  User

  

- **Preconditions**  
  - 사용자는 깃라잡이에 로그인된 상태여야 한다.
  - 사용자의 프로필 데이터가 데이터베이스에 저장되어 있어야 한다.
  

- **Trigger**  
  사용자가 “프로필 보기” 또는 자신의 아바타/닉네임을 클릭했을 때 프로세스가 시작된다.
  
- **Success Post Condition**  
   -  "내 프로필" 페이지로 이동하고, 닉네임, 기술 스택, 선호 언어, OSS기여도등을 보여준다.
   -  사용자가 “프로필 보기” 또는 자신의 아바타/닉네임을 클릭했을 때 프로세스가 시작된다.
   -  사용자는 자신의 프로필 정보를 확인할 수 있다.
  
- **Failed Post Condition** 
  데이터베이스 연결 오류 또는 사용자 정보 불러오기 실패 시, 프로필 페이지가 표시되지 않고 오류 메시지가 나타난다.
  
#### MAIN SUCCESS SCENARIO

| Step | Action                              |
| ---- | ----------------------------------- |
| S    | 용자가 상단 메뉴에서 자신의 아바타 또는 닉네임을 클릭한다.                |
| 1    | 시스템은 사용자의 인증 토큰을 확인하여 로그인 상태를 검증한다.            |
| 2    | 시스템은 사용자 ID를 기반으로 데이터베이스에서 프로필 데이터를 조회한다. |
| 3    | 조회된 정보에는 닉네임, GitHub 아이디, 기술 스택, 선호 언어, 활동 내역 등이 포함된다.                  |
| 4    | 시스템은 조회한 정보를 프로필 화면(UI)에 표시한다.            |
| 5    | 사용자는 프로필 페이지에서 자신의 기본 정보를 확인한다.  |
| 6    | 사용자가 프로필 페이지에서 “수정하기” 버튼을 클릭하면, 프로필 수정(Use case #6)으로 연결된다.  |
| 7    | 프로필 조회 프로세스가 종료된다.  |

  
  

#### EXTENSION SCENARIOS

| Step | Branching Action |
| ---- | ---------------- |
|  1a    |   인증 토큰이 만료되었을 경우, “로그인이 필요합니다.” 메시지를 표시하고 로그인 페이지로 이동한다.               |
|  2a    |  데이터베이스 접근 실패 시, “프로필 정보를 불러올 수 없습니다.” 메시지를 표시하고 재시도 버튼을 활성화한다.                |
|  3a    | 일부 필드 값이 누락된 경우, 시스템은 기본값(예: “미입력”)을 표시한다.  |
|  4a    | 네트워크 지연으로 정보 표시가 늦어질 경우, 로딩 스피너를 표시한다.  |

  
  
  

#### RELATED INFORMATION

- **Performance**: 프로필 데이터 로딩은 평균 1초 이내에 완료되어야 한다.

- **Frequency**: 사용자는 로그인 후 프로필 페이지를 자주 조회할 수 있다.

- **Concurrency**: 2,000명의 사용자가 동시에 프로필을 조회해도 서버 성능이 저하되지 않아야 한다.

- **Due Date**: 2025. 11. 01 (예정)



### **Use case # : 프로필 수정**

  

#### GENERAL CHARACTERISTICS

- **Summary**    
  로그인한 사용자가 자신의 프로필(닉네임, 소개, 기술 스택, 선호 언어, 프로필 이미지 등)을 수정하는 기능이다.

- **Scope**  
  깃라잡이

  

- **Level**  
  User level  

  

- **Author**  
  박솔

  

- **Last Update**  
  2025. 10. 16

  

- **Status**  
  Design

  

- **Primary Actor**  
  User

  

- **Preconditions**  
  - 사용자는 깃라잡이에 로그인된 상태여야 한다.
  - 사용자 프로필 데이터가 DB에 존재해야 한다.
  - (이미지 업로드 시) 허용 확장자/용량/가로세로 제한 정책이 사전에 정의돼 있어야 한다. -> 선택사항
  

- **Trigger**  
  사용자가 "프로필 수정" 버튼을 클릭할 때, 프로세스가 시작된다.
  
- **Success Post Condition**  
  - 수정된 프로필 정보가 DB에 저장되고 화면에 즉시 반영된다.
  - 최근 수정 시각이 갱신된다.
  
- **Failed Post Condition** 
  - 검증 실패, 업로드 실패, DB 오류 등으로 저장이 되지 않으며 오류 메시지가 표시된다.
  - 기존 프로필 값은 유지된다.
  
#### MAIN SUCCESS SCENARIO

| Step | Action                                             |
| ---- | -------------------------------------------------- |
| S    | 사용자가 프로필 페이지에서 “프로필 수정” 버튼을 클릭한다.                            |
| 1    | 시스템은 현재 저장된 프로필 값을 로드하여 수정 폼(닉네임, 소개, 기술 스택, 선호 언어, 프로필 이미지)을 표시한다.                       |
| 2    | 사용자가 하나 이상의 필드를 수정하고 “저장”을 클릭한다.           |
| 3    | 시스템(프론트)은 필수/형식 검증을 수행한다(닉네임 길이·금칙어, 소개 글자수, 기술 스택 선택 수, 파일 용량/확장자 등).   |
| 4    | 시스템(백엔드)은 서버 측 재검증을 수행하고, 동시 수정 방지(버전/etag) 확인을 한다.|
| 5    | 이미지가 포함된 경우, 시스템은 파일을 스토리지에 업로드하고 URL을 발급한다.  |
| 6    | 시스템은 변경 필드만 선택적으로 업데이트하는 트랜잭션을 수행한다. |
| 7    | 시스템은 성공 메시지를 표시하고, 최신 프로필 데이터를 다시 조회하여 화면에 반영한다.  |
| 8    | 프로필 수정 이력이 감사 로그에 기록된다.  |
| 9    | 프로세스가 종료된다. |

  
  

#### EXTENSION SCENARIOS

| Step | Branching Action |
| ---- | ---------------- |
| 3a     | 검증 실패(닉네임 중복/금칙어, 글자수 초과, 기술 스택 최대 개수 초과 등) → 필드별 에러 메시지와 포커스 이동 후 재입력 유도. |
| 4a     | 인증 토큰 만료 → “로그인이 필요합니다” 메시지와 함께 로그인 페이지로 이동. |
| 4b   | 동시 수정 충돌(낙관적 락/버전 불일치) → “다른 세션에서 수정되었습니다” 안내, 최신값 불러오기 후 병합/다시 저장 유도. |
| 5a   | 이미지 업로드 실패(용량 초과, 형식 불가, 네트워크 오류) → 실패 사유 표시, 이전 이미지 유지, 재시도 버튼 제공.  |
| 6a   | DB 트랜잭션 실패/타임아웃 → “저장 중 오류가 발생했습니다” 메시지와 재시도 버튼 제공. |
| 7a   | 캐시 미갱신/지연 → 강제 새로고침 안내 또는 최신 데이터 재조회 처리. |
| 7b   | 일부 필드만 저장됨(부분 실패) → 롤백 정책에 따라 전체 취소 후 사용자에게 재시도 안내. |
  
  
  

#### RELATED INFORMATION

- **Performance**: 저장 API p95 ≤ 500ms(이미지 업로드 제외), 이미지 업로드 포함 p95 ≤ 2.5s.

- **Frequency**: 사용자는 필요 시 수시로 수정할 수 있다(일 0~3회 예상).

- **Concurrency**: 동시 수정 요청 500 RPS 처리 가능(낙관적 락/버전 관리).

- **Due Date**: 2025. 11. 01 (예정)


  ### **Use case # : 알림내역 조회**

  

#### GENERAL CHARACTERISTICS

- **Summary**    
  사용자가 GitHub 계정을 통해 깃라잡이에 로그인하는 기능이다.

- **Scope**  
  깃라잡이

  

- **Level**  
  User level  

  

- **Author**  
  박솔

  

- **Last Update**  
  2025. 10. 16

  

- **Status**  
  Design

  

- **Primary Actor**  
  User

  

- **Preconditions**  
  사용자가 깃라잡이에 계정이 등록되어 있어야 한다.
  

- **Trigger**  
  사용자가 "GitHub로 로그인" 버튼을 클릭할 때.
  
- **Success Post Condition**  
  사용자를 메인 페이지로 이동시킨다.
  
- **Failed Post Condition** 
  로그인되지 않고 오류 메시지가 표시된다.
  
#### MAIN SUCCESS SCENARIO

| Step | Action |
| ---- | ------ |
| S    |        |
| 1    |        |
| 2    |        |
| 3    |        |
| 4    |        |
| 5    |        |
| 6    |        |
| 7    |        |
| 8    |        |
| 9    |        |

  
  

#### EXTENSION SCENARIOS

| Step | Branching Action |
| ---- | ---------------- |
|      |                  |
|      |                  |
  
  
  

#### RELATED INFORMATION

- **Performance**:

- **Frequency**:

- **Concurrency**:

- **Due Date**:



#### RELATED INFORMATION
- **Performance**: 
- **Frequency**: 
- **Concurrency**: 
- **Due Date**:

  ### **Use case # : vscode.dev 열기 **

  

#### GENERAL CHARACTERISTICS

- **Summary**    
  사용자가 GitHub 계정을 통해 깃라잡이에 로그인하는 기능이다.

- **Scope**  
  깃라잡이

  

- **Level**  
  User level  

  

- **Author**  
  박솔

  

- **Last Update**  
  2025. 10. 16

  

- **Status**  
  Design

  

- **Primary Actor**  
  User

  

- **Preconditions**  
  사용자가 깃라잡이에 계정이 등록되어 있어야 한다.
  

- **Trigger**  
  사용자가 "GitHub로 로그인" 버튼을 클릭할 때.
  
- **Success Post Condition**  
  사용자를 메인 페이지로 이동시킨다.
  
- **Failed Post Condition** 
  로그인되지 않고 오류 메시지가 표시된다.
  
#### MAIN SUCCESS SCENARIO

| Step | Action |
| ---- | ------ |
| S    |        |
| 1    |        |
| 2    |        |
| 3    |        |
| 4    |        |
| 5    |        |
| 6    |        |
| 7    |        |
| 8    |        |
| 9    |        |

  
  

#### EXTENSION SCENARIOS

| Step | Branching Action |
| ---- | ---------------- |
|      |                  |
|      |                  |
  
  
  

#### RELATED INFORMATION

- **Performance**:

- **Frequency**:

- **Concurrency**:

- **Due Date**:



#### RELATED INFORMATION
- **Performance**: 
- **Frequency**: 
- **Concurrency**: 
- **Due Date**: 

---
## 스터디 관리
### **Use case # : 스터디 생성**

  

#### GENERAL CHARACTERISTICS

- **Summary**    
  사용자가 오픈소스 학습이나 프로젝트 협업을 위한 스터디를 새로 개설하는 기능이다.  

- **Scope**  
  깃라잡이

  

- **Level**  
  User level  

  

- **Author**  
  정동현

  

- **Last Update**  
  2025. 10. 12

  

- **Status**  
  Design

  

- **Primary Actor**  
  User

  

- **Preconditions**  
  사용자는 깃라잡이에 로그인한 상태여야 한다.
  

- **Trigger**  
  사용자가 “스터디 생성” 버튼을 클릭하여 새 스터디를 개설하려고 할 때
  
- **Success Post Condition**  
  새 스터디가 데이터베이스에 성공적으로 등록된다.
  스터디를 개설한 유저는 "스터디장"권한을 얻는다.
  생성된 스터디는 스터디 목록 및 상세 페이지에서 조회 가능해진다.
  스터디 생성 완료 메시지가 표시되며, 스터디 홈 화면으로 이동한다.
  
- **Failed Post Condition** 
  필수 입력 항목 누락, 중복된 스터디명, 서버 오류 등의 이유로 스터디 생성이 완료되지 않는다.
  오류 메시지가 표시되고, 사용자는 수정 후 다시 시도할 수 있다.
  
#### MAIN SUCCESS SCENARIO

| Step | Action                                       |
| ---- | -------------------------------------------- |
| S    | 사용자가 "스터디 생성" 버튼을 클릭한다.                      |
| 1    | 사용자가 스터디 생성 버튼을 클릭할 때 시작된다.                  |
| 2    | 사용자는 스터디명, 주제, 소개, 모집 인원, 기술 스택 등의 정보를 입력한다. |
| 3    | 사용자가 저장 버튼을 누르면 시스템은 입력값을 검증한다.              |
| 4    | 검증이 완료되면 시스템은 새로운 스터디 정보를 데이터베이스에 저장한다.      |
| 5    | 스터디 생성 완료 메시지를 표시하고 스터디 상세 페이지로 이동한다.        |
| 6    | 스터디가 성공적으로 생성되면 종료된다.                        |

  
  

#### EXTENSION SCENARIOS

| Step | Branching Action |
| ---- | ---------------- |
|      |                  |
|      |                  |
  
  
  

#### RELATED INFORMATION

- **Performance**:

- **Frequency**:

- **Concurrency**:

- **Due Date**:



#### RELATED INFORMATION
- **Performance**: 
- **Frequency**: 
- **Concurrency**: 
- **Due Date**: 

### **Use case # : 스터디 수정**

  

#### GENERAL CHARACTERISTICS

- **Summary**    
  스터디장이 스터디의 정보, 상태를 변경할 수 있는 기능이다.

- **Scope**  
  깃라잡이

  

- **Level**  
  User level  

  

- **Author**  
  정동현

  

- **Last Update**  
  2025. 10. 14

  

- **Status**  
  Design

  

- **Primary Actor**  
  User

  

- **Preconditions**  
  사용자가 스터디장 권한을 가지고 있어야 한다
  

- **Trigger**  
  스터디장이 스터디 상세화면에서 "정보 수정"을 클릭할 때.
  
- **Success Post Condition**  
  수정된 스터디 정보가 데이터베이스에 저장되고, 최신 내용이 화면에 반영된다.
  
- **Failed Post Condition** 
  수정이 반영되지 않고 오류 메시지가 표시된다.
  
#### MAIN SUCCESS SCENARIO

| Step | Action                                           |
| ---- | ------------------------------------------------ |
| S    | 스터디장이 스터디 상세 페이지에서 "정보 수정" 버튼을 클릭한다.             |
| 1    | 스터디장이 수정 버튼을 클릭할 때 시작된다.                         |
| 2    | 시스템은 스터디장 권한을 확인하고, 기존 스터디 정보를 수정 가능한 입력창에 표시한다. |
| 3    | 스터디장은 스터디명, 설명, 모집 구분 등의 항목을 수정한다.               |
| 4    | 스터디장이 저장 버튼을 클릭하면 시스템은 입력값의 유효성을 검사한다.           |
| 5    | 검증이 완료되면 시스템은 변경된 정보를 데이터베이스에 반영한다.              |
| 6    | 수정 완료 메시지를 표시하고 갱신된 스터디 상세 페이지를 보여준다.            |
| 7    | 수정이 성공적으로 완료되면 종료된다.                             |

  
  

#### EXTENSION SCENARIOS

| Step | Branching Action |
| ---- | ---------------- |
|      |                  |
|      |                  |
  
  
  

#### RELATED INFORMATION

- **Performance**:

- **Frequency**:

- **Concurrency**:

- **Due Date**:



#### RELATED INFORMATION
- **Performance**: 
- **Frequency**: 
- **Concurrency**: 
- **Due Date**: 


### **Use case # : 스터디 삭제**

  

#### GENERAL CHARACTERISTICS

- **Summary**    
  스터디장이 자신이 개설한 스터디를 삭제하는 기능이다.

- **Scope**  
  깃라잡이

  

- **Level**  
  User level  

  

- **Author**  
  정동현

  

- **Last Update**  
  2025. 10. 13

  

- **Status**  
  Design

  

- **Primary Actor**  
  User

  

- **Preconditions**  
  스터디장 권한을 가지고 있어야 한다.
  

- **Trigger**  
  스터디장이 "스터디 삭제"버튼을 클릭한 경우
  
- **Success Post Condition**  
  스터디와 관련된 모든 정보가 삭제되고, 스터디 목록 화면으로 이동한다.
  
- **Failed Post Condition** 
  권한이 없거나 오류가 발생하면 삭제되지 않고 오류 메시지가 표시된다.
  
#### MAIN SUCCESS SCENARIO

| Step | Action                                         |
| ---- | ---------------------------------------------- |
| S    | 스터디장이 스터디 상세 페이지에서 "스터디 삭제" 버튼을 클릭한다.          |
| 1    | 사용자가 스터디 삭제 버튼을 클릭할 때 시작된다.                    |
| 2    | 시스템은 스터디장 권한을 확인하고, 삭제 여부를 묻는 확인창을 표시한다.       |
| 3    | 사용자가 삭제를 확정하면 시스템은 해당 스터디의 데이터를 데이터베이스에서 삭제한다. |
| 4    | 시스템은 삭제 완료 메시지를 표시하고 스터디 목록 페이지로 이동시킨다.        |
| 5    | 스터디가 성공적으로 삭제되면 종료된다.                          |

  
  

#### EXTENSION SCENARIOS

| Step | Branching Action |
| ---- | ---------------- |
|      |                  |
|      |                  |
  
  
  

#### RELATED INFORMATION

- **Performance**:

- **Frequency**:

- **Concurrency**:

- **Due Date**:



#### RELATED INFORMATION
- **Performance**: 
- **Frequency**: 
- **Concurrency**: 
- **Due Date**: 

### **Use case # : 스터디 목록 조회**

  

#### GENERAL CHARACTERISTICS

- **Summary**    
  사용자가 개설된 모든 스터디의 목록을 조회하는 기능이다.

- **Scope**  
  깃라잡이

  

- **Level**  
  User level  

  

- **Author**  
  정동현

  

- **Last Update**  
  2025. 10. 13

  

- **Status**  
  Design

  

- **Primary Actor**  
  User

  

- **Preconditions**  
  사용자가 로그인 상태여야 한다.
  

- **Trigger**  
  사용자가 "스터디 찾기"버튼을 누를 경우
  
- **Success Post Condition**  
  등록된 스터디 목록을 화면에 출력한다.
  
- **Failed Post Condition** 
  서버 오류나 네트워크 장애로 목록을 불러오지 못하면 오류 메시지를 표시한다.
  
#### MAIN SUCCESS SCENARIO

| Step | Action                                |
| ---- | ------------------------------------- |
| S    | 사용자가 "스터디 조회"를 클릭한다.                  |
| 1    | 사용자가 스터디 목록 페이지에 접근할 때 시작된다.          |
| 2    | 시스템은 데이터베이스에서 등록된 모든 스터디 정보를 조회한다.    |
| 3    | 조회된 스터디 목록을 이름과 주제로 정렬하여 화면에 표시한다.    |
| 4    | 사용자는 관심 있는 스터디를 선택하여 상세 조회로 이동할 수 있다. |
| 5    | 스터디 목록이 성공적으로 표시되면 종료된다.              |

  
  

#### EXTENSION SCENARIOS

| Step | Branching Action |
| ---- | ---------------- |
|      |                  |
|      |                  |
  
  
  

#### RELATED INFORMATION

- **Performance**:

- **Frequency**:

- **Concurrency**:

- **Due Date**:



#### RELATED INFORMATION
- **Performance**: 
- **Frequency**: 
- **Concurrency**: 
- **Due Date**: 

### **Use case # : 스터디 상세 조회**

  

#### GENERAL CHARACTERISTICS

- **Summary**    
  사용자가 선택한 스터디의 상세 정보를 조회하는 기능이다.

- **Scope**  
  깃라잡이

  

- **Level**  
  User level  

  

- **Author**  
  정동현

  

- **Last Update**  
  2025. 10. 13

  

- **Status**  
  Design

  

- **Primary Actor**  
  User

  

- **Preconditions**  
  사용자가 로그인 상태여야 한다.
  

- **Trigger**  
  사용자가 스터디 목록에서 특정 스터디를 클릭할 때.
  
- **Success Post Condition**  
  시스템이 해당 스터디의 상세 정보를 화면에 표시한다.
  
- **Failed Post Condition** 
  스터디 정보가 존재하지 않거나 서버 오류가 발생하면 오류 메시지를 표시한다.
  
#### MAIN SUCCESS SCENARIO

| Step | Action                             |
| ---- | ---------------------------------- |
| S    | 사용자가 스터디 목록에서 특정 스터디를 클릭한다.        |
| 1    | 사용자가 스터디를 선택할 때 시작된다.              |
| 2    | 시스템은 해당 스터디의 상세 정보를 데이터베이스에서 조회한다. |
| 3    | 조회된 정보를 화면에 표시한다.                  |
| 4    | 사용자는 표시된 상세 페이지에서 참가 신청을 할 수 있다.   |
| 5    | 스터디 정보가 정상적으로 표시되면 종료된다.           |

  
  

#### EXTENSION SCENARIOS

| Step | Branching Action |
| ---- | ---------------- |
|      |                  |
|      |                  |
  
  
  

#### RELATED INFORMATION

- **Performance**:

- **Frequency**:

- **Concurrency**:

- **Due Date**:



#### RELATED INFORMATION
- **Performance**: 
- **Frequency**: 
- **Concurrency**: 
- **Due Date**:

### **Use case # : 스터디 참여 신청**

  

#### GENERAL CHARACTERISTICS

- **Summary**    
  사용자가 스터디에 참여 신청을 하는 기능이다.

- **Scope**  
  깃라잡이

  

- **Level**  
  User level  

  

- **Author**  
  정동현

  

- **Last Update**  
  2025. 10. 14

  

- **Status**  
  Design

  

- **Primary Actor**  
  User

  

- **Preconditions**  
  사용자는 로그인 상태이며, 해당 스터디의 멤버가 아니어야 한다.
  

- **Trigger**  
  사용자가 스터디 상세 페이지에서 "스터디 참여 신청" 버튼을 클릭할 때.
  
- **Success Post Condition**  
  참여 신청 정보가 스터디장에게 알림으로 전송된다.
  
- **Failed Post Condition** 
  요청이 처리되지 않고 오류 메시지가 표시된다.
  
#### MAIN SUCCESS SCENARIO

| Step | Action                                      |
| ---- | ------------------------------------------- |
| S    | 사용자가 스터디 상세 페이지에서 "스터디 참여 신청" 버튼을 클릭한다.     |
| 1    | 사용자가 가입 신청 버튼을 클릭할 때 시작된다.                  |
| 2    | 시스템은 스터디에 속한 유저인지 확인한다.                     |
| 3    | 시스템은 신청 가능한 상태일 경우 스터디장에게 새 참여 신청 알림을 전송한다. |
| 4    | 사용자에게 "참여 신청이 완료되었습니다." 메시지를 표시한다.          |
| 5    | 신청이 정상적으로 접수되면 종료된다.                        |

  
  

#### EXTENSION SCENARIOS

| Step | Branching Action |
| ---- | ---------------- |
|      |                  |
|      |                  |
  
  
  

#### RELATED INFORMATION

- **Performance**:

- **Frequency**:

- **Concurrency**:

- **Due Date**:



#### RELATED INFORMATION
- **Performance**: 
- **Frequency**: 
- **Concurrency**: 
- **Due Date**: 

### **Use case # : 스터디 참여 승인/거절**

  

#### GENERAL CHARACTERISTICS

- **Summary**    
  스터디장이 참여 신청 목록을 확인하고, 신청자의 스터디 참여를 승인하거나 거절하는 기능이다.

- **Scope**  
  깃라잡이

  

- **Level**  
  User level  

  

- **Author**  
  정동현

  

- **Last Update**  
  2025. 10. 14

  

- **Status**  
  Design

  

- **Primary Actor**  
  User

  

- **Preconditions**  
  스터디장 권한을 가지고 있어야하며, 스터디에 참여 신청이 존재해야 한다.
  

- **Trigger**  
  스터디장이 알림 또는 스터디 상세 페이지에서 신청자 목록을 확인하고 "승인" 또는 "거절" 버튼을 클릭할 때.
  
- **Success Post Condition**  
  승인: 신청자가 스터디 멤버로 등록되고, 승인 알림이 전송된다.
  거절: 신청자에게 거절 알림이 전송된다.
  
- **Failed Post Condition** 
  요청이 처리되지 않고 오류 메시지가 표시된다.
  
#### MAIN SUCCESS SCENARIO

| Step | Action                                  |
| ---- | --------------------------------------- |
| S    | 스터디장이 스터디 참여 신청 목록을 확인한다.               |
| 1    | 스터디장이 스터디 참여 신청 목록에 접근할 때 시작된다.         |
| 2    | 시스템은 스터디장 권한을 확인하고, 대기 중인 신청자 목록을 표시한다. |
| 3    | 스터디장이 신청자 옆의 "승인" 또는 "거절" 버튼을 클릭한다.     |
| 4    | 선택된 동작에 따라 신청자의 상태를 "승인" 또는 "거절"로 변경한다. |
| 5    | 처리 결과를 신청자에게 알림으로 전송한다.                 |
| 6    | 참여 신청에 대한 처리가 완료되면 종료된다.                |

  
  

#### EXTENSION SCENARIOS

| Step | Branching Action |
| ---- | ---------------- |
|      |                  |
|      |                  |
  
  
  

#### RELATED INFORMATION

- **Performance**:

- **Frequency**:

- **Concurrency**:

- **Due Date**:



#### RELATED INFORMATION
- **Performance**: 
- **Frequency**: 
- **Concurrency**: 
- **Due Date**: 

### **Use case # : 스터디 탈퇴**

  

#### GENERAL CHARACTERISTICS

- **Summary**    
  스터디에 속한 사용자가 스터디를 탈퇴하는 기능이다.

- **Scope**  
  깃라잡이

  

- **Level**  
  User level  

  

- **Author**  
  정동현

  

- **Last Update**  
  2025. 10. 14

  

- **Status**  
  Design

  

- **Primary Actor**  
  User

  

- **Preconditions**  
  해당 스터디 멤버에 등록되어 있어야 한다.
  

- **Trigger**  
  사용자가 스터디 상세 페이지에서 "스터디 탈퇴" 버튼을 클릭할 때.
  
- **Success Post Condition**  
  사용자가 스터디 멤버 목록에서 제거된다.
  
- **Failed Post Condition** 
  탈퇴가 정상적으로 처리되지 않고 오류메시지를 표시된다.
  
#### MAIN SUCCESS SCENARIO

| Step | Action                                          |
| ---- | ----------------------------------------------- |
| S    | 사용자가 스터디 상세 페이지에서 "스터디 탈퇴" 버튼을 클릭한다.            |
| 1    | 사용자가 스터디 탈퇴 버튼을 클릭할 때 시작된다.                     |
| 2    | 시스템은 사용자의 로그인 상태와 스터디 멤버 여부를 확인한다.              |
| 3    | 시스템은 사용자에게 탈퇴 여부를 묻는 팝업창을 표시한다.                 |
| 4    | 사용자가 "확인"버튼을 누르면 시스템은 해당 사용자를 스터디 멤버 목록에서 제거한다. |
| 5    | 탈퇴 완료 팝업 메시지를 표시하고 스터디 목록 페이지로 이동시킨다.           |
| 6    | 탈퇴가 성공적으로 처리되면 종료된다.                            |

  
  

#### EXTENSION SCENARIOS

| Step | Branching Action |
| ---- | ---------------- |
|      |                  |
|      |                  |
  
  
  

#### RELATED INFORMATION

- **Performance**:

- **Frequency**:

- **Concurrency**:

- **Due Date**:



#### RELATED INFORMATION
- **Performance**: 
- **Frequency**: 
- **Concurrency**: 
- **Due Date**: 

### **Use case # : 스터디 강퇴**
  


#### GENERAL CHARACTERISTICS

- **Summary**    
  스터디 리더가 특정 스터디 구성원을 강제로 탈퇴시키는 기능

- **Scope**  
  깃라잡이

  

- **Level**  
  User level  

  

- **Author**  
  김동규

  

- **Last Update**  
  2025. 10. 16

  

- **Status**  
  Design

  

- **Primary Actor**  
  User (팀 리더)

  

- **Preconditions**  
  스터디 리더 계정으로 로그인되어 있어야 한다.
  스터디가 존재하고, 강퇴 대상 사용자가 그 스터디의 구성원이어야 한다.
  리더 권한이 존재 해야한다.
  

- **Trigger**  
  사용자(리더)가 스터디 상세 페이지에서 "스터디 강퇴" 버튼을 클릭할 때.
  
- **Success Post Condition**  
  사용자가 스터디 멤버 목록에서 제거된다.
  강퇴된 사용자에게 알림이 전송된다.
  
- **Failed Post Condition** 
  권한이나 서버 오류 발생 시 오류 메시지가 표시된다.
  
#### MAIN SUCCESS SCENARIO

| Step | Action                                          |
| ---- | ----------------------------------------------- |
| S    | 리더가 스터디 구성원 목록에서 특정 사용자의 '강퇴' 버튼을 클릭한다.            |
| 1    | 시스템이 리더의 권한을 검증한다.                     |
| 2    | 시스템은 '정말 강퇴하시겠습니까?' 확인 메시지를 띄운다.              |
| 3    | 리더가 '확인'을 누른다.                 |
| 4    | 시스템은 해당 사용자를 스터디 멤버 목록에서 제거한다.     |
| 5    | 시스템은 강퇴된 사용자에게 알림을 전송한다.           |
| 6    | "구성원이 강퇴되었습니다." 메시지를 표시한다.                            |

  
  

#### EXTENSION SCENARIOS

| Step | Branching Action |
| ---- | ---------------- |
|   1a | 리더가 아닌 사용자가 강퇴 기능을 실행할 경우 "권한이 없습니다."를 출력한다. |
|   2a   | 리더가 '취소'를 선택하면 강퇴 요청이 취소된다.        |
  
  
  

#### RELATED INFORMATION

- **Performance**:

- **Frequency**:

- **Concurrency**:

- **Due Date**:



#### RELATED INFORMATION
- **Performance**: 
- **Frequency**: 
- **Concurrency**: 
- **Due Date**: 

### **Use case # : 스터디 일정 등록**

  

#### GENERAL CHARACTERISTICS

- **Summary**    
  사용자가 참여 중인 스터디의 새로운 일정을 등록하는 기능

- **Scope**  
  깃라잡이

  

- **Level**  
  User level  

  

- **Author**  
  김동규

  

- **Last Update**  
  2025. 10. 16

  

- **Status**  
  Design

  

- **Primary Actor**  
  User (리더 또는 권한자)

  

- **Preconditions**  
  사용자는 로그인 상태여야 한다
  해당 스터디 멤버에 등록되어 있어야 한다.
  일정등록 권한이 있어야 한다
  

- **Trigger**  
  사용자가 "일정 등록" 버튼을 클릭할 때.
  
- **Success Post Condition**  
  입련한 일정 정보가 저장되고 스터디 캘린더에 반영된다.
  등록 완료 메시지가 표시된다.
  
- **Failed Post Condition** 
  필수 입력값 누락시 오류 메시지가 표시된다.
  
#### MAIN SUCCESS SCENARIO

| Step | Action                                          |
| ---- | ----------------------------------------------- |
| S    | 사용자가 스터디 상세 페이지에서 "일정 등록" 버튼을 클릭한다.            |
| 1    | 시스템은 일정 등록 폼(날짜, 시간, 장소, 메모 등)을 표시한다.                    |
| 2    | 사용자는 일정 정보를 입력 후 '저장' 버튼을 클릭한다.
| 3    | 시스템은 입력값을 검증한다.              |
| 4    | 일정 등록이 성공하면 스터디 캘린더에 해당 일정이 표시된다.                 |
| 5    | 시스템은 '일정이 등록되었습니다.' 메시지를 표시한다. |
| 6    | 스터디 멤버들에게 알림이 전송된다.           |                            
  
  

#### EXTENSION SCENARIOS

| Step | Branching Action |
| 2a | 필수 입력 항목이 누락되면 '저장' 버튼이 비활성화된다.|
|      |                  |
|      |                  |
  
  
  

#### RELATED INFORMATION

- **Performance**:

- **Frequency**:

- **Concurrency**:

- **Due Date**:



#### RELATED INFORMATION
- **Performance**: 
- **Frequency**: 
- **Concurrency**: 
- **Due Date**: 

### **Use case # : 스터디 일정 수정**

  

#### GENERAL CHARACTERISTICS

- **Summary**    
  기존에 등록된 스터디 일정을 수정하는 기능이다.

- **Scope**  
  깃라잡이

  

- **Level**  
  User level  

  

- **Author**  
  김동규

  

- **Last Update**  
  2025. 10. 16

  

- **Status**  
  Design

  

- **Primary Actor**  
  User (스터디 리더 또는 권한자)

  

- **Preconditions**  
  사용자는 로그인 상태여야 한다
  수정하려는 일정이 존재해야 하고 권한이 있어야 한다.
  

- **Trigger**  
  사용자가 "수정" 버튼을 클릭할 때.
  
- **Success Post Condition**  
  수정된 일정 정보가 반영된다.
  
- **Failed Post Condition** 
  일정수정이 정상적으로 처리되지 않고 오류메시지가 표시된다.
  
#### MAIN SUCCESS SCENARIO

| Step | Action                                          |
| ---- | ----------------------------------------------- |
| S    | 사용자가 "수정" 버튼을 클릭한다.            |
| 1    | 시스템은 해당 일정의 기존 정보를 수정 폼에 표시한다.                    |
| 2    | 사용자는 변경할 내용을 입력하고 '저장' 버튼을 클릭한다.              |
| 3    | 시스템은 변경 사항을 검증한다.                 |
| 4    | 시스템은 수정된 정보를 업데이트 하고 변경사항을 반영한다. |
| 5    | 시스템이 "일정 수정 완료" 메시지를 표시한다          |
| 6    | 스터디 멤버에게 알림을 전송한다.                            |

  
  

#### EXTENSION SCENARIOS

| Step | Branching Action |
| ---- | ---------------- |
|   2a   |  필수 입력 항목 누락 시 저장이 제한된다.                |
|      |                  |
  
  
  

#### RELATED INFORMATION

- **Performance**:

- **Frequency**:

- **Concurrency**:

- **Due Date**:



#### RELATED INFORMATION
- **Performance**: 
- **Frequency**: 
- **Concurrency**: 
- **Due Date**: 

### **Use case # : 스터디 일정 삭제**

  

#### GENERAL CHARACTERISTICS

- **Summary**    
  기존에 등록된 스터디 일정을 삭제하는 기능이다.

- **Scope**  
  깃라잡이

  

- **Level**  
  User level  

  

- **Author**  
  김동규

  

- **Last Update**  
  2025. 10. 16

  

- **Status**  
  Design

  

- **Primary Actor**  
  User (스터디 리더 또는 권한자)

  

- **Preconditions**  
  사용자는 로그인 상태여야 한다
  수정하려는 일정이 존재해야 하고 권한이 있어야 한다.
  

- **Trigger**  
  사용자가 "삭제" 버튼을 클릭할 때.
  
- **Success Post Condition**  
  일정이 데이터베이스에서 삭제되고 화면에서 즉시 제거된다.
  
- **Failed Post Condition** 
  일정삭제가 정상적으로 처리되지 않고 오류메시지가 표시된다.
  
#### MAIN SUCCESS SCENARIO

| Step | Action                                          |
| ---- | ----------------------------------------------- |
| S    | 사용자가 "삭제" 버튼을 클릭한다.            |
| 1    | 시스템은 "정말 삭제하시겠습니까?" 확인 팝업을 표시한다.                   |
| 2    | 사용자가 삭제를 확정한다.             |
| 3    | 시스템은 해당 일정을 데이터베이스에서 삭제한다.               |
| 4    | 삭제가 완료되면 화면에서 즉시 반영된다. |
| 5    | 시스템이 "일정 삭제 완료" 메시지를 표시한다.         |
| 6    | 스터디 멤버에게 알림을 전송한다.                            |

  
  

#### EXTENSION SCENARIOS

| Step | Branching Action |
| ---- | ---------------- |
|   1a   |  '취소' 선택 시 삭제 요청이 취소된다.             |
|      |                  |
  
  
  

#### RELATED INFORMATION

- **Performance**:

- **Frequency**:

- **Concurrency**:

- **Due Date**:



#### RELATED INFORMATION
- **Performance**: 
- **Frequency**: 
- **Concurrency**: 
- **Due Date**: 

### **Use case # : 스터디 화상채팅방 생성**

  

#### GENERAL CHARACTERISTICS

- **Summary**    
  스터디 구성원들이 참여할 수 있는 화상채팅방을 새로 개설하는 기능

- **Scope**  
  깃라잡이

  

- **Level**  
  User level  

  

- **Author**  
  김동규

  

- **Last Update**  
  2025. 10. 16

  

- **Status**  
  Design

  

- **Primary Actor**  
  User

  

- **Preconditions**  
  사용자는 로그인 상태여야 한다
  스터디가 존재해야 한다.
  

- **Trigger**  
  사용자가 스터디 페이지에서 "화상채팅방 생성" 버튼을 클릭할 때.
  
- **Success Post Condition**  
  화상채팅방이 생성되고, 고유 방 ID가 발급된다.
  스터디 구성원들에게 "화상 채팅방 개설" 알림이 전송된다.
  
- **Failed Post Condition** 
  화상채팅방 개설이 정상적으로 처리되지 않고 오류메시지가 표시된다.
  
#### MAIN SUCCESS SCENARIO

| Step | Action                                          |
| ---- | ----------------------------------------------- |
| S    | 사용자가 스터디 페이지에서 '화상채팅방 생성' 버튼을 클릭한다.           |
| 1    | 시스템은 새로운 화상채팅방을 생성한다.                   |
| 2    | 고유한 방 ID와 접근 URL을 발급한다.              |
| 3    | 생성된 방 정보를 데이터베이스에 저장한다.                |
| 4    | 스터디 구성원들에게 화상채팅방 초대 알림을 전송한다. |
| 5    | 시스템은 "화상채팅방이 생성되었습니다." 메시지를 표시한다.        |
  
  

#### EXTENSION SCENARIOS

| Step | Branching Action |
| ---- | ---------------- |
|      |                  |
|      |                  |
  
  
  

#### RELATED INFORMATION

- **Performance**:

- **Frequency**:

- **Concurrency**:

- **Due Date**:



#### RELATED INFORMATION
- **Performance**: 
- **Frequency**: 
- **Concurrency**: 
- **Due Date**: 

### **Use case # : 스터디 화상채팅방 종료**

  

#### GENERAL CHARACTERISTICS

- **Summary**    
  화상채팅이 끝난 후 세션을 종료하는 기능

- **Scope**  
  깃라잡이

  

- **Level**  
  User level  

  

- **Author**  
  김동규

  

- **Last Update**  
  2025. 10. 16

  

- **Status**  
  Design

  

- **Primary Actor**  
  User

  

- **Preconditions**  
  방 생성자로 로그인 되어있어야 한다.
  해당 화상채팅방이 활성화된 상태여야 한다.
  

- **Trigger**  
  사용자가 화상채팅 중 "종료" 버튼을 클릭했을 때
  
- **Success Post Condition**  
  화상채팅방 세션이 종료되고, 모든 사용자가 퇴장 처리된다.
  
- **Failed Post Condition** 
  화상채팅방 종료가 정상적으로 처리되지 않고 오류메시지가 표시된다.
  
#### MAIN SUCCESS SCENARIO

| Step | Action                                          |
| ---- | ----------------------------------------------- |
| S    | 사용자가 화상채팅 인터페이스에서 '종료' 버튼을 클릭한다.           |
| 1    | 시스템은 현재 화상채팅 세션을 종료하고 모든 사용자를 강제 퇴장 시킨다.            |
| 2    | 고유한 방 ID와 접근 URL을 발급한다.              |
| 3    | 시스템은 "화상채팅방이 종료되었습니다." 메시지를 표시한다.        |
  
  

#### EXTENSION SCENARIOS

| Step | Branching Action |
| ---- | ---------------- |
|      |                  |
|      |                  |
  
  
  

#### RELATED INFORMATION

- **Performance**:

- **Frequency**:

- **Concurrency**:

- **Due Date**:



#### RELATED INFORMATION
- **Performance**: 
- **Frequency**: 
- **Concurrency**: 
- **Due Date**: 

### **Use case # : 스터디 화상채팅방 입장**

  

#### GENERAL CHARACTERISTICS

- **Summary**    
  스터디 구성원이 생성된 화상채팅방에 입장하는 기능

- **Scope**  
  깃라잡이

  

- **Level**  
  User level  

  

- **Author**  
  김동규

  

- **Last Update**  
  2025. 10. 16

  

- **Status**  
  Design

  

- **Primary Actor**  
  User

  

- **Preconditions**  
  로그인 상태여야 한다.
  해당 스터디의 구성원이어야 하며, 화상채팅방이 활성 상태여야 한다.
  

- **Trigger**  
  사용자가 스터디 페이지 또는 초대 알림에서 "입장" 버튼을 클릭했을 때 시작된다.
  
- **Success Post Condition**  
  사용자가 화상채팅방에 정상적으로 입장하고, 오디오·비디오 스트림이 연결된다.
  
- **Failed Post Condition** 
  방이 존재하지 않거나 이미 종료된 경우 입장이 거부된다.
  
#### MAIN SUCCESS SCENARIO

| Step | Action                                          |
| ---- | ----------------------------------------------- |
| S    | 사용자가 스터디 페이지에서 '화상채팅방 입장' 버튼(또는 URL)을 클릭한다.        |
| 1    | 시스템은 사용자 로그인 및 스터디 참여 여부를 검증한다.         |
| 2    | 시스템은 해당 방의 활성 상태를 확인한다.             |
| 3    | 사용자가 방에 연결되고, 오디오/비디오 스트림이 초기화 된다.       |
| 4    | 시스템은 참가자 목록에 해당 사용자를 추가한다.       |
| 5    | "화상채팅방에 입장했습니다." 메시지가 표시된다.      |
  
  

#### EXTENSION SCENARIOS

| Step | Branching Action |
| ---- | ---------------- |
|   1a   |      비회원일 경우 로그인 페이지로 이동한다            |
|   1a   |      스터디 구성원이 아닐 경우 "입장 권한이 없습니다" 메시지를 표시한다.            |
|   2a   |      이미 종료된 경우 "화상채팅방이 종료되었습니다." 메시지를 표시한다.           |
|   3a   |      네트워크 오류 발생 시 '재접속' 옵션을 제공한다.           |
  
  
  

#### RELATED INFORMATION

- **Performance**:

- **Frequency**:

- **Concurrency**:

- **Due Date**:



#### RELATED INFORMATION
- **Performance**: 
- **Frequency**: 
- **Concurrency**: 
- **Due Date**: 

---
## 알림

---
## 게시판

---
## 오픈소스 이슈 관리

---
## 기여도 및 도전과제

---

## 3. Class diagram
- Draw class diagrams.  
- Describe each class in detail (attributes, methods, others) (table type).  
- 12pt, 160%.  

---

## 4. Sequence diagram
- Draw sequence diagrams for the whole functions of your system.  
- Explain each sequence diagram.  
- 12pt, 160%.  

---

## 5. State machine diagram
- Draw state machine diagrams for the client and the server system.  
- Explain each state machine diagram.  
- 12pt, 160%.  

---

## 6. User interface prototype
- Design user interface for your software system.  
- It will be easy if you just think that you make a preliminary user manual of your system based on your user interface.  
- 12pt, 160%.  

---

## 7. Implementation requirements
- Describe operating environments to implement your system.  
- 12pt, 160%.  

---

## 8. Glossary
- Specifically describe all of the terms used in this document.  
- 12pt, 160%.  

---

## 9. References
- Describe all of your references (book, paper, technical report etc).  
- 12pt, 160%.  
