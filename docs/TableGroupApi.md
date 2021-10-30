## TableGroup API

### 개별 테이블을 묶어서 관리할 수 있는 테이블 그룹과 관련된 기능

----

#### POST: /api/table-groups
- 통합 계산을 위해 개별 테이블을 그룹화하는 테이블 그룹을 생성한다. 

- **응답**
    - Status Code: 201
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

- **응답**
    ```json
    {
        "id": 1,
        "createdDate": "2021-10-30T22:29:59.029",
        "orderTables": [
            {
                "id": 1,
                "tableGroupId": 1,
                "numberOfGuests": 0,
                "empty": false
            },
            {
                "id": 2,
                "tableGroupId": 1,
                "numberOfGuests": 0,
                "empty": false
            }
        ]
    }
    ```

----

#### DELETE: /api/table-groups/{tableGroupId}
- 통합 계산을 위해 개별 테이블을 그룹화하는 tableGroupId에 해당하는 테이블 그룹을 삭제한다.

- **요청**
    ```
    Body 없음
    ```

- **응답**
    - Status Code: 204
    ```
    Body 없음
    ```