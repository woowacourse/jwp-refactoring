# 키친포스

## 요구 사항

- [ ]  상품을 등록할 수 있다.
    - [ ]  가격은 0 이상이어야 한다.
- [ ]  상품 목록을 조회할 수 있다.

- [ ]  메뉴그룹을 등록할 수 있다. ex) “추천메뉴”
- [ ]  메뉴그룹 목록을 조회할 수 있다.

- [ ]  메뉴를 등록할 수 있다.
    - [ ]  가격은 0 이상이어야 한다.
    - [ ]  존재하지 않는 메뉴 그룹으로는 만들 수 없다.
    - [ ]  메뉴안의 상품들의 가격의 합이 메뉴의 가격을 넘을 수 없다.
- [ ]  메뉴 목록을 조회할 수 있다.

- [ ]  주문을 등록할 수 있다.
    - [ ]  아이템 목록이 비어있는 상태로 주문을 등록할 수 없다.
    - [ ]  메뉴에 등록되지 않은 메뉴를 주문상품목록에 등록할 수 없다.
    - [ ]  주문테이블이 비어있을 수 없다. (?)
    - [ ]  주문을 하면 `COOKING` 상태로 바뀌어야 한다.
    - [ ]  현재 시간으로 주문시간이 등록되어야 한다.
- [ ]  주문 목록을 조회할 수 있다.
- [ ]  주문 상태를 변경할 수 있다.
    - [ ]  `COMPLETION` 상태에서는 변경할 수 없다.

- [ ]  (주문테이블을 묶은) 테이블 그룹을 생성할 수 있다.
    - 통합 계산을 위해 주문 테이블을 그룹화
    - [ ]  테이블 그룹의 사이즈는 2 이상이어야 한다.
    - [ ]  존재하지 않는 주문 테이블들을 테이블 그룹에 등록할 수 없다.
    - [ ]  현재시간으로 테이블그룹의 생성날짜가 등록되어야 한다.
- [ ]  테이블 그룹을 해제할 수 있다.
    - [ ]  각 테이블들이 `COOKING` 혹은 `MEAL` 상태이면 해제할 수 없다.

- [ ]  테이블을 생성할 수 있다.
- [ ]  테이블 목록을 조회할 수 있다.
- [ ]  테이블을 비울 수 있다.
    - [ ]  테이블 그룹이 존재하면 비울 수 없다.
    - [ ]  테이블이 `COOKING` 혹은 `MEAL` 상태이면 해제할 수 없다.
- [ ]  테이블의 손님 수를 바꿀 수 있다.
    - [ ]  손님 수는 0 이상이어야 한다.

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
