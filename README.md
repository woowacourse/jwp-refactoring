# 키친포스

## 요구 사항
### 상품

- [x] 상품을 등록할 수 있다.
    - [x] 상품의 가격은 0 원 이상이어야 한다.
    - [x] 상품의 이름은 비어있을 수 없다.
- [x] 상품의 목록을 조회할 수 있다.

### 메뉴 그룹

- [x] 메뉴 그룹을 등록할 수 있다.
    - [x] 메뉴 그룹의 이름은 비어있을 수 없다.
- [x] 메뉴 그룹의 목록을 조회할 수 있다.

### 메뉴

- [ ] 1 개 이상의 등록된 상품으로 메뉴를 등록할 수 있다.
- [ ] 메뉴의 가격이 올바르지 않으면 등록할 수 없다.
    - [ ] 메뉴의 가격은 0 원 이상이어야 한다.
    - [ ] 메뉴에 속한 상품 금액의 합은 메뉴의 가격보다 크거나 같아야 한다.
- [ ] 메뉴는 특정 메뉴 그룹에 속해야 한다.
- [ ] 메뉴의 목록을 조회할 수 있다.

### 주문 테이블

- [ ] 주문 테이블을 등록할 수 있다.
    - [ ] 1명 이상의 손님과 함께 빈 테이블로 등록할 수 없다.
- [ ] 주문 테이블의 목록을 조회할 수 있다.
- [ ] 빈 테이블 설정 또는 해지할 수 있다.
    - [ ] 단체 지정된 주문 테이블은 빈 테이블 설정 또는 해지할 수 없다.
    - [ ] 주문 상태가 조리 또는 식사인 주문 테이블은 빈 테이블 설정 또는 해지할 수 없다.
- [ ] 방문한 손님 수를 입력할 수 있다.
    - [ ] 방문한 손님 수가 0명 이하이면 입력할 수 없다.
    - [ ] 빈 테이블은 방문한 손님 수를 입력할 수 없다.

### 단체 지정

- [ ] 2 개 이상의 빈 테이블을 단체로 지정할 수 있다.
- [ ] 단체 지정은 중복될 수 없다.
- [ ] 단체 지정을 해지할 수 있다.
- [ ] 단체 지정된 주문 테이블의 주문 상태가 조리 또는 식사인 경우 단체 지정을 해지할 수 없다.

### 주문

- [ ] 1 개 이상의 등록된 메뉴로 주문을 등록할 수 있다.
    - [ ] 빈 테이블에는 주문을 등록할 수 없다.
- [ ] 주문의 목록을 조회할 수 있다.
- [ ] 주문 상태를 변경할 수 있다.
    - [ ] 주문 상태가 계산 완료인 경우 변경할 수 없다.



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
