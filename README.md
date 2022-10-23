# 키친포스

## 요구 사항

### MenuGroup

- MenuGroup을 생성할 수 있다.
- MenuGroup 목록을 조회할 수 있다.

### Menu

- Menu를 등록할 수 있다.
    - 가격이 null이거나 0 미만이면 예외가 발생한다.
    - 존재하지 않는 MenuGroup이면 예외가 발생한다.
    - 존재하지 않는 Product이면 예외가 발생한다.
    - 가격이 일치하지 않으면 예외가 발생한다.
- Menu 목록을 조회할 수 있다.

### Product

- Product를 등록할 수 있다.
    - 가격이 null이거나 0 미만이면 예외가 발생한다.
- Product 목록을 조회할 수 있다.

### Order

- Order를 등록할 수 있다.
    - OrderLineItem이 존재하지 않으면 예외가 발생한다.
    - OrderLineItem 중 DB에 더 이상 존재하지 않는 것이 있다면 예외가 발생한다.
    - 존재하지 않는 OrderTable이면 예외가 발생한다.
    - OrderStatus를 COOKING, OrderedTime을 현재 시간으로 설정한다.
- Order 목록을 조회할 수 있다.
- Order의 상태를 변경할 수 있다.
    - 존재하지 않는 Order이면 예외가 발생한다.
    - 현재 OrderStatus가 COMPLETION이면 예외가 발생한다.

### Table

- Table을 등록할 수 있다.
- Table 목록을 조회할 수 있다.
- Table의 방문한 손님 수를 변경할 수 있다.
    - Guest 수가 0 미만이면 예외가 발생한다.
    - 존재하지 않는 OrderTable이면 예외가 발생한다.
    - 해당 OrderTable이 손님이 없는 상태이면 예외가 발생한다.
- Table의 손님 유무 상태 값을 변경할 수 있다.
    - 존재하지 않는 OrderTable이면 예외가 발생한다.
    - 해당 OrderTable의 TableGroup이 존재하지 않으면 예외가 발생한다.
    - 해당 OrderTable에 해당하는 Order가 없거나, 있어도 OrderStatus가 COOKING 또는 MEAL이 아니라면 예외가 발생한다.

### Table Group

- TableGroup을 등록할 수 있다.
    - OrderTable의 크기가 2 미만이면 예외가 발생한다.
    - OrderTable 중 DB에 더 이상 존재하지 않는 것이 있다면 예외가 발생한다.
    - OrderTable 중 해당 Table이 empty 상태가 아니거나 TableGroup이 null이면 예외가 발생한다.
    - createdDate를 현재 시간으로 설정한다.
- TableGroup을 삭제할 수 있다.

---

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
