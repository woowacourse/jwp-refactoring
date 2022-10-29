# 키친포스

## 요구 사항

### 상품(Product)

- 상품은 등록 가능하다.
    - 상품의 가격이 올바르지 않으면 등록할 수 없다.
        - 상품의 가격은 0 원 이상이어야 한다.
- 상품의 목록 조회가 가능하다.

### 메뉴 그룹(MenuGroup)

- 메뉴 그룹은 생성 가능하다.
- 메뉴 그룹의 목록 조회가 가능하다.

### 메뉴(Menu)

- 메뉴는 등록 가능하다.
    - 메뉴에 가격이 올바르지 않으면 등록할 수 없다.
        - 메뉴에 가격은 0 원 이상이어야 한다.
        - 메뉴의 가격은 메뉴 상품들의 가격의 총합보다 클 수 없다.
    - 지정한 메뉴 그룹이 존재하지 않으면 등록할 수 없다.
- 메뉴는 목록 조회가 가능하다.

### 주문(Order)

- 주문은 등록 가능하다.
    - 주문 항목이 비어있으면 등록할 수 없다.
    - 주문 항목에 기재된 메뉴가 존재하지 않을 경우 등록할 수 없다.
    - 주문 테이블이 존재하지 않을 경우 등록할 수 없다.
    - 주문 테이블이 비어있을 경우 등록할 수 없다
- 주문은 목록 조회가 가능하다.
- 주문 상태는 변경 가능하다.
    - 주문이 존재하지 않을 경우 변경할 수 없다.
    - 주문이 계산 완료된 경우 변경할 수 없다.
    - 주문 상태가 잘못된 경우 변경할 수 없다.
        - 주문 상태는 `COOKING`, `MEAL`, `COMPLETION` 만 가능하다.

### 테이블(Table)

- 주문 테이블은 등록 가능하다.
- 주문 테이블은 빈 테이블로 변경할 수 있다.
    - 주문 테이블이 없는 경우 변경할 수 없다.
    - 단체 지정이 되어있는 경우 변경할 수 없다.
    - 주문 테이블에 남아있는 주문이 존재하고 그 상태가 `COOKING`, `MEAL` 일 경우 변경할 수 없다.
- 주문 테이블은 목록 조회가 가능하다.
- 손님 수를 변경할 수 있다.
    - 손님 수를 0명 미만으로 변경할 수 없다.
    - 빈 테이블은 변경할 수 없다.

### 단체 지정(TableGroup)

- 테이블을 단체로 지정할 수 있다.
    - 빈 테이블이 아닐 경우 단체로 지정할 수 없다.
    - 두 개 미만의 테이블인 경우 단체로 지정할 수 없다.
    - 입력한 주문 테이블이 존재하지 않을 경우 단체로 지정할 수 없다.
    - 입력한 주문 테이블이 이미 단체로 지정된 경우 단체로 지정할 수 없다.
- 테이블의 단체 지정을 해제할 수 있다.
    - 주문 테이블에 남아있는 주문이 존재하고 그 상태가 `COOKING`, `MEAL`인 경우 변경할 수 없다.
    - 단체지정이 해제되면 주문 테이블을 비운다.

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

## 서비스 리팩토링

- [x] 현재 서비스는 도메인 객체를 이용하여 View에서 입력, 출력 용도로도 사용하고 있다. 이를 해결하기 위해서 DTO를 만들어 해결해보자.
    - [x] 메뉴그룹
    - [x] 메뉴
    - [x] 주문
    - [x] 상품
    - [x] 테이블
    - [x] 단체지정
- [x] 불필요한 setter를 제거한다.
- [x] 테스트 픽스쳐 패키지를 분리한다.
- [ ] 레포지토리를 리팩토링한다.
