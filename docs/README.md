# 리팩토리 방향

- [x] 메뉴 정보가 변경되더라도 주문 항목이 변경되지 않게 구현한다.

## 클래스 간 의존성 개선
- [x] 클래스 사이 의존 관계를 단방향이 되도록한다.

### 리팩토링한 의존관계
- Order 
  - -> OrderLineItems
  - -> OrderTable
  - -> OrderLineItem
  - -> OrderStatus

- OrderTables
  - -> OrderTable
  - -> TableGroup

- OrderLineItems
  - -> OrderLineItem

- OrderTable
  - -> TableGroup
  - -> NumberOfGuests

- OrderLineItem
  - -> Menu

- Menu
  - -> MenuProducts
  - -> MenuProduct
  - -> MenuGroup
  - -> Price

- MenuProducts
  - -> MenuProduct
  - -> Price

- MenuProduct
  - -> Product
  - -> Price

- Product
  - -> Price

## 패키지 간 의존성 개선
- [x] 패키지 사이 의존 관계를 단방향이 되도록한다.

### 패키지 구조

- 주문(order)
  - Order
  - OrderLineItems
  - OrderStatus

- 메뉴(menu)
  - MenuGroup
  - Menu
  - MenuProducts
  - MenuProduct
  - Product
  - Price

- 테이블(table)
  - TableGroup
  - OrderTables
  - OrderTable
  - NumberOfGuests

### 패키지 의존성
- 주문
  - -> 메뉴
  - -> 테이블
