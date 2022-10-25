# 키친포스

## 요구 사항

### Product

- 새로운 상품을 생성할 수 있다.
    - [API] `POST api/products`
    - [Status code] `201 created`
    - [Request] 이름, 가격 정보
    - [예외] 상품의 가격은 빈값일 수 없으며, 0 이상이다.
    - [Response] 생성된 상품의 id, 이름, 가격 정보
- 전체 상품 목록을 조회할 수 있다.
    - [API] `GET /api/products`
    - [Status code] `200 ok`
    - [Response] 전체 상품의 id, 이름, 가격 정보

### Menu Group

- 새로운 메뉴 그룹을 생성할 수 있다.
    - [API] `POST api/menu-groups`
    - [Status code] `201 created`
    - [Request] 이름 정보
    - [Response] 생성된 메뉴 그룹의 id, 이름 정보
- 전체 메뉴 그룹 목록을 조회할 수 있다.
    - [API] `GET /api/menu-groups`
    - [Status code] `200 ok`
    - [Response] 전체 메뉴 그룹의 id, 이름 정보

### Menu

- 새로운 메뉴를 생성할 수 있다.
    - [API] `POST api/menus`
    - [Status code] `201 created`
    - [Request] 이름, 가격, 메뉴 그룹 id, 상품 정보
    - [예외] 메뉴의 가격은 빈값일 수 없으며, 0 이상이다.
    - [예외] 존재하지 않는 메뉴그룹 id로 생성할 수 없다.
    - [예외] 존재하지 않는 상품 id로 생성할 수 없다.
    - [예외] 가격은 각 상품의 합보다 큰 수일 수 없다.
    - [Response] 생성된 메뉴의 id, 이름, 가격, 메뉴 그룹 id, 상품 정보
- 전체 메뉴 목록을 조회할 수 있다.
    - [API] `GET /api/menus`
    - [Status code] `200 ok`
    - [Response] 전체 상품의 id, 이름, 가격, 메뉴 그룹 id, 상품정보

### Order

- 새로운 주문을 생성할 수 있다.
    - [API] `POST api/orders`
    - [Status code] `201 created`
    - [Request] 주문 테이블 id, 주문 항목 정보
    - 주문상태는 `COOKING`이다.
    - 주문 시간은 현재 시간이다.
    - [예외] 주문 항목은 비어있을 수 없다.
    - [예외] 존재하지 않는 주문 항목 id로 생성할 수 없다.
    - [예외] 존재하지 않는 주문 테이블 id로 생성할 수 없다.
    - [Response] 생성된 주문의 id, 주문 테이블 id, 주문 상태, 주문 시간, 주문 항목 정보
- 전체 주문 정보를 조회할 수 있다.
    - [API] `GET /api/orders`
    - [Status code] `200 ok`
    - [Response] 전체 주문의 id, 주문 테이블 id, 주문 상태, 주문 시간, 주문 항목 정보
- 특정 주문의 주문 상태를 변경할 수 있다.
    - [API] `PUT /api/orders/{orderId}/order-status`
    - [Status code] `200 ok`
    - [Request] 주문 상태 정보
    - [예외] 존재하지 않은 주문 id로 변경할 수 없다.
    - [예외] `COMPLETION` 상태인 경우 변경할 수 없다.
    - [Response] 변경된 주문의 id, 주문 테이블 id, 주문 상태, 주문 시간, 주문 항목 정보

### Table

- 새로운 테이블 생성할 수 있다.
    - [API] `POST api/tables`
    - [Status code] `201 created`
    - [Request]  방문한 손님 수, 빈 테이블 여부 정보
    - [Response] 생성된 테이블의 id, 테이블 그룹 id, 방문한 손님 수, 빈 테이블 여부 정보
- 전체 테이블 정보를 조회할 수 있다.
    - [API] `GET /api/tables`
    - [Status code] `200 ok`
    - [Response] 전체 테이블의 id, 테이블 그룹 id, 방문한 손님 수, 빈 테이블 여부 정보
- 특정 테이블을 빈 테이블로 변경할 수 있다.
    - [API] `PUT /api/tables/{orderTableId}/empty`
    - [Status code] `200 ok`
    - [예외] 존재하지 않은 테이블 id로 변경할 수 없다.
    - [예외] `COOKING` 혹은 `MEAL` 상태의 테이블을 변경할 수 없다.
    - [예외] 특정 테이블 그룹에 포함되어있는 경우 변경할 수 없다.
    - [Response] 변경된 테이블의 id, 테이블 그룹 id, 방문한 손님 수, 빈 테이블 여부 정보
- 특정 테이블의 방문한 손님 수를 변경할 수 있다.
    - [API] `PUT /api/tables/{orderTableId}/number-of-guests`
    - [Status code] `200 ok`
    - [Request] 방문한 손님 수 정보
    - [예외] 존재하지 않은 테이블 id로 변경할 수 없다.
    - [예외] 0 미만의 수로 변경할 수 없다.
    - [Response] 변경된 테이블의 id, 테이블 그룹 id, 방문한 손님 수, 빈 테이블 여부 정보

### Table Group

- 새로운 테이블 그룹을 생성할 수 있다.
    - [API] `POST api/tables-groups`
    - [Status code] `201 created`
    - [Request] 테이블 정보
    - [예외] 비어있거나, 2개 미만의 테이블을 테이블 그룹으로 생성할 수 없다.
    - [예외] 비어있지 않은 테이블은 그룹으로 생성할 수 없다.
    - [예외] 이미 그룹이 정해져있는 테이블은 또 다른 테이블 그룹으로 생성할 수 없다.
    - [예외] 존재하지 않는 테이블로 생성할 수 없다.
    - [Response] 생성된 테이블 그룹의 id, 생성일, 테이블 정보
- 그룹을 해제할 수 있다.
    - [API] `DELETE api/tables-groups/{tableGroupId}`
    - [Status code] `204 no content`
    - [예외] `COOKING` 혹은 `MEAL` 상태의 테이블을 그룹 해제 할 수 없다.

## 용어 사전

| 한글명    | 영문명 | 설명 |
|--------| --- | --- |
| 상품     | product | 메뉴를 관리하는 기준이 되는 데이터 |
| 메뉴 그룹  | menu group | 메뉴 묶음, 분류 |
| 메뉴     | menu | 메뉴 그룹에 속하는 실제 주문 가능 단위 |
| 메뉴 상품  | menu product | 메뉴에 속하는 수량이 있는 상품 |
| 금액     | amount | 가격 * 수량 |
| 주문 테이블 | order table | 매장에서 주문이 발생하는 영역 |
| 빈 테이블  | empty table | 주문을 등록할 수 없는 주문 테이블 |
| 주문     | order | 매장에서 발생하는 주문 |
| 주문 상태  | order status | 주문은 조리 ➜ 식사 ➜ 계산 완료 순서로 진행된다. |
| 방문한 손님 수 | number of guests | 필수 사항은 아니며 주문은 0명으로 등록할 수 있다. |
| 단체 지정  | table group | 통합 계산을 위해 개별 주문 테이블을 그룹화하는 기능 |
| 주문 항목  | order line item | 주문에 속하는 수량이 있는 메뉴 |
| 매장 식사  | eat in | 포장하지 않고 매장에서 식사하는 것 |
