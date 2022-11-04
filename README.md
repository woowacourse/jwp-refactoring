# 키친포스

## 요구 사항

### 상품(Product)

- 상품을 등록할 수 있다.
    - 상품의 가격은 0원 이상이다.
- 상품 전체 리스트를 조회할 수 있다.

### 메뉴 그룹(MenuGroup)

- 메뉴 그룹을 생성할 수 있다.
- 메뉴 그룹 전체 리스트를 조회할 수 있다.

### 메뉴(Menu)

- 메뉴를 등록할 수 있다.
    - 메뉴의 가격은 0원 이상이다.
    - 메뉴 그룹이 존재해야 한다.
    - 메뉴의 모든 메뉴 상품이 존재해야 한다.
    - 메뉴의 가격은 모든 메뉴 상품의 가격 총합보다 크면 안된다.
- 메뉴 전체 리스트를 조회할 수 있다.

### 주문(Order)

- 주문을 등록할 수 있다.
    - 주문 항목은 비어있을 수 없다.
    - 주문 테이블에 주문이 존재하지 않는 경우 주문할 수 없다.
    - 주문 테이블은 비어있을 수 없다.
    - 주문한 요청한 메뉴의 수와 주문된 메뉴의 수가 다를 수 없다. 
- 주문 전체 조회를 할 수 있다.
- 특정 주문 상태를 변경할 수 있다.
    - 해당 주문 번호의 주문이 존재해야 한다.
    - 주문이 완료인 경우 상태를 변경할 수 없다.

### 테이블(Table)

- 주문 테이블을 등록할 수 있다.
- 주문 테이블 전체 조회를 할 수 있다.
- 주문 테이블을 빈 테이블로 변경할 수 있다.
    - 주문 테이블이 없는 경우 변경할 수 없다.
    - 단체 지정이 되어있는 경우 변경할 수 없다.
    - 주문 테이블에 남아있는 주문의 상태가 COOKING, MEAL 일 경우 빈 테이블로 변경할 수 없다.
- 주문 테이블 손님 수를 변경할 수 있다.
    - 손님 수는 0 이상이여야 한다.
    - 존재하는 테이블이어야 한다.
    - 빈 테이블은 변경할 수 없다.

### 단체 지정(TableGroup)

- 테이블을 단체로 등록할 수 있다.
    - 빈 테이블이 아닐 경우 단체로 지정할 수 없다.
    - 두 개 미만의 테이블인 경우 단체로 지정할 수 없다.
    - 주문 테이블이 존재하지 않을 경우 단체로 지정할 수 없다.
    - 주문 테이블이 이미 단체 지정인 경우 단체로 지정할 수 없다.
- 테이블의 단체 지정을 해제할 수 있다.
    - 주문 테이블에 남아있는 주문이 존재하고 그 상태가 COOKING, MEAL인 경우 변경할 수 없다.

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
