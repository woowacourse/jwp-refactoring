# 키친포스

## 요구 사항

### MenuGroups
- [x] `POST /api/menu-groups` 요청을 통해 MenuGroup을 추가할 수 있다.
    - [x] MenuGroup을 DB에 저장한다.
    - [x] MenuGroup의 이름은 Null 이어서는 안된다.

- [x] `GET /api/menu-groups` 요청을 통해 MenuGroup 목록을 조회할 수 있다.
    - [x] MenuGroup 리스트를 DB에서 찾아 반환한다.

### Menu
- [x] `POST /api/menus` 요청을 통해 Menu를 추가할 수 있다.
    - [x] Menu를 DB에 저장한다.
        - [x] Menu 가격이 NULL이거나 0보다 작으면 예외가 발생한다.
        - [x] Menu의 MenuGroupId가 DB에 존재하지 않으면 예외가 발생한다.
        - [x] MenuProduct들에 있는 모든 MenuProduct의 Product 리스트를 DB에서 조회해 가격을 더한다.
            - [x] 모든 Product 리스트의 Id는 DB에 존재해야 한다.
                - [x] 하나라도 존재하지 않으면 예외가 발생한다.
        - [x] 전체 Product * 가격이 Menu의 가격보다 작다면 예외가 발생한다.
        - [x] Menu의 모든 MenuProduct들도 함께 저장한다.

- [x] `GET /api/menus` 요청을 통해 Menu 목록을 조회할 수 있다.
    - [x] 각 Menu에는 Menu에 속하는 MenuProduct들이 포함되어 있다.

### Order
- [x] `POST /api/orders` 요청을 통해 Order를 추가할 수 있다.
    - [x] Order에 속하는 OrderLineItem 목록이 없는 경우 예외가 발생한다.
    - [x] Order에 속하는 OrderLineItem 중 실제 없는 메뉴가 존재하는 경우 예외가 발생한다.
      (Order에 속하는 OrderLineItem 개수를 DB 조회 후 비교)
    - [x] Order의 상태는 `COOKING`으로 기록된다.
    - [x] Order 시각은 현재 시각을 기입한다.
    - [x] Order이 추가될 때 Order에 속하는 OrderLineItem들도 함께 추가된다.

- [x] `GET /api/orders` 요청을 통해 Order 목록을 조회할 수 있다.
    - [x] 각 Order에는 Order에 속하는 OrderLineItem 목록이 포함되어 있다.

- [x] `PUT /api/orders/{orderId}/order-status` 요청을 통해 매장 Order 상태를 수정할 수 있다.
    - [x] OrderId를 통해 DB에서 기존 Order를 가져온다.
        - [x] DB에 존재하지 않으면 예외가 발생한다.
    - [x] 기존 Order의 OrderStatus가 COMPLETION인 경우 예외가 발생한다.
    - [x] 기존 Order의 OrderStatus를 매개변수 Order의 OrderStatus로 세팅한다.
    - [x] 기존 Order를 다시 DB에 저장한다.
    - [x] OrderId를 통해 DB에서 Order의 OrderLineItem 리스트를 가져온다.
    - [x] OrderLineItem 리스트를 포함한 Order를 반환한다.

### Product
- [x] `POST /api/products` 요청을 통해 Product를 추가할 수 있다.
    - [x] 새로운 Product의 가격이 NULL이거나 0보다 작으면 예외가 발생한다.

- [x] `GET /api/products` 요청을 통해 Product 리스트를 조회할 수 있다.
    - [x] DB에 저장되어있는 모든 Product 리스트를 반환한다.

### TableGroup
- [x] `POST /api/table-groups` 요청을 통해 Order TableGroup을 추가할 수 있다.
    - [x] TableGroup의 OrderTable 리스트가 비어있거나 사이즈가 2보다 작으면 예외가 발생한다.
    - [x] TableGroup의 OrderTableIds 리스트와 기존 OrderTables의 크기가 다르면 예외가 발생한다.
    - [x] 기존 OrderTable들은 비어있지(!empty) 않거나, TableGroupId를 가지고 있는 경우(!NULL) 예외가 발생한다.
    - [x] TableGroup에 현재 시간을 반영한다.
    - [x] OrderTable 리스트의 TableGroupId를 TableGroup의 Id로 바꾼다.
    - [x] OrderTable 리스트의 empty값을 false로 바꾸어 TableGroup과 관계가 생김을 표현한다.
    - [x] OrderTable 리스트를 DB에 저장한다.
    - [x] TableGroup에 OrderTable 리스트를 추가한다.

- [x] `DELETE /api/table-groups/{tableGroupId}` 요청을 통해 Order TableGroup를 해제 할 수 있다.
    - [x] TableGroupId로 DB에서 OrderTable 리스트를 조회한다.
    - [x] 각 OrderTable의 ID나, Order의 OrderStatus가 COOKING 또는 MEAL인 경우 예외가 발생한다.
    - [x] OrderTable 리스트의 TableGroupId를 모두 NULL로 바꾼다.
    - [x] OrderTable 리스트의 empty값을 false로 바꾼다.

