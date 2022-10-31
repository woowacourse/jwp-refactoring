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
        - [x] MenuGroupService
        - [x] MenuService
        - [x] OrderService
            - Order 메서드 추가
                - changeOrderStatus()
                - addOrderLineItem()
        - [x] ProductService
        - [x] TableGroupService
        - [x] TableService
            - OrderTable 메서드 추가
                - changeNumberOfGuest()
                - changeEmpty()
    - 추가 수정 사항
        - 가격 검증 로직 : Price 클래스 생성하여 예외 검증 처리

## 1차 피드백

- [x] DTO -> Domain 전환 방식
    - DTO가 바뀐다면 도메인도 변경될 수 있는 구조
- [x] MenuService - getSumOfMenuPrice()
    - 네이밍 : get메서드는 값을 가져온다는 의미
    - 메서드에서 상품 조회와 가격의 합을 구하는 일 2개 수행 중, 분리 필요
- [x] OrderService - orderLineItemDao.save() 메서드 분리
    - 가독성을 위해서
- [ ] Repository 클래스를 만들어 참조하는 객체의 조립 책임을 분리
- [ ] Dao에 getId()를 통해 분기처리하는 로직 개선
- [ ] Menu에는 수량 * 가격이 존재하지 않지만 검증하는 로직이 있음 -> 개선 필요
- [ ] DTO에서 검증 로직 수행 어색함
