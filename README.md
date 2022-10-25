# 키친포스

## 요구 사항

### Product

- 생성(`/api/products`)
    - 가격이 null이거나 0 이하의 값이어서는 안된다.
- 조회 (`/api/products`)
    - 전체 product 리스트를 조회한다.

### Menu Group

- 생성(`api/menu-groups`)
- 조회(`api/menu-groups`)

### Menu

- 생성(`api/menus`)
    - 가격이 null이거나 0이하의 값이어서는 안된다.
    - 존재하지 않는 MenuGroupId를 가지고 있어서는 안된다.
    - menuProducts에 해당하는 product가 존재하지 않아서는 안된다.
    - Products 가격의 총합이 0 이상이어야한다.
- 조회(`api/menus`)

### Order

- 생성(`api/orders`)
    - 하나 이상의 orderLineItem을 가지고 있어야 한다.
    - 없는 메뉴를 주문해서는 안된다.
    - 주문한 테이블이 존재하지 않으면 예외를 발생시킨다.
- 조회(`api/orders`)
- 주문 상태 변경(`api/orders/{orderId}/order-status`)
    - 존재하는 주문에 대해서만 주문을 변경할 수 있다.
    - 이미 완료된 상태라면 변경할 수 없다.

### Table Group

- 생성(`api/table-groups`)
    - table의 개수가 1개 이하거나 비어있으면 안된다.
    - 존재하지 않는 테이블이 있으면 예외를 잘생시킨다.
- 삭제(`api/table-groups/{tableGroupId}`)
    - 주문 상태가 Cooking이거나 meal이라면 삭제할 수 없다.

### Table

- 생성(`api/tables`)
    - orderTable의 id와 tableGroupId를 null로 세팅하고 저장한다.
- 조회(`api/tables`)
- 수정-비우기(`/api/tables/{orderTableId}/empty`)
    - 존재하지 않는 orderTable에 대해서 예외를 발생시킨다.
    - 존재하지 않는 tableGroup에 대해서 예외를 발생시킨다.
    - 주문상태가 Cooking이거나 meal이면 예외를 발생시킨다.
- guest 수 수정 (`api/tables/{orderTableId}/number-of-guests`)
    - guest의 수가 0명 이상이어야 한다.
    - 존재하지 않는 orderTable에 대해서 예외를 발생시킨다.

## 용어 사전

| 한글명      | 영문명              | 설명                            |
|----------|------------------|-------------------------------|
| 상품       | product          | 메뉴를 관리하는 기준이 되는 데이터           |
| 메뉴 그룹    | menu group       | 메뉴 묶음, 분류                     |
| 메뉴       | menu             | 메뉴 그룹에 속하는 실제 주문 가능 단위        |
| 메뉴 상품    | menu product     | 메뉴에 속하는 수량이 있는 상품             |
| 금액       | amount           | 가격 * 수량                       |
| 주문 테이블   | order table      | 매장에서 주문이 발생하는 영역              |
| 빈 테이블    | empty table      | 주문을 등록할 수 없는 주문 테이블           |
| 주문       | order            | 매장에서 발생하는 주문                  |
| 주문 상태    | order status     | 주문은 조리 ➜ 식사 ➜ 계산 완료 순서로 진행된다. |
| 방문한 손님 수 | number of guests | 필수 사항은 아니며 주문은 0명으로 등록할 수 있다. |
| 단체 지정    | table group      | 통합 계산을 위해 개별 주문 테이블을 그룹화하는 기능 |
| 주문 항목    | order line item  | 주문에 속하는 수량이 있는 메뉴             |
| 매장 식사    | eat in           | 포장하지 않고 매장에서 식사하는 것           |
