# 키친포스

## 요구 사항

### 메뉴 그룹
- id, name
- 메뉴 그룹을 생성한다
- 메뉴 그룹을 전체 조회한다

### 메뉴
- id, name, price, menuGroupId, menuProducts
- 메뉴를 생성한다
    - 0보다 작은 가격은 허용하지 않는다
    - 메뉴 그룹에 포함되어야 한다 
    - 메뉴의 포함된 상품의 가격의 합은 메뉴의 가격보다 같거나 작아야 한다
- 메뉴를 전체 조회한다

### 주문
- id, orderTableId, orderStatus, orderedTime, orderLineItems
- 주문을 생성한다
  - 주문 항목은 1개 이상이어야 한다
  - 메뉴의 수와 주문 항목의 수는 같다
  - 기존에 존재하는 주문 테이블에 매핑된다
  - 처음 주문 상태는 COOKING 이다
  - 주문 생성 시간을 저장한다
- 주문을 전체 조회한다
- 주문 상태를 변경한다
    - 주문 상태가 COMPLETION에서는 상태를 변경할 수 없다
    - 주문 상태는 COOKING, MEAL, COMPLETION 중 하나이다

### 상품
- id, name, price
- 상품을 생성한다
  - 가격은 0원보다 커야 한다
- 상품을 전체 조회한다

### 단체 지정
- id, createdDate, orderTables
- 단체 지정을 생성한다
    - 주문 테이블은 2개 이상이다
    - 기존에 존재하는 주문 테이블과 매핑된다
    - 비어있지 않은 테이블로 단체 지정할 수 없다
    - 이미 단체 지정된 테이블은 단체 지정할 수 없다
- 단체 지정을 해제한다

### 테이블
- id, tableGroupId, numberOfGuests, empty
- 테이블을 생성한다
- 테이블을 전체 조회한다
- 빈 공간으로 변경한다
  - 단체로 지정되어 있으면 변경할 수 없다 
  - cooking, meal 주문 상태는 변경할 수 없다
- 게스트 숫자를 변경한다
  - 0명 이상이다

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
