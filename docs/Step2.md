## 2단계 - 서비스 리팩터링

1. Controller 파라미터 - DTO 변경
    - 목적
        - Controller에서 도메인 사용하지 않기 위함.
        - Request에서는 도메인의 모든 필드를 사용하지 않음.
    - 목록
        - [x] MenuGroupRestController
        - [x] MenuRestController
        - [ ] OrderRestController
        - [x] ProductRestController
        - [x] TableGroupRestController
        - [ ] TableRestController
2. 서비스 로직 코드 분리
    - Domain의 예외 검증 로직을 Domain으로 이동
    - 목록
        - [ ] MenuGroupService
        - [ ] MenuService
        - [ ] OrderService
        - [ ] ProductService
        - [ ] TableGroupService
        - [ ] TableService
    - 추가 수정 사항
        - 가격 검증 로직 : Price 클래스 생성하여 예외 검증 처리 
