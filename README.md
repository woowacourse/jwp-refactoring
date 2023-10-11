# 키친포스

## 요구 사항

### Table
- 테이블을 생성한다
  - 요청 : POST api/tables
  - Header :
    - Location : /{tableId}
  - requestBody :
      ```json
      {
        "numberOfGuests" : int
      }
      ```
  - responseBody :
    ```json
     {
        "id": long,
        "tableGroupId": null,
        "numberOfGuests": int,
        "empty": false
     }
    ```
- 주문이 들어온 전체 테이블의 목록을 조회한다
  - 요청 : GET api/tables
  - responseBody :
    ```json
      {
        {
          "id" : long,
          "tableGroupId" : null,
          "numberOfGuests" : int,
          "empty" : boolean
        },
        {
          "id" : long,
          "tableGroupId" : null,
          "numberOfGuests" : int,
          "empty" : boolean
        } ...
    }
    ```
- 주문이 있는 테이블을 빈 테이블로 바꿀 수 있다
  - 요청 : PUT /api/tables/{orderTableId}/empty
  - requestBody : 
    ```json
    {
      "empty" : boolean
    }
    ```
  - responseBody :
    ```json
    {
        "id": long,
        "tableGroupId": null,
        "numberOfGuests": int,
        "empty": boolean
    }
    ```
- 테이블의 손님 수를 변경한다
    - 요청 : PUT /api/tables/{orderTableId}/number-of-guests
    - requestBody :
      ```json
      {
        "numberOfGuests": int
      }
      ```
    - responseBody :
      ```json
      {
          "id": long,
          "tableGroupId": null,
          "numberOfGuests": int,
          "empty": boolean
      }
      ```
### TableGroup
- 통합 계산을 위해 테이블을 엮는다
    - 요청 : POST /api/table-groups
    - requestBody :
      ```json

        {
          "id": long,
          "orderTables" : [
            {
              "id": long,
              "tableGroupId" : long
            },
            {
              "id": long,
              "tableGroupId" : long
            }
          ]
        }

      ```
    - responseBody :
      ```json
      {
          "id": long,
          "createdDate" : "2023-10-12T00:26:44.814989",
          "orderTables" : [
            {
              "id" : long,
              "tableGroupId" : null,
              "numberOfGuests" : int,
              "empty" : boolean
            },
            {
              "id" : long,
              "tableGroupId" : null,
              "numberOfGuests" : int,
              "empty" : boolean
            }...
          ]
      }
      ```
- 엮은 테이블을 푼다
  - 요청 : DELETE /api/table-groups/{tableGroupId}

### Product
- 상품을 생성한다
    - 요청 : POST /api/products
    - Header :
      - Location : /{productId}
    - requestBody :
      ```json
      {
        "name": string,
        "price": big decimal
      }
      ```
    - responseBody :
      ```json
      {
        "id" : long,
        "name": string,
        "price": big decimal
      }
      ```
- 전체 상품 목록을 조회한다
    - 요청 : GET /api/products
    - responseBody :
      ```json
      [
        {
          "id" : long,
          "name": string,
          "price": big decimal
        },
        {
          "id" : long,
          "name": string,
          "price": big decimal
        }...
      ]
      ```
### Order
- 주문을 생성한다
    - 요청 : POST /api/orders
    - requestBody :
      ```json
      {
        "orderTableId": long,
        "orderLineItems" :[
          {
            "seq" : long,
            "orderId" : long,
            "menuId" : long,
            "quantity" : long
          }
        ]
      }
      ```
    - responseBody :
      ```json
      {
        "id": long,
        "orderTableId": long,
        "orderStatus": ENUM: ("COOKING", "MEAL","COMPLETION"),
        "orderedTime": "2023-10-12T00:26:44.814989",
        "orderLineItems": [
          {
            "seq": long,
            "orderId": long,
            "menuId": long,
            "quantity": long
          }
        ]
      }
      ```
- 전체 주문 목록을 받는다
  - 요청 : GET /api/orders/
  - Header :
      - Location : /{orderId}
  - responseBody :
    ```json
    [
        {
          "id": long,
          "orderTableId": long,
          "orderStatus": ENUM: ("COOKING", "MEAL","COMPLETION"),
          "orderedTime": "2023-10-12T00:26:44.814989",
          "orderLineItems": [
            {
              "seq": long,
              "orderId": long,
              "menuId": long,
              "quantity": long
            }
          ]
        },
        {
          "id": long,
          "orderTableId": long,
          "orderStatus": ENUM: ("COOKING", "MEAL","COMPLETION"),
          "orderedTime": "2023-10-12T00:26:44.814989",
          "orderLineItems": [
            {
              "seq": long,
              "orderId": long,
              "menuId": long,
              "quantity": long
            }
          ]
        }...
    ]
    ```
- 주문을 변경한다
    - 요청 : PUT /api/orders/{orderId}/order-status
    - requestBody :
      ```json
      {
        "orderStatus": ENUM: ("COOKING", "MEAL","COMPLETION"),
      ```
    - responseBody :
      ```json
      {
        "id": long,
        "orderTableId": long,
        "orderStatus": ENUM: ("COOKING", "MEAL","COMPLETION"),
        "orderedTime": "2023-10-12T00:26:44.814989",
        "orderLineItems": [
          {
            "seq": long,
            "orderId": long,
            "menuId": long,
            "quantity": long
          }
        ]
      }
      ```
### Menu
- 단일 메뉴를 생성한다
    - 요청 : POST /api/menus
    - Header :
      - Location : /{menuId}
    - requestBody :
      ```json
      {
        "name": string,
        "price": big decimal,
        "menuGroupId": long,
        "menuProducts": [
          {
            "seq": long,
            "menuId": long,
            "productId": long,
            "quantity": long 
          }
        ]
      }
      ```
    - responseBody :
      ```json
      {
        "id" : long,
        "name": string,
        "price": big decimal,
        "menuGroupId": long,
        "menuProducts": [
          {
            "seq": long,
            "menuId": long,
            "productId": long,
            "quantity": long 
          }
        ]
      }
      ```
- 전체 메뉴 목록을 조회한다
    - 요청 : GET /api/products
    - responseBody :
      ```json
      [
        {
          "id" : long,
          "name": string,
          "price": big decimal,
            "menuGroupId": long,
          "menuProducts": [
            {
              "seq": long,
              "menuId": long,
              "productId": long,
              "quantity": long 
            }
          ]
        },
        {
          "id" : long,
          "name": string,
          "price": big decimal,
            "menuGroupId": long,
          "menuProducts": [
            {
              "seq": long,
              "menuId": long,
              "productId": long,
              "quantity": long 
            }
          ]
        }...
      ]
      ```

### MenuGroup
- 각 메뉴를 분류별로 묶은 메뉴 그룹을 생성할 수 있다
    - 요청 : POST /api/menu-groups
    - Header :
      - Location : /{menuGroups}
    - requestBody :
      ```json
      {
        "name": string
      }
      ```
    - responseBody :
      ```json
      {
        "id" : long,
        "name": string
      }
      ```
- 메뉴그룹 목록을 조회한다
    - 요청 : GET /api/menu-groups
    - responseBody :
      ```json
      [
        {
          "id" : long,
          "name": string
        },
        {
          "id" : long,
          "name": string
        }...
      ]
      ```

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
