# 키친포스

## 요구 사항

### 이미 구현되어 있는 코드 분석(도메인)
- Product
    - 상품 생성
        - 가격 예외 검증
            - 가격이 음수인 경우
            - 가격이 없는 경우(null)
    - 상품 조회
- Menu
    - 메뉴 생성
        - 가격 예외 검증
            - 가격이 음수인 경우
            - 가격이 없는 경우(null)
        - 메뉴 그룹이 있는지 검증 
        - 메뉴 총액 검증
            - sum product 보다 총액이 크면 안된다.
        - product가 없는 경우  
        - 메뉴와 상품을 함께 저장한다.
    - 메뉴 조회 
        - menu, product를 함께 조회해서 반환한다.
- MenuGroup
    - 생성
    - 조회
- Order
    - 생성
        - 주문한 menu가 없으면 예외를 반환
        - 주문한 menu가 데이터베이스와 저장되어 있지 않으면 예외
        - order table이 디비에 없으면 예외
        - order table이 비어있으면 예외
        - 주문과 메뉴를 함께 저장한다.
    - 조회
        - 주문과 메뉴를 함께 조회한다.
    - 주문상태 변경
        - 해당 order가 디비에 없으면 예외
        - 이미 완료된(complete) 주문은 변경X
        - OrderStatus를 변경하고, 메뉴와 오더를 함께 조회하여 반환한다.
- Table
    - 생성
        - 비어있는 테이블을 생성한다.
    - 조회
    - 비어있는 테이블 수정
        - 테이블이 디비에 없으면 예외를 반환한다.  
        - 테이블이 테이블 그룹에 속해있으면 예외를 반환
        - 조리중,식사중 상태인 테이블이면 예외
        - 비어있는 테이블에 대해 손님을 입장시키거나, 식사를 마친 손님을 보낼 수 있다.   
    - 손님 수 수정
        - 손님의 수가 음수면 예외 반환
        - order table이 없으면 예외
        - 테이블이 비어있으면 예외 
- Table Group
    - 테이블 그루핑
        - 테이블 수가 0이거나, 2보다 작으면 예외 - 2개 이상부터 그루핑 가능 
        - 테이블들이 DB에 없으면 예외
        - 손님이 있는 테이블이거나, 기존 테이블 그룹에 있는 경우 예외(한 그룹에만 소속)
        - order table에 table group, empty 세팅 후 저장 
        - 테이블 그룹 저장
    - 테이블 그룹 해제
        - 식사중이거나, 조리중인 경우 예외
        - 테이블 그룹에서 해제하고 저장          

### 미션 요구사항(모든 클래스 테스트 작성하기 - 1단계)
- Controller
    - [ ] Table
    - [ ] TableGroup
    - [ ] Menu
    - [ ] MenuGroup
    - [ ] Order
    - [ ] Product
- Service
    - [ ] Table
    - [ ] TableGroup
    - [ ] Menu
    - [ ] MenuGroup
    - [ ] Order
    - [ ] Product 
- Dao
    - [ ] MenuDao
    - [ ] MenuGroupDao
    - [ ] MenuProductDao
    - [ ] OrderDao
    - [ ] OrderLineItemDao
    - [ ] OrderTableDao
    - [ ] ProductDao
    - [ ] TableGroupDao
    
        
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
