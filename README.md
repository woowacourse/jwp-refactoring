# 키친포스

## 요구 사항
### 상품 : 메뉴를 관리하는 기준이 되는 데이터
- [ ] 상품을 생성할 수 있다
  - [ ] 상품의 가격은 0원 이상이어야 한다
- [ ] 상품의 목록을 조회할 수 있다

### 메뉴 그룹 : 메뉴 묶음, 분류
- [ ] 메뉴 그룹을 생성할 수 있다
- [ ] 메뉴 그룹의 목록을 조회할 수 있다

### 메뉴 : 메뉴 그룹에 속하는 실제 주문 가능 단위
- [ ] 메뉴를 생성할 수 있다
  - [ ] 메뉴의 가격은 0원 이상이어야 한다
  - [ ] 존재하는 메뉴그룹이어야 한다
  - [ ] 존재하는 상품이어야한다.
  - [ ] 메뉴의 가격은 상품가격 * 수량의 합보다 작거나 같아야한다
- [ ] 메뉴의 목록을 조회할 수 있다

### 주문 
- [ ] 주문을 할 수 있다
  - [ ] 주문상품이 하나 이상 존재해야 한다
  - [ ] 주문상품 속 메뉴가 중복되면 안된다
  - [ ] 주문하려는 테이블(order table)가 존재해야한다
  - [ ] 주문하려는 테이블은 비어있으면 안된다
- [ ] 주문 목록을 조회할 수 있다
- [ ] 주문 상태를 변경할 수 있다 (주문은 조리 ➜ 식사 ➜ 계산 완료 순서로 진행된다)
  - [ ] 존재하는 주문이어야 한다
  - [ ] 해당 주문이 이미 COMPLETION 상태이면 주문 상태를 변경할 수 없다

### 주문 테이블 : 매장에서 주문이 발생하는 영역 (empty table : 주문을 등록할 수 없는 주문 테이블)
- [ ] 테이블을 등록할 수 있다
- [ ] 테이블의 상태를 변경할 수 있다 (empty)
  - [ ] 존재하는 테이블이어야 한다
  - [ ] 주문상태가 COOKING이거나 MEAL이면 수정할 수 없다.
- [ ] 테이블의 손님 수를 변경할 수 있다 (numberOfGuests)
  - [ ] 바꾸려는 손님 수가 0명 이상이어야 한다
  - [ ] 존재하는 테이블이어야 한다
  - [ ] 빈 테이블이 아니어야 한다
- [ ] 테이블의 목록을 조회할 수 있다

### 테이블 그룹 : 통합 계산을 위해 개별 주문 테이블을 그룹화하는 기능
- [ ] 테이블 그룹을 추가할 수 있다 (테이블 여러개 그룹화)
  - [ ] 묶으려는 테이블은 2개이상이어야 한다
  - [ ] 존재하는 테이블이어야한다
  - [ ] 비지 않은 테이블이거나 이미 테이블 그룹을 가지면 그룹화할 수 없다
- [ ] 테이블 그룹을 삭제할 수 있다
  - [ ] 주문상태가 COOKING이거나 MEAL이면 삭제할 수 없다

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
