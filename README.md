# 키친포스

## 요구 사항

### 상품
- POST /api/products
    - 상품 생성
- GET /api/products
    - 상품 목록 조회

### 메뉴 그룹
- POST /api/menu-groups
    - 메뉴 그룹 생성
- GET /api/menu-groups
    - 메뉴 그룹 목록 조회

### 메뉴
- POST /api/menus
    - 메뉴 생성
- GET /api/menus
    - 메뉴 목록 조회

### 단체 테이블
- POST /api/table-groups
    - 테이블 단체 지정
- DELETE /api/table-groups/{tableGroupId}
    - 단체 테이블 해체

### 테이블
- POST /api/tables
    - 테이블 생성
- GET /api/tables
    - 테이블 목록 조회
- PUT /api/tables/{orderTableId}/empty
    - 테이블 상태 수정 - 비어있는지
- PUT /api/tables/{orderTableId}/number-of-guests
    - 테이블 상태 수정 - 손님 수

### 주문
- POST /api/orders
    - 주문 생성
- GET /api/orders
    - 주문 목록 조회
- PUT /api/orders/{orderId}/order-status
    - 주문 상태 수정 - 조리, 식사, 계산 완료


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
