# 키친포스

## 요구 사항

- Menu Group 생성
  - [x] MenuGroup 을 입력받는다.
    - [x] 저장한다.

- Menu Group 반환
  - [x] 전체 MenuGroup 을 반환받는다.

- Menu 생성
  - [x] Menu 를 입력받는다.
    - [x] price 는 null 이거나 0 보다 작을 수 없다. (0은 가능하다.)
    - [x] 존재하지 않는 Menu Group 이면 안된다.
    - [x] Menu 에 포함되어 있는 MenuProducts 가 존재하지 않으면 안된다.
    - [x] Menu Product 의 총합보다 price 가 더 크면 안된다.
    - [x] Menu 와 Menu Products 를 저장한다.

- Menu 반환
  - [x] 전체 Menu 를 반환받는다

- Product 생성
  - [x] Product 를 입력 받는다.
    - [x] price 가 null 이거나 0 보다 작을 수 없다. (0은 가능하다.)
    - [x] Product 를 저장한다.
- Product 반환
  - [x] 전체 Product 를 반환받는다.

- Table Group 생성
  - [x] TableGroup 을 입력받는다.
    - [x] OrderTables 에서 ID 들을 꺼내온다.
    - [x] OrderTables 가 null 이거나, 1개 이하이면 안된다.
    - [x] 입력받은 OrderTables 가 존재하지 않으면 안된다.
    - [x] OrderTables 중 하나라도 주문이 가능한 상태이거나, 혹은 table group 에 속하면 안된다.
    - [x] table group 을 저장한다.
    - [x] 이제 OrderTable 에게 Table Group 을 할당하고 Empty 를 false 로 설정한다.
    - [x] OrderTable 을 업데이트한다.

- Table Group 삭제
  - [x] TableGroup ID 를 입력받는다.
    - [x] OrderTables 를 가져온다.
    - [x] OrderTables 중, 현재 요리, 식사 중인 것이 있으면 안된다.
    - [x] table group 할당을 해제해주고, orders 를 주문 가능한 상태로 만든다.
    - [x] orders table  을 업데이트한다.

- Order 생성
  - [x] Order 를 입력받는다.
    - [x] 주문한 메뉴는 하나 이상이어야 한다.
    - [x] 주문한 메뉴 중 존재하지 않는 메뉴가 있으면 안된다.
    - [x] OrderTable 이 존재하지 않으면 안된다.
    - [x] 해당 Table 에서 주문이 불가능한 상태이면 주문할 수 없다.
    - [x] 현재 상태를 Cooking, 으로 바꾸고 또 주문 시각을 현재로 설정해준다.
    - [x] 주문한 orderLineItem 도 저장해준다.

- 전체 Order 반환
  - [x] 전제 Order 를 반환한다.

- Order 의 상태를 변경한다.
  - [x] Order 의 id 와 OrderStatus 를 입력받는다.
    - [x] 이미 존재하는 Order 여야한다.
    - [x] 이미 식사를 완료한 상태이면 Status 를 변경할 수 없다.
    - [x] Order 를 업데이트한다.
    - [x] Order 를 반환한다.

- OrderTable 저장
  - [x] OrderTable 을 입력받는다.
    - [x] OrderTable 의 TableGroupId 를 null 로 설정한다. (table group 에 속해있지 않기 때문에)

- 전체 OrderTable 반환
  - [x] 전체 OrderTable 을 반환받는다.

- OrderTable 의 empty 상태를 변경한다.
  - [x] OrderTableId 와 Empty 를 입력받는다.
    - [x] 존재하지 않는 OrderTableId 면 안된다.
    - [x] OrderTable 이 이미 table group 에 속해있으면 안된다.
    - [x] OrderTable 에서 시킨 orders 중 하나라도 요리, 식사 중이면 안된다.
    - [x] OrderTable 의 empty 를 업데이트한다.

- OrderTable 의  number of guest 를 변경한다.
  - [x]  orderTableId 와 numberOfGuest 를 입력받는다.
    - [x] 바꾸려는 number of guest 가 0 미만이면 안된다.
    - [x] 존재하지 않는 orders table 이면 안된다.
    - [x] 주문이 불가능한 상태이면 안된다.
    - [x] OrderTable 의 numberOfGuest 를 업데이트한다.

## 용어 사전

| 한글명 | 영문명 | 설명 |
| --- | --- | --- |
| 상품 | product | 메뉴를 관리하는 기준이 되는 데이터 |
| 메뉴 그룹 | menu group | 메뉴 묶음, 분류 |
| 메뉴 | menu | 메뉴 그룹에 속하는 실제 주문 가능 단위 |
| 메뉴 상품 | menu product | 메뉴에 속하는 수량이 있는 상품 |
| 금액 | amount | 가격 * 수량 |
| 주문 테이블 | orders table | 매장에서 주문이 발생하는 영역 |
| 빈 테이블 | empty table | 주문을 등록할 수 없는 주문 테이블 |
| 주문 | orders | 매장에서 발생하는 주문 |
| 주문 상태 | orders status | 주문은 조리 ➜ 식사 ➜ 계산 완료 순서로 진행된다. |
| 방문한 손님 수 | number of guests | 필수 사항은 아니며 주문은 0명으로 등록할 수 있다. |
| 단체 지정 | table group | 통합 계산을 위해 개별 주문 테이블을 그룹화하는 기능 |
| 주문 항목 | orders line item | 주문에 속하는 수량이 있는 메뉴 |
| 매장 식사 | eat in | 포장하지 않고 매장에서 식사하는 것 |

