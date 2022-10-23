# 키친포스
## 요구 사항
### 상품 (Product)
- 상품 생성
  - 요청 URL: `POST /api/products`
  - 상품 가격(price)는 null이 아닌 0이상의 값이어야 한다.
- 상품 목록 조회
  - 요청 URL: `GET /api/products`

### 메뉴 그룹 => 코스, 세트 메뉴 (MenuGroup)
- 메뉴 그룹 생성
  - 요청 URL: `POST /api/menu-groups`
- 메뉴 그룹 목록 조회
  - 요청 URL: `GET /api/menu-groups`

### 메뉴 (Menu) 
- 메뉴 생성
  - 요청 URL: `POST /api/menus`
  - 메뉴 가격(price)는 null이 아닌 0이상의 값이어야 한다.
  - 기존에 존재하는 메뉴 상품여야 한다.
  - 상품들은 기존에 존재하는 데이터여야 한다.
  - 메뉴 가격은 메뉴를 형성하고 있는 상품들 가격의 합보다 적어야 한다.
- 메뉴 목록 조회
  - 요청 URL: `GET /api/menus`

### 주문 (Order)
- 주문 생성
  - 요청 URL: `POST /api/orders`
  - 주문 항목은 비어있을 수 없다.
  - 주문 테이블은 기존에 존재하는 데이터여야 한다.
  - 주문 테이블은 비어있을 수 없다.
- 주문 목록 조회
  - 요청 ULR: `GET /api/orders`
- 주문 상태 변경
  - 요청 URL: `PUT /api/orders/{orderId}/order-status`
  - 주문 상태가 이미 `COMPLETION` 상태일 경우 예외가 발생한다.

### 테이블 (Table)
- 주문 테이블 생성
  - 요청 URL: `POST /api/tables`
- 주문 테이블 목록 조회
  - 요청 URL: `GET /api/tables`
- 빈 테이블로 상태 변경
  - 요청 URL: `PUT /api/tables/{orderTableId}/empty`
  - 존재하는 테이블이어야 한다.
  - 테이블 그룹화가 된 테이블은 변경할 수 없다.
  - 주문 테이블에 남아있는 주문의 상태가 `COOKING`, `MEAL`인 경우 변경할 수 없다.
- 손님 수 변경
  - 요청 URL: `PUT /api/tables/{orderTableId}/number-of-guests`
  - 손님 수를 음수로 변경할 수 없다.
  - 존재하는 테이블이어야 한다.
  - 비어있는 테이블을 변경할 수 없다.

### 테이블 그룹화 => 단체 (TableGroup)
- 테이블들 그룹화 (통합 계산을 위해)
  - 요청 URL: `POST /api/table-groups`
  - 그룹화를 진행할 주문 테이블의 수는 2개 이상이어야 한다.
  - 주문 테이블이 비어있거나 이미 그룹화가 된 테이블이 있을 경우 그룹화를 진행할 수 없다.
- 테이블 그룹화 취소
  - 요청 URL: `DELETE /api/table-groups/{tableGroupId}`
  - 주문 테이블에 남아있는 주문의 상태가 `COOKING`, `MEAL`인 경우 취소할 수 없다.

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
