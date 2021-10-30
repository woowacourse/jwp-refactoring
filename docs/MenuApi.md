## Menu API

### 실제 주문을 하는 단위인 메뉴와 관련된 기능

----

#### GET: /api/menus
- 등록된 전체 메뉴에 대한 정보를 반환한다
- **요청**
    ```
    Body 없음
    ```

- **응답**
    ```json
    [
      {
        "id": 1,
        "name": "후라이드치킨",
        "price": 16000.00,
        "menuProducts": [
          {
            "seq": 1,
            "product": {
              "id": 1,
              "name": "후라이드",
              "price": 16000.00
            },
            "quantity": 1,
            "productId": 1,
            "menuId": 1
          }
        ],
        "menuGroupId": 2
      },
      {
        "id": 2,
        "name": "양념치킨",
        "price": 16000.00,
        "menuProducts": [
          {
            "seq": 2,
            "product": {
              "id": 2,
              "name": "양념치킨",
              "price": 16000.00
            },
            "quantity": 1,
            "productId": 2,
            "menuId": 2
          }
        ],
        "menuGroupId": 2
      },
      {
        "id": 3,
        "name": "반반치킨",
        "price": 16000.00,
        "menuProducts": [
          {
            "seq": 3,
            "product": {
              "id": 3,
              "name": "반반치킨",
              "price": 16000.00
            },
            "quantity": 1,
            "productId": 3,
            "menuId": 3
          }
        ],
        "menuGroupId": 2
      }
    ]
    ```

----

#### POST: /api/menus
- 새로운 메뉴를 생성한다
- **요청**
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

- **응답**
    - Status Code: 201
    ```json
    {
        "id": 7,
        "name": "후라이드+후라이드",
        "price": 19000,
        "menuProducts": [
            {
                "seq": 7,
                "product": {
                    "id": 1,
                    "name": "후라이드",
                    "price": 16000.00
                },
                "quantity": 2,
                "productId": 1,
                "menuId": 7
            }
        ],
        "menuGroupId": 1
    }
    ```
