# 키친포스

## 요구 사항

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

## 기능 요구 사항
- 메뉴
    - [x] 메뉴를 생성할 수 있다.
      - 메뉴의 가격이 0보다 작을 수 없다.
      - 메뉴 상품의 가격의 합보다 메뉴 가격이 비쌀 수 없다.
    - [x] 메뉴를 전체 조회할 수 있다.
    - [x] setter를 지운다.
- 메뉴 그룹
    - [x] 메뉴 그룹을 생성할 수 있다.
    - [x] 메뉴 그룹을 전체 조회할 수 있다.
    - [x] setter를 지운다.
- 메뉴 상품
    - [x] setter를 지운다
- 상품
    - [x] 상품을 생성할 수 있다.
      - 상품의 가격이 0보다 작을 수 없다.
    - [x] 상품을 전체 조회할 수 있다.
    - [x] setter를 지운다.
- 주문
    - [x] 주문 할 수 있다.
      - 주문 항목들이 비어 있을 수 없다.
      - 주문 테이블이 비어 있을 수 없다.
    - [x] 주문 목록을 전체 조회할 수 있다.
    - [x] 주문 상태를 변경할 수 있다.
      - 주문 상태가 완료가 되면 상태를 변경할 수 없다.
    - [ ] setter를 지운다.
  - 주문 항목
    - [x] setter를 지운다.
- 주문 테이블
    - [x] 주문 테이블을 만들 수 있다.
    - [x] 주문 테이블을 전체 조회할 수 있다.
    - [x] 주문 테이블의 상태를 비어있는 상태로 변경할 수 있다.
      - 주문 테이블이 단체 지정이 되어 있어야 한다.
      - 주문 상태가 요리중이나 식사중이면 변경할 수 없다.
    - [x] 주문 테이블의 손님 수를 변경할 수 있다.
      - 손님의 수가 0보다 작을 수 없다. 
      - 주문 테이블이 비어 있을 수 없다.
    - [x] setter를 지운다.
- 단체 지정
    - [x] 단체 지정(Table Group)을 할 수 있다.
      - 주문 테이블이 비어 있을 수 없다.
      - 주문 테이블의 수가 2보자 작을 수 없다.
      - 실제 저장된 주문 테이블이 비어 있어야 한다.
      - 실제 저장된 주문 테이블이 그룹이 아니어야 한다.
    - [x] 단체 지정(Table Group)을 해제할 수 있다.
      - 주문 상태가 요리중이거나 식사중이면 해제할 수 없다.
    - [x] setter를 지운다.
