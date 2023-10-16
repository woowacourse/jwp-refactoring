# 키친포스

## 요구 사항
- 1단계
  - [x] 서비스 테스트 코드 작성


## 기능 목록

### Controller

1. MenuGroupRestController
   - 메뉴 그룹을 생성할 수 있다. (`POST` - `/api/menu-groups`)
   - 모든 메뉴 그룹 목록을 확인할 수 있다. (`GET` - `/api/menu-groups`)

2. MenuRestController
   - 특정 메뉴 그룹에 대한 메뉴를 생성할 수 있다. (`POST` - `/api/menus`)
   - 모든 메뉴 목록을 확인할 수 있다. (`GET` - `/api/menus`)

3. OrderRestController
   - 주문을 생성할 수 있다. (`POST` - `/api/orders`)
   - 모든 주문 목록을 확인할 수 있다. (`GET` - `/api/orders`)
   - 특정 주문의 주문 상태를 변경할 수 있다. (`PUT` - `/api/orders/{orderId}/order-status`) 

4. ProductRestController
   - 상품을 생성할 수 있다. (`POST` - `/api/products`)
   - 모든 상품 목록을 확인할 수 있다. (`GET` - `/api/products`)

5. TableGroupRestController
   - 단체 지정 주문을 생성할 수 있다. (`POST` - `/api/table-groups`)
   - 단체 지정 주문을 삭제할 수 있다. (`DELETE` - `/api/table-groups/{tableGroupId}`)

6. TableRestController
   - 주문 테이블을 생성할 수 있다. (`POST`- `/api/tables`)
   - 주문 테이블 목록을 확인할 수 있다. (`GET` - `/api/tables`)
   - 주문 테이블이 주문을 등록할 수 없도록 변경할 수 있다. (`PATCH` - `/api/tables/{orderTableId}/empty`)
   - 주문 테이블에 손님의 수를 변경할 수 있다. (`PUT` - `api/tables/{orderTableId}/number-of-guests`)

### Service

1. MenuGroupService
   - `create` 메뉴 그룹을 생성할 수 있다.
   - `list` 메뉴 그룹 목록을 확인할 수 있다.

2. MenuService
   - `create` 메뉴를 생성할 수 있다.
     - [조건] 메뉴의 가격은 0 이상의 정수이다.
     - [조건] 메뉴를 등록할 메뉴 그룹이 존재해야한다.
     - [조건] 메뉴에 등록한 상품이 존재해야한다.
     - [조건] 메뉴 금액과 메뉴에 등록된 상품의 총 합은 같아야한다.
   - `list` 메뉴 목록을 확인할 수 있다.

3. OrderService
   - `create` 주문을 생성할 수 있다.
     - [조건] 주문 항목에 주문이 존재해야한다.
     - [조건] 메뉴의 수와 실제 주문한 메뉴의 수는 같아야한다.
     - [조건] 주문을 등록할 주문 테이블이 존재해야한다.
     - [조건] 주문을 등록할 주문 테이블은 빈 테이블이 아니어야한다.
   - `list` 주문 목록을 확인할 수 있다.
   - `changeOrderStatus` 주문의 상태를 변경할 수 있다.
     - [조건] 주문이 존재해야한다.
     - [조건] 이미 완료된 주문이 아니어야한다.

4. ProductService
   - `create` 상품을 생성할 수 있다.
     - [조건] 상품의 가격은 0 이상의 정수이다.
   - `list` 상품 목록을 확인할 수 있다.

5. TableGroupService
   - `create` 단체 지정 주문을 생성할 수 있다.
     - [조건] 빈 테이블이 아니고, 2명 이상이 주문해야한다.
     - [조건] 설정된 주문 테이블 수와 실제 주문 테이블 수가 같아야한다.
     - [조건] 새로 생성된 테이블이 빈 테이블이어야한다.
   - `ungroup` 단체 지정 주문을 soft delete 할 수 있다

6. TableService
   - `create` 주문 테이블을 생성할 수 있다.
   - `list` 주문 테이블 목록을 확인할 수 있다.
   - `changeEmpty` 주문을 더이상 등록할 수 없는 상태로 변경한다. (이미 계산이 끝난 주문을 수정할 수 없도록 만든 것 같음)
   - `changeNumberOfGuests` 주문 테이블의 손님의 수를 변경할 수 있다.

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
