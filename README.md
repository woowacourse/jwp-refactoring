# 키친포스

## 요구 사항

### 상품(Product)

> 메뉴를 관리하는 기준이 되는 데이터

- [x] 상품을 등록할 수 있다.
    - [x] 상품은 이름을 가진다.
        - [x] 최대 길이는 255자이다.
    - [x] 상품은 가격을 가진다.
        - [x] 0원 이상이어야 한다.
        - [x] 정수 최대 17자리, 소숫점 최대 2자리까지 가능하다.
- [x] 상품의 목록을 조회할 수 있다.

### 메뉴 그룹(menu group)

> 메뉴 묶음, 분류

- [x] 메뉴 그룹을 등록할 수 있다.
    - [x] 메뉴 그룹은 이름을 가진다.
        - [x] 최대 길이는 255자이다.
- [x] 메뉴 그룹의 목록을 조회할 수 있다.

### 메뉴(menu)

> 메뉴 그룹에 속하는 실제 주문 가능 단위

- [x] 메뉴를 등록할 수 있다.
    - [x] 메뉴는 이름을 가진다.
        - [x] 최대 길이는 255자이다.
    - [x] 메뉴는 가격을 가진다.
        - [x] 0원 이상이어야 한다.
        - [x] 정수 최대 17자리, 소숫점 최대 2자리까지 가능하다.
        - [x] 메뉴 상품(menu product) 금액(가격*수량)들의 합보다 작아야 한다.
    - [x] 메뉴는 하나의 메뉴 그룹(menu group)을 가진다.
        - [x] 메뉴 그룹은 존재해야 한다.
    - [x] 메뉴는 여러 개의 메뉴 상품(menu product)들을 가진다.
        - [x] 메뉴 상품들은 존재해야 한다.
        - [x] 메뉴 상품은 수량을 가진다.
- [x] 메뉴의 목록을 조회할 수 있다.

### 주문(order)

> 매장에서 발생하는 주문

- [x] 주문을 등록할 수 있다.
    - [x] 주문은 테이블을 가진다.
        - [x] 테이블은 존재해야 한다.
        - [x] 테이블이 비어있는 상태에선 주문을 등록할 수 없다.
    - [x] 주문은 주문 상태를 가진다.
        - [x] 기본 상태는 `조리`이다.
    - [x] 주문은 주문 시간을 가진다.
    - [x] 주문은 주문 항목(order line item)들을 가진다.
        - [x] 주문 항목은 비어있을 수 없다.
        - [x] 주문 항목들의 메뉴는 존재해야 한다.
- [x] 주문의 목록을 조회할 수 있다.
- [x] 주문의 상태를 변경할 수 있다.
    - [x] 주문이 존재하지 않으면 상태를 변경할 수 없다.
    - [x] 주문의 상태가 `계산 완료`일 때 상태를 변경할 수 없다.

### 테이블(table)

> 테이블은 상태에 따라 주문 테이블(order table, 매장에서 주문이 발생하는 영역), 빈 테이블(empty table, 주문을 등록할 수 없는 주문 테이블)로 나뉜다.

- [x] 테이블은 상태에 따라 두 가지 테이블로 나뉜다.
    - [x] 주문 테이블(order table)은 테이블이 비어있는 상태이다.
    - [x] 빈 테이블(empty table)은 테이블이 비어있지 않은 상태이다.
- [x] 테이블을 등록할 수 있다.
    - [x] 테이블은 상태를 가진다.
    - [x] 테이블은 방문한 손님 수를 가진다.
- [x] 테이블 목록을 조회할 수 있다.
- [x] 테이블의 상태를 변경할 수 있다.
    - [x] 테이블이 존재하지 않으면 변경할 수 없다.
    - [x] 테이블이 단체 지정(table group) 되어있으면 상태를 변경할 수 없다.
    - [x] 테이블이 `조리` or `식사 주문` 상태(order status)일 때 상태를 변경할 수 없다.
- [x] 테이블에 방문한 손님 수를 변경할 수 있다.
    - [x] 방문한 손님 수는 0명 이상이어야 한다.
    - [x] 테이블이 존재하지 않으면 변경할 수 없다.
    - [x] 테이블이 비어있는 상태면 변경할 수 없다.

### 단체 지정(table group)

> 통합 계산을 위해 개별 주문 테이블을 그룹화하는 기능

- [x] 단체 지정을 등록할 수 있다.
    - [x] 단체 지정 시 2개 이상의 테이블들을 가진다.
        - [x] 테이블은 비어있어야 한다.
        - [x] 테이블은 단체 지정이 되어 있지 않아야 한다.
- [x] 단체 지정을 해제할 수 있다.
    - [x] 테이블이 `조리` or `식사` 상태일 때 해제할 수 없다.

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
