# 키친포스

## 요구 사항

- [x] MenuGroup
  - [x] 새로운 MenuGroup을 생성해 DB에 저장한다.
    - [x] DB에 새로운 MenuGroup을 저장한다.
    - [x] DB에 저장한 MenuGroup을 MenuGroupResponse로 변환해 반환한다.
  - [x] DB에 저장되어있는 모든 MenuGroup들을 조회해 MenuGroupResponse로 변환해서 반환한다.
  
- [x] Menu
  - [x] 새로운 Menu를 생성해 DB에 저장한다.
    - [x] Product들을 DB에서 조회한다.
    - [x] Menu를 생성해 DB에 저장한다.
    - [x] MenuProduct들을 생성하고 Menu, Product를 할당해 DB에 저장한다.
    - [x] Menu를 MenuResponse로 변환해서 반환한다.
      - [x] MenuResponse는 MenuProductResponse들을 갖고있다.
    - [x] 예외
      - [x] MenuRequest
        - [x] price = null
        - [x] price < 0
        - [x] menuGroupId
          - [x] DB에 존재하지 않을 때
        - [x] menuProducts
          - [x] Product DB에서 조회
            - [x] DB에 존재하지 않을 때
            - [x] MenuProduct들의 price 합 < MenuRequest의 price
  - [x] DB에 저장되어있는 모든 Menu들을 조회해 MenuResponse로 변환해서 반환한다.
    - [x] Menu를 MenuResponse로 변환해서 반환한다.
      - [x] MenuResponse는 MenuProductResponse들을 갖고있다.

- [x] Order
  - [x] 새로운 Order를 생성해 DB에 저장한다.
    - [x] orderTableId로 DB에서 OrderTable을 가져온다.
    - [x] Order을 생성한다.
      - [x] OrderTable을 할당한다.
      - [x] OrderStatus를 COOKING으로 할당한다.
    - [x] Order의 orderedTime을 현재시간으로 세팅한다.
    - [x] Order를 DB에 저장한다.
    - [x] OrderLineItems에 Order를 할당해서 DB에 저장한다.
    - [x] Order, OrderLineItems를 Response로 변환해 반환한다.
    - [x] 예외
      - [x] OrderLineItems가 비어있을 때
      - [x] OrderTable
        - [x] DB에 존재하지 않을 때
        - [x] empty = true
  - [x] DB에 저장되어있는 모든 Order들을 반환한다.
    - [x] DB에서 모든 Order들을 조회한다.
      - [x] DB에서 모든 Order들을 조회한다.
      - [x] DB에서 각 Order들의 OrderLineItem들을 조회한다.
      - [x] Order들과 각 Order의 OrderLineItem들을 Response로 변환해서 반환한다.
  - [x] Order의 OrderStatus를 변경한다.
    - [x] orderId로 DB에서 Order를 가져온다.
    - [x] Order의 orderStatus를 요청 파라미터 OrderStatus로 변경한다.
    - [x] 변경사항을 DB에 반영한다.
    - [x] DB에서 Order의 모든 OrderLineItem들을 가져온다.
    - [x] Order와 OrderLineItem들을 Response로 변환해서 반환한다.
    - [x] 예외
      - [x] Order
        - [x] DB에 존재하지 않을 때
        - [x] OrderStatus = COMPLETION

- [x] Product
  - [x] 새로운 Product를 생성해 DB에 저장한다.
    - [x] DB에 새로운 Product를 저장한다.
    - [x] DB에 저장한 Product를 ProductResponse로 변환해 반환한다.
  - [x] DB에 저장되어있는 모든 Product들을 조회해 ProductResponse로 변환해서 반환한다.
  - [x] 예외
    - [x] ProductRequest
      - [x] price = null
      - [x] price < 0

- [x] TableGroup
  - [x] 새로운 TableGroup을 생성해 DB에 저장한다.
    - [x] 요청 매개변수의 id값들로 DB에서 OrderTable들을 조회한다.
    - [x] TableGroup에 현재 시간을 설정하고 DB에 저장한다.
    - [x] OrderTable들의 empty값을 false로 변경한다.
    - [x] OrderTable들에 TableGroup을 할당한다.
    - [x] 변경사항을 DB에 반영하고, TableGroupResonse로 변환해 반환한다.
    - [x] 예외
      - [x] TableGroupRequest
        - [x] orderTableRequests
          - [x] empty
          - [x] size < 2
          - [x] DB에서 조회한 OrderTable들
            - [x] 존재하지 않을 때
            - [x] empty = false
            - [x] TableGroupId != null
  - [x] TableGroup를 해제한다.
    - [x] 요청 파라미터 tableGroupId로 DB에서 OrderTable들을 조회한다.
    - [x] OrderTable들 값 변경
      - [x] TableGroupId = null
      - [x] empty = false
    - [x] DB에 변경사항을 반영한다.
    - [x] 예외
      - [x] OrderTable들
        - [x] OrderTable에 해당하는 Order들의 OrderStatus값이 모두 COMPLETION이 아닐 때

