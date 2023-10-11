# 키친포스

## 요구 사항
### 1. menus
1. `GET {{host}}/api/menus`
   > 메뉴 목록을 불러온다.
   
2. `POST {{host}}/api/menus`
   > 새로운 메뉴를 추가한다.
   - 가격이 null 혹은 음수이면 예외가 발생한다.
   - 존재하지 않는 메뉴 그룹이라면 예외가 발생한다.
   - 대응하는 menuProduct가 존재하지 않으면 예외가 발생한다.
   - 총 가격이 MenuProduct들의 총 합계 금액보다 크면 예외가 발생한다.

### 2. menu-groups
1. `GET {{host}}/api/menu-groups`
   > 메뉴 그룹을 확인한다.
2. `POST {{host}}/api/menu-groups`
   > 새로운 메뉴 그룹을 추가한다.

### 3. products
1. `GET {{host}}/api/products`
   > 전체 상품을 조회한다.
2. `POST {{host}}/api/products`
   > 새로운 상품을 생성한다.
   - 가격이 null 혹은 음수이면 예외가 발생한다.

### 4. orders
1. `POST {{host}}/api/orders`
    - 주문을 한다.
2. `GET {{host}}/api/orders`
    - 전체 주문 정보를 불러온다.
3. `PUT {{host}}/api/orders/{orderId}/order-status`
    - 해당 주문의 주문 상태(MEAL, COMPLETION)를 변경한다.
4. 

### 5. tables
1. `GET {{host}}/api/tables`
    - 전체 테이블의 정보를 불러온다.
2. `POST {{host}}/api/tables`
    - 새로운 테이블을 추가한다.
3. `PUT {{host}}/api/tables/{tableId}/empty`
    - 해당 테이블의 empty 여부를 수정한다.
4. `PUT {{host}}/api/tables/{tableId}/number-of-guests`
    - 해당 테이블에 앉은 손님의 수를 수정한다.

### 6. table-groups
1. `POST {{host}}/api/table-groups`
    - 테이블 그룹을 생성한다.
2. `DELETE {{host}}/api/table-groups/{tableGroupId}`
    - 해당 테이블 그룹을 해제한다.


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
