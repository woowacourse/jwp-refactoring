# 키친포스

## 요구 사항

- Menu(메뉴)
  - 메뉴를 추가할 수 있다.
    - name, price, menuGroupId, menuProducts를 입력받는다.
      - menuProducts는 productId, quantity를 가진다.
    - 추가한 메뉴의 정보를 반환한다.
    - 조건
      - price는 null이거나 음수일 수 없다.
      - menuGroupId가 있어야 한다. 
      - 모든 productId가 있어야 한다.
      - 모든 product의 가격 합보다 price이 같거나 커야한다.
  - 메뉴 목록을 확인할 수 있다.
    - 저장된 모든 메뉴를 반환한다.
- MenuGroup(메뉴 묶음)
  - 메뉴 묶음을 추가할 수 있다.
    - name을 입력받는다.
    - 추가한 MenuGroup을 반환한다.
  - 메뉴 묶음 목록을 확인할 수 있다.
- Product(상품)
  - 상품을 등록할 수 있다.
    - name, price를 입력받는다.
      - price는 null이거나 음수일 수 없다.
    - 추가한 Product를 반환한다.
  - 상품 목록을 확인할 수 있다.
- Order(주문)
  - 주문을 추가할 수 있다.
    - orderTable, orderLineItems을 입력한다. 
      - orderLineItems은 menuId, quantity를 가진다.
    - 추가한 주문을 반환한다.
    - 조건
      - orderLineItems가 empty면 안된다.
      - menuId들 총 개수와 orderLineItems의 수가 같아야 한다.
      - orderTable가 없거나 비어있으면 안된다. 
  - 현재 주문 목록을 확인할 수 있다. 
  - 주문의 OrderStatus를 수정할 수 있다.
    - orderId와 OrderStatus를 입력한다.
    - 조건
      - orderId이 있어야 한다.
      - 저장된 order의 OrderStatus가 COMPLETION면 안된다.
- Table(주문할 수 있는 테이블)
  - 주문 테이블(order table)을 추가할 수 있다.
    - numberOfGuests와 empty를 입력한다.
    - 추가한 OrderTable를 반환한다.
  - 주문 테이블 목록을 확인할 수 있다.
  - 주문 테이블의 empty를 변경한다.
    - orderTableId와 empty를 입력한다.
    - 변경된 OrderTable을 반환한다.
  - 방문한 손님 수를 변경할 수 있다.
    - orderTableId, numberOfGuests를 입력한다.
    - 변경된 OrderTable을 반환한다.
    - 조건
      - orderTableId이 있어야 한다.
      - numberOfGuests는 0이상이어야 한다.
- TableGroup(주문 테이블 묶음)
  - 통합 계산을 위해 개별 주문 테이블을 그룹화할 수 있다.
    - orderTables을 입력받는다.
      - OrderTable은 
    - 조건
      - orderTables의 개수는 2개 이상이어야한다.
      - orderTables의 모든 orderTable
  - 주문 테이블 묶음의 모든 테이블을 묶음 해제할 수 있다. 
    - tableGroupId을 입력받는다. 

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
