## Order API

### 매장에서 발생하는 주문과 관련된 기능

----

#### GET: /api/orders
- 매장에서 발생한 주문들에 대한 정보를 반환한다
- **요청**
    ```
    Body 없음
    ```

- **응답**
    - Status Code: 200
    ```json
    [
        {
            "id": 1,
            "orderTableId": 1,
            "orderStatus": "MEAL",
            "orderedTime": "2021-10-30T22:30:43.472",
            "orderLineItems": [
                {
                    "seq": 1,
                    "orderId": 1,
                    "menuId": 1,
                    "quantity": 1
                }
            ]
        }
    ]
    ```

----

#### POST: /api/orders
- 매장에서 발생한 주문 정보를 생성한다
- **요청**
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

- **응답**
    - Status Code: 201
    ```json
    {
        "id": 1,
        "orderTableId": 1,
        "orderStatus": "COOKING",
        "orderedTime": "2021-10-30T22:30:43.472",
        "orderLineItems": [
            {
                "seq": 1,
                "orderId": 1,
                "menuId": 1,
                "quantity": 1
            }
        ]
    }
    ```

---- 

#### PUT: /api/orders/{orderId}/order-status
- 매장에서 발생한 orderId에 해당하는 주문 정보를 수정한다
- **요청**
    ```json
    {
      "orderStatus": "MEAL"
    }
    ```

- **응답**
    - Status Code: 200
    ```json
    {
        "id": 1,
        "orderTableId": 1,
        "orderStatus": "MEAL",
        "orderedTime": "2021-10-30T22:30:43.472",
        "orderLineItems": [
            {
                "seq": 1,
                "orderId": 1,
                "menuId": 1,
                "quantity": 1
            }
        ]
    }
    ```
