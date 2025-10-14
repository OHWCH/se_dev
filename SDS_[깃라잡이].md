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
  정동현,박솔

- **Last Update**  
  2025. 10. 14

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

- **Failed Post Condition**  
  GitHub 인증에 실패하거나 사용자 데이터 저장 중 오류가 발생한 경우, 회원가입이 완료되지 않으며 오류 메시지가 출력된다.


#### MAIN SUCCESS SCENARIO
| Step | Action                                       |
| ---- | -------------------------------------------- |
| S    | 사용자가 회원가입 버튼을 누른다.                           |
| 1    | 사용자가 회원가입 버튼을 누를 때 시작된다.                     |
| 2    | 시스템은 GitHub OAuth 인증 페이지로 리다이렉트한다.           |
| 3    | 사용자가 GitHub 계정으로 로그인하고, 깃라잡이 앱의 접근 권한을 승인한다. |
| 4    | 시스템은 해당 GitHub ID가 기존 등록된 사용자 계정인지 확인한다.     |
| 5    | 등록된 사용자가 아니라면 추가 정보 입력 페이지를 표시한다.            |
| 6    | 사용자는 닉네임, 기술 스택, 선호 언어를 입력하고 저장 버튼을 누른다.     |
| 7    | 시스템은 사용자 정보를 데이터베이스에 저장하고, 프로필을 생성한다.        |
| 8    | 회원가입 완료 메시지를 표시하고 메인 페이지로 이동한다.              |
| 9    | 회원가입이 성공하면 끝난다.                              |


#### EXTENSION SCENARIOS
| Step | Branching Action |
|------|------------------|



#### RELATED INFORMATION
- **Performance**: 
- **Frequency**: 
- **Concurrency**: 
- **Due Date**: 

### **Use case # : 로그인**

  

#### GENERAL CHARACTERISTICS

- **Summary**    
  사용자가 GitHub 계정을 통해 깃라잡이에 로그인하는 기능이다.

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

### **Use case # : 로그아웃**

  

#### GENERAL CHARACTERISTICS

- **Summary**    
  사용자가 깃라잡이에서 로그아웃을 하는 기능이다.

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
  사용자는 깃라잡이에 로그인된 상태여야 한다.
  

- **Trigger**  
  사용자가 "로그아웃" 버튼을 클릭할 때.
  
- **Success Post Condition**  
  로그인 페이지로 이동한다.
  
- **Failed Post Condition** 
  사용자에게 오류메시지가 출력된다.
  
#### MAIN SUCCESS SCENARIO

| Step | Action                            |
| ---- | --------------------------------- |
| S    | 사용자가 프로필 메뉴에서 "로그아웃" 버튼을 클릭한다.    |
| 1    | 사용자가 로그아웃 버튼을 클릭할 때 시작된다.         |
| 2    | 시스템은 사용자의 토큰 정보를 제거한다.            |
| 3    | 로그아웃 완료 메시지를 표시하고 로그인 페이지로 이동시킨다. |
| 4    | 로그아웃이 성공적으로 완료되면 종료된다.            |

  
  

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

### **Use case # : 회원 탈퇴**

  

#### GENERAL CHARACTERISTICS

- **Summary**    
  사용자가 깃라잡이에서 자신의 계정을 영구적으로 삭제하는 기능이다.

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
  사용자가 깃라잡이에 로그인된 상태여야 한다.
  

- **Trigger**  
  사용자가 "회원 탈퇴" 버튼을 클릭할 때.
  
- **Success Post Condition**  
  계정 정보가 데이터베이스에서 삭제되고, 로그인 화면으로 이동한다.
  
- **Failed Post Condition** 
  오류 메시지가 표시되고 계정은 유지된다.
  
#### MAIN SUCCESS SCENARIO

| Step | Action                                     |
| ---- | ------------------------------------------ |
| S    | 사용자가 "회원 탈퇴" 버튼을 클릭한다.                     |
| 1    | 사용자가 탈퇴 버튼을 클릭할 때 시작된다.                    |
| 2    | 시스템은 탈퇴 확인 창을 표시하고, 사용자의 동의를 요청한다.         |
| 3    | 사용자가 탈퇴를 확정하면 시스템은 사용자 계정 및 관련 데이터를 삭제한다.  |
| 4    | 시스템은 삭제가 완료되면 "회원 탈퇴가 완료되었습니다." 메시지를 표시한다. |
| 5    | 사용자는 로그인 페이지로 리다이렉트된다.                     |
| 6    | 계정 삭제가 성공적으로 완료되면 종료된다.                    |

  
  

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

### **Use case # : 프로필 조회**

  

#### GENERAL CHARACTERISTICS

- **Summary**    
  사용자가 자신의 프로필 정보를 조회하는 기능이다.

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
  사용자는 깃라잡이에 로그인된 상태여야 한다.
  

- **Trigger**  
  사용자가 프로필 아이콘을 클릭할 때.
  
- **Success Post Condition**  
  "내 프로필" 페이지로 이동하고, 닉네임, 기술 스택, 선호 언어, OSS기여도등을 보여준다.
  
- **Failed Post Condition** 
  프로필 정보를 불러오지 못하면 오류 메시지가 표시된다.
  
#### MAIN SUCCESS SCENARIO

| Step | Action                              |
| ---- | ----------------------------------- |
| S    | 사용자가 프로필 아이콘을 클릭한다.                 |
| 1    | 사용자가 프로필 메뉴를 선택할 때 시작된다.            |
| 2    | 시스템은 데이터베이스에서 해당 사용자의 프로필 정보를 조회한다. |
| 3    | 조회된 정보를 화면에 표시한다.                   |
| 4    | 프로필 정보가 성공적으로 표시되면 종료된다.            |

  
  

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

### **Use case # : 프로필 수정**

  

#### GENERAL CHARACTERISTICS

- **Summary**    
  사용자가 자신의 프로필 정보를 수정하는 기능이다.

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
  사용자는 깃라잡이에 로그인된 상태여야 한다.
  

- **Trigger**  
  사용자가 "프로필 수정" 버튼을 클릭할 때.
  
- **Success Post Condition**  
  수정된 정보가 데이터베이스에 업데이트 되고, 화면에 표시된다.
  
- **Failed Post Condition** 
  수정사항 반영되지 않고 오류 메시지가 표시된다.
  
#### MAIN SUCCESS SCENARIO

| Step | Action                                             |
| ---- | -------------------------------------------------- |
| S    | 사용자가 "프로필 수정" 버튼을 클릭한다.                            |
| 1    | 사용자가 프로필 수정 버튼을 클릭할 때 시작된다.                        |
| 2    | 현재 사용자의 기존 프로필 정보를 불러와 수정 가능한 입력창에 표시한다.           |
| 3    | 사용자는 닉네임, 기술 스택, 선호 언어 등의 정보를 수정하고 저장 버튼을 누른다.     |
| 4    | 시스템은 입력값의 유효성을 검사하고, 문제가 없으면 변경된 정보를 데이터베이스에 저장한다. |
| 5    | 수정 완료 메시지를 표시하고, 갱신된 프로필 정보를 화면에 보여준다.             |
| 6    | 수정이 성공적으로 완료되면 종료된다.                               |

  
  

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
