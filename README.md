# 키친포스

## 요구 사항
### 상품
- 상품을 등록할 수 있다.
    - 상품 가격이 null 이거나 0 미만이면 예외가 발생한다.
- 모든 상품을 조회할 수 있다.
### 메뉴 그룹
- 메뉴 그룹을 등록할 수 있다.
### 메뉴
- 메뉴 그룹에 메뉴를 등록할 수 있다.
  - 메뉴의 가격이 null 이거나 음수이면 예외가 발생한다.
  - 메뉴가 속한 메뉴그룹이 존재하지 않으면 예외가 발생한다.
  - 메뉴에 등록하려는 상품이 존재하지 않는 상품이면 예외가 발생한다.
  - 메뉴의 가격이 메뉴의 상품들 * 가격보다 크면 예외가 발생한다.(할인이 들어가 메뉴 가격이 더 쌀 수 있음)
### 주문테이블
- 테이블을 등록할 수 있다.
- 테이블의 손님 수를 바꿀 수 있다.
  - 손님의 수가 0 미만이면 예외가 발생한다.
  - 기존에 저장된 테이블이 emtpy 상태면 예외가 발생한다.
- 테이블의 empty 상태를 바꿀 수 있다.
  - 테이블 그룹이 존재하면 예외가 발생한다.
  - 해당 테이블에서 일어난 주문들 중 상태가 조리, 식사인 주문이 존재할 경우 예외가 발생한다.
### 테이블 그룹
- 테이블들을 테이블 그룹으로 묶을 수 있다.
  - 테이블의 수가 2 미만이면 예외가 발생한다.
  - 묶기를 요청한 테이블중 하나라도 실제로 존재하지 않는다면 예외가 발생한다.
  - 테이블이 empty 상태가 아니거나 테이블의 테이블 그룹이 이미 존재한다면 예외가 발생한다.
  - 테이블 그룹을 묶은 시간을 같이 저장한다.
- 테이블 그룹을 해제할 수 있다.
  - 테이블들의 주문들 중 조리, 식사 상태가 있는 경우 예외가 발생한다.
### 주문
- 주문을 등록할 수 있다.
  - 주문메뉴가 존재하지 않는 요청일 경우 예외가 발생한다.
  - 주문받은 메뉴가 실제 저장되어 있는 메뉴에 속하지 않는다면 예외가 발생한다.
  - 주문테이블이 존재하지 않으면 예외가 발생한다.
  - 주문테이블이 empty 상태면 예외가 발생한다.
  - 주문메뉴에 주문을 연관시켜 저장한다.
- 주문상태를 변경할 수 있다.
  - 상태를 변경하려는 주문이 저장되어 있지 않으면 예외가 발생한다.
  - 저장된 주문상태가 계산완료이면 예외가 발생한다.

---

## 2단계 구현사항
- [x] 컨트롤러 계층의 요청과 응답에 DTO를 사용하도록 변경
- [x] 비즈니스 로직을 도메인 객체가 담당하도록 변경
- [x] JPA로 마이그레이션

## 3단계 구현사항
- [ ] 도메인별로 패키지를 나누기
- [ ] 패키지 사이의 의존 관계가 단방향이 되도록 변경
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
