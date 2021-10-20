# 키친포스

## 요구 사항

- [ ] MenuGroup
  - [ ] 새로운 MenuGroup을 생성해 DB에 저장한다.
  - [ ] 모든 MenuGroup들을 DB에서 찾아 반환한다.

- [ ] Menu
  - [ ] 새로운 Menu를 생성해 DB에 저장한다.
    - [ ] Menu의 가격이 null이거나 0보다 작으면 예외를 발생시킨다.
    - [ ] Menu의 MenuGroup의 Id가 DB에 존재하지 않으면 예외를 발생시킨다.
    - [ ] Menu에 있는 모든 Product들을 DB에서 조회해 가격을 더한다.
      - [ ] 모든 Product들의 Id는 DB에 존재해야 한다.
        - [ ] 하나라도 존재하지 않으면 예외를 발생시킨다.
    - [ ] 요청 매개변수 Menu의 가격이 Menu에 있는 모든 Product들의 가격의 합보다 크면, 예외를 발생시킨다.
    - [ ] Menu를 DB에 저장한다.
    - [ ] Menu의 모든 MenuProduct들에 Menu의 Id를 할당해 DB에 저장한다.
    - [ ] Id를 갖고있는 Menu를 반환한다.
      - [ ] Menu의 모든 MenuProduct들은 Id와 Menu의 Id를 갖고있다.
  - [ ] 모든 Menu들을 DB에서 찾아 반환한다.
    - [ ] 각 Menu는 MenuProduct들을 갖고있다.

- [ ] Order
  - [ ] 새로운 Order를 생성해 DB에 저장한다.
    - [ ] Order의 OrderLineItems가 null이면 예외를 발생시킨다.
    - [ ] Order의 모든 OrderLineItems의 Menu의 Id들을 취합한다.
    - [ ] Order의 OrderLineItems의 size가 DB에 있는 OrderLineItems의 Menu의 개수와 같아야 한다.
      - [ ] OrderLineItem들은 Menu의 Id들로 찾는다.
      - [ ] 다르면 예외를 발생시킨다.
    - [ ] Order의 Id를 null로 세팅한다.
    - [ ] Order의 OrderTableId로 DB에서 OrderTable을 가져온다.
      - [ ] 존재하지 않으면 예외를 발생시킨다.
    - [ ] OrderTable이 비어있으면 예외를 발생시킨다.
    - [ ] Order의 OrderTableId를 DB에서 조회한 OrderTable의 Id로 세팅한다.
    - [ ] Order의 OrderStatus를 COOKING으로 세팅한다.
    - [ ] Order의 OrderTime을 현재시간으로 세팅한다.
    - [ ] Order를 DB에 저장한다.
    - [ ] Order의 모든 OrderLineItems에 Order의 Id를 세팅해서 DB에 저장한다.
    - [ ] Id를 갖고있는 Order를 반환한다.
      - [ ] Order의 모든 OrderLineItems들은 Id를 갖고있다.
  - [ ] DB에 저장되어있는 모든 Order들을 반환한다.
    - [ ] DB에서 모든 Order들을 조회해온다.
      - [ ] DB에서 모든 Order들을 조회해온다.
      - [ ] Order 각각의 Id로 DB에서 해당 Order의 모든 OrderLineItem들을 조회해 Order에 세팅한다.
      - [ ] Order들을 반환한다.
        - [ ] 모든 Order들은 각각의 모든 OrderLineItem들을 갖고 있다.
  - [ ] Order의 OrderStatus를 변경한다.
    - [ ] Order의 Id로 DB에서 Order를 가져온다.
      - [ ] DB에 존재하지 않으면 예외를 발생시킨다.
    - [ ] DB에서 조회한 Order의 OrderStatus가 COMPLETION이면, 예외를 발생시킨다.
    - [ ] DB에서 꺼내온 Order의 OrderStatus를 매개변수 Order의 OrderStatus로 세팅한다.
    - [ ] DB에서 꺼내온 Order를 DB에 다시 저장한다.
    - [ ] Order의 Id로 DB에서 Order의 모든 OrderLineItem들을 가져온다.
    - [ ] OrderLineItem들을 포함한 Order를 반환한다.
  
- [ ] Product
  - [ ] 새로운 Product를 생성해 DB에 저장한다.
    - [ ] 새로운 Product의 가격이 null이거나 0보다 작으면, 예외를 발생시킨다.
    - [ ] DB에 새로운 Product를 저장한다.
    - [ ] DB에 저장한 Product를 반환한다.
  - [ ] DB에 저장되어있는 모든 Product들을 반환한다.

