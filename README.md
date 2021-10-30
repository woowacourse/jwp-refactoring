# 키친포스

## 요구 사항
### [상품 API] 
#### 메뉴를 구성하는 개별 상품과 관련된 기능
- **GET: /api/products**
    - 등록된 전체 상품들을 반환한다

- **POST: /api/products**
    - 새로운 상품을 등록한다
    ```json
    {
      "name": "강정치킨",
      "price": 17000
    }
    ```
----
### [메뉴 API] 
#### 실제 주문을 하는 단위인 메뉴와 관련된 기능
- **GET: /api/menus**
    - 등록된 전체 메뉴에 대한 정보를 반환한다

- **POST: /api/menus**
    - 새로운 메뉴를 생성한다
    ```json
    {
      "name": "후라이드+후라이드",
      "price": 19000,
      "menuGroupId": 1,
      "menuProducts": [
        {
          "productId": 1,
          "quantity": 2
        }
      ]
    }
    ```
----
### [메뉴 그룹 API] 
#### 메뉴들을 카테고리(동일한 유형)별로 묶는 메뉴 그룹과 관련된 기능
- **GET: /api/menu-groups**
    - 등록된 전체 메뉴 그룹 카테코리를 반환한다

- **POST: /api/menu-groups**
    - 메뉴 그룹 카테고리를 생성한다
    ```json
    {
      "name": "추천메뉴"
    }
    ```
----
### [주문 API]
#### 매장에서 발생하는 주문과 관련된 기능
- **GET: /api/orders**
    - 매장에서 발생한 주문들에 대한 정보를 반환한다

- **POST: /api/orders**
    - 매장에서 발생한 주문 정보를 생성한다
    ```json
    {
      "orderTableId": 1,
      "orderLineItems": [
        {
          "menuId": 1,
          "quantity": 1
        }
      ]
    }
    ```

- **PUT: /api/orders/{orderId}/order-status**
    - 매장에서 발생한 orderId에 해당하는 주문 정보를 수정한다
    ```json
    {
      "orderStatus": "MEAL"
    }
    ```
----
### [테이블 API]
#### 개별 테이블 정보와 관련된 기능
- **GET: /api/tables**
    - 매장에 있는 테이블들에 대한 정보를 반환한다. 

- **POST: /api/tables**
    - 매장에 있는 테이블 정보를 추가한다. 
    ```json
    {
      "numberOfGuests": 0,
      "empty": true
    }
    ```

- **PUT: /api/tables/{tableId}/empty**
    - 매장에 있는 테이블 중 tableId에 해당하는 테이블의 empty 여부를 변경한다. 
    ```json
    {
      "empty": true
    }
    ```

- **PUT: /api/tables/{tableId}/number-of-guests**
    -  매장에 있는 테이블 중 tableId에 해당하는 테이블의 number-of-guest를 변경한다.
    ```json
    {
      "numberOfGuests": 4
    }
    ```
----
### [테이블 그룹 API]
#### 개별 테이블을 묶어서 관리할 수 있는 테이블 그룹과 관련된 기능
- **POST: /api/table-groups**
    - 통합 계산을 위해 개별 테이블을 그룹화하는 테이블 그룹을 생성한다. 
    ```json
    {
      "orderTables": [
        {
          "id": 1
        },
        {
          "id": 2
        }
      ]
    }
    ```

- **DELETE: /api/table-groups/{tableGroupId}**
    - 통합 계산을 위해 개별 테이블을 그룹화하는 tableGroupId에 해당하는 테이블 그룹을 삭제한다.
----

## 리팩터링 중점 사안
- [ ] 핵심 비즈니스 로직은 도메인 객체가 담당하도록 구현할 것!
- [ ] 단위 테스트 가능한 코드를 분리해 단위 테스트 작성
    - 테스트하기 쉬운 부분과 어려운 부분을 분리할 것
    - 테스트 하기 쉬운 부분에 대해 단위 테스트 구현
- [x] JDBC를 JPA로 이전
    - [x] Dao 구현체로 부터의 의존성 제거
- [ ] setter 메서드 삭제
- [ ] 생성자 만들기
- [ ] DTO 만들기

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
