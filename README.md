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

## 기능 요구 사항
### 상품
-[ ] 상품을 조회한다
-[ ] 상품을 추가한다
  -[ ] 상품이름은 제약조건이 없다
  -[ ] 상품가격은 0이상이어야 한다

### 메뉴
-[ ] 메뉴를 조회한다
-[ ] 메뉴를 추가한다
  -[ ] 메뉴가격은 0이상이어야 한다
  -[ ] 메뉴에 해당하는 메뉴그룹이 존재해야한다
  -[ ] 메뉴에 해당하는 상품이 없으면 안된다
  -[ ] 메뉴의 가격이 해당 상품들의 가격 * 수량보다 크면 안된다 (세트메뉴가 낱개들의 합 보다 크면 안된다)

### 메뉴그룹
-[ ] 메뉴그룹을 조회한다
-[ ] 메뉴그룹을 추가한다

### 테이블그룹
-[ ] 테이블그룹을 추가한다
  -[ ] 그룹화될 주문테이블이 2개미만이면 안된다
  -[ ] 그룹화될 주문테이블은 모두 존재해야 한다
  -[ ] 그룹화될 주문테이블이 주문 가능한 상태이거나(notEmpty) 이미 테이블 그룹에 묶여있으면 안된다
  -[ ] 오더테이블를 주문 가능한 상태(notEmpty)로 바꾸고, 테이블 그룹을 추가한다
-[ ] 테이블그룹을 삭제한다
  -[ ] 테이블 그룹에 속한 주문테이블에 해당하는 주문들이 COOKING또는 MEAL 상태이면 안된다
  -[ ] 테이블 그룹에 속한 주문테이블의 상태를 주문이 가능한 상태(notEmpty)로 바꾸고, 테이블 그룹 id를 null로 바꾼다

### 주문테이블
-[ ] 주문테이블을 조회한다
-[ ] 주문테이블을 추가한다
  -[ ] 주문테이블 id와 테이블그룹 id는 null로 변경 후 저장한다
    -[ ] 값이 있어도 null로 변경해야 한다
-[ ] 주문테이블을 수정한다
  - empty
    -[ ] 해당 주문테이블이 없으면 안된다
    -[ ] 해당 주문테이블에 테이블 그룹이 연결되어 있으면 안된다
    -[ ] 해당 주문테이블에 연결된 COOKING또는 MEAL 상태의 주문이 존재하면 안된다
  - number-of-guests
    -[ ] 해당 주문테이블이 없으면 안된다
    -[ ] 게스트 수가 0미만이면 안된다
    -[ ] 해당 주문테이블이 주문을 할 수 없는 테이블(Empty)상태이면 안된다

### 주문
-[ ] 주문을 조회한다
-[ ] 주문을 추가한다
  -[ ] 주문목록이 비어있으면 안된다
  -[ ] 주문목록내 메뉴가 없는 메뉴이면 안된다
  -[ ] 주문테이블은 이미 존재해야 한다
  -[ ] 주문테이블이 주문을 할 수 없는 테이블(empty)이면 안된다
  -[ ] 주문 상태를 COOKING으로 바꾸고 시각을 현재시각으로 바꾼다
-[ ] 주문상태를 수정한다
  -[ ] 해당하는 주문이 없으면 안된다
  -[ ] 수정하려는 주문의 주문상태가 이미 COMPLETION이면 안된다
  -[ ] 수정하려는 상태가 존재하지 않는 주문 상태면 안된다. (ex: OrderStatus = ready)

## ERD
![img.png](img.png)
