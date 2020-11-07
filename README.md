# 키친포스

## 요구 사항

### 상품

- 메뉴에 들어가는 상품을 생성할 수 있다.
    - 상품은 상품의 이름과 가격을 포함한다.
    - 가격은 음수일 수 없다.

- 상품을 모두 조회할 수 있다.
    - 모든 상품을 한 번에 조회할 수 있다.
    
### 메뉴 그룹

- 메뉴 그룹을 생성할 수 있다.
    - 메뉴 그룹은 어떠한 메뉴가 속한 그룹이다.
    
- 메뉴 그룹을 조회할 수 있다.
    - 모든 메뉴 그룹을 조회할 수 있다.
    
### 메뉴
 
- 새로운 메뉴를 생성할 수 있다
    - 메뉴는 이름, 가격, 메뉴 그룹, 상품의 개수를 포함한다.
    - 메뉴는 특정한 메뉴 그룹에 속한다.
    - 메뉴의 가격은 음수일 수 없다.
    - 메뉴 가격은 메뉴에 포함된 상품 가격의 총 합보다 작거나 같아야 한다.

- 메뉴 목록을 조회할 수 있다.
    - 모든 메뉴를 한 번에 조회할 수 있다.

### 주문 테이블

- 주문 테이블을 생성할 수 있다.
    - order table은 tablegroup id와 사람 수, 현재 비어 있는지에 대한 정보를 담고 있다.

- 모든 table을 조회할 수 있다.
    - 모든 주문 테이블을 한 번에 조회할 수 있다.

- 주문 테이블을 비어있는 상태로 만들 수 있다.
    - 빈 테이블 설정을 위해서는 테이블 그룹에 속해 있지 않아야 한다.
    - 주문 테이블의 상태가 cooking이거나 meal인 상태가 아니어야 한다.

- 주문 테이블에 속한 손님의 수를 변경할 수 있다. 
    - 손님의 수는 음수일 수 없다.
    - 비어있는 주문 테이블에는 손님의 수를 변경할 수 없다.
    
### 테이블 그룹

- 테이블 그룹을 생성할 수 있다.
    - [x] 여러 주문 테이블을 한 그룹으로 형성할 수 있다.
    - [x] 최소 2개의 테이블을 한 그룹으로 묶을 수 있다.
    - [x] 한 그룹으로 묶으려고 하는 테이블들은 이미 생성되어 있어야 한다.
    - [x] 그룹으로 묶으려는 주문 테이블은 비어있어야 한다.
    - [x] 그룹으로 묶으려는 주문 테이블은 다른 그룹에 속하지 않아야 한다.
    
- 테이블 그룹을 해제할 수 있다.
    - [x] 그룹으로 지정된 테이블들을 해제할 수 있다.
    - [x] 그룹으로 지정된 모든 테이블 중 하나라도 '조리' 또는 '식사 중' 상태이면 해제할 수 없다.
    
### 주문

- 주문을 생성할 수 있다.
    - 주문은 1개 이상의 메뉴를 포함해야 한다.
    - 존재하지 않는 메뉴는 주문할 수 없다.
    - 주문하는 테이블이 없거나 비어있느면 주문할 수 없다.
    - 테이블이 비어있으면 주문할 수 없다.

- 주문을 조회할 수 있다.
    - 모든 주문 목록을 조회할 수 있다.
    
- 주문의 상태를 변경할 수 있다.
    - 주문 상태가 '완료'이면 상태를 변경할 수 없다.


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
