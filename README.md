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

## TODO
- [x] 요구 사항 가독성 있게 변경

## 요구 사항

- [ ] setter 정리
- [ ] ui 레이어와 service 레이어 분리
  - [x] Product
  - [x] OrderTable
  - [x] TableGroup
  - [x] Order
  - [ ] Menu
  - [ ] MenuGroup
- [ ] JPA 로 마이그레이션, Repository 적용
- [ ] test template 에서 실제 객체가 아닌 dto 를 사용하도록 리팩토링

### 도메인 로직 분리
- [x] MenuGroupService : 로직이 간단하여 분리할 것이 없음.
- [ ] MenuService
- [ ] OrderService
- [ ] ProductService
- [ ] TableGroupService
- [ ] TableService

### 통합 테스트
- [x] /api/menus POST success
- [x] /api/menus GET success
- [x] /api/menu-groups POST success
- [x] /api/menu-groups GET success
- [x] /api/menus POST success
- [x] /api/menus GET success
- [x] /api/menu-groups POST success
- [x] /api/menu-groups GET success
- [x] /api/orders POST success
- [x] /api/orders GET success
- [x] /api/orders PUT success
- [x] /api/products POST success
- [x] /api/products GET success
- [x] /api/tables POST success
- [x] /api/tables GET success
- [x] /api/tables/{orderTableId}/empty PUT success
- [x] /api/tables/{orderTableId}/number-of-guests PUT success
- [x] /api/table-groups POST success
- [x] /api/table-groups/{tableGroupId} DELETE success

### 서비스 슬라이스 테스트

- [x] Menu 테스트
  - [x] Menu 생성 성공
  - [x] Menu 생성 실패
    - [x] menu 의 price 가 null 인 경우
    - [x] menu 의 price 가 음수인 경우
    - [x] menuGroupId 에 해당하는 menuGroup 이 존재하지 않는 경우
    - [x] menuProduct 의 productId 에 해당하는 product 가 존재하지 않는 경우
    - [x] menu 의 price 가 모든 product 의 총 금액보다 큰 경우
  - [x] /api/menus GET success

- [x] MenuGroup 테스트
  - [x] 로직이 간단해서 추가하지 않음

- [x] Order 테스트
  - [x] Order 생성 성공
  - [x] Order 생성 실패
    - [x] orderLineItems 가 비어있는 경우
    - [x] orderLineItem 개수가 menu 수와 일치하지 않는 경우
    - [x] orderTable 이 존재하지 않는 경우
    - [x] orderTable 이 비어있는 경우
  - [x] 모든 Order 조회 성공
  - [x] Order 상태 변경 성공
  - [x] /api/orders PUT failure
    - [x] orderId 에 대한 order 가 존재하지 않는 경우
    - [x] order 가 이미 완료된 order 인 경우

- [x] Product 테스트
  - [x] Product 생성 성공
  - [x] Product 생성 실패
    - [x] product 의 price 가 null 인 경우
    - [x] product 의 price 가 음수인 경우
  - [x] 모든 Product 조회 성공

- [x] Table 테스트
  - [x] Table 생성 성공
  - [x] 모든 Table 조회 성공
  - [x] Table empty 상태 변경 성공
  - [x] Table empty 상태 변경 실패
    - [x] orderTable 이 존재하지 않는 경우
    - [x] tableGroup 이 존재하는 경우
    - [x] order 의 상태가 COMPLETION 이 아닌 경우
  - [x] Table numberOfGuests 상태 변경 성공
  - [x] Table numberOfGuests 상태 변경 실패
    - [x] numberOfGuests 가 음수인 경우
    - [x] 저장된 orderTable 이 존재하지 않는 경우
    - [x] 저장된 orderTable 이 비어있는 경우

- [x] TableGroup 테스트
  - [x] TableGroup 생성 성공
  - [x] TableGroup 생성 실패
    - [x] 요청한 orderTables 가 비어있는 경우
    - [x] 요청한 orderTable 의 수가 2 개 미만인 경우
    - [x] 요청한 orderTable 중 존재하지 않는 것이 있는 경우
    - [x] 요청한 orderTable 이 비어있지 않는 경우
    - [x] 요청한 orderTable 이 이미 tableGroup 을 가지고 있는 경우
  - [x] TableGroup 삭제 성공
  - [x] TableGroup 삭제 실패
    - [x] 요청한 orderTable 의 order 상태가 COMPLETION 이 아닌 경우
