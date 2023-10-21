# 키친포스

## 요구 사항

- [x] MenuGroup
    - [x] menuGroup 생성
    - [x] menuGroup 목록 조회
- [x] Menu
    - [x] menu 생성
        - [x] 가격이 비어있거나, 0보다 작은 경우 예외처리
        - [x] menu의 menuGroup이 존재하지 않는 경우 예외처리
        - [x] product가 존재하지 않는 경우 예외처리
        - [x] menu의 menuProduct의 가격합이 price 보다 큰 경우 예외처리
    - [x] menu 목록조회
- [x] Order
    - [x] order 생성(주문하기)
        - [x] 주문하려는 상품의 목록이 비어있는 경우 예외처리
        - [x] 주문하려는 상품의 목록의 사이즈와 조호된 menu의 사이즈가 다르면 예외처리
        - [x] 주문 테이블을 조회했을 때 비어있으면 예외처리
        - [x] 주문의 상태를 cooking으로 변경
    - [x] order 목록 조회
    - [x] order 상태 변경
        - [x] order의 상태가 이미 완료된 상태인 경우 예외처리
- [x] Product
    - [x] product 생성
        - [x] 가격이 0이하인 경우 예외처리
    - [x] product 목록 조회
- [x] TableGroup
    - [x] TableGroup 생성
        - [x] tableGroup을 묶는 orderTable이 비어있거나 size가 2이하면 예외처리
        - [x] 입력된 orderTable과 동일한 id로 조회한 orderTable의 size가 다르면 오류가 발생한다 .
        - [x] orderTable이 비어있지 않은 경우 예외처리
    - [x] TableGroup 해제
        - [x] tableGroup에 속한 orderTable의 주문 상태가 COMPLETION이 아닌 경우 예외처리
- [x] Table
    - [x] table 생성
    - [x] table 목록 조회
    - [x] table 상태 변경(비우기)
        - [x] table의 상태가 cooking이거나 meal이면, 예외처리
    - [x] table 방문 손님 수 변경
        - [x] 입력된 손님 수가 0미만인 경우 예외처리
        - [x] ordertable이 비어있으면 예외처리
- [x] setter 메서드 제거
    - [x] Menu
    - [x] MenuGroup
    - [x] MenuProduct
    - [x] Order
    - [x] OrderTable
    - [x] TableGroup
    - [x] OrderLineItem
    - [x] Product
- [x] dao에 의존적인 로직을 도메인 로직으로 변경
    - [x] MenuGroupService
    - [x] MenuService
    - [x] TableService
    - [x] TableGroupService
    - [x] ProductService
    - [x] OrderService
- [x] dto를 사용하도록 리팩터링
    - [x] MenuGroupService
    - [x] MenuService
    - [x] TableService
    - [x] TableGroupService
    - [x] ProductService
    - [x] OrderService

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
