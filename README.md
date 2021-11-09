# 키친포스

## 요구 사항

- 상품을 추가할 수 있다
  - [예외]
  - 가격이 null일 때
  - 가격이 음수일 때
- 상품 목록을 볼 수 있다
- 메뉴 그릅을 추가할 수 있다
- 메뉴 그룹의 목록을 볼 수 있다
- 메뉴를 추가할 수 있다
  - [예외]
  - 메뉴의 가격이 null일 때
  - 메뉴의 가격이 음수일 때
  - 메뉴의 가격이 상품 가격 * 수량보다 높을 경우
  - 메뉴의 메뉴 그룹이 DB에 등록되지 않은 메뉴 그룹일 경우
- 메뉴 목록을 볼 수 있다
- 주문 테이블을 추가할 수 있다
- 주문 테이블을 빈 테이블로 변경할 수 있다
  - [예외]
  - 주문 테이블의 테이블 그룹 id가 null이 아닐 경우
  - 변경할 주문 테이블의 주문 상태가 조리나 식사일 경우
- 주문 테이블의 방문한 손님 수를 변경할 수 있다
  - [예외]
  - 변경 방문 손님 수가 음수일 때
  - 변경할 주문 테이블이 빈 테이블일 때
- 주문 테이블 목록을 볼 수 있다
- 주문을 추가할 수 있다
  - [예외]
  - 주문의 주문 항목 목록이 비어있을 경우
  - 주문 항목의 메뉴가 DB에 등록되지 않았을 경우
  - 주문 테이블이 빈 테이블일 경우
- 주문 상태를 변경할 수 있다
  - [예외]
  - 변경할 주문 상태가 완료일 경우
- 주문 목록을 볼 수 있다
- 테이블 그룹(단체 지정)을 추가할 수 있다
  - [예외]
  - 테이블 그룹의 주문 테이블 리스트의 사이즈가 2미만일 경우
  - 주문 테이블이 DB에 등록되지 않았을 경우
  - 주문 테이블이 빈 테이블이 아닐 경우
  - 주문 테이블의 테이블 그룹 id가 null이 아닐 경우
- 테이블 그룹을 ungroup할 수 있다
  - [예외]
  - 주문 테이블의 주문 상태가 조리나 식사일 경우
  




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
