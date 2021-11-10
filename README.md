# 키친포스

## 요구 사항

### Menu Group `/api/menu-groups`
- [ ] POST 메뉴 그룹 추가 
- [ ] GET 메뉴 그룹 불러오기

### Menu `/api/menus`
- [ ] POST 메뉴 생성
- [ ] GET 메뉴 불러오기

### Order(주문) `/api/order`
- [ ] POST 주문 생성
- [ ] GET 주문 불러오기
- [ ] PUT `/{orderId}/order-status` 주문 수정하기

### Products(상품) `/api/products`
- [ ] POST 상품 생성
- [ ] GET 상품 불러오기

### Table Group(단체 지정) `/api/table-groups`
- [ ] POST 테이블 그룹 생성
- [ ] DELETE `/{tableGroupId}` 테이블 그룹 해제

### Table `/api/tables`
- [ ] POST 주문 테이블 생성 
- [ ] GET 주문 테이블 불러오기
- [ ] PUT `/{orderTableId}/empty` 주문 테이블 상태 변경
  1. 자정된 TableGroup이 없어야한다
    2. 주문 상태가 COOKING, MEAL이면 안된다
- [ ] PUT `/{orderTableId}/number-of-guests` 손님 수 수정


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
