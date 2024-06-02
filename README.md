## 기술 스택
- Kotlin , Java OpenJDK 17
- Spring Boot, Security, Data JPA, Web 3.1.3
- MySQL 8.0

## Architecture Flow
<center><img src="https://github.com/lms990108/watcHere_backend/assets/103021300/1aef9ff7-eeca-4529-84e9-1bdb13aa7bb7" width="600"></center>

## ERD
<center><img src="https://github.com/lms990108/watcHere_backend/assets/103021300/c57324b0-d54b-44f7-af64-94b0a9b1b2f3" width="600"></center>

## 주요 기능

1. Batch Process
<center><img width="600" alt="image" src="https://github.com/lms990108/watcHere_backend/assets/103021300/1a948d97-a841-43c6-b9bb-b8edd1c17413"></center>

- Spring Boot에서 Entity Class를 통해 DB Table 구현
- 같은 DB Table에 접근하는 Batch Program 을 Node.js로 구현
- Batch를 통해 자체적인 DB를 확보함으로써 외부 API(TMDB API)에 대한 의존성 완화

2. 검색
<center><img width="600" alt="image" src="https://github.com/lms990108/watcHere_backend/assets/103021300/9f071fbc-b1c7-4898-91c6-30322c39a970"></center>
<center><img width="600" alt="image" src="https://github.com/lms990108/watcHere_backend/assets/103021300/8119e48a-7236-4f95-904c-dec4c3562c06"></center>

- TMDB Search API 활용
- Search 결과인 ID를 통해 자체 DB에서 결과 제공
- 기존 Entity, Dto 활용

3. 소셜 로그인
<center><img width="600" alt="image" src="https://github.com/lms990108/watcHere_backend/assets/103021300/073e7e78-5f7a-4741-b8b3-f962fd8794ff"></center>center>

- 로컬 로그인은 제외하고 소셜 로그인만 활용
- JWT 토큰을 활용한 인증 프로세스 구성

4. 컨텐츠 카테고리/리스트
<center><img width="825" alt="image" src="https://github.com/lms990108/watcHere_backend/assets/103021300/d92bdb77-f9c3-4894-ba8d-2b3662e52ca4"></center>

- TMDB Discover API 활용
- 대량의 이미지 출력을 위해 지연로딩 구현 (FE)

5. 리뷰 및 평점
<center><img width="600" alt="image" src="https://github.com/lms990108/watcHere_backend/assets/103021300/54212bba-b0bc-41a3-acc2-7a167b628f88"></center>

- 리뷰 작성/조회/수정/삭제 + 정렬/신고기능 제공

6. 관리자
<center><img width="600" alt="image" src="https://github.com/lms990108/watcHere_backend/assets/103021300/c3c86a15-9e7a-43c8-801f-522774b93027"></center>
