# 키친포스

## 요구 사항

### 코드 분석(도메인)
- Product
    - 생성
        - Product 예외 검증
            - Price가 음수인 경우
            - Price가 설정되지 않은 경우(null)
    - 조회

- Menu
    - 생성
        - Price 예외 검증
            - Price가 음수인 경우
            - Price가 없는 경우(null)
        - Menu Group에 포함되어 있는지 검증 
        - Menu Price 검증
            - Product Price의 합보다 Menu Price가 크면 안된다.
        - Product가 없는 경우  
        - Menu와 Product을 함께 저장한다.
    - 조회 
        - Menu와 Product를 함께 조회해서 반환한다.

- Menu Group
    - 생성
    - 조회

- Order
    - 생성
        - Order Menu 없으면 예외를 반환한다.
        - Order Menu 존재하지 않으면 예외를 반환한다.
        - Order Table이 존재하지 않으면 예외를 반환한다.
        - Order Table이 비어있으면 예외를 반환한다.
        - Order와 Menu를 함께 저장한다.
    - 조회
        - Order와 Menu를 함께 조회한다.
    - Status 변경
        - Status를 변경할 Order가 존재하지 않으면 예외를 반환한다.
        - 변경하려는 Order의 Status가 완료 상태(Complete Status)면 예외를 반환한다.
        - Order Status를 변경하고, Menu와 Order를 함께 조회하여 반환한다.
- Table
    - 생성
        - 비어있는 Table을 생성한다.
    - 조회
    - Table Empty 수정
        - Table이 존재하지 않으면 예외를 반환한다.  
        - Table이 속해있는 Table Group이 존재하면 예외를 반환한다.
        - 조리중(Cooking), 식사중(Meal) Status인 Table이면 예외를 반환한다.
        - Empty Table에 대해 손님을 입장시키거나, 식사를 마친(Complete Status) 손님을 보낼 수 있다.   
    - Number of Guest 수정
        - Number of Guest가 음수면 예외를 반환한다.
        - Order Table이 존재하지 않으면 예외를 반환한다.
        - Table이 비어있으면(Empty) 예외를 반환한다. 
- Table Group
    - Table Group 생성
        - Group을 지정할 Table 수가 0이거나, 2보다 작으면 예외를 반환한다. (두 개 이상의 Table만 Group 지정할 수 있다.) 
        - Group을 지정할 Table이 존재하지 않으면 예외를 반환한다.
        - 비어있지 않은(Not Empty) 테이블이거나, 다른 Table Group에 포함되어 있는 경우 예외를 반환한다.
        - Order Table에 Table Group과, Empty를 설정 후 저장한다.
        - Table Group을 저장한다. 
    - Table Group 해제
        - 식사중 상태(Meal Status)이거나, 조리중 상태(Cooking Status)인 경우 예외를 반환한다.
        - Table Group 해제 후 저장한다.          

### 미션 요구사항(모든 클래스 테스트 작성하기 - 1단계)
- Controller
    - [x] Table
    - [x] TableGroup
    - [x] Menu
    - [x] MenuGroup
    - [x] Order
    - [x] Product
- Service
    - [x] Table
    - [x] TableGroup
    - [x] Menu
    - [x] MenuGroup
    - [x] Order
    - [x] Product 
- Dao
    - [x] MenuDao
    - [x] MenuGroupDao
    - [x] MenuProductDao
    - [x] OrderDao
    - [x] OrderLineItemDao
    - [x] OrderTableDao
    - [x] ProductDao
    - [x] TableGroupDao
    
        
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
