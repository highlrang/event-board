<h1>게시글 프로젝트</h1>

<h3>기술 스택</h3>

- Java 11
- Spring Boot 2.6.x
- JPA, Querydsl
- MySQL
- JQuery
- Thymeleaf, Bootstrap
    <br><br><br>

<h3>REST API와 함께 보는 게시글 웹 사이트 기능</h3>

  + BOARD
    + 게시글 목록 : GET /api/board
    + 게시글 상세 : GET /api/board/{id}
    + 게시글 등록 : POST /api/board
    + 게시글 수정 : PUT /api/board/{id}
    + 게시글 삭제 : DELETE /api/board/{id}
      <br><br><br>
  + FILE
    + 파일 등록 : POST /api/file
    + 파일 다운로드 : GET /api/file/{id}/download
    + 파일 삭제 : DELETE /api/file/{id}
      <br><br><br>
  + REGISTRATION
    + 등록자 목록 : GET /api/registration
    + 등록 : POST /api/registration
    + 등록 취소(삭제) : DELETE /api/registration
    + 등록자 선정 : PUT /api/registration/status
      <br><br><br>
  + USER
     + 회원가입 : POST /api/user
     + 아이디 중복 확인 : GET /api/user/validate-user-id
       <br><br><br>
    
<h3>프로젝트의 특징</h3>

  + 검증과 예외 처리
    + 서비스에서 사용자에게 친절하고 완성도 있는 경험을 제공하는 것을 고려.
    + javax validation을 이용한 검증 처리
    + exception handler를 이용한 rest api 형식의 예외 처리
      <br><br><br>
  + TDD
    + 안정성과 효율성을 고려하여 테스트와 함께 기능 개발
    + Mockito와 AssertJ를 이용한 Junit 단위테스트
    + Spring container 환경이 필요한 경우에는 통합테스트
      <br><br><br>
  + REST API
    + Rest API를 이용한 데이터 통신으로 백엔드 역할의 본질에 집중
    + Spring에서는 @RestController, @RequestBody, ResponseEntity 이용
    + 프론트에서는 Jquey Ajax를 통한 Api 요청과 응답 처리
      <br><br><br>
