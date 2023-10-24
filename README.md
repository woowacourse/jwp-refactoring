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
        - [x] MenuGroup을 저장할 수 있다.
        - [x] MenuGroup의 목록을 조회할 수 있다.
    - [x] MenuService
        - [x] Menu의 가격은 `null`이 아니어야 한다.
        - [x] Menu의 가격은 0 이상이어야 한다.
        - [x] 저장된 MenuGroup이 존재해야 한다.
        - [x] Menu의 가격이 MenuProduct에 속하는 모든 상품의 가격 * 수량의 합보다 작거나 같아야 한다.
        - [x] Menu를 저장하고 MenuProduct에 속하는 모든 상품을 Menu와 연결하여 저장하고 반환한다.
        - [x] 모든 Menu의 MenuProduct와 Menu를 반환한다.
    - [x] OrderService
        - [x] Order를 저장할 수 있다.
            - [x] OrderLineItem은 비어있을 수 없다.
            - [x] 모든 OrderLineItem의 메뉴는 저장되어 있어야 한다.
            - [x] Order의 OrderTable은 저장되어 있어야 한다.
            - [x] Order의 OrderTable은 비어있지 않아야 한다.
            - [x] 생성한 Order의 상태는 COOKING이다.
            - [x] OrderLineItem의 OrderId를 생성한 Order의 id로 설정하고 저장한다.
        - [x] 모든 주문 목록을 조회할 수 있다.
        - [x] 주문의 상태를 변경할 수 있다.
            - [x] 주문이 존재해야 한다.
            - [x] 완료된 주문의 상태는 변경할 수 없다.
    - [x] ProductService
        - [x] 상품의 가격이 `null`이 아니고 0 이상일 경우 저장할 수 있다.
        - [x] 모든 상품의 목록을 반환할 수 있다.
    - [x] TableGroupService
        - [x] TableGroup을 저장할 수 있다.
            - [x] TableGroup의 OrderTable의 크기는 2 이상이어야 한다.
            - [x] TableGroup의 모든 OrderTable은 저장되어 있어야 한다.
            - [x] TableGroup의 모든 OrderTable은 비어있어야 한다.
            - [x] TableGroup의 모든 OrderTable의 TableGroup은 `null`이어야 한다.
            - [x] TableGroup의 모든 OrderTable의 TableGroupId를 현재 TableGroup의 id로 업데이트한다.
            - [x] TableGroup의 모든 OrderTable의 empty를 `false`로 업데이트한다.
        - [x] TableGroup을 해제한다.
            - [x] 해제하려는 모든 TableGroup의 OrderTable의 주문 상태는 COOKING이나 MEAL이 아니어야 한다.
            - [x] 해제하려는 모든 TableGroup의 OrderTable의 TableGroupId를 `null`로 업데이트한다.
            - [x] 해제하려는 모든 TableGroup의 OrderTable의 empty를 `false`로 업데이트한다.
    - [x] TableService
        - [x] Table을 저장할 수 있다.
        - [x] Table의 목록을 조회할 수 있다.
        - [x] Table의 empty 상태를 변경할 수 있다.
            - [x] Table이 존재해야 한다.
            - [x] Table의 TableGroupId 값이 `null`이어야 한다.
            - [x] Table의 주문 상태가 COOKING이나 MEAL이 아니어야 한다.
            - [x] Table의 empty 여부를 orderTable의 상태로 변경한다.
        - [x] Table의 손님 수를 변경한다.
            - [x] 손님의 수는 0 이상이어야 한다.
            - [x] 손님 수를 변경할 테이블이 존재해야 한다.
            - [x] 테이블이 비어있는 경우 손님의 수를 변경할 수 없다.

### 2단계 - 서비스 리팩터링

- [ ] setter 사용하지 않기
- [ ] 인덴트 1 이하로 유지하기
- [ ] 메서드 10라인 이하로 유지하기
- [ ] 원시값 포장 및 일급컬렉션 사용하기
- [ ] 3개 이상의 인스턴스 변수 갖지 않기

### 2단계 - 피드백 요구 사항

- [ ] 2단계 체크리스트 체크하기
- [ ] 메뉴 상품 가격 검증 후 예외처리 도메인으로 이동
- [ ] OrderService 메서드 추출
- [ ] DTO 도입
- [ ] OrderService create 메서드 추출
- [ ] OrderService의 changeOrderStatus 검증로직 메서드 분리하여 메서드명으로 의도 표현
- [x] 도메인 패키지 양방향 참조 끊기

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
