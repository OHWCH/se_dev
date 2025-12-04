
|    **Project title**    |                                                                                                                  깃라잡이                                                                                                                   |
| :---------------------: | :-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------: |
| **Project description** | 본 프로젝트 '깃라잡이'는 오픈소스에 기여하고 싶지만 시작에 어려움을 겪는 초보 개발자들을 위한 오픈소스 기여 길라잡이 플랫폼입니다. 온보딩 튜토리얼, 스터디 그룹 및 협업 기능을 통해 학습과 성장을 지원하며, 기여도 시각화와 보상 시스템으로 동기를 부여합니다. 커뮤니티와 최신 OSS뉴스 피드 까지 제공하여, 신규 기여자들이 오픈소스 생태계에 성공적으로 안착하고 함께 성장할 수 있는 개발 환경을 목표로 합니다. |
|         **학번**          |                                                                                                                 **이름**                                                                                                                  |
|        22111138         |                                                                                                                   오원창                                                                                                                   |
|        22212006         |                                                                                                                   박솔                                                                                                                    |
|        22112018         |                                                                                                                   김관호                                                                                                                   |
|        22313542         |                                                                                                                   정동현                                                                                                                   |
|        22112084         |                                                                                                                   김동규                                                                                                                   |
|        22112110         |                                                                                                                   김성민                                                                                                                   |