- [x] OrderTable
  - [x] 새로운 OrderTable을 생성해 DB에 저장한다.
    - [x] OrderTable의 Id와 tableGroupId를 null로 세팅한다.
    - [x] DB에 OrderTable을 저장한다.
    - [x] 저장한 OrderTable을 OrderTableResponse로 변환해 반환한다.
  - [x] DB에 저장되어있는 모든 OrderTable들을 조회해 OrderTableResponse로 변환해서 반환한다.
  - [x] OrderTable의 Empty상태를 바꾼다.
    - [x] DB에서 orderTableId로 OrderTable을 조회한다.
    - [x] OrderTable의 empty값을 OrderTableRequest의 empty값으로 세팅한다.
    - [x] 변경사항을 DB에 반영하고, OrderTable을 OrderTableResponse로 변환해 반환한다.
    - [x] 예외
      - [x] orderTableId의 OrderTable이 DB에 존재하지 않을 때
      - [x] OrderTable이 TableGroup에 포함되어있을 때
      - [x] OrderTable에 COMPLETION상태가 아닌 Order가 존재할 때
  - [x] OrderTable의 NumberOfGuest값을 변경한다.
    - [x] 요청 매개변수로 받은 orderTableId로 DB에서 OrderTable을 조회한다.
    - [x] DB에서 조회한 OrderTable의 numberOfGuest값을 매개변수로 받은 numberOfGuest값으로 변경한다.
    - [x] DB에 변경사항을 반영한다.
    - [x] OrderTable을 OrderTableResponse로 변환해 반환한다.
    - [x] 예외
      - [x] orderTableId
        - [x] DB에 존재하지 않을 때
        - [x] DB에서 조회한 OrderTable의 empty값이 true일 때
      - [x] OrderTableRequest
        - [x] numberOfGuest < 0 또는 numberOfGuest = null


<br/>

## 리팩터링

- [x] 도메인 엔티티
  - [x] id 필드 기준으로 equals&hashCode 추가
  - [x] protected 기본생성자 추가
  - [x] 모든 필드를 매개변수로 갖는 생성자 추가
  - [x] JPA 적용
    - [x] DB 스키마 참고
      - [x] nullable = true/false 적용
      - [x] 모두 N : 1 단방향 매핑
  - [x] setter 제거
  - [x] 로직 내부로 이동
    - [x] 단위 테스트
- [x] Dao -> JPA 변경
- [x] DTO 도입
- [x] Controller 리팩터링
- [x] Service 리팩터링
- [x] 테스트 가독성 좋게 리팩터링

<br/>

## 리팩터링 - 코드리뷰 반영
- [x] DTO 정적 팩토리 메서드 of 제거 -> new 키워드(생성자) 사용

<br/>

## 용어 사전

| 한글명 | 영문명 | 설명 |
| --- | --- | --- |
| 상품 | product | 메뉴를 관리하는 기준이 되는 데이터 |
| 메뉴 그룹 | menu group | 메뉴 묶음, 분류 |
| 메뉴 | menu | 메뉴 그룹에 속하는 실제 주문 가능 단위 |
| 메뉴 상품 | menu product | 메뉴에 속하는 수량이 있는 상품 |
| 금액 | amount | 가격 * 수량 |
| 주문 테이블 | order table | 매장에서 주문이 발생하는 영역 |
| 빈 테이블 | empty table | 주문을 등록할 수 없는 주문 테이블 |
| 주문 | order | 매장에서 발생하는 주문 |
| 주문 상태 | order status | 주문은 조리 ➜ 식사 ➜ 계산 완료 순서로 진행된다. |
| 방문한 손님 수 | number of guests | 필수 사항은 아니며 주문은 0명으로 등록할 수 있다. |
| 단체 지정 | table group | 통합 계산을 위해 개별 주문 테이블을 그룹화하는 기능 |
| 주문 항목 | order line item | 주문에 속하는 수량이 있는 메뉴 |
| 매장 식사 | eat in | 포장하지 않고 매장에서 식사하는 것 |
