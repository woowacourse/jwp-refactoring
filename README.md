# 키친포스

## 요구 사항

### API

#### Menu(/api/menus)

- post: 새로운 메뉴 등록
- get: 모든 메뉴 조회

#### MenuGroup(/api/menu-groups)

- post: 새로운 메뉴 그룹 등록
- get: 모든 메뉴 그룹 조회

#### Order(/api/orders)

- post: 새로운 주문 등록
- get: 모든 주문 목록 조회
- put: 다음 OrderStatus 로 변경

#### Product(/api/products)

- post: 새로운 상품 등록
- get: 모든 상품 목록 조회

#### TableGroup(/api/table-groups)

- post: 새로운 테이블 그룹 등록
- get: 모든 테이블 그룹 조회
- delete: OrderStatus 가 Completion 인 경우 TableGroup 에서 OrderTable 제거

#### Table(/api/tables)

- post: 새로운 테이블 등록
- get: 모든 테이블 조회
- put(/{orderTableId}/empty): OrderStatus 가 Completion 인 경우 EmptyTable 로 변경
- put(/orderTableId{/number-of-guests): numberOfGuests 변경

### 도메인

#### Menu

- 생성
    - 가격이 null 이거나 0보다 작으면 예외
    - MenuGroup 에 이미 존재하면 예외

#### Order

- 생성
    - OrderLineItems 가 비었을 경우 예외
    - OrderTable 이 비었을 경우 예외

- Cooking -> Meal -> Completion 순서로 상태를 바꿀 수 있다.

#### Product

- 생성
    - 가격이 null 이거나 0보다 작으면 예외

#### TableGroup

- 생성
    - OrderTable 의 원소 개수가 2개 미만이면 예외

#### OrderTable

- OrderTable 을 Empty 상태로 만든다.
- OrderTable 의 numberOfGuests 를 변경한다.
- OrderStatus 가 Completed 인 경우 tableGroup 을 제거한다.

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