| ID    | 기능명(Function name) | 요약(Summary) | 세부 요구사항 설명(Predicative summary) | 입력변수 | 정상입력값 partitioning | 예외입력값 partitioning | 추적성(Traceability) | 우선순위(Priority) | 테스트 결과 |
|-------|-------------------------|---------------|------------------------------------------|-----------|-------------------------|--------------------------|----------------------|--------------------|-------------|
| 1     | **회원관리** | GitHub OAuth 기반으로 사용자의 가입·로그인·로그아웃·계정 삭제를 관리한다. | - | - | - | - | - | H | |
| 1.1   | 회원 가입 | 신규 사용자가 GitHub OAuth를 통해 최초 가입한다. | GitHub OAuth 인증을 최초로 성공한 사용자를 user 테이블에 생성한다. | github_id, accessToken, login, avatar_url, bio | github_id가 기존 DB에 존재하지 않고, GitHub에서 정상적으로 login/avatar_url/bio가 반환됨 | github_id가 이미 DB에 존재함, GitHub API 응답에 login 누락, accessToken이 만료·무효 | 1.4.1, 1.4.2, 1.4.3 | H | |
| 1.1.1 |  |  | 사용자는 GitHub OAuth 인증을 통해 최초 가입 절차를 시작한다. | OAuth authorize 요청 파라미터: client_id, redirect_uri, scope, state | client_id가 GitHub OAuth App에 등록된 값과 일치, redirect_uri가 GitHub 설정과 동일, scope가 요구 범위 내, state가 비어있지 않음 | 잘못된 client_id, GitHub 설정과 다른 redirect_uri, 허용되지 않은 scope, state 누락 또는 변조 | 1.4.1 | H | |
| 1.1.2 |  |  | 최초 가입 시 GitHub에서 제공하는 `login`, `avatar_url`, `bio` 정보를 기반으로 기본 프로필이 자동 생성된다. | GitHub API `/user` 응답의 login, avatar_url, bio 필드 | login이 비어 있지 않고 문자열 형식, avatar_url이 유효한 URL 형식, bio가 문자열 또는 null | login null/빈 문자열, avatar_url이 URL 형식이 아님, GitHub API 호출 실패(네트워크 오류, 5xx 응답 등) | 1.5.1 | H | |
| 1.1.3 |  |  | 최초 가입 시 사용자 정보(UserEntity)가 DB에 저장되며 `created_at`이 기록된다. | github_id, is_admin, commitCount, IssueCount, PRCount, created_at | github_id가 unique, is_admin=false, 통계 필드가 0 이상 정수, created_at이 현재 시간 기준으로 정상 저장 | github_id 중복으로 인한 DB unique 제약 위반, DB 연결 오류, created_at이 null로 저장됨 | - | H | |
| 1.2   | 로그인 | 기존 사용자가 GitHub OAuth로 서비스에 재접속한다. | 기존 github_id 기준으로 사용자를 조회하여 JWT를 발급한다. | github_id, accessToken | github_id가 DB에 존재, accessToken이 유효, 탈퇴 계정이 아님(deleted_at=null) | github_id 미존재, accessToken 만료·무효, deleted_at이 설정된 계정 | 1.4.1, 1.4.2, 1.4.3, 1.7.1 | H | |
| 1.2.1 |  |  | 사용자는 GitHub OAuth 인증을 통해 로그인한다. | code(Authorization Code), state | code가 null/빈 문자열이 아니고, 유효 기간 내, 아직 사용되지 않은 코드, state 일치 | code 누락, 만료되었거나 이미 사용된 code, state 불일치, GitHub에서 error 파라미터 반환 | 1.4.1 | H | |
| 1.2.2 |  |  | 기존에 가입된 github_id가 있을 경우 해당 프로필 정보로 로그인 처리된다(JWT 발급). | github_id, DB user_id, is_admin | github_id로 UserEntity 조회 성공, is_admin boolean 값 정상, user_id가 PK로 존재 | github_id로 UserEntity 조회 실패, user 레코드 손상(필수 필드 null), deleted_at 설정된 계정 | - | H | |
| 1.2.3 |  |  | Refresh Token을 보유 중인 사용자는 자동 로그인(토큰 재발급)을 사용할 수 있다. | refreshToken (JWT 문자열) | refreshToken 형식이 유효(JWT 구문), 서명 검증 성공, 만료 시간 내, 토큰의 userId가 DB에 존재 | refreshToken 형식 오류, 서명 검증 실패, 만료된 refreshToken, 토큰의 userId가 DB에 없음 | 1.7.1 | H | |
| 1.2.4 |  |  | GitHub 인증 실패 또는 탈퇴 계정인 경우 로그인 오류 메시지가 표시된다. | code, accessToken, user.deleted_at | code 또는 accessToken이 유효하지 않거나, user.deleted_at != null | GitHub OAuth 서버 다운, 네트워크 오류, 탈퇴 계정으로 로그인 시도, OAuth 거부(사용자가 cancel) | - | H | |
| 1.3   | 로그아웃 | 사용자가 서비스 이용을 종료한다. | 클라이언트가 보유 중인 JWT를 삭제하고 보호된 자원 접근을 중단한다. | (클라이언트 측) 저장된 accessToken, refreshToken | 토큰이 클라이언트 저장소(localStorage, cookie 등)에서 정상 삭제됨 | 클라이언트에 토큰이 남아 있어 재요청 시도, 브라우저 저장소 삭제 실패 | - | H | |
| 1.3.1 |  |  | 사용자가 로그아웃 버튼을 누르면 로그아웃이 수행된다. | 로그아웃 버튼 클릭 이벤트, 현재 로그인 상태 플래그 | 로그인 상태에서 로그아웃 버튼 클릭 시 토큰 삭제 및 상태 플래그 false 처리 | 비로그인 상태에서 로그아웃 버튼 클릭, UI 이벤트 미동작 | - | H | |
| 1.3.2 |  |  | JWT 기반의 stateless 구조이므로 서버는 세션을 관리하지 않으며, 클라이언트는 보유 중인 Access/Refresh Token을 삭제한다. | accessToken, refreshToken (클라이언트 보유 여부) | 클라이언트가 두 토큰을 모두 삭제하고 이후 Authorization 헤더를 보내지 않음 | 클라이언트가 accessToken 또는 refreshToken 중 하나라도 유지하여 재사용 시도 | - | H | |
| 1.3.3 |  |  | 로그아웃이 성공하면 자동 로그인(Refresh Token 기반 재발급)도 중단된다. | refreshToken 보유 여부 | 로그아웃 시 refreshToken 삭제되어 `/auth/refresh` 호출 시 토큰 없음 | 로그아웃 후에도 refreshToken이 남아 있어 자동 로그인 API 호출 시도 | - | H | |
| 1.3.4 |  |  | 로그아웃 후에는 보호된 기능에 접근할 수 없으며 로그인 화면으로 이동한다. | 보호된 API 호출 시 Authorization 헤더, 라우팅 경로 | Authorization 헤더 없음 → 401/403 응답 후 로그인 화면으로 리다이렉트 | 만료·무효 토큰 포함 Authorization 헤더로 보호된 API 접근 시도, 라우팅 오류로 로그인 화면 이동 실패 | - | H | |
| 1.4   | 깃허브 인증 | GitHub 계정을 통해 사용자를 인증한다. | GitHub OAuth 2.0 Authorization Code Flow로 사용자 인증을 수행한다. | client_id, client_secret, redirect_uri, scope | GitHub OAuth 설정과 동일한 client_id/client_secret/redirect_uri, scope 범위 정상 | 잘못된 client_id/client_secret, redirect_uri 불일치, scope 거부 | - | H | |
| 1.4.1 |  |  | 로그인 버튼 클릭 시 GitHub OAuth authorize 페이지로 이동한다. | authorize URL, client_id, redirect_uri | authorize URL이 정상 생성되고 브라우저에서 GitHub 로그인 페이지가 열림 | authorize URL 생성 실패, GitHub 로그인 페이지 접속 불가(404, 5xx) | - | H | |
| 1.4.2 |  |  | GitHub에서 제공한 `code`를 Access Token으로 교환하여 인증을 완료한다. | code, client_id, client_secret, redirect_uri | 유효한 code와 올바른 client_id/client_secret/redirect_uri로 access_token 발급 성공 | code 누락·만료·중복 사용, client_id/client_secret 불일치, redirect_uri 불일치, GitHub 토큰 엔드포인트 4xx/5xx | - | H | |
| 1.4.3 |  |  | GitHub API(`/user`)를 호출하여 사용자의 github_id(login)을 조회한다. | accessToken, `/user` API 엔드포인트, Authorization 헤더 | Bearer accessToken이 유효하여 `/user` 호출 성공, login 필드가 존재 | accessToken 만료·무효, GitHub API rate limit 초과, `/user` 호출 실패, login 필드 누락 | - | H | |
| 1.5   | 프로필 조회 | 사용자가 자신의 프로필을 확인한다. | 로그인된 사용자의 user 레코드를 조회하여 프로필 정보를 반환한다. | userId(JWT sub), Authorization 헤더 | Authorization에 유효한 accessToken 포함, userId로 DB 조회 성공 | Authorization 누락, accessToken 만료·위조, userId에 해당하는 user 없음, deleted_at 설정 계정 | - | H | |
| 1.5.1 |  |  | 사용자는 설정 화면에서 자신의 프로필 정보(닉네임, githubId, 프로필 이미지, bio, 통계 정보)를 조회할 수 있다. | userId, DB에 저장된 nickname, github_id, avatar_url/profileImg, bio, commitCount, IssueCount, PRCount | userId 기준 UserEntity 조회 성공, 각 필드가 null 허용 범위 내에서 정상 값 | userId 조회 실패, 필수 필드 손상(예: github_id null), DB 연결 오류 | - | H | |
| 1.6   | 프로필 수정 | 사용자가 자신의 기본 프로필 정보를 편집한다. | 사용자가 닉네임/이미지/bio를 수정 요청하면 UserEntity의 해당 필드를 갱신한다. | userId, nickname, profileImg, bio | userId 유효, nickname이 규칙(길이, 문자 제한 등) 충족, profileImg 유효 URL, bio 길이 제한 내 | userId에 해당 user 없음, nickname 길이 초과·금지문자 포함, profileImg URL 형식 아님, bio 길이 초과 | - | H | |
| 1.6.1 |  |  | 사용자는 설정 화면에서 프로필 편집 기능을 실행할 수 있다. | 편집 화면 진입 요청(userId, Authorization) | 유효한 accessToken으로 편집 화면 진입, 현재 프로필 정보 로딩 성공 | 비로그인 상태 접근, 만료된 accessToken, 삭제 계정으로 접근 | - | H | |
| 1.6.2 |  |  | 닉네임, 프로필 이미지, bio를 수정할 수 있다(GitHub 로그인 정보는 변경 불가). | nickname, profileImg, bio | nickname: 허용 길이/문자, profileImg: URL 형식, bio: 허용 길이 이내 | nickname: 공백/금지어 포함/너무 김, profileImg: URL 아님, bio: 허용 길이 초과 | - | H | |
| 1.6.3 |  |  | 닉네임이 중복되면 오류 메시지를 안내하고 다른 닉네임을 입력하도록 요청한다. | nickname | DB에 동일 nickname이 없을 때만 저장 허용 | DB에 동일 nickname 존재(중복), DB 조회 실패 | - | H | |
| 1.6.4 |  |  | 프로필 수정 후 저장하면 UserEntity의 해당 필드가 갱신된다. | userId, nickname, profileImg, bio | userId 유효, 수정 요청 값 검증 통과, DB update 성공 | DB update 실패(트랜잭션 오류), userId 미존재, 입력값 유효성 검사 실패 | - | H | |
| 1.7   | 토큰 관리 | 사용자의 인증 상태를 유지한다. | Access Token 만료 시 Refresh Token을 통해 새 토큰을 발급한다. | accessToken, refreshToken | accessToken 만료 시점 이후, refreshToken 유효, userId 조회 성공 | accessToken과 refreshToken 모두 만료, refreshToken 위조·형식 오류, userId 미존재 | - | H | |
| 1.7.1 |  |  | Access Token이 만료된 경우 Refresh Token으로 새로운 JWT 세트를 재발급한다. | refreshToken (요청 Body의 refreshToken 필드) | refreshToken이 유효(JWT 구조·서명·만료시간 정상), 토큰에서 추출한 userId가 DB에 존재, deleted_at=null | refreshToken 누락, 형식 오류, 서명 검증 실패, 만료된 refreshToken, userId 조회 실패, deleted_at 설정 계정 | - | H | |
| 1.7.2 |  |  | Refresh Token이 유효하지 않을 경우 재발급이 불가능하며 로그인 화면으로 이동한다. | refreshToken | refreshToken 유효성 검사가 실패하면 재발급 로직 중단 후 401/403 응답 | 비어 있거나 null인 refreshToken, 변조된 JWT, 다른 키로 서명된 토큰, 만료된 토큰 | - | H | |
| 1.8   | 계정 삭제 | 사용자가 자신의 계정을 탈퇴한다. | 사용자 요청 시 soft delete 방식으로 계정을 비활성화한다. | userId, 삭제 의사 확인 플래그(예: confirm=true) | userId 유효, 삭제 의사 확인 후 deleted_at에 현재 시간 기록 | userId 미존재, 삭제 의사 미확인(취소), DB update 실패 | - | H | |
| 1.8.1 |  |  | 사용자가 ‘계정 삭제’를 선택하면 soft delete 방식으로 계정이 비활성화된다(deleted_at 설정). | userId, 탈퇴 요청 이벤트 | 삭제 버튼 클릭 후 userId 기준 UserEntity 조회 성공, deleted_at에 현재 시간 설정 | 삭제 요청 시 userId 미존재, 이미 deleted_at이 설정된 계정, DB 오류 | - | H | |
| 1.8.2 |  |  | 탈퇴한 계정으로는 다시 로그인할 수 없다. | github_id, deleted_at | github_id 조회 결과 deleted_at != null 인 경우 로그인 차단 및 오류 메시지 반환 | 삭제 계정임에도 로그인 허용(로직 오류), deleted_at 값이 잘못 저장되어 상태 판단 실패 | - | H | |
| 2 | **스터디 관리** | | | | | | | | |
| 2.1   | 스터디 생성             | 신규 스터디를 개설한다.     | 사용자가 스터디명·소개·주제·정원 정보를 입력하여 신규 스터디를 생성한다. |      |                    |                    | -                 | H    |        |
| 2.1.1 |                    |                   | 사용자는 ‘스터디 생성’ 버튼을 눌러 정보 입력을 시작한다.         |      |                    |                    | -                 | H    |        |
| 2.1.2 |                    |                   | 스터디 생성 시 개설자가 스터디 리더로 자동 지정된다.            |      |                    |                    | -                 | H    |        |
| 2.1.3 |                    |                   | 생성 성공 시 스터디 상세 화면으로 이동한다.                 |      |                    |                    | -                 | H    |        |
| 2.2   | 스터디 수정 (스터디장)      | 스터디 리더가 정보를 수정한다. | 리더만 스터디명·소개·정원 등의 자료를 수정할 수 있다.           |      |                    |                    | -                 | H    |        |
| 2.2.1 |                    |                   | 리더는 스터디명·소개·정원을 편집할 수 있다.                 |      |                    |                    | -                 | H    |        |
| 2.2.2 |                    |                   | 일반 멤버는 수정 기능 접근 불가                        |      |                    |                    | -                 | H    |        |
| 2.2.3 |                    |                   | 수정된 정보는 DB에 즉시 반영된다.                      |      |                    |                    | -                 | H    |        |
| 2.3   | 스터디 삭제 (스터디장)      | 스터디 삭제 기능         | 리더가 스터디를 삭제할 수 있으나, 리더 혼자일 때만 가능하다.       |      |                    |                    | -                 | H    |        |
| 2.3.1 |                    |                   | 스터디 관리 화면에서 삭제 요청이 가능하다.                  |      |                    |                    | -                 | H    |        |
| 2.3.2 |                    |                   | 리더 혼자 존재해야 삭제 가능                          |      |                    |                    | -                 | H    |        |
| 2.3.3 |                    |                   | 멤버가 2명 이상일 경우 삭제/탈퇴 불가                    |      |                    |                    | -                 | H    |        |
| 2.4   | 스터디 목록 조회          | 모든 스터디 목록 조회      | 스터디의 이름/카테고리/정원 등을 기준으로 목록을 조회한다.         |      |                    |                    | -                 | H    |        |
| 2.4.1 |                    |                   | 스터디명을 기준으로 검색 가능                          |      |                    |                    | -                 | H    |        |
| 2.4.2 |                    |                   | 목록에는 제목·소개·정원 정보가 표시된다.                   |      |                    |                    | -                 | H    |        |
| 2.5   | 스터디 상세 조회          | 특정 스터디 상세보기       | 스터디의 소개, 멤버 목록, 일정 정보 등을 확인한다.            |      |                    |                    | -                 | H    |        |
| 2.5.1 |                    |                   | 상세 화면에서 모든 스터디 정보가 표시된다.                  |      |                    |                    | -                 | H    |        |
| 2.6   | 스터디 참여 신청          | 사용자가 스터디 가입 신청    | 사용자가 스터디에 가입 요청(APPLIED 상태)을 한다.          |      |                    |                    | 2.7.1             | H    |        |
| 2.6.1 |                    |                   | 목록/상세 화면에서 '가입하기' 버튼을 누르는 것으로 신청          |      |                    |                    | -                 | H    |        |
| 2.6.2 |                    |                   | 정원 초과 시 가입 불가                             |      |                    |                    | -                 | H    |        |
| 2.6.3 |                    |                   | 승인 기반의 스터디는 신청 시 APPLIED 상태로 등록된다.        |      |                    |                    | 2.7.1             | H    |        |
| 2.7   | 스터디 참여 관리 (스터디장)   | 리더가 멤버 승인·거절·강퇴   | 스터디 리더가 신청자 승인/거절 및 멤버 강퇴를 수행한다.          |      |                    |                    | -                 | H    |        |
| 2.7.1 |                    |                   | 신청자(APPLIED) 승인/거절 가능                     |      |                    |                    | -                 | H    |        |
| 2.7.2 |                    |                   | 리더는 승인된(APPROVED) 멤버를 강퇴할 수 있다.           |      |                    |                    | -                 | H    |        |
| 2.8   | 스터디 일정 관리          | 일정 생성·참여·조회       | 스터디 일정 생성 및 참여 기능 제공                      |      |                    |                    | -                 | H    |        |
| 2.8.1 | 일정 생성(스터디장)        | 리더만 일정 생성         | 리더는 comment/시작/종료 시간을 입력하여 스케줄을 생성한다.     |      |                    |                    | -                 | H    |        |
| 2.8.2 | 일정 참여              | 일정 참석 기능          | 스터디 멤버는 일정에 참여(출석 체크)할 수 있다.              |      |                    |                    | -                 | H    |        |
| 2.8.3 | 일정 목록 조회           | 전체 일정 조회          | 일정 목록 + 참여 인원 수 + 본인 참석 여부 반환             |      |                    |                    | -                 | H    |        |
| 3 | **게시판** | | | | | | | | |
| 3.1 | 게시글 작성 | QnA, 자유게시판 등에 게시글을 작성한다. | 사용자가 게시글을 생성하고 시스템에 등록하는 기능이다. | | | | UC 3.1 | H | |
| 3.1.1 | | | 사용자는 ‘게시글 작성’ 버튼을 눌러 게시글 작성 화면으로 진입한다. | | | | UC 3.1.1 | H | |
| 3.1.2 | | | 게시글의 제목, 내용, 태그를 입력한다. | categoryId, title, content, tags, githubId | title (1~100자), content (1자 이상), categoryId (유효한 ID), githubId (유효한 ID) | title (100자 초과/빈 값), content (빈 값), categoryId (유효하지 않은 ID), githubId (DB에 없는 ID) | UC 3.1.2 | H | |
| 3.1.3 | | | 작성 완료 후 저장 시 DB에 새로운 게시글이 등록되고 해당 상세 글로 이동한다. | postId (새로 생성된 ID) | | | UC 3.1.3 | H | |
| 3.2 | 게시글 수정 (본인 글) | 자신이 작성한 게시글을 수정한다. | 기존 게시글의 내용을 갱신하는 기능이며, 작성자만 수정 가능하다. | | | | UC 3.2 | H | |
| 3.2.1 | | | 자신이 작성한 게시글 상세 화면에서 ‘수정’ 버튼을 클릭하여 수정 화면으로 진입한다. | postId, githubId | postId (유효), githubId (게시글 작성자와 일치) | | UC 3.2.1 | H | |
| 3.2.2 | | | 다른 사용자가 작성한 게시글의 '수정' 버튼은 보이지 않거나 비활성화된다. | postId, githubId | githubId (게시글 작성자와 불일치) | | UC 3.2.2 | H | |
| 3.2.3 | | | 사용자가 수정 완료 후 저장을 누르면 DB의 게시글 데이터가 갱신된다. | postId, newTitle, newContent, githubId | newTitle(1~100자), newContent(1자 이상) | postId (유효하지 않음), githubId (권한 없음) | UC 3.2.3 | H | |
| 3.3 | 게시글 삭제 (본인 글) | 자신이 작성한 게시글을 삭제한다 (Soft Delete). | 작성자만 삭제 가능하며, 논리적으로 삭제 처리된다. | | | | UC 3.3 | H | |
| 3.3.1 | | | 자신이 작성한 게시글 상세 화면에서 ‘삭제’ 버튼을 클릭한다. | postId, githubId | postId (유효), githubId (게시글 작성자와 일치 | | UC 3.3.1 | H | |
| 3.3.2 | | | 삭제 시, "정말 삭제하시겠습니까?"와 같은 확인 팝업을 제공한다. | | 팝업에서 '예' 선택 | 팝업에서 '아니오' 선택 (취소) | UC 3.3.2 | H | |
| 3.3.3 | | | 다른 사용자가 작성한 게시글의 '삭제' 버튼은 보이지 않거나 비활성화된다. | postId, githubId | githubId (게시글 작성자와 불일치) | | UC 3.3.3 | H | |
| 3.4 | 게시글 목록 조회 | 게시글 전체 목록을 조회한다. | 페이징 및 정렬 기능을 포함한 게시글 목록을 제공한다. | | | | UC 3.4 | H | |
| 3.4.1 | | | 목록에는 게시글 번호, 제목, 작성자(GitHub ID), 작성일, 조회수, 댓글 수가 표시된다. | page, size, sortType | page (1 이상), size (1 이상), sortType (최신순/조회순) | page (0 이하), size (0 이하), sortType (유효하지 않은 값) | UC 3.4.1 | H | |
| 3.4.2 | | | 사용자는 최신순, 조회순으로 게시글 목록을 정렬할 수 있다. | sortType | 'createdAt' (최신순), 'viewCount' (조회순) | | UC 3.4.2 | H | |
| 3.4.3 | | | 게시글 항목을 클릭하면 해당 게시글 상세 화면으로 이동한다. | postId | postId (유효한 ID) | postId (삭제되었거나 존재하지 않는 ID) | UC 3.4.3 | H | |
| 3.5 | 게시글 상세 조회 | 게시글의 세부 내용을 확인한다. | 특정 게시글의 내용을 조회하고 관련 댓글 및 메타 정보를 표시한다. | | | | 
UC 3.5 | H | |
| 3.5.1 | | | 상세 화면에는 게시글 제목, 내용, 작성자(GitHub ID), 작성일, 조회수, 태그, 댓글 목록이 표시된다. | postId, githubId | postId (유효한 ID) | postId (삭제되었거나 존재하지 않는 ID) | UC 3.5.1 | H | |
| 3.5.2 | | | 게시글의 조회수는 상세 화면 진입 시 (본인 글이 아닐 경우) 자동으로 1 증가한다. | postId, githubId | githubId (작성자와 불일치) | githubId (작성자와 일치) -> 조회수 증가 안 함 | UC 3.5.2 | H | |
| 3.7 | 댓글 작성 | 생성된 게시물에 댓글을 등록한다. | 특정 게시글에 새로운 댓글을 추가하는 기능이다. | | | | UC 3.7 | H | |
| 3.7.1 | | | 사용자는 댓글을 작성 후 "등록" 버튼 클릭 시 등록된다. | postId, content, githubId | postId (유효), content (1자 이상), githubId (유효) | content (빈 값), postId (유효하지 않음) | UC 3.7.1 | H | |
| 3.7.2 | | | 등록된 댓글은 해당 게시글의 댓글 목록에 최신순으로 즉시 표시된다. | | | | UC 3.7.2 | H | |
| 3.7.3 | | | 댓글에는 작성자 프로필, 닉네임, 내용, 작성일시 정보가 포함된다. | | | | UC 3.7.3 | H | |
| 3.8 | 댓글 삭제 (본인 댓글) | 자신이 작성한 댓글을 삭제한다 (Soft Delete). | 작성자만 삭제 가능하며, 댓글 목록에서 즉시 사라진다. | | | | UC 3.8 | H | |
| 3.8.1 | | | 사용자는 자신이 작성한 댓글 옆의 ‘삭제’ 버튼을 클릭하여 삭제를 요청한다. | commentId, githubId | commentId (유효), githubId (댓글 작성자와 일치) | commentId (유효하지 않음) | UC 3.8.1 | H | |
| 3.8.2 | | | 다른 사용자가 작성한 댓글의 '삭제' 버튼은 보이지 않는다. | commentId, githubId | githubId (댓글 작성자와 불일치) | |
 UC 3.8.2 | H | |
| 3.8.3 | | | 삭제 시 확인 팝업을 제공한다. | | 팝업에서 '예' 선택 | 팝업에서 '아니오' 선택 (취소) | UC 3.8.3 | H | |
| 4 | **관리자** | | | | | | | | |
| 4.1 | 회원 목록 조회 | 관리자가 전체 회원 목록을 조회한다. | | | | | | | |
| 4.1.1 | | | (신규) 관리자 페이지에서 전체 회원 목록(닉네임, 이름, 가입일 등)을 조회하고 검색할 수 있다. | | | | - | M | |
| 4.1.2 | | | (신규) 관리자는 특정 회원을 선택하여 강제 탈퇴시킬 수 있다. | | | | - | M | |
| 4.2 | 신고 승인 | 관리자가 신고된 내역을 처리한다. | | | | | | | |
| 4.2.1 | | | (신규) 관리자는 신고된 게시글/댓글 목록(신고 사유, 신고자, 피신고자)을 확인할 수 있다. | | | | 3.6.1, 3.6.2 | M | |
| 4.2.2 | | | (신규) 관리자는 신고 내역을 검토 후 승인(삭제) 또는 반려 처리를 할 수 있다. | | | | 4.4.1, 4.5.1 | M | |
| 4.3 | 스터디 강제 삭제 | 관리자가 부적절한 스터디를 삭제한다. | | | | | | | |
| 4.3.1 | | | (신규) 관리자는 관리자 페이지에서 모든 스터디를 강제로 삭제할 수 있다. | | | | - | M | |
| 4.4 | 게시글 강제 삭제 | 관리자가 부적절한 게시글을 삭제한다. | | | | | | | |
| 4.4.1 | | | 관리자는 모든 사용자의 게시글을 삭제할 수 있다. | | | | 4.2.2 | M | |
| 4.5 | 댓글 강제 삭제 | 관리자가 부적절한 댓글을 삭제한다. | | | | | | | |
| 4.5.1 | | | 관리자는 모든 사용자의 댓글을 삭제할 수 있다. | | | | 4.2.2 | M | |
| 5 | **오픈소스 이슈** | | | | | | | | |
| 5.1 | 굿퍼스트 이슈 검색 | 초보자가 기여하기 좋은 이슈를 탐색한다. | | | | | | | |
| 5.1.1 | | | 시스템은 GitHub API를 호출하여 'good first issue' 라벨이 포함된 이슈를 조회한다. | | | | - | H | |
| 5.1.2 | | | 시스템은 검색 결과(제목, 링크, 작성자)를 반환한다. | | | | - | H | |
| 5.1.3 | | | 사용자는 페이지를 통해 이슈 목록을 추가로 로드할 수 있다. | | | | - | H | |
| 5.2 | 기여도 측정 | 자신의 오픈소스 기여도를 확인한다. | | | | | | | |
| 5.2.1 | | | 시스템은 사용자의 커밋, PR, 이슈 생성 횟수를 한 번의 요청으로 조회한다. | | | | - | H | |
| 5.2.2 | | | 기여도 점수를 계산(이슈 5점, PR 3점, 커밋 1점). | | | | 5.3.1 | H | |
| 5.2.3 | | | 최신 기여 정보(DB)와 동기화한다. | | | | - | H | |
| 5.3 | 도전과제/배지 획득 | 기여 활동에 따라 보상을 획득한다. | | | | | | | |
| 5.3.1 | | | 기여도 점수로 등급 배지(브론즈~루비)를 수여한다. | | | | 5.2.2 | H | |
| 5.3.2 | | | 사용자는 과제 목록에서 목표 횟수와 현재 달성도를 확인할 수 있다. | | | | 5.2.3 | M | |
| 5.3.3 | | | 기여도 갱신 시 자동으로 완료 처리된다. | | | | 5.2.3 | M | |
| 5.4 | 개인할일 관리(Todo) | 개인 할 일을 관리한다. | | | | | | | |
| 5.4.1 | | | 사용자는 개인 Todo를 등록할 수 있다. | | | | - | H | |
| 5.4.2 | | | 등록된 Todo 목록을 최신순으로 무한 스크롤 조회한다. | | | | - | M | |
| 5.4.3 | | | Todo 완료 여부를 토글할 수 있다. | | | | - | M | |
| 5.4.4 | | | 여러 완료된 Todo를 선택하여 일괄 삭제할 수 있다. | | | | - | M | |

