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

## 요구 사항

### 메뉴 그룹 (Menu Group)
* 메뉴 그룹 목록을 조회할 수 있다. 
* 메뉴 그룹을 생성할 수 있다.

### 메뉴 (Menu)
* 메뉴를 생성할 수 있다.
  * 가격은 0원 이상이여야 한다.
  * 메뉴 그룹이 존재해야 한다.
  * 메뉴를 구성할 상품이 존재해야 한다.
  * 각 상품의 가격의 합은 메뉴의 가격보다 클 수 없다.
* 메뉴의 목록을 조회할 수 있다.

### 주문 (Order)
* 주문을 생성할 수 있다.
  * 주문 항목은 비어있을 수 없다.
  * 주문 항목을 구성하는 메뉴는 존재해야 한다.
  * 주문 항목을 구성하는 메뉴는 중복될 수 없다.
  * 주문 테이블이 존재해야 한다. (빈 테이블이 아니어야 한다.)
  * 주문은 생성시 조리(COOKING) 상태가 된다.
* 주문 목록을 조회할 수 있다.
* 주문의 상태를 변경할 수 있다.
  * 주문이 존재해야 한다.
  * 주문 상태가 계산 완료(COMPLETION)이면 변경할 수 없다.
  * 주문 상태 변경은 조리 ➜ 식사 ➜ 계산 완료 순서로 진행돼야 한다.

### 상품 (Product)
* 상품을 생성할 수 있다.
  * 가격은 0원 이상이여야 한다.
* 상품 목록을 조회할 수 있다.

### 테이블 그룹 (Table Group)
* 테이블 그룹을 생성할 수 있다.
  * 2개 이상의 테이블로 테이블 그룹을 형성할 수 있다.
  * 테이블 그룹을 구성하는 테이블은 존재해야 한다.
  * 테이블 그룹을 구성하는 테이블은 중복될 수 없다.
  * 테이블 그룹으로 묶여진 테이블은 빈 테이블이어야 한다.
  * 이미 테이블 그룹을 형성한 테이블을 묶을 수 없다.
* 테이블 그룹을 삭제할 수 있다.
  * 테이블 그룹을 형성하는 테이블은 계산 완료(COMPLETION)이어야 한다.
  * 테이블의 테이블 그룹을 해제시킨다. (해제 시킨 테이블은 주문 테이블로 설정한다.)

### 테이블 (Table)
* 테이블을 생성할 수 있다.
  * 생성 시 테이블 그룹 ID는 null로 설정한다.
* 테이블 목록을 조회할 수 있다.
* 테이블의 상태(빈 또는 주문 테이블)를 변경할 수 있다.
  * 테이블이 존재해야 한다.
  * 테이블 그룹이 없어야 한다.
  * 테이블은 계산 완료(COMPLETION)이어야 한다.
* 테이블의 방문한 손님 수를 변경할 수 있다.
  * 변경하려고 하는 방문 손님 수는 0명 이상이어야 한다.
  * 테이블은 존재해야 한다. (빈 테이블이 아니어야 한다.)
