# 키친포스

## 요구 사항

### Product

1. 상품을 생성할 수 있다.
    1. 상품의 이름은 빈 값일 수 없다.
    2. 상품의 가격은 0 이상이어야 한다.
2. 상품 리스트를 조회할 수 있다.

### Menu Group

1. 메뉴 그룹을 생성할 수 있다.
2. 메뉴 그룹 리스트를 조회할 수 있다.

### Menu

1. 메뉴를 생성할 수 있다.
    1. 메뉴의 이름은 빈 값일 수 없다.
    2. 메뉴 그룹은 빈 값일 수 없다.
    3. 메뉴의 가격은 메뉴에 포함된 상품의 가격의 합과 같다.
2. 메뉴 리스트를 조회할 수 있다.

### Order

1. 주문을 생성할 수 있다.
    1. 주문하는 테이블이 없으면 주문을 생성할 수 없다.
    2. 주문 상태는 빈 값일 수 없다.
    3. 주문은 COOKING ➜ MEAL ➜ COMPLETION 순서로 진행된다.
    4. 주문 항목이 존재하지 않으면 주문을 생성할 수 없다.
    5. 주문 항목에 포함된 메뉴는 중복될 수 없다.
2. 주문 리스트를 조회할 수 있다.
3. 주문의 상태를 변경할 수 있다.

### Table

1. 테이블을 생성할 수 있다.
    1. 테이블 상태는 빈 값일 수 없다.
    2. 테이블은 비어있거나 차있거나 둘 중 한 가지의 상태이다.
2. 테이블의 상태를 변경할 수 있다.
3. 테이블에 포함된 손님의 수를 변경할 수 있다.
    1. 손님의 수는 0명일 수 있다.
4. 테이블 리스트를 조회할 수 있다.

### Table Group

1. 단체를 지정할 수 있다.
    1. 테이블은 최소 두 개 이상부터 단체로 지정할 수 있다.
    2. 이미 단체에 속한 테이블은 단체 지정을 할 수 없다.
    3. 테이블이 비어있지 않으면 단체 지정을 할 수 없다.
2. 단체 지정을 취소할 수 있다.

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
