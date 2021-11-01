## MenuGroup API

### 메뉴들을 카테고리(동일한 유형)별로 묶는 메뉴 그룹과 관련된 기능

----

#### GET: /api/menu-groups
- 등록된 전체 메뉴 그룹 카테코리를 반환한다
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
            "name": "두마리메뉴"
        },
        {
            "id": 2,
            "name": "한마리메뉴"
        },
        {
            "id": 3,
            "name": "순살파닭두마리메뉴"
        },
        {
            "id": 4,
            "name": "신메뉴"
        },
        {
            "id": 5,
            "name": "추천메뉴"
        }
    ]
    ```

----

#### POST: /api/menu-groups
- 메뉴 그룹 카테고리를 생성한다
- **요청**
    ```json
    {
      "name": "추천메뉴"
    }
    ```

- **응답**
    - Status Code: 201
    ```json
    {
        "id": 5,
        "name": "추천메뉴"
    }
