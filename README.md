# 키친포스

## 요구 사항

### 상품, Product

- 상품을 생성할 수 있다.
    - `POST /api/products`
    - `ERROR` 상품의 가격은 0원 미만 혹은 `null` 일 수 없다.
- 상품 목록을 조회할 수 있다.
    - `GET /api/products`

### 메뉴 그룹, Menu Group

- 메뉴 그룹을 생성할 수 있다.
  - `POST /api/menu-groups`
- 메뉴 그룹 목록을 조회할 수 있다.
  - `GET /api/menu-groups`

### 메뉴, Menu

- 메뉴를 생성할 수 있다.
  - `POST /api/menus`
  - `ERROR` 메뉴의 가격은 0원 미만 혹은 `null` 일 수 없다.
  - `ERROR` 존재하지 않는 메뉴그룹에 속할 수 없다.
  - `ERROR` 메뉴의 가격은 메뉴에 속한 상품의 합보다 클 수 없다.
- 메뉴 목록을 조회할 수 있다.
  - `GET /api/menus`

### 주문 테이블, Order Table

- 주문 테이블을 생성할 수 있다.
  - `POST /api/tables`
- 주문 테이블 목록을 조회할 수 있다.
  - `GET /api/tables`
- 주문 테이블을 비활성화할 수 있다.
  - `PUT /api/tables/{orderTableId}/empty`
  - `ERROR` 존재하지 않는 주문 테이블을 비활성화할 수 없다.
  - `ERROR` 단체 지정된 주문 테이블을 비활성화할 수 없다.
  - `ERROR` 주문 테이블 상태가 현재 조리중 혹은 식사중 일 경우 비활성화할 수 없다.
- 주문 테이블에 배정된 손님 수를 변경할 수 있다.
  - `PUT /api/tables/{orderTableId}/number-of-guests`
  - `ERROR` 손님 수는 0 미만일 수 없다.
  - `ERROR` 존재하지 않는 주문 테이블은 손님 수를 변경할 수 없다.
  - `ERROR` 비활성화된 주문 테이블은 손님 수를 변경할 수 없다.

### 주문, Order

- 주문을 생성할 수 있다.
  - `POST /api/orders`
  - `ERROR` 주문 항목 목록은 비어있을 수 없다.
  - `ERROR` 주문 항목 목록엔 중복되는 메뉴가 있거나 존재하지 않는 메뉴를 포함할 수 없다.
  - `ERROR` 존재하지 않는 주문 테이블에 속할 수 없다.
  - `ERROR` 주문을 등록할 수 없는 주문 테이블일 수 없다.
- 주문 목록을 조회할 수 있다.
  - `GET /api/orders`
- 주문 상태를 변경할 수 있다.
  - `PUT /api/orders/{orderId}/order-status`
  - `ERROR` 존재하지 않는 주문은 변경할 수 없다.
  - `ERROR` 계산이 이미 완료된 주문은 변경할 수 없다.

### 단체 지정, Table Group

- 여러 주문 테이블을 단체 지정할 수 있다.
  - `POST /api/table-groups`
  - `ERROR` 주문 테이블이 2개 미만일 경우 단체 지정할 수 없다.
  - `ERROR` 존재하지 않는 주문 테이블을 포함한 단체를 지정할 수 없다.
- 단체 지정을 해제할 수 있다.
  - `DELETE /api/table-groups/{tableGroupId}`
  - `ERROR` 단체 지정된 주문 테이블 중 계산 완료되지 않은 테이블이 있을 수 없다.

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
