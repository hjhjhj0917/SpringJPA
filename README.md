# Spring Boot Multi-DB Practice

Spring Boot를 기반으로 다양한 데이터베이스(JPA/MariaDB, MongoDB, Redis)의 특징을 이해하고, 각 목적에 맞게 연동 및 활용하는 방법을 학습한 2학년 1학기 백엔드 실습 프로젝트입니다.

## Tech Stack

### Backend
- **Framework**: Java, Spring Boot
- **ORM & Data Access**: Spring Data JPA, QueryDSL, MyBatis
- **Database**: MariaDB, MongoDB
- **In-Memory & Cache**: Redis

### Frontend
- **Languages/Libraries**: HTML5, CSS3, JavaScript, jQuery

---

## Key Features

### 1. 관계형 데이터베이스 처리 (JPA & QueryDSL)
- **공지사항 및 회원 관리 (`NoticeController`, `UserInfoController`)**
- Spring Data JPA를 이용한 기본적인 CRUD 로직 구현
- **QueryDSL**을 활용한 동적 쿼리 작성 및 복잡한 엔티티 조인(`NoticeJoinRepository`) 실습
- 관계형 데이터베이스(MariaDB)를 활용한 안정적인 유저 및 게시글 데이터 영속성 관리

### 2. 비정형 데이터 처리 (MongoDB)
- **멜론 차트 데이터 연동 (`MelonController`, `MongoController`)**
- 음원 차트(Top 100), 가수 정보 등 비정형/반정형 데이터의 수집 및 저장
- MongoDB의 Document 구조를 활용한 빠르고 유연한 데이터 읽기/쓰기 구현 및 검색 기능(`melonSearch.html`) 제공

### 3. 인메모리 데이터 캐싱 및 세션 관리 (Redis)
- **Redis 자료구조 실습 (`RedisController`)**
- 빠른 데이터 접근이 필요한 정보에 대해 Redis 적용 (`String`, `List`, `Hash`, `JSON` 등 다양한 데이터 타입 저장 실습)
- 애플리케이션의 캐시 레이어(`CacheConfig`, `RedisConfiguration`)를 구성하여 데이터베이스 부하 완화 및 응답 속도 최적화

---

## Project Structure

```text
src/main/java/kopo/poly/
 ├── config/          # Cache, QueryDSL, Redis 설정 파일
 ├── controller/      # 도메인별 요청 처리 (Notice, Melon, Mongo, Redis, User)
 │    ├── response/   # 공통 API 응답 처리 (CommonResponse)
 ├── dto/             # 계층 간 데이터 전송 객체
 ├── persistance/     # NoSQL 데이터 접근 계층
 │    ├── mongodb/    # MongoDB Mapper 및 인터페이스
 │    └── redis/      # Redis Mapper 및 인터페이스
 ├── repository/      # JPA 데이터 접근 계층
 │    └── entity/     # JPA 엔티티 설계 (NoticeEntity, UserInfoEntity 등)
 ├── service/         # 핵심 비즈니스 로직 처리 계층
 └── util/            # 공통 유틸리티 (암호화, 날짜 처리, CmmUtil 등)

src/main/resources/
 ├── static/          # CSS, JS(jQuery), HTML 등 정적 리소스
 │    ├── html/       # 테스트 및 기능별 화면 (melon, redis 폴더 분리)
 ├── templates/       # Thymeleaf 등 템플릿 엔진용 뷰 파일 (notice, user)
 ├── application.properties # 애플리케이션 환경 설정
 └── ehcache.xml      # 로컬 캐싱 설정
