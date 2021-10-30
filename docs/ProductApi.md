## Product API

### 메뉴를 구성하는 개별 상품과 관련된 기능

----

#### GET: /api/products
- 등록된 전체 상품들을 반환한다
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
            "name": "후라이드",
            "price": 16000.00
        },
        {
            "id": 2,
            "name": "양념치킨",
            "price": 16000.00
        },
        {
            "id": 3,
            "name": "반반치킨",
            "price": 16000.00
        }
    ]
    ```

----

#### POST: /api/products
- 새로운 상품을 등록한다
- **요청**
    ```json
    {
        "name": "강정치킨",
        "price": 17000
    }
    ```

- **응답**
    - Status Code: 201
    ```json
    {
        "id": 7,
        "name": "강정치킨",
        "price": 17000
    }
    ```