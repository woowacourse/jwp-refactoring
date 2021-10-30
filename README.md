# 키친포스

## 요구 사항
### 상품
1. 상품을 입력할 수 있다.
   - [API] POST /api/products 
   - 상품은 ID, 이름, 가격으로 이루어진다.
   - [예외] 가격은 0원 미만이거나 비어있을 수 없다. 
2. 상품 목록을 받아올 수 있다.
   - [API] GET /api/products

### 주문 테이블
1. 테이블을 등록할 수 있다.
   - [API] POST /api/tables
   - 테이블은 비어있는지 여부, 손님의 숫자, 속한 단체 지정으로 이루어져 있다.
2. 테이블 목록을 받아올 수 있다.
   - [API] GET /api/tables
3. 테이블의 '비어있음' 상태를 참이나 거짓으로 바꿀 수 있다.
   - [API] PUT /api/tables/{id}/empty
   - [예외] 해당 ID의 테이블이 없으면 예외
   - [예외] 해당 테이블에 주문이 남아있으면 예외
4. 테이블 손님 숫자를 수정할 수 있다.
   - [API] PUT /api/tables/{id}/number-of-guests
   - [예외] 해당 ID의 테이블이 없으면 예외
   - [예외] 상태가 '비어있음'이 아니라면 예외  
   - [예외] 손님 숫자가 0 미만이면 예외

### 단체 지정
1. 단체 지정을 생성할 수 있다.
   - [API] /api/table-groups
   - 테이블 그룹은 생성시간과 주문 테이블 리스트를 가지고 있다.
   - [예외] 주문 테이블이 2개 이하인 단체 지정은 생성할 수 없다.
2. 단체 지정을 해제할 수 있다.
   - [API] /api/table-groups/{id}
   - [예외] 해당 id의 테이블그룹이 없다면 예외
   - [예외] 단체지정에 속한 테이블들에 주문이 남아있으면 예외
   
### 주문
1. 주문을 생성할 수 있다.
   - [API] POST /api/orders
   - 주문은 주문 테이블 id, 주문상태, 주문시간, 주문항목(순서, 메뉴 id, 개수) 목록으로 이루어져 있다.
   - [예외] 주문 항목 목록이 비어있다면 예외
   - [예외] 주문 항목 메뉴 id에 해당하는 메뉴가 없다면 예외
   - [예외] 주문 테이블 id에 해당하는 주문 테이블이 없다면 예외
2. 주문 목록을 받아올 수 있다.
   - [API] GET /api/orders
3. 주문 상태를 변경할 수 있다.
   - [API] PUT /api/orders/{id}/order-status
   - 주문 상태는 요리중, 식사중, 마감이 있다.
   - [예외] 이미 주문이 마감된 상태면 예외
   
### 메뉴
1. 메뉴를 생성할 수 있다.
   - [API] POST /api/menus
   - 메뉴는 이름, 가격, 속한 메뉴그룹, 속한 상품(순서, 개수)로 이루어져 있다.
   - [예외] 메뉴 그룹 id가 없으면 예외
   - [예외] 가격이 0보다 작으면 예외
   - [예외] 속한 상품들의 가격 합 보다 메뉴의 가격이 더 크면 예외 
2. 메뉴 목록을 받아올 수 있다.
   - [API] GET /api/menus

### 메뉴 그룹
1. 메뉴 그룹을 생성할 수 있다.
   - [API] POST /api/menu-groups
   - 메뉴 그룹은 이름으로 이루어져 있다.
2. 메뉴 그룹 목록을 받아올 수 있다.
   - [API] GET /api/menu-groups

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
