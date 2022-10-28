## 2단계 - 서비스 리팩터링

1. Controller Request 파라미터 - DTO 변경
    - 목적
        - Controller에서 도메인 사용하지 않기 위함.
        - Request에서는 도메인의 모든 필드를 사용하지 않음.
    - 목록
        - [x] MenuGroupRestController
        - [x] MenuRestController
        - [x] OrderRestController
        - [x] ProductRestController
        - [x] TableGroupRestController
        - [x] TableRestController
2. Controller Response 파라미터 - DTO 변경
    - 목적
        - 실제 DB에 저장되는 데이터와 Domain이 달라 관리가 어려움.
        - 예시
            - Orders에는 OrderLineItem이 없으나 도메인에는 존재.
    - 목록
        - [x] MenuGroupRestController
        - [x] MenuRestController
        - [x] OrderRestController
        - [x] ProductRestController
        - [x] TableGroupRestController
        - [x] TableRestController
3. 서비스 로직 코드 분리
    - Domain의 예외 검증 로직을 Domain으로 이동
    - 목록
        - [ ] MenuGroupService
        - [ ] MenuService
        - [ ] OrderService
            - Order 메서드 추가
                - changeOrderStatus()
                - addOrderLineItem()
        - [ ] ProductService
        - [ ] TableGroupService
        - [ ] TableService
            - OrderTable 메서드 추가
                - changeNumberOfGuest()
                - changeEmpty()
    - 추가 수정 사항
        - 가격 검증 로직 : Price 클래스 생성하여 예외 검증 처리 
