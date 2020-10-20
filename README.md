# 키친포스

## 요구 사항
- MenuGroup 관련 기능
    - [x] 메뉴 그룹을 생성하는 기능 구현
    - [x] 등록된 모든 메뉴 그룹을 확인하는 기능 구현

- Menu 관련 기능
    - [x] 메뉴를 생성하는 기능 구현
        - [ ] Menu의 price가 null이면 예외 발생
        - [ ] Menu의 price가 0보다 작으면 예외 발생
        - [ ] Menu의 price가 메뉴에 포함된 Product의 price 합보다 크면 예외 발생
    - [x] 등록된 모든 메뉴를 확인하는 기능 구현

- Order 관련 기능
    - [x] 주문을 등록하는 기능 구현
        - [ ] 주문된 상품이 없으면 예외 발생
        - [ ] 주문된 상품 개수와 해당하는 메뉴 개수가 다르면 예외 발생
        - [ ] 주문 테이블이 빈 테이블이면 예외 발생
    - [x] 등록된 모든 주문을 확인하는 기능 구현
    - [x] 주문의 상태를 변경하는 기능 구현
        - [ ] 주문 상태가 COMPLETION이면 예외 발생

- Product 관련 기능
    - [x] 상품을 생성하는 기능 구현
        - [ ] 상품의 price가 null이면 예외 발생
        - [ ] 상품의 price가 0보다 작으면 예외 발생
    - [x] 등록된 모든 상품을 확인하는 기능 구현

- TableGroup 관련 기능
    - [x] 테이블 그룹을 만드는 기능 구현
        - [ ] 테이블 그룹의 테아블 리스트가 비어있으면 예외 발생
        - [ ] 테이블 그룹의 테이블 수가 DB에서 조회한 테이블의 수와 다르면 예외 발생
    - [x] 테이블 그룹을 삭제하는 기능 구현
        - [ ] 테이블들의 주문 상태가 COOKING 혹은 MEAL이면 예외 발생

- Table 관련 기능
    - [x] 테이블을 생성하는 기능 구현
    - [x] 등록된 모든 테이블을 확인하는 기능 구현
    - [x] 빈 테이블 여부를 설정하는 기능 구현
        - [ ] 테이블 그룹에 포함되는 테이블이면 예외 발생
        - [ ] 테이블의 주문 상태가 COOKING 혹은 MEAL이면 예외 발생
    - [x] 테이블의 손님 수를 설정하는 기능 구현
        - [ ] 손님 수가 0보다 작으면 예외 발생
        - [ ] 테이블이 비어있으면 예외 발생

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
