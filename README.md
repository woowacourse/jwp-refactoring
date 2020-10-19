# 키친포스
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
---

## 기능 요구사항 
### 상품 (Product)
- 상품 추가, 전체 목록 조회 기능
- 상품 추가 기능
  - 추가 시, **이름, 가격**을 입력 받는다.
  - 상품의 가격은 0원 이상이어야 한다. 
    - 가격이 0원 미만인 경우 등록 할 수 없다.

### 메뉴 그룹 (Menu Group)
- 메뉴 그룹 추가, 전체 목록 조회 기능이 있다.
- 메뉴 그룹 추가 기능
  - 추가시 **이름**을 입력 받는다.

### 메뉴 (Menu)
- 메뉴 등록, 전체 목록 조회 기능이 있다.
- 메뉴 등록 기능
  - 메뉴 등록시, **이름, 가격, 메뉴 그룹, 메뉴 상품 목록** 을 입력받는다.
  - 메뉴의 가격은 0원 이상이어야 한다.
    - 가격이 0원 미만인 경우 등록 할 수 없다.
  - 메뉴는 반드시 하나의 메뉴 그룹에 소속해야 한다.
    - 메뉴 등록시 소속 메뉴 그룹이 없으면 등록할 수 없다.
  - 메뉴에 등록된 가격은 **메뉴 상품 목록** 가격의 합보다 작거나 같아야 한다. 
    - 메뉴의 가격이 메뉴 상품 목록의 가격의 합보다 크면 메뉴를 등록할 수 없다. 

### 주문테이블 (OrderTable)
- 전체 목록 조회, 테이블 등록, 테이블 손님 수 변경, 빈 테이블로 변경 기능
- 새 테이블 등록 기능
  - 손님 인원 수는 필수 값이 아니다. 
    - 값이 비어있는 경우, 기본 값을 0으로 입력받는다.
    - 빈 테이블 여부는 기본 값을 true로 입력 받는다.
- 방문한 손님 수 변경 기능
  - 방문한 손님수 변경 시, 변경 값은 0 이상으로 받는다.
    - 테이블 손님 인원 값이 0 미만인 경우, 변경할 수 없다.
  - 빈 테이블이 경우, 손님 수를 변경 할 수 없다. 
- 빈 테이블로 변경 기능
  - 단체 테이블은 빈 테이블로 변경할 수 없다. 
  - 테이블의 주문 상태가 **조리중, 식사중**인 경우 빈 테이블로 변경 할 수 없다.

### 단체 지정 (TableGroup)
- 테이블 그룹 지정, 테이블 그룹 해제 기능 
- 테이블 그룹 지정 기능 
  - 그룹으로 지정할 테이블 목록을 입력 받는다.
  - 그룹으로 지정할 테이블의 수는 2개 이상이여야 한다. 
    - 테이블의 수가 1개 이하인 경우, 그룹 지정을 할 수 없다. 
  - 입력 받는 테이블 목록에는 중복된 테이블이 들어갈 수 없다.
  - 테이블 중에 **비어있지 않는** 테이블은 들어갈 수 없다. 
  - 테이블 중 **이미 그룹 지정된 테이블**은 들어갈 수 없다. 
- 테이블 그룹 해제 기능
  - 그룹 해제할 테이블 그룹의 ID를 입력 받는다.
  - 그룹에 포함된 테이블 중 **조리중, 식사중**인 테이블이 존재하면, 테이블 그룹 해제를 할 수 없다.
  
### 주문
 - 주문 추가, 주문 목록 조회, 주문 상태 변경 기능
 - 주문 추가 기능
    - 1 개 이상의 등록된 메뉴로 주문을 등록할 수 있다.
    - 빈 테이블에는 주문을 등록할 수 없다.
 - 전체 주문 조회 기능
 - 주문 상태 변경 기능
    - 주문 상태가 계산 완료인 경우 변경할 수 없다.