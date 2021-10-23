# 키친포스

## 요구 사항

### 1. Product(상품)

#### Business Logic

* 생성
    * Product를 생성한다.
        * Product의 가격이 null이거나 음수면 예외가 발생한다.
* 조회
    * Product 목록을 조회한다.

#### API

* GET : /api/products
* POST : /api/products

<br>

### 2. MenuGroup(메뉴 그룹)

#### Business Logic

* 생성
    * MenuGroup을 생성한다.
* 조회
    * MenuGroup 목록을 조회한다.

#### API

* GET : /api/menu-groups
* POST : /api/menu-groups

<br>

### 3. MenuProduct(메뉴 상품)

#### Business Logic

* 개별 로직 없음

#### API

* 개별 로직 없음

<br>

### 4. Menu(메뉴)

#### Business Logic

* 생성
    * Menu를 생성한다.
        * Menu를 등록하기 위해서는 Menu가 속한 MenuGroup이 선행 존재해야한다.
        * Menu에 속한 MenuProduct와 연결된 Product를 ID로 조회하지 못하면 예외가 발생한다.  
        * Menu의 가격이 null이거나 음수면 예외가 발생한다.
        * Menu의 가격은 메뉴에 포함된 List\<MenuProduct(메뉴 상품)\>들의 누계 가격(각 MenuProduct 가격 * 수량)의 총합보다 크면 예외가 발생한다.
* 조회
    * Menu 목록을 조회한다.

#### API

* GET : /api/menus
* POST : /api/menus

<br>

### 5. OrderTable(주문 테이블)

#### Business Logic

* 생성
    * OrderTable을 생성한다.
* 조회
    * OrderTable 목록을 조회한다.
* 수정
    * OrderTable의 상태를 변경한다.
        * 상태를 변경하려는 테이블이 특정 TableGroup(단체 지정)에 이미 속해있으면 예외가 발생한다.
        * 상태를 변경하려는 테이블에 속한 Order들이 조리 중이거나 식사 중이라면 예외가 발생한다.
    * OrderTable의 손님 수를 변경한다.
        * 변경하려는 손님 수가 0 이하면 예외가 발생한다.
        * 손님 수를 변경하려는 테이블이 이미 빈 테이블이면 예외가 발생한다.

#### API

* GET : /api/tables
* POST : /api/tables
* PUT : /api/tables/{orderTableId}/empty
* PUT : /api/tables/{orderTableId}/number-of-guests

<br>

### 6. TableGroup(단체 지정)

#### Business Logic

* 생성
    * TableGroup을 생성한다.
        * TableGroup에 속한 List\<OrderTable\>들은 DB에 저장되어있어야한다.
        * TableGroup에 속한 List\<OrderTable\> 크기가 0이거나 2 미만이면 예외가 발생한다.
        * TableGroup에 속한 List\<OrderTable\>의 개별 OrderTable이 비어있지 않거나 이미 다른 TableGroup에 속한 경우 예외가 발생한다.
        * TableGroup에 속한 List\<OrderTable\>의 개별 OrderTable empty 속성은 false다.
* 삭제
    * TableGroup 단체 지정을 해지한다.
        * TableGroup에 속한 List\<OrderTable\>의 각 OrderTable에 속한 Order들이, 조리 중이거나 식사 중이라면 예외가 발생한다.
        * TableGroup에 속한 List\<OrderTable\>의 각 OrderTable empty 속성은 false로 변경된다.

#### API

* GET : /api/table-groups
* DELETE : /api/table-groups/{tableGroupId}

<br>

### 7. Order(주문)

#### Business Logic

* 생성
    * Order를 생성한다.
        * Order에 속한 List\<OrderLineItem(주문 항목)\>의 개별 OrderLineItem이 속한 Menu들은 모두 DB에 저장되어있어야한다.
        * Order가 속한 OrderTable(주문 테이블)이 DB에 저장되어있어야한다.
        * Order에 속한 List\<OrderLineItem(주문 항목)\>이 비어있으면 예외가 발생한다.
        * Order가 속한 OrderTable(주문 테이블)이 비어있으면 예외가 발생한다.
        * Order를 생성하면 Order의 상태는 Cooking이 된다.
* 수정
    * Order 상태를 수정한다.
        * 상태가 이미 Completion이면 예외가 발생한다.

#### API

* GET : /api/orders
* POST : /api/orders
* PUT : /api/orders/{orderId}/order-status

<br>

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
