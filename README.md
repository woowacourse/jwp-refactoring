# 키친포스

## 요구 사항

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


## 기능 목록

## 메뉴 그룹

```bash
###
POST {{host}}/api/menu-groups
Content-Type: application/json

{
  "name": "추천메뉴"
}

###
GET {{host}}/api/menu-groups

###
```

`/api/menu-groups`

- 메뉴 그룹을 생성한다.
    - 메뉴 그룹을 저장한다.
    - ex) 추천 메뉴

## 메뉴

```bash
###
POST {{host}}/api/menus
Content-Type: application/json

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

###
GET {{host}}/api/menus

###
```

- 메뉴(메뉴 그룹에 속하는 실제 주문 가능 단위)를 생성한다.
    - 메뉴 포함 정보
        - 이름
        - 가격
        - 포함되는 메뉴 그룹 아이디
        - 메뉴에 속하는 상품 리스트
    - [x] 전달 받은 메뉴의 가격이 입력되지 않았다면 예외가 발생한다.
    - [x] 전달 받은 메뉴의 가격이 0보다 작으면 예외가 발생한다.
    - [x] 유효하지 않은 메뉴 그룹 아이디를 전달 받으면 예외가 발생한다.
    - [x] 유효하지 않은 메뉴 메뉴 상품 아이디를 전달 받으면 예외가 발생한다. 
    - [x] 메뉴의 가격이 메뉴에 포함된 상품 가격을 합친 것보다 작은 경우 예외가 발생한다.

## 주문

```bash
###
POST {{host}}/api/orders
Content-Type: application/json

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

- 메뉴를 주문한다.
    - 주문 테이블 아이디를 받는다.
        - 유효하지 않은 주문 테이블 아이디라면 예외가 발생한다.
        - 주문 테이블 아이디에 해당하는 주문 테이블이 empty table이라면 예외가 발생한다.
    - 메뉴를 리스트로 받는다.
        - 주문 항목이 1개 미만이면 예외가 발생한다.
        - 주문 항목에서 입력받은 메뉴가 올바른 메뉴가 아니라면 예외가 발생한다.

```bash
###
GET {{host}}/api/orders
```

- 모든 주문 내역을 조회한다.

```bash
###
PUT {{host}}/api/orders/1/order-status
Content-Type: application/json

{
  "orderStatus": "MEAL" # COOKING, COMPLETION
} 
```

- 주문 상태를 변경한다.
    - 주문 아이디와 주문 상태를 전달 받는다.
    - 주문 상태는 조리(`COOKING`) ➜  식사(`MEAL`) ➜  계산 완료(`COMPLETION`) 순서로 진행된다.
    - 유효하지 않은 주문 번호를 입력한 경우 예외가 발생한다.
    - order status가 잘못 입력된 경우 예외가 발생한다.

## 상품

```bash
###
POST {{host}}/api/products
Content-Type: application/json

{
  "name": "강정치킨",
  "price": 17000
}
```

- 상품을 등록한다.
    - 상품 이름을 입력 받는다.
    - 상품 가격을 입력 받는다.
        - 상품 가격이 입력되지 않았거나 0 보다 작은 경우 예외가 발생한다.

```bash
###
GET {{host}}/api/products

```

- 등록된 모든 상품을 조회한다.

## 단체 테이블

```bash
###
POST {{host}}/api/table-groups
Content-Type: application/json

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

- 단체 테이블을 등록할 수 있다.
    - 테이블 아이디를 전달한다.
        - 테이블 아이디가 입력되지 않은 경우 예외가 발생한다.
        - 테이블 아이디 개수가 2보다 작은 경우 예외가 발생한다.

```bash
###
DELETE {{host}}/api/table-groups/1
```

- 단체 테이블을 삭제할 수 있다.
    - 단체 테이블 아이디를 입력 받는다.
    - 단체 테이블에 포함된 주문 테이블 중 주문 상태가 조리(`COOKING`) 또는 식사(`MEAL`)인 경우 예외가 발생한다.

## 테이블

```bash
###
POST {{host}}/api/tables
Content-Type: application/json

{
  "numberOfGuests": 0,
  "empty": true
}
```

- 테이블을 등록할 수 있다.
    - 손님 수를 저장한다.
    - 사용 불가능한 좌석 상태를 저장한다.

```bash
###
GET {{host}}/api/tables
```

- 테이블을 모두 조회할 수 있다.

```bash
###
PUT {{host}}/api/tables/1/empty
Content-Type: application/json

{
  "empty": false
}
```

- 사용 불가능한 테이블 상태(`empty`)를 사용 가능한 상태로 바꾼다.
    - 유효하지 않은 테이블 아이디를 전달 받은 경우 예외가 발생한다.
    - 테이블 아이디가 그룹 테이블에 포함되어 있다면 예외가 발생한다.
    - 만약 주문 테이블 아이디에 해당하는 테이블의 주문 상태가 `COOKING` 또는 `MEAL` 인 경우 예외가 발생한다.

```bash
###
PUT {{host}}/api/tables/1/number-of-guests
Content-Type: application/json

{
  "numberOfGuests": 4
}
```

- 손님 수를 변경한다.
    - 입력 받은 손님 수가 0보다 작으면 예외가 발생한다.
    - 유효하지 않은 주문 테이블 아이디를 전달 받은 경우 예외가 발생한다.
