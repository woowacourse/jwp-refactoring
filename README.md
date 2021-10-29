# 키친포스

## 요구 사항

### 상품 (Product)

- [x] 상품을 생성할 수 있다.
    - [x] 상품의 가격이 0원 미만일 경우 생성할 수 없다.
- [x] 상품 목록을 조회할 수 있다.

### 메뉴 (Menu)

- [x] 메뉴를 생성할 수 있다.
    - [x] 메뉴의 가격은 0원 미만일 경우 생성할 수 없다.
    - [x] 존재하지 않는 메뉴 그룹에 속한 경우 생성할 수 없다.
    - [x] 존재하지 않는 상품을 포함한 경우 생성할 수 없다.
    - [x] 메뉴 가격이 메뉴 상품 가격의 총합보다 클 수 없다.
- [x] 메뉴 목록을 조회할 수 있다.

### 메뉴 그룹 (Menu Group)

- [x] 메뉴 그룹을 생성할 수 있다.
- [x] 메뉴 그룹 목록을 조회할 수 있다.

### 주문 테이블 (Table)

- [x] 주문 테이블을 생성할 수 있다.
- [x] 주문 테이블 목록을 조회할 수 있다.
- [x] 빈 테이블로 변경할 수 있다.
    - [x] 존재하지 않는 주문 테이블은 변경할 수 없다.
    - [x] 단체 지정에 속해있는 주문 테이블은 변경할 수 없다.
    - [x] 조리, 식사 주문 상태를 가진 주문이 있는 주문 테이블은 변경할 수 없다.
- [x] 주문 테이블의 방문한 손님 수를 변경할 수 있다.
    - [x] 방문한 손님 수는 0 미만으로 변경할 수 없다.
    - [x] 존재하지 않는 주문 테이블일 경우 방문한 손님 수를 변경할 수 없다.
    - [x] 빈 테이블은 방문한 손님 수를 변경할 수 없다.

### 주문 (Order)

- [ ] 주문을 생성할 수 있다.
    - [ ] 주문 항목이 없는 경우 생성할 수 없다.
    - [ ] 존재하지 않는 메뉴를 포함할 경우 생성할 수 없다.
    - [ ] 존재하지 않는 주문을 포함할 경우 생성할 수 없다.
- [ ] 주문 목록을 조회할 수 있다.
- [ ] 주문 상태를 변경할 수 있다.
    - [ ] 존재하지 않는 주문의 상태는 변경할 수 없다.
    - [ ] 계산 완료된 주문의 상태는 변경할 수 없다.

### 단체 지정 (Table Group)

- [ ] 단체 지정을 생성할 수 있다.
    - [ ] 주문 테이블의 개수는 2 이상이어야 한다.
    - [ ] 존재하지 않는 주문 테이블이 포함되어 있는 경우 생성할 수 없다.
- [ ] 단체 지정을 해체할 수 있다.
    - [ ] 조리, 식사 상태의 주문 테이블을 포함한 경우 해체할 수 없다.

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

