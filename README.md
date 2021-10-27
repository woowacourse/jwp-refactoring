# 키친포스

## 요구 사항

## 메뉴 그룹 (MenuGroup)
- [x] 메뉴 그룹을 추가할 수 있다.
- [x] 메뉴 그룹을 조회할 수 있다. 

## 메뉴(Menu)
- [x] 메뉴를 추가할 수 있다. 
  - [x] 가격은 0 원 이상이어야 한다. (0 이하 혹은 비어있으면 예외가 발생한다)
  - [x] 메뉴가 포함된 '메뉴 그룹'이 존재하지 않으면 예외가 발생한다. 
  - [x] 메뉴 상품들 중에 존재하지 않는 상품이있으면 예외가 발생한다.
  - [x] 메뉴의 가격이 개별로 주문한 금액을 더한 값보다 크면 예외가 발생한다. (할인O, 할인X를 염두한 조건인 듯)
- [x] 메뉴를 조회할 수 있다.

## 주문(Order)
- [x] 주문을 생성할 수 있다. 
  - [x] 주문 항목이 비어있는 경우 예외가 발생한다. 
  - [x] 주문 항목들 중에 존재하지 않는 주문 항목이 있다면 예외가 발생한다.
  - [x] 존재하지 않는 주문 테이블이면 예외가 발생한다. 
- [x] 주문의 상태를 변경한다. 
  - [x] 이미 '계산 완료'된 주문이라면 예외가 발생한다.
- [x] 주문 목록을 조회할 수 있다.

### 상품(Product)
- [x] 상품을 등록할 수 있다.
- [x] 상품의 가격이 올바르지 않으면 등록할 수 없다.
    - [x] 상품의 가격은 0 원 이상이어야 한다.
- [x] 상품의 목록을 조회할 수 있다.

### 단체 지정(TableGroup)
- [x] 새로운 단체 지정을 추가할 수 있다.
  - [x] 주문 테이블의 갯수는 2개 이상이어야 한다. (2 미만이면 예외가 발생한다)
  - [x] 존재하지 않는 주문 테이블이 있으면 예외가 발생한다. 
  - [x] 주문 테이블이 비어있지 않은 주문 테이블이 있으면 예외가 발생한다.  
  - [x] 이미 단체 지정이 되어있으면 예외가 발생한다.
- [x] 지정된 단체 지정을 해제할 수 있다. 
  - [x] '계산 완료' 상태가 아닌 주문 테이블이 있다면 예외가 발생한다.

### 최종적으로 체크 
- [ ] Integration Test, Unit Test를 꼼꼼히 작성한다.
- [ ] 롬복을 사용하지 않고 구현한다.

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
