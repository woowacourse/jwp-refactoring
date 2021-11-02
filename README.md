# 키친포스

## 요구 사항

### 메뉴그룹

- [x] 메뉴그룹 생성
  - [x] 메뉴그룹은 id와 name으로 이루어져 있다.
- [x] 모든 메뉴그룹 가져오기



### 메뉴

- [x] 메뉴 생성
  - [x] id, name, price, menuGroup, menuProducts로 구성됨
  - [x] 메뉴가격이 비어있거나 0보다 작으면 에러 처리
  - [x] 메뉴그룹ID가 없다면 에러처리
  - [x] MenuProducts는 seq, menuId, ProductId, quantity로 구성됨
  - [x] ProductId가 db에 없다면 에러 처리
  - [x] 메뉴의 합이 db에서 가져온 상품의의 합보다 크다면 에러처리
- [x] 모든 메뉴 가져오기



### 주문

- [x] 주문 생성
  - [x] 주문은 id, OrderTableId, orderStatus, orderedTime, OrderLineItems로 구성됨
  - [x] OrderLineITem은 seq, orderId, menuId, quantity로 구성됨
  - [x] orderLineItems가 비어있다면 에러 처리
  - [x] orderLineItems와 DB의 메뉴가 일치하지 않으면 에러 처리
   - [x] orderTable이 없거나 내용이 비어있다면 에러 처리
- [x] 모든 주문 가져오기
- [x] 주문 수정하기
  - [x] 주문번호를 이용하여 주문내역 수정
  - [x] 주문번호로 조회시 없다면 에러 출력
  - [x] 주문이 완료된 상태라면 에러 처리



### 상품

- [x] 제품 생성
  - [x] 제품은 id, name, price로 구성되어 있다.
  - [x] 가격이 비어있거나 0보다 적다면 에러 처리
- [x] 모든 제품 가져오기



### 테이블

- [x] 테이블 생성
  - [x] OrderTable은 id, TableGroupId, memberOfGuests, empty로 구성됨
- [x] 테이블 리스트 가져오기
- [x] 빈 테이블로 변경
  - [x] id를 이용해 빈 테이블로 변경한다
  - [x] 잘못된 id가 들어오면 에러 처리
  - [x] 저장된 테이블에 그룹ID가 포함되어 있다면 에러처리
  - [x] 저장된 테이블이 요리중이거나 식사중이라면 에러 처리
- [x] 테이블의 인원을 변경한다.
  - [x] 인원이 0보다 적으면 에러 처리
  - [x] 잘못된 id가 들어오면 에러 처리
  - [x] 저장된 테이블이 비어있다면 에러 처리


### 단체

- [x] 단체 생성
  - [x] 단체는 id, createdDate, orderTables로 지정됨
  - [x] OrderTable은 id, TableGroupId, memberOfGuests, empty로 구성됨
  - [x] 역할에 대한 것은 용어사전에 설명이 되어 있다.
  - [x] 테이블 사이즈가 없거나 2 미만이라면 에러 처리.
  - [x] db에 저장된 테이블 정보와 다르다면 에러 처리.
  - [x] 이미 id가 지정되어 있다면 에러처리
- [x] 그룹 해제
  - [x] 식사중이거나 요리중이면 에러 처리



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