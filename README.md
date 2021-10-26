# 키친포스

## 요구 사항
### MenuGroup

* [x] MenuGroup을 등록할 수 있다.
* [x] 존재하는 MenuGroup 조회를 할 수 있다.

### Menu

* [x] Menu를 등록할 수 있다.
  * [x] Menu 생성 후에 Menu의 MenuProduct 내에 menu_id를 할당해주어야 한다.
* [x] Menu의 가격이 올바르지 않으면 등록할 수 없다.
    * [x] 가격은 null이어서는 안된다.
    * [x] 가격은 0보다 작을 수 없다.
* [x] 존재하지 않은 MenuGroup이면 등록할 수 없다.
* [x] Menu에 속하는 MenuProduct의 Product가 존재하지 않으면 등록할 수 없다.
* [x] Menu의 가격은 Menu에 들어가는 모든 Product 가격들의 합 보다 높아서는 안된다.
* [x] 존재하는 Menu 조회를 할 수 있다.

### Order

* [x] Order 요청 시에는 order_line_item이 반드시 있어야 한다.(메뉴 주문을 무조건 해야 한다)
* [x] 동일한 Menu가 별개의 order_line_item에 들어있어서는 안된다.(메뉴의 종류의 개수와 order_line_item의 개수가 같아야 한다)
* [x] 비어있는 order_table에서 order 요청을 할 수 없다.
* [x] Order를 등록할 수 있다.
    * [x] Order 생성 후에 OrderLineItem의 order_id에 생성된 Order의 id가 할당되어야 한다.
* [x] 존재하는 Order 조회를 할 수 있다.
  * [x] order_line_item 또한 조회 되어야 한다.
* [x] Order의 상태를 바꿀 수 있다.
  * [x] 존재하는 Order 이어야 한다.
  * [x] 상태 변경 후에는 order_line_item을 포함한 Order를 반환받는다.
  * [x] 이미 COMPLETION 상태의 Order는 상태를 변경할 수 없다.

### Product

* [x] Product를 등록할 수 있다.
* [x] Product의 가격이 올바르지 않으면 등록할 수 없다.
    * [x] 가격은 null이어서는 안된다.
    * [x] 가격은 0보다 작을 수 없다.
* [x] 존재하는 Product를 조회할 수 있다.

### TableGroup

* [ ] TableGroup을 등록할 수 있다.
* [ ] 올바른 order_table 값을 요청해야 한다.
    * 주어지는 order_table 은 비어있어서는 안된다.
    * 2개 미만의 order_table 요청이어서는 안된다.
    * 중복된 order_table 요청이어서는 안된다.
    * 등록되어있는 order_table 이어야 한다.
    * 다른 table_group에 속한 order_table이 있어서는 안된다.
* [ ] TableGroup 생성 후에 해당 group에 속한 order_table들의 table_group_id에 생성된 TableGroup의 id가 할당되어야 하고, empty 필드가 false로 변경되어야 한다.
* [ ] TableGroup 생성 후에 TableGroup의 List<OrderTable> orderTables 필드에 OrderTable들이 들어가 있어야 한다.
* [ ] TableGroup을 해제할 수 있다.
* [ ] TableGroup에 속한 order_table이 현재 이용 중이면(COMPLETION이 아니면) 해제할 수 없다.

### OrderTable

* [ ] OrderTable을 추가할 수 있다.
* [ ] 등록된 OrderTable을 조회할 수 있다.
* [ ] OrderTable의 Empty 여부를 수정할 수 있다.
    * 존재하지않은 OrderTable Id가 주어져서는 안된다.
    * TableGroup에 속한 OrderTable의 Empty는 수정할 수 없다.
    * OrderTable이 현재 이용 중이면(COMPLETION이 아니면) 상태를 변경할 수 없다.
* [ ] OrderTable의 NumberOfGuests를 수정할 수 있다.
    * 0 미만의 값으로 수정할 수 없다.
    * 존재하지않은 OrderTable Id가 주어져서는 안된다.
    * empty가 true인 OrderTable이어서는 안된다.
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
