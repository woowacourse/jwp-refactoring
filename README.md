# 키친포스

## 요구 사항

### 1단계 - 테스트를 통한 코드 보호

- [ ] 컨트롤러 테스트 구현
    - [ ] MenuGroupRestController
    - [ ] MenuRestController
    - [ ] OrderRestController
    - [ ] ProductRestController
    - [ ] TableGroupRestController
    - [ ] TableRestController

- [x] 서비스 테스트 구현
    - [x] MenuGroupService
    - [x] MenuService
        - [x] Menu의 가격은 `null`이 아니어야 한다.
        - [x] Menu의 가격은 0 이상이어야 한다.
        - [x] 저장된 MenuGroup이 존재해야 한다.
        - [x] Menu의 가격이 MenuProduct에 속하는 모든 상품의 가격 * 수량의 합보다 작거나 같아야 한다.
        - [x] Menu를 저장하고 MenuProduct에 속하는 모든 상품을 Menu와 연결하여 저장하고 반환한다.
        - [x] 모든 Menu의 MenuProduct와 Menu를 반환한다.
    - [x] OrderService
    - [x] ProductService
        - [x] 상품의 가격이 `null`이 아니고 0 이상일 경우 저장할 수 있다.
        - [x] 모든 상품의 목록을 반환할 수 있다.
    - [x] TableGroupService
    - [x] TableService

## 용어 사전

| 한글명      | 영문명              | 설명                            |
|----------|------------------|-------------------------------|
| 상품       | product          | 메뉴를 관리하는 기준이 되는 데이터           |
| 메뉴 그룹    | menu group       | 메뉴 묶음, 분류                     |
| 메뉴       | menu             | 메뉴 그룹에 속하는 실제 주문 가능 단위        |
| 메뉴 상품    | menu product     | 메뉴에 속하는 수량이 있는 상품             |
| 금액       | amount           | 가격 * 수량                       |
| 주문 테이블   | order table      | 매장에서 주문이 발생하는 영역              |
| 빈 테이블    | empty table      | 주문을 등록할 수 없는 주문 테이블           |
| 주문       | order            | 매장에서 발생하는 주문                  |
| 주문 상태    | order status     | 주문은 조리 ➜ 식사 ➜ 계산 완료 순서로 진행된다. |
| 방문한 손님 수 | number of guests | 필수 사항은 아니며 주문은 0명으로 등록할 수 있다. |
| 단체 지정    | table group      | 통합 계산을 위해 개별 주문 테이블을 그룹화하는 기능 |
| 주문 항목    | order line item  | 주문에 속하는 수량이 있는 메뉴             |
| 매장 식사    | eat in           | 포장하지 않고 매장에서 식사하는 것           |