- [ ] TableGroup
  - [ ] 새로운 TableGroup을 생성해 DB에 저장한다.
    - [ ] 요청 매개변수의 TableGroup에서 모든 OrderTable들을 꺼낸다.
    - [ ] 꺼낸 모든 OrderTable들의 컬렉션이 null이거나 사이즈가 2보다 작으면 예외를 발생시킨다.
    - [ ] 모든 OrderTable들의 Id들을 추출한다.
    - [ ] 추출한 Id에 해당하는 OrderTable들을 DB에서 조회한다.
    - [ ] 요청 매개변수 TableGroup의 OrderTables와 DB에서 조회한 OrderTables의 size가 다르면 예외를 발생시킨다.
    - [ ] DB에서 조회한 OrderTable들은 각각 모두 empty 변수값이 true여야 하고, TableGroupId가 null이어야 한다.
      - [ ] 조건에 맞지 않으면 예외를 발생시킨다.
    - [ ] 요청 매개변수의 TableGroup에 현재 시간을 세팅한다.
    - [ ] 요청 매개변수의 TableGroup을 DB에 저장한다.
    - [ ] DB에서 조회한 OrderTable들의 TableGroupId를 DB에 저장한 TableGroup의 Id로 세팅한다.
    - [ ] DB에서 조회한 OrderTable들의 empty값을 false로 세팅한다.
    - [ ] DB에 OrderTable들을 저장한다.
    - [ ] TableGroup에 OrderTable들을 세팅한다.
    - [ ] TableGroup을 반환한다.
  - [ ] TableGroup를 해제한다.
    - [ ] TableGroupId로 DB에서 OrderTable들을 조회한다.
    - [ ] OrderTable들의 Id를 추출한다.
    - [ ] OrderTable들의 Id가 모두 DB에 존재하고, 각 OrderTable에 해당하는 Order의 OrderStatus값이 COOKING, MEAL중 하나여야 한다.
      - [ ] 그렇지 않으면 예외를 발생시킨다.
    - [ ] OrderTable들의 TableGroupId를 모두 null로 세팅한다.
    - [ ] OrderTable들의 empty값을 false로 세팅한다.
    - [ ] DB에 OrderTable들을 저장한다.

- [ ] OrderTable
  - [ ] 새로운 OrderTable을 생성해 DB에 저장한다.
    - [ ] 요청 매개변수의 OrderTable의 Id와 TableGroupId를 null로 세팅한다.
    - [ ] DB에 OrderTable을 저장한다.
  - [ ] DB에 저장되어있는 모든 OrderTable들을 조회해 반환한다.
  - [ ] OrderTable의 Empty상태를 바꾼다.
    - [ ] DB에서 OrderTableId로 OrderTable을 조회한다.
      - [ ] 존재하지 않으면 예외를 발생시킨다.
    - [ ] OrderTable의 TableGroupId가 null이 아니면 예외를 발생시킨다. !!!!!!!!!!!!!!!!!
    - [ ] OrderTable의 Id가 DB에 존재하고, 각 OrderTable의 OrderStatus값이 COOKING, MEAL중 하나여야 한다.
      - [ ] 그렇지 않으면 예외를 발생시킨다.
    - [ ] DB에서 조회한 OrderTable의 empty값을 요청 매개변수 OrderTable의 empty값으로 세팅한다.
    - [ ] DB에서 조회한 OrderTable을 DB에 저장한다.
    - [ ] DB에 저장한 OrderTable을 반환한다.
  - [ ] OrderTable의 NumberOfGuest값을 변경한다.
    - [ ] 요청 매개변수로 받은 OrderTable의 NumberOfGuest값을 꺼낸다.
    - [ ] NumberOfGuest의 값이 0보다 작으면 예외를 발생시킨다.
    - [ ] 요청 매개변수로 받은 OrderTableId로 DB에서 OrderTable을 조회한다.
      - [ ] DB에 존재하지 않으면 예외를 발생시킨다.
    - [ ] DB에서 조회한 OrderTable의 empty값이 true이면 예외를 발생시킨다.
    - [ ] DB에서 조회한 OrderTable의 NumberOfGuest값을 매개변수로 받은 OrderTable의 NumberOfGuest값으로 세팅한다.
    - [ ] DB에서 조회한 OrderTable을 DB에 저장한다.
    - [ ] DB에 저장한 OrderTable을 반환한다.

<br/>

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
