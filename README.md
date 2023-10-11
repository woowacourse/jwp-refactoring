# 키친포스

## 요구 사항

메뉴 그룹
* 메뉴 그룹 생성
* 메뉴 그룹 조회

메뉴
* 메뉴 생성
  * 메뉴의 가격은 null이거나 음수일 수 없다.
  * 메뉴 그룹의 id가 존재해야 한다.
* 메뉴 조회
  * MenuProduct를 set하고 반환한다.

주문
* 주문 생성
  * OrderLineItem이 비어있으면 안된다.
  * Menu의 id들이 존재해야 한다.
  * OrderTable이 존재해야 한다.
  * OrderTable이 비어있으면 안된다.
* 주문 조회
* 주문 상태 변경
  * 주문이 존재해야 한다.
  * 기존 Order의 status가 complete면 안된다.

제품
* Product 생성
  * price가 null이거나 음수면 안된다.
* 제품 조회

테이블 그룹
* 테이블 그룹 생성
  * orderTables가 비거나 2개 미만이면 안된다.
  * 저장된 orderTables와 요청한 orderTables의 개수가 같아야 한다.
  * 저장된 orderTable가 비어있지 않거나 group id가 null이 아니면 안된다.
* 테이블 그룹 해제
  * 테이블 중 cooking이거나 meal 상태면 안된다.

테이블
* 테이블 생성
* 테이블 조회
* 테이블 상태를 받아 빈상태 여부 변경
  * OrderTable이 존재해야 한다.
  * OrderTable의 groupId가 null이면 안된다.
  * OrderTable의 status가 cooking이거나 meal이면 안된다.
* 테이블 손님 수 변경
  * 변경되는 손님 수가 음수면 안된다.
  * OrderTable이 존재해야 한다.
  * OrderTable이 empty면 안된다.

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
