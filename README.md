# 키친포스

## 요구 사항

### 상품

- [x] 상품을 등록할 수 있다.
  - [x] 상품의 가격이 0보다 작으면 예외가 발생한다.
- [x] 상품 목록을 조회할 수 있다.

### 메뉴 그룹

- [x] 메뉴 그룹을 등록할 수 있다.
- [x] 메뉴 그룹 목록을 조회할 수 있다.

### 메뉴

- [x] 메뉴를 등록할 수 있다.
  - [x] 메뉴의 가격이 0보다 작으면 예외가 발생한다.
  - [x] 메뉴의 메뉴 그룹이 존재하지 않으면 예외가 발생한다.
  - 메뉴 상품의 총 가격을 계산할 수 있다.
    - [x] 메뉴 상품이 존재하지 않는 상품이면 예외가 발생한다.
    - [x] 메뉴 가격과 메뉴 상품의 총 가격이 다르면 예외가 발생한다.
- [x] 메뉴 목록을 조회할 수 있다.

### 테이블

- [x] 테이블을 등록할 수 있다.
- [x] 테이블 목록을 조회할 수 있다.
- [x] 주문 테이블을 빈 테이블로 변경할 수 있다.
  - [x] 주문 테이블이 존재하지 않으면 예외가 발생한다.
  - [x] 단체 지정되어 있는 테이블이면 예외가 발생한다.
  - [x] 주문 상태가 `조리` 또는 `식사`면 예외가 발생한다.
- [x] 주문 테이블의 방문한 손님 수를 변경할 수 있다.
  - [x] 방문한 손님 수가 0보다 작으면 예외가 발생한다.
  - [x] 주문 테이블이 존재하지 않으면 예외가 발생한다.
  - [x] 빈 테이블이면 예외가 발생한다.

### 주문

- [x] 주문을 등록할 수 있다.
  - [x] 주문 항목이 비어있으면 예외가 발생한다.
  - [x] 주문 항목 중 실제 메뉴에 존재하지 않는 메뉴가 있으면 예외가 발생한다.
  - [x] 주문의 주문 테이블이 존재하지 않으면 예외가 발생한다.
  - [x] 빈 테이블이면 예외가 발생한다.
  - [x] 주문 상태를 `조리`로 등록한다.
  - 주문 시간을 현재 시간으로 등록한다.
- [x] 주문 목록을 조회할 수 있다.
- [x] 주문 상태를 변경할 수 있다.
  - [x] 주문이 존재하지 않으면 예외가 발생한다.
  - [x] 주문 상태가 `계산 완료`면 예외가 발생한다.

### 단체 지정

- [x] 테이블들을 단체 지정할 수 있다.
  - [x] 1개 이하의 주문 테이블을 단체 지정할 경우 예외가 발생한다.
  - [x] 단체 지정하려는 주문 테이블 중 실제 주문 테이블에 존재하지 않는 테이블이 있으면 예외가 발생한다.
  - [x] 빈 테이블이 아니면 예외가 발생한다.
  - [x] 이미 단체 지정된 테이블이면 예외가 발생한다.
  - 단체 지정 날짜를 현재 시간으로 등록한다.
  - [x] 테이블들을 빈 테이블에서 주문 테이블로 설정한다.
- [x] 단체 지정을 해제할 수 있다.
  - [x] 테이블들의 주문 상태가 `조리` 또는 `식사`면 예외가 발생한다.

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

---

## 의존성 리팩터링

- [X] Menu -> MenuGroup
  - AS-IS : 단방향 직접 참조
  ```mermaid
  graph LR
    Menu --> MenuGroup
  ```
  - TO-BE : 단방향 간접 참조
  ```mermaid
  graph LR
    Menu -.-> MenuGroup
  ```
- [ ] Menu <-> MenuProduct
  - AS-IS : 양방향 직접 참조
  ```mermaid
  graph LR
    Menu --> MenuProduct
    MenuProduct --> Menu
  ```
  - TO-BE : 단방향 직접 참조
  ```mermaid
  graph LR
    MenuProduct --> Menu
  ```
- [ ] MenuProduct -> Product
  - AS-IS : 단방향 직접 참조
  ```mermaid
  graph LR
    MenuProduct --> Product
  ```
  - TO-BE : 단방향 간접 참조
  ```mermaid
  graph LR
    MenuProduct -.-> Product
  ```
- [ ] OrderLineItem -> Menu
  - AS-IS : 단방향 직접 참조
  ```mermaid
  graph LR
    OrderLineItem --> Menu
  ```
  - TO-BE : 단방향 간접 참조
  ```mermaid
  graph LR
    OrderLineItem -.-> Menu
  ```
- [ ] Order <-> OrderLineItem
  - AS-IS : 양방향 직접 참조
  ```mermaid
  graph LR
    Order --> OrderLineItem
    OrderLineItem --> Order
  ```
  - TO-BE : 단방향 직접 참조
  ```mermaid
  graph LR
    OrderLineItem --> Order
  ```
- [ ] Order -> OrderTable
  - AS-IS : 단방향 직접 참조
  ```mermaid
  graph LR
    Order --> OrderTable
  ```
  - TO-BE : 단방향 간접 참조
  ```mermaid
  graph LR
    Order -.-> OrderTable
  ```
- [ ] OrderTable <-> TableGroup
  - AS-IS : 양방향 직접 참조
  ```mermaid
  graph LR
    OrderTable --> TableGroup
    TableGroup --> OrderTable
  ```
  - TO-BE : 단방향 직접 참조
  ```mermaid
  graph LR
    OrderTable --> TableGroup
  ```