### OrderTable
- [x] `POST /api/tables` 요청을 통해 Order Table을 추가할 수 있다.
    - [x] 요청 매개변수의 OrderTable의 Id와 TableGroupId를 NULL로 세팅한다.

- [x] `GET /api/tables` 요청을 통해 Order Table 목록을 조회할 수 있다.
    - [x] DB에 저장되어있는 모든 OrderTable 리스트를 조회해 반환한다.

- [x] `PUT /api/tables/{orderTableId}/empty` 요청을 통해 Order Table을 Order 불가 Table(빈 Table)로 바꿀 수 있다.
    - [x] DB에서 OrderTableId로 OrderTable을 조회한다. 존재하지 않을 경우 예외가 발생한다.
    - [x] 기존 OrderTable의 TableGroupId가 NULL이 아니면 예외가 발생한다.
    - [x] DB에 OrderTableId가 존재하거나, 신규 Order의 OrderTstatus가 COKKING 또는 MEAL인 경우 예외가 발생한다.
    - [x] 기존 OrderTable의 empty값을 신규 OrderTable의 empty값으로 세팅한다.

- [x] `PUT /api/tables/{orderTableId}/number-of-guests` 요청을 통해 Order Table의 손님 수를 바꿀 수 있다.
    - [x] OrderTable의 NumberOfGuest가 0보다 작으면 예외가 발생한다.
    - [x] OrderTableId로 DB에서 기존 OrderTable을 조회한다. 존재하지 않으면 예외가 발생한다.
    - [x] 기존 OrderTable의 empty가 true인 경우 예외가 발생한다.
    - [x] 기존 OrderTable의 NumberOfGuest를 신규 OrderTable의 NumberOfGuest값으로 바꾼다.

<br>

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

<br>

## 엔티티 단위 테스트
- [x] Menu
  - [x] name null, blank
  - [x] price null, negative
  - [x] MenuGroup null
- [x] MenuGroup
  - [x] name null, blank
- [x] MenuProduct
  - [x] menu null
  - [x] product null
  - [x] quantity null, negative
- [x] MenuProducts
  - [x] menu.price > all(product.price * quantity)
- [x] Product
  - [x] name null, blank
  - [x] price null, negative
- [x] Price
  - [x] value null, negative
- [x] Order
  - [x] orderTable null
  - [x] already completion
- [x] Orders
  - [x] completed status
- [x] OrderLineItem
  - [x] order null
  - [x] menu null
  - [x] quantity null, negative
- [x] OrderLineItems
  - [x] empty
  - [x] menu duplicate
- [x] OrderTable
  - [x] numberOfGuests negative
  - [x] groupBy status
  - [x] ungroup status
  - [x] changeNumberOfGuests
    - [x] negative
    - [x] orderTable status empty
  - [x] changeEmpty nonNull tableGroup
- [x] OrderTables
  - [x] orderTable count < 2
  - [x] orderTable already not empty or grouped
  - [x] groupBy
- [x] TableGroup

## 추가 리팩토링
- [x] 커스텀 Exception 추가
- [x] 인수테스트 DisplayName 변경하기
- [x] 엔티티 원시값 포장
  - [x] quantity
  - [x] price
  - [x] name
  - [x] numberOfGuests
- [x] DTO getter 디미터 법칙
- [ ] OrderTable에서 TableGroup 값에 따라 empty 설정을 해야하는가 고민
- [x] 서비스 계층 의존 -> 최초의 순환참조 경험 -> Repository 계층 의존하도록 변경

<br>

## 의존성 리팩토링 
- [ ] 메뉴의 이름과 가격이 변경되면 주문 항목도 함께 변경된다. 메뉴 정보가 변경되더라도 주문 항목이 변경되지 않게 구현한다.
- [x] 클래스 간의 방향도 중요하고 패키지 간의 방향도 중요하다. 클래스 사이, 패키지 사이의 의존 관계는 단방향이 되도록 해야 한다.
- [x] 나눈 의존관계에 따라 패키지 구조를 분리한다.
  - [x] ControllerAdvice를 분리한다.
  - [x] 단위 테스트 제대로 복제되었는지 확인하기
  - [x] Exception 제대로 패키지 나뉘어졌는지 확인하기
  - [ ] 어쩔 수 없이 의존중인 로직 서비스 레이어로 다시 삐져나오도록(?) 리팩토링

- 모듈을 극단적으로 분리해보는 경험
  - 중복코드가 잔뜩 발생하더라도, 물리적인 서버 분리도 가능할 정도로 구성해보기

- [x] menu 패키지 리팩토링
- [x] menugroup 패키지 리팩토링
- [x] order 패키지 리팩토링
- [x] product 패키지 리팩토링
- [x] table 패키지 리팩토링
- [x] tablegroup 패키지 리팩토링
- [x] 이벤트 리스너 적용해보기
