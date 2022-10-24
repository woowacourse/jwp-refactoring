# 키친포스

## 요구 사항


- 메뉴 그룹
  - 메뉴 그룹을 추가한다
  - 메뉴 그룹을 전체 조회한다

- 메뉴
  - 메뉴를 추가한다
    - 메뉴 가격이 양수여야 한다
    - 메뉴 가격이 상품 전체 금액보다 작거나 같아야 한다
  - 메뉴를 전체 조회한다

- 주문
  - 주문을 한다
    - 하나 이상의 주문 항목을 포함해야 한다
    - 주문 항목의 메뉴는 서로 달라야 한다
    - 주문하고자 하는 주문 테이블은 비어 있어야 한다
  - 주문 상태를 변경한다
    - 주문 상태는 `조리 -> 식사 -> 계산 완료` 순으로 변경되어야 한다
    - 결제가 완료되지 않은 주문이어야 한다
  - 주문을 전체 조회한다

- 상품
  - 상품을 추가한다
    - 가격은 음수가 아니어야 한다
  - 상품을 전체 조회한다

- 단체 지정
  - 단체 지정을 한다
    - 2개 이상의 주문 테이블을 지정해야 한다
    - 비어있는 주문 테이블이어야 한다
    - 단체 지정되지 않은 주문 테이블이어야 한다
  - 단체 지정을 해제한다
    - 지정된 주문 테이블의 모든 계산이 완료되어 있어야 한다

- 주문 테이블
  - 주문 테이블을 추가한다
  - 주문 테이블을 비운다
    - 단체 지정되지 않은 주문 테이블이어야 한다
    - 계산이 완료된 테이블이어야 한다
  - 주문 테이블의 손님 수를 변경한다
    - 주문 테이블의 손님 수는 음수가 될 수 없다
    - 비어있는 주문 테이블이어야 한다
  - 주문 테이블을 전체 조회한다

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
