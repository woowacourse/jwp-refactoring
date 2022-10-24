# 키친포스

## ERD 다이어그램
![image](https://user-images.githubusercontent.com/57438644/197608335-931e00f2-0666-4b7e-a505-b569f068d5a9.png)

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


## 도메인 값 정리

### 메뉴
- menu(메뉴): 주문이 가능한 메뉴 `후라이드치킨`, `양념치킨`
  - menu_group: `한마리메뉴`, '두마리메뉴`
- product: 메뉴를 관리하는 기준 데이터 `후라이드치킨`, `양념치킨`
- menu_product: menu에 속하는 수량이 있는 product 관리

### 주문
- orders(주문): 매장에서 주문
  - `order_table_id`, 주문상태, 주문시간
- order_table_id: 주문이 발생하는 테이블
  - `table_group_id`, number_od_guests, empty
- table_group: 통합 계산을 위한 테이블을 그룹화

### OrderLineItem
- 메뉴, 주문, 수량(quantity)을 가짐


# 서비스 요구사항
### ProductService
- [x] Product를 생성한다.
  - [x] price가 null이거나 0인 경우 예외를 던진다.
- [x] Product를 전체 조회한다.

### MenuService
- [ ] menu를 생성한다.
  - [ ] price가 null이거나 0인 경우 예외를 던진다.
  - [ ] menuGroup이 없는경우 예외를 던진다.
  - [ ] product의 총 price가 menu보다 큰 경우 예외를 던진다.
- [ ] menu를 전체 조회한다.

### MenuGroupService
- [ ] menuGroup을 생성한다.
- [ ] menuGroup을 전체 조회한다.

### OrderService
- [ ] Order를 생성한다.
  - [ ] orderLineItems가 비어있는 경우 예외를 던진다.
  - [ ] orderLineItems의 개수와 menu에 포함된 개수가 일치하지 않으면 예외를 던진다.
  - [ ] orderTable이 없는 경우 예외를 던진다.
- [ ] order를 전체 조회한다.
- [ ] order의 status를 변경한다.
  - [ ] order의 상태가 `COMPLETION`인 경우 예외를 던진다.

### TableGroupService
* [ ] TableGroup을 생성한다.
  * [ ] orderTables가 비어있거나 사이즈가 2보다 작은 경우 예외를 던진다.
  * [ ] orderGroup이 가진 orderTables의 사이즈와 저장된 orderTables의 사이즈가 다른 경우 예외를 던진다.
  * [ ] 저장된 orderTables 중 비어있지 않은 경우 예외를 던진다.
  * [ ] 저장된 orderTables 중 tableGroupId가 null이 아닌 경우 예외를 던진다.
* [ ] tableGroup을 해제한다.
  * [ ] orderTables의 orderStatus가 `COOKING`, `MEAL`인 경우 예외를 던진다.

### TableService
- [ ] orderTable을 생성한다.
- [ ] orderTable을 전체 조회한다.
- [ ] orderTable의 empty를 변경한다.
  - [ ] tableGroupId가 null이 아닌 경우 예외를 던진다.
  - [ ] orderTable에 속한 order 중 `COOKING`, `MEAL`이 존재하는 경우 예외를 던진다.
- [ ] orderTable의 `changeNumberOfGuests`를 변경한다.
  - [ ] numberOfGuests가 0 미만인 경우 예외를 던진다.
  - [ ] 저장된 orderTable이 비어있는 경우 예외를 던진다.
