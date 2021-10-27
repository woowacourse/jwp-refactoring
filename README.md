# 키친포스

## 요구 사항

### 메뉴 그룹
- [ ] 메뉴 그룹을 생성한다
- [ ] 메뉴 그룹 리스트를 반환한다
### 메뉴
- [ ] 메뉴를 생성한다. 
  - [ ] 메뉴 상품들도 함께 저장된다.
  - [ ] 가격은 null 이거나 음수이면 예외를 반환한다.
  - [ ] 가격이 메뉴 상품들의 가격보다 크면 예외를 반환한다
- [ ] 메뉴 리스트를 반환한다.
  - [ ] 메뉴 그룹들을 포함해 반환한다.
### 주문
- [ ] 주문을 생성한다.
  - [ ] 주문 항목들도 함깨 저장된다. 
  - [ ] 주문 항목이 비어있으면 예외를 반환한다.
  - [ ] 주문 항목의 수와 메뉴의 수가 일치하지 않으면 예외를 반환한다.
  - [ ] 주문 테이블이 존재하지 않거나 빈 테이블이면 예외를 반환한다.
- [ ] 주문 리스트를 반환한다.
  - [ ] 주문 항목들을 포함해 반환한다.
- [ ] 주문 상태를 변경한다.
  - [ ] 주문이 존재하지 않으면 예외를 반환한다.
  - [ ] 완료된 주문 변경 시 예외를 반환한다.
### 상품
- [ ] 상품을 생성한다.
  - [ ] 가격이 null 이거나 음수이면 예외를 반환한다.
- [ ] 상품 리스트를 반환한다.
### 단체 지정
- [ ] 단체 지정을 생성한다.
  - [ ] 주문 테이블들이 2개 미만 이면 예외를 반환한다.
  - [ ] 주문 테이블들이 존재하지 않으면 예외를 반환한다. 
  - [ ] 주문 테이블들이 비어있지 않거나 단체 지정 되어있으면 예외를 반환한다.
- [ ] 단체 지정을 해제한다.
  - [ ] 주문 테이블들의 단체지정 id를 null로 저장한다.
  - [ ] 주문 테이블들이 존재하지 않거나 완료되었으면 예외를 반환한다.
### 주문 테이블
- [ ] 주문 테이블을 생성한다.
- [ ] 주문 테이블 리스트를 반환한다.
- [ ] 주문 테이블의 상태를 변경한다.
  - [ ] 주문 테이블이 존재하지 않으면 예외를 반환한다.
  - [ ] 주문 테이블이 단체 지정되어 있으면 예외를 반환한다.
  - [ ] 완료된 주문이면 예외를 반환한다.
- [ ] 주문 테이블의 손님 수를 변경한다.
  - [ ] 손님 수가 0 미만이면 예외를 반환한다.
  - [ ] 주문 테이블이 존재하지 않으면 예외를 반환한다.
  - [ ] 빈 테이블이면 예외를 반환한다.

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
