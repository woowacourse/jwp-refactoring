# 키친포스 👩🏻‍🍳
- [미션 요구사항](./docs)

### Product
- 새 상품을 등록한다.  
  - 상품의 가격은 null이 될 수 없다.
  - 상품의 가격은 음수가 될 수 없다.  
- 등록된 모든 상품을 조회한다.  

### Order
- 새 주문을 등록한다.  
  - 주문 항목은 비어있을 수 없다.  
  - 주문 항목은 null이 될 수 없다.  
  - 주문 항목과 menuId는 일대일이다.  
  - 주문에는 유효한 TableId가 등록되어야한다.  
  - 주문에 등록된 테이블은 비어있을 수 없다.  
- 주문 항목을 포함한 모든 주문 목록을 조회한다.  
- 주문 상태를 변경한다.  
  - 주문 상태에는 조리, 식사, 계산 완료가 있다.  
  - 상태를 변경할 주문에 해당하는 주문이 존재해야한다.  
  - 상태를 변경할 주문이 이미 계산 완료인 경우 상태를 변경할 수 없다.  

### Menu
- 새 메뉴를 등록한다.  
  - 메뉴의 가격은 null이 될 수 없다.  
  - 메뉴의 가격은 음수가 될 수 없다.  
  - 존재하는 메뉴 그룹 Id를 포함해야한다.  
  - 등록할 메뉴의 상품이 존재해야한다.  
  - 메뉴 상품과 수량을 곱한 값이 메뉴에 등록할 값과 같거나 작아야한다.  
- 메뉴 상품을 포함한 모든 메뉴 목록을 조회한다.  
- 새 메뉴 그룹을 등록한다.  
- 모든 메뉴 그룹을 조회한다.  

### Table
- 새 테이블을 등록한다.
- 모든 주문 테이블을 조회한다.  
- 주문 테이블을 빈 테이블로 수정한다.  
  - 존재하는 테이블만 수정할 수 있다.  
  - 단체로 지정된 테이블은 수정할 수 없다.  
  - 수정할 테이블에는 계산 완료 상태의 주문이 포함될 수 없다.
- 주문 테이블에 방문한 손님 수를 수정한다.  
  - 방문 손님 수는 음수가 될 수 없다.  
  - 존재하는 테이블만 수정할 수 있다.  
  - 비어있지 않은 테이블만 수정할 수 있다.  
- 주문 테이블을 그룹화하여 단체로 지정한다.
  - 테이블그룹에 주문 테이블이 비어있을 수 없다.
  - 테이블그룹에 포함된 주문 테이블은 2개 이상이어야 한다.
  - 입력으로 들어온 주문 테이블과 저장된 주문 테이블의 수는 같아야한다.
  - 비어있지 않은 주문 테이블을 포함할 수 없다.  
  - 주문 테이블에 연결된 테이블 그룹이 없어야한다.  
- 단체 테이블의 그룹을 해제한다.   
  - 계산완료되지 않은 테이블을 포함한 경우 해제할 수 없다.  

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
