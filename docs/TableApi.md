## Table API

### 개별 테이블 정보와 관련된 기능

----

#### GET: /api/tables
- 매장에 있는 테이블들에 대한 정보를 반환한다. 

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
            "tableGroupId": null,
            "numberOfGuests": 0,
            "empty": false
        },
        {
            "id": 2,
            "tableGroupId": null,
            "numberOfGuests": 0,
            "empty": false
        },
        {
            "id": 3,
            "tableGroupId": null,
            "numberOfGuests": 0,
            "empty": true
        }
    ]
    ```

----

#### POST: /api/tables
- 매장에 있는 테이블 정보를 추가한다. 

- **요청**
    ```json
    {
      "numberOfGuests": 0,
      "empty": true
    }
    ```

- **응답**
    - Status Code: 201
    ```json
    {
        "id": 9,
        "tableGroupId": null,
        "numberOfGuests": 0,
        "empty": true
    }
    ```

----

#### PUT: /api/tables/{tableId}/empty
- 매장에 있는 테이블 중 tableId에 해당하는 테이블의 empty 여부를 변경한다. 

- **요청**
    ```json
    {
      "empty": true
    }
    ```

- **응답**
    - Status Code: 200
    ```json
    {
        "id": 9,
        "tableGroupId": null,
        "numberOfGuests": 0,
        "empty": true
    }
    ```

----

#### PUT: /api/tables/{tableId}/number-of-guests
- 매장에 있는 테이블 중 tableId에 해당하는 테이블의 number-of-guest를 변경한다.

- **요청**
    ```json
    {
      "numberOfGuests": 4
    }
    ```

- **응답**
    - Status Code: 200
    ```json
    {
        "id": 9,
        "tableGroupId": null,
        "numberOfGuests": 4,
        "empty": false
    }
    ```
