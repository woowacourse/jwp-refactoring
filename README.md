# 키친포스

## ✅ 요구 사항

### 1. 상품
- [x] 상품을 생성할 수 있다.
  - 요청으로 상품 정보를 받는다.
    - [x] 상품 가격이 존재하지 않거나 0 미만이면 예외가 발생한다.
  
- [x] 상품 목록을 조회할 수 있다.

### 2. 메뉴
- [ ] 메뉴를 생성할 수 있다.
  - 요청으로 메뉴 상품 정보 목록이 담긴 메뉴 정보를 받는다.
    - [ ] 메뉴의 가격이 존재하지 않거나 0원 미만이면 예외가 발생한다.
    - [ ] 메뉴 그룹이 존재하지 않으면 예외가 발생한다. 
    - [ ] 메뉴 상품 목록에서 존재하지 않는 '상품'이 있으면 에외가 발생한다.
    - [ ] 메뉴 총 가격이 메뉴 상품 가격의 합보다 크면 예외가 발생한다.(같거나 작은 것은 할인 고려?)

- [ ] 메뉴 목록을 조회할 수 있다.
  - 메뉴 목록 조회 시 메뉴 목록들에 메뉴 상품을 조회해서 설정한다.

### 3. 메뉴 그룹
- [ ] 메뉴 그룹을 생성할 수 있다.
- [ ] 메뉴 그룹 목록을 조회할 수 있다.

### 4. 주문 테이블
- [ ] 주문 테이블을 생성할 수 있다.
  - 요청으로 손님 수, 빈 테이블 여부를 받아서 생성한다.
  - 테이블 그룹 ID는 비어있는 상태로 생성한다. 

- [ ] 주문 테이블의 빈 테이블 여부를 변경할 수 있다.
  - 요청으로 주문 테이블 ID, 변경할 상태를 받아서 변경한다.
    - [ ] 주문 테이블이 존재하지 않으면 예외가 발생한다.
    - [ ] 주문 테이블의 테이블 그룹이 존재하지 않으면 예외가 발생한다.
    - [ ] 주문 테이블의 상태가 '식사'이거나 '조리'이면 예외가 발생한다.
  
- [ ] 주문 테이블의 손님 수를 변경할 수 있다.
  - 요청으로 주문 테이블 ID, 손님 수를 받아서 변경한다.
    - [ ] 손님 수가 0 미만이면 예외가 발생한다.
    - [ ] 주문 테이블이 존재하지 않으면 예외가 발생한다.
    - [ ] 주문 테이블이 비어있다면(empty) 예외가 발생한다.


### 5. 주문 테이블 그룹
- [ ] 주문 테이블 그룹을 생성할 수 있다.
  - 요청으로 주문 테이블 그룹 ID 목록을 받아서 생성한다.
    - [ ] 주문 테이블 그룹에 주문 테이블이 없거나 주문 테이블이 둘 미만이라면 예외가 발생한다.
    - [ ] 요청 주문 테이블의 개수와 실제 주문 테이블의 개수가 다르다면 예외가 발생한다.
    - [ ] 실제 주문 테이블이 비어있거나 실제 주문 테이블의 주문 테이블 그룹이 존재하지 않는다면 예외가 발생한다.

- [ ] 주문 테이블 그룹을 해제할 수 있다.
  - 요청으로 주문 테이블 그롭 ID를 받아서 주문 테이블 그룹을 해제한다.
  - [ ] 주문 테이블 그룹에 속한 주문 테이블 중 '조리', '식사' 상태인 주문 테이블이 있으면 예외가 발생한다.

### 6. 주문
- [ ] 주문을 생성할 수 있다.
  - 요청으로 주문 테이블 ID, 주문 메뉴 정보 목록을 받아서 생성한다.
    - [ ] 주문 메뉴 정보 목록이 비어있으면 예외가 발생한다.
    - [ ] 요청한 주문 메뉴 정보 목록의 개수와 메뉴 아이디로 조회한 메뉴의 개수가 다르면 예외가 발생한다.
    - [ ] 주문 테이블이 존재하지 않으면 예외가 발생한다.
    - [ ] 주문 테이블이 비어있다면(empty) 예외가 발생한다.
  - [ ] 주문 생성 시 주문 상태는 '조리' 상태로 설정한다.
  - [ ] 주문 생성 시 주문 시간은 현재 시간으로 설정한다.

- [ ] 주문 목록을 조회할 수 있다.
  - [ ] 주문 목록 조회 시 주문 목록들에 주문 메뉴들을 조회해서 설정한다.

---

## 📘 용어 사전

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
