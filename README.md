# 키친포스

## 요구 사항

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


## 요구 사항

### 상품

- [] 상품을 등록할 수 있다. (`POST /api/menus`)
  - [] `price`는 0 이상이어야 한다.
- [] 상품들을 조회할 수 있다.

### 메뉴 그룹

- [] 메뉴 그룹을 등록할 수 있다. (`POST /api/menu-groups`)
- [] 메뉴 그룹들을 조회할 수 있다. (`GET /api/menu-groups`)

### 메뉴

- [] 메뉴를 등록할 수 있다. (`POST /api/menus`)
  - [] `price`는 0 이상 이어야한다.
  - [] `price`는 `menuProducts`의 가격의 합 이하여야 된다.
  - [] `menuGroupId`는 등록된 메뉴 그룹의 ID 이어야한다.
  - [] `menuProducts.menuId`는 등록된 메뉴의 ID 이어야한다.
  
- [] 메뉴들을 조회할 수 있다. (`GET /api/menus`)
  
### 주문 테이블

- [] 주문 테이블을 등록할 수 있다. (`POST /api/tables`)
- [] 주문 테이블들을 조회할 수 있다. (`GET /api/tables/`)
- [] 주문 테이블이 비어있는지 나타내는 상태를 수정할 수 있다. (`PUT /api/tables/{orderTableId}/empty`)
  - [] `orderTableId`는 등록된 주문 테이블의 ID 이어야한다.
  - [] 주문 테이블이 단체로 지정되어 있지 않아야한다.
  - [] 주문 테이블이 조리나 식사 상태일 수 없다.
- [] 주문 테이블의 방문한 손님 수를 수정할 수 있다. (`PUT /api/tables/{orderTableId}/number-of-guets`)
  - [] `numberOfGuests`가 0 이상 이어야한다.
  - [] `orderTableId`가 등록된 주문 테이블의 ID 이어야한다.
  - [] 주문 테이블이 빈 테이블이 아니어야한다.

### 단체 지정

- [] 단체 지정을 등록할 수 있다. (`POST /api/table-groups`)
  - [] 2개 이상의 주문 테이블을 지정해야 된다.
  - [] `orderTables[].id`는 모두 등록된 테이블의 ID 이어야한다.
  - [] 주문 테이블이 모두 빈 테이블 이어야한다.
  - [] 주문 테이블이 모두 단체 지정이 되어있지 않아야한다.
- [] 단체 지정을 제거할 수 있다. (`DELETE /api/table-groups/{tableGroupId}`)
  - [] tableGroupId는 존재하는 테이블의 ID 이어야한다.

### 주문

- [] 주문을 등록할 수 있다. (`POST /api/orders`)
  - [] `orderLineItems`는 한 개 이상 이어야한다.
  - [] `orderLineItems.menuId`는 모두 등록된 메뉴의 ID 이어야한다.
  - [] `orderTableId`는 등록된 주문 테이블의 ID 이어야한다.
  - [] `orderTableId`는 비어있지 않은 테이블의 ID 이어야한다.
- [] 주문들을 조회할 수 있다. (`GET /api/orders`)
- [] 주문 상태를 수정할 수 있다. (`PUT /api/orders/{orderId}/order-status`)
  - [] `orderId`는 등록된 주문의 ID 이어야한다.
  - [] 계산 완료 상태의 주문은 수정할 수 없다.
