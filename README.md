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

## TODO
- [ ] 요구 사항 가독성 있게 변경

## 요구 사항
- [ ] Menu 테스트
  - [x] /api/menus POST success
  - [ ] /api/menus POST failure
    - [ ] menu 의 price 가 null 인 경우
    - [ ] menu 의 price 가 0 보다 작은 경우
    - [ ] menuGroup 이 존재하지 않는 경우
    - [ ] menuProduct 의 product 가 존재하지 않는 경우
    - [ ] menu 가 모든 product 의 총 금액보다 큰 경우
  - [x] /api/menus GET success

- [x] MenuGroup 테스트
  - [x] /api/menu-groups POST success
  - [x] /api/menu-groups GET success

- [ ] Order 테스트
  - [x] /api/orders POST success
  - [ ] /api/orders POST failure
    - [ ] orderLineItem 이 없는 경우
    - [ ] orderLineItem 개수가 메뉴 수와 일치하지 않는 경우
    - [ ] orderTable 이 존재하지 않는 경우
    - [ ] orderTable 이 비어있는 경우
  - [x] /api/orders GET success
  - [x] /api/orders PUT success
  - [ ] /api/orders PUT failure
    - [ ] order 가 존재하지 않는 경우
    - [ ] order 가 이미 완료된 order 인 경우

- [ ] Product 테스트
  - [x] /api/products POST success
  - [ ] /api/products POST failure
    - [ ] product 의 price 가 null 인 경우
    - [ ] product 의 price 가 0 보다 작은 경우
  - [x] /api/products GET success

- [ ] Table 테스트
  - [x] /api/tables POST success
  - [ ] /api/tables POST failure
  - [x] /api/tables GET success
  - [x] /api/tables/{orderTableId}/empty PUT success
  - [ ] /api/tables/{orderTableId}/empty PUT failure
    - [ ] orderTable 이 존재하지 않는 경우
    - [ ] tableGroup 이 존재하지 않는 경우
    - [ ] order 의 상태가 COOKING 이거나 MEAL 인 경우 (COMPLETION 이 아닌 경우)
  - [x] /api/tables/{orderTableId}/number-of-guests PUT success
  - [ ] /api/tables/{orderTableId}/number-of-guests PUT failure
    - [ ] numberOfGuests 가 0 보다 작은 경우
    - [ ] orderTable 이 존재하지 않는 경우
    - [ ] orderTable 이 비어있는 경우

- [ ] TableGroup 테스트
  - [x] /api/table-groups POST success
  - [ ] /api/table-groups POST failure
    - [ ] 요청한 orderTable 이 0 개인 경우 (empty)
    - [ ] 요청한 orderTable 의 수가 2 개 미만인 경우
    - [ ] 요청한 orderTable 중 존재하지 않는 것이 있는 경우
    - [ ] 요청한 orderTable 이 비어있지 않는 경우
    - [ ] 요청한 orderTable 이 이미 tableGroup 이 있는 경우
  - [x] /api/table-groups/{tableGroupId} DELETE success
  - [ ] /api/table-groups/{tableGroupId} DELETE failure
    - [ ] 요청한 orderTable 의 order 상태가 COOKING 또는 MEAL 인 경우 (COMPLETION 이 아닌 경우)
