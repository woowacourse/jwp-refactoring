# 키친포스

## 요구 사항

### Product

- [x] 상품의 목록을 조회할 수 있다.
- [x] 상품을 등록할 수 있다.
    - [x] 상품의 가격이 올바르지 않으면 상품을 등록할 수 없다.
        - [x] 상품의 가격이 비어있거나, 상품의 가격이 0보다 작으면 예외가 발생한다.

### MenuGroup

- [x] 메뉴의 묶음을 조회할 수 있다.
- [x] 메뉴의 묶음을 등록할 수 있다.
    - [x] 등록 시에는 이름을 보내준다(ex: 추천메뉴)

### Menu

- [x] 메뉴의 목록을 조회할 수 있다.
    - [x] `MENU` 테이블과 `MENU PRODUCT` 테이블을 조회하여 결과를 받아온다.
- [x] 메뉴를 등록할 수 있다.
    - [x] 메뉴의 가격이 비어있거나 0보다 작으면 예외를 발생한다.
    - [x] 받아온 menuGroupId가 존재하지 않으면 예외를 발생한다.
    - [x] List(MenuProduct) 의 리스트를 순회하면서 MenuProduct의 product id로 product를 탐색한다.
        - [x] product가 없으면 예외를 발생한다.
        - [x] product의 가격과 menuProduct의 양으로 가격의 합계(sum) 을 구한다.
            - [x] menu.price가 sum 보다 크면 예외를 발생한다.
    - [x] 위의 조건을 모두 만족하면 menu를 저장한다.
    - [x] 위의 조건을 모두 만족하면 menuProduct를 저장한다.``

### Order

- [x] 주문을 등록할 수 있다.
    - [x] 주문에 속하는 수량이 있는 메뉴(OrderLineItem)이 비어있으면 예외를 발생한다.
    - [x] 주문에 속하는 수량이 있는 메뉴에서 menu의 id들을 추출하여 등록되어잇는 메뉴의 수와 다르면 예외를 발생한다.
    - [x] order table id로 ordertable을 찾아서 없으면 예외를 발생한다.
    - [x] order table 내부의 empty 값이 true이면 예외를 발생한다.
    - [x] order의 상태를 업데이트 한 후 저장한다.
    - [x] orderLineItem에 대해서 orderId값을 set하고 저장한다.
- [x] 주문의 목록을 조회할 수 있다.
    - [x] `ORDER` 테이블과 `ORDER_LINE_ITEM` 테이블을 조합하여 결과를 가져온다.
- [x] 한 개의 주문에 대하여 주문의 상태를 변경할 수 있다.
    - [x] 입력받은 order id에 대해서 ORDER 가 존재하지 않으면 예외를 발생한다.
    - [x] 입력받은 order id에 해당하는 주문의 상태가 이미 'COMPLETION' 이면 예외를 발생한다.
    - [x] orderstatus를 새로 저장한다.
    - [x] order를 새로 저장한다.

### Table

- [x] table 을 등록할 수 있다.
- [x] table 전체 목록을 조회한다.
- [ ] table의 empty 상태를 바꾼다.
    - [x] orderTableId로 DB를 조회했을 때 없으면 예외를 발생한다.
    - [x] DB 조회 시 찾은 ordertable에 tableGroupID가 있으면 예외를 발생한다.
    - [x] 찾은 ordertable의 주문 상태가 COMPLETION이 아니면 예외를 발생한다.
    - [x] ordertable의 isEmpty 상태를 변경한다.
- [ ] table의 손님 수를 등록한다.
    - [ ] 받은 손님의 수가 0보다 작으면 예외를 발생한다.
    - [ ] order table id 로 조회했을 때 order table이 없으면 예외가 발생한다.
    - [ ] 찾은 order table 이 empty이면 예외를 발생한다.
    - [ ] ordertable에 손님의 수를 등록하고 저장한다.

### TableGroup

- [ ] table group을 등록할 수 있다.
    - [ ] 내부의 order table의 사이즈가 1보다 작거나 같으면 예외를 발생한다.
    - [ ] order table id로 저장된 table을 찾아서 둘의 사이즈가 다르면 예외를 발생한다.
    - [ ] 찾은 ordertables를 순회하면 각 ordertable이 empty거나 ordertable의 tablegroupid가 비어잇으면 에외를 발생한다.
    - [ ] 테이블 그룹의 생성날짜를 저장한다.
    - [ ] 테이블 그룹을 저장하고 order table을 set 한다.
- [ ] table group을 삭제할 수 있다.
    - [ ] table group id로 ordertable을 찾는다.
    - [ ] 찾은 order table에 대해 id를 추출하고,
    - [ ] 찾은 Id에 해당하는 주문들이 COMPLETION 상태가 아니라면 예외를 발생한다.
    - [ ] order table을 null, false 로 초기화한다.

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
