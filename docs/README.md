# 리팩토리 방향

- [ ] 메뉴 정보가 변경되더라도 주문 항목이 변경되지 않게 구현한다.

## 의존성 개선
- [ ] 클래스 사이 의존 관계를 단방향이 되도록한다. 
- [ ] 패키지 사이 의존 관계를 단방향이 되도록한다.

## 리팩토링 전 의존관계
- Order 
  - -> OrderLineItems
  - -> OrderTable
  - [ ] <-> OrderLineItem
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
  - [ ] <-> Order
  - -> Menu

- Menu
  - -> MenuProducts
  - [ ] <-> MenuProduct
  - -> MenuGroup
  - -> Price

- MenuProducts
  - -> MenuProduct
  - -> Price

- MenuProduct
  - [ ] <-> Menu
  - -> Product
  - -> Price

- Product
  - -> Price
