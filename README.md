# 키친포스

## 요구 사항

### 메뉴 그룹 (MenuGroup)
- [x] 메뉴 그룹을 등록할 수 있다.
- [x] 메뉴 그룹 목록을 조회할 수 있다.

### 메뉴 (Menu)
- [x] 메뉴를 등록할 수 있다.
  - [예외 처리]
    - [x] 메뉴의 가격이 0원 미만일 시
    - [x] 메뉴의 가격이 null일 시
    - [x] 메뉴 그룹이 존재하지 않을 시
    - [x] 메뉴 상품이 존재하지 않을 시
    - [x] 메뉴 가격이 메뉴 상품의 가격 합보다 클 시
- [x] 메뉴 목록을 조회한다.

### 주문 (Order)
- [x] 주문을 생성할 수 있다.
  - [예외 처리]
    - [x] 주문 메뉴 목록이 비어있을 시
    - [x] 주문 메뉴 개수와 실제 주문 메뉴 개수가 불일치 할 시
    - [x] 주문 테이블이 존재하지 않을 시
    - [x] 주문 테이블이 비어있을 시
- [x] 주문 목록을 조회할 수 있다.
- [x] 주문 상태를 변경할 수 있다.
  - [예외 처리]
    - [x] 현재 주문이 존재하지 않을 시
    - [x] 현재 주문 상태가 완료 상태일 시

### 상품 (Product)
- [x] 상품을 등록할 수 있다.
  - [예외 처리]
    - [x] 상품 가격이 null일 시
    - [x] 상품 가격이 0원 미만일 시
- [x] 상품 목록을 조회할 수 있다.


### 테이블 그룹 (TableGroup)
- [x] 테이블 그룹을 생성할 수 있다.
  - [예외 처리]
    - [x] 주문 테이블 목록이 비어있을 시
    - [x] 주문 테이블이 2개 미만일 시
    - [x] 주문 테이블 개수와 실제 저장되어있던 주문 테이블 개수가 불일치 할 시
    - [x] 비어있지 않은 주문 테이블이 한 개라도 존재할 시
    - [x] TableGroupId가 null이 아닌 주문 테이블이 한 개라도 존재할 시
- [x] 테이블 그룹을 제거할 수 있다.
  - [예외 처리]
    - [x] 테이블 그룹에 속한 주문 테이블 중, 요리 또는 식사중인 테이블이 하나라도 존재할 시

### 주문 테이블 (OrderTable)
- [x] 주문 테이블을 생성할 수 있다.
- [x] 주문 테이블 목록을 조회할 수 있다.
- [x] 주문 테이블의 빈 테이블인지 여부를 수정할 수 있다.
  - [예외 처리]
    - [x] 주문 테이블이 존재하지 않을 시
    - [x] 테이블 그룹 아이디가 null이 아닐 시
    - [x] 주문 테이블의 상태가 요리 또는 식사중일 시
- [x] 주문 테이블의 손님 수를 수정할 수 있다.
  - [예외 처리]
    - [x] 주문 테이블의 손님 수가 0 미만일 시
    - [x] 주문 테이블이 존재하지 않을 시
    - [x] 주문 테이블이 비어있을 시

## 의존성 리팩터링

- [x] TableGroup <-> OrderTable 양방향 직접 참조에서 TableGroup <- OrderTable 단방향 직접 참조로 변경
- [x] OrderTable <-> Order 양방향 직접 참조에서 OrderTable <- Order 단방향 직접 참조로 변경
- [x] Order <-> OrderLineItem 양방향 직접 참조에서 Order <- OrderLineItem 단방향 직접 참조로 변경
- [x] OrderLineItem -> Menu 단방향 직접 참조에서 OrderLineItem -> Menu 간접 참조로 변경
- [x] Menu <-> MenuProduct 양방향 직접 참조에서 Menu <- MenuProduct 단방향 직접 참조로 변경

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
