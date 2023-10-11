# 키친포스

## 요구 사항

### 메뉴 그룹
- 메뉴 그룹을 등록할 수 있다.
- POST /api/menu-groups
    - 요청 바디: 메뉴 그룹 명
    - 응답 바디: 생성된 메뉴 그룹 객체의 JSON

- 메뉴 그룹을 전체 조회할 수 있다.
- GET /api/menu-groups
    - 응답 바디: 전체 메뉴 그룹 객체 리스트의 JSON
### 메뉴
- 상품을 등록할 수 있다.
- POST /api/menus
    - 요청 바디: 상품 명, 가격, 메뉴 그룹, 메뉴 상품 리스트
    - 응답 바디: 생성된 상품 객체의 JSON
- 상품을 전체 조회할 수 있다.
- GET /api/menus
    - 응답 바디: 전체 상품 객체 리스트의 JSON
### 주문
- 주문을 추가할 수 있다.
- POST /api/orders
    - 요청 바디: 주문 테이블 id, 메뉴 id와 수량이 포함된 주문 항목 리스트
    - 응답 바디: 생성된 주문 객체의 JSON

- 주문을 전체 조회할 수 있다.
- GET /api/orders
    - 응답 바디: 전체 주문 객체 리스트의 JSON

- 특정 주문의 주문 상태를 변경할 수 있다. 주문 상태는 COOKING -> MEAL -> COMPLETION 순으로 진행된다.
- PUT /api/orders/{orderId}/order-status
    - 요청 바디: 주문 상태
    - 응답 바디: 변경된 주문 상태 객체의 JSON

### 상품
- 상품을 추가할 수 있다.
- POST /api/products
    - 요청 바디: 상품 명, 가격
    - 응답 바디: 생성된 상품 객체의 JSON

- 전체 상품을 조회할 수 있다.
- GET /api/products
    - 응답 바디: 전체 상품 객체 리스트의 JSON

### 단체 지정
- 여러 주문 테이블을 단체 테이블로 묶을 수 있다.
- POST /api/table-groups
    - 요청 바디: 주문 테이블 id의 리스트
    - 응답 바디: 생성된 단체 지정 객체의 JSON

- 지정된 단체 테이블을 삭제할 수 있다.
- DELETE /api/table-groups/{tableGroupId}
    - 응답: 204

### 테이블
- 테이블을 추가할 수 있다.
- POST /api/tables
    - 요청 바디: 방문한 손님 수, 빈 테이블 여부
    - 응답 바디: 생성된 주문 테이블 객체의 JSON

- 전체 테이블을 조회할 수 있다.
- GET /api/tables
    - 응답 바디: 전체 테이블 객체 리스트의 JSON

- 특정 테이블의 빈 테이블 여부를 변경할 수 있다.
- PUT /api/tables/{tableId}/empty
    - 요청 바디: 빈 테이블 여부
    - 응답 바디: 변경된 테이블 객체의 JSON

- 특정 테이블의 방문한 손님 수를 변경할 수 있다.
- PUT /api/tables/{tableId}/number-of-guests
    - 요청 바디: 방문한 손님 수
    - 응답 바디: 변경된 테이블 객체의 JSON

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
