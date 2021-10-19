# 키친포스

## 요구 사항
### Order
#### 주문 등록 기능
* [ ] Order 를 받은 Table id와 Order 의 Menu 정보(Menu의 id와  Menu 의 수량)을 포함한 요청으로 Order 를 등록할 수 있다.
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
#### 주문 전체 조회 기능
* [ ] 전체 Order 를 조회할 수 있다.
  
#### 주문 상태 변경 기능 
* [ ] Order의 상태를 변경할 수 있다.
```json
{
  "orderStatus": "MEAL"
}
```

```json
{
  "orderStatus": "COMPLETION"
}
```

### Product
#### 상품 등록 기능
* [ ] Product name과 price로 Product를 등록할 수 있다.
```json
{
  "name": "강정치킨",
  "price": 17000
}
```
#### 상품 전체 조회 기능
* [ ] 전체 Product를 조회할 수 있다.

### Menu
#### 메뉴 생성 기능
* [ ] Menu 이름, 가격, 속하는 메뉴 그룹, 메뉴 포함하는 Product 정보를 포함한 요청으로 Menu를 등록할 수 있다.
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
#### 메뉴 전체 조회 기능 - @GetMapping("/api/menus")
* [ ] 전체 Menu를 조회할 수 있다.

### MenuGroup
#### 메뉴 그룹 생성 기능
* [ ] MenuGroup name을 포함한 요청으로 Menu Group을 생성할 수 있다.
```json
{
  "name": "추천메뉴"
}
```

#### 메뉴 그룹 전체 조회 기능 - @GetMapping("/api/menu-groups")
* [ ] 전체 MenuGroup을 조회할 수 있다.

### Table
#### 테이블 생성 기능
* [ ] 테이블 정보(테이블 상태, 테이블 방문 손님 수)를 포함한 요청으로 Table을 등록할 수 있다.
```json
{
  "numberOfGuests": 0,
  "empty": true
}
```
#### 테이블 전체 조회 기능
* [ ] 전체 Table을 조회할 수 있다.

#### 테이블 상태 비움 기능
* [ ] 특정 테이블 상태를 변경할 수 있다.
```json
{
  "empty": false
}
```
#### 테이블 인원 변경 기능
* [ ] 특정 테이블의 방문 손님 수를 변경할 수 있다.
```json
{
  "numberOfGuests": 4
}
```

### TableGroup
#### 테이블 그룹 생성 기능
* [ ] Table Group에 포함시킬 Table 리스트 정보를 포함한 요청으로 TableGroup을 등록할 수 있다.
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

#### 테이블 그룹 삭제 기능
* [ ] 특정 테이블 그룹을 삭제할 수 있다.

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
