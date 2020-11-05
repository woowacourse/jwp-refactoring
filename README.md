# 키친포스

## 요구 사항
### Command

#### 테이블
- 테이블을 생성한다.

- 테이블의 주문 존재 여부를 변경한다.
    - 테이블이 존재해야 한다.
    - 단체 지정은 존재하지 않아야 한다.
    - 테이블에 모든 주문은 완료 상태이어야 한다. (요리중, 식사중 금지)
    
- 테이블의 손님 수를 변경한다.
    - 손님 수를 음수로 변경할 수 없다.
    - 테이블이 존재해야 한다.
    - 빈 테이블은 손님 수를 변경할 수 없다.
    
#### 단체 지정
- 단체 테이블을 지정한다.
    - 요청엔 2개 이상의 테이블이 존재해야 한다.
    - 요청한 테이블들이 전부 하나씩 존재해야 한다.
    - 테이블에 주문이 없어야 한다.
    - 단체 지정이 되어있지 않아야 한다.

- 단체 테이블을 해제한다.
    - 테이블에 모든 주문은 완료 상태이어야 한다. (요리중, 식사중 금지)
    
#### 상품
- 상품을 생성한다. 
    - 가격이 존재해야 한다.
    - 가격은 음수가 될 수 없다.
    
#### 주문
- 주문을 생성한다. 
    - 주문 항목이 존재해야 한다.
    - 주문 항목은 하나의 메뉴를 가진다.
    - 테이블이 존재해야 한다.
    - 테이블은 빈 테이블 일 수 없다.

- 주문 상태를 변경한다. 
    - 주문이 존재해야 한다.
    - 주문의 상태가 완료일 수 없다.
    
#### 메뉴
- 메뉴를 생성한다. 
    - 메뉴의 가격이 존재해야 한다.
    - 가격은 음수일 수 없다.
    - 메뉴 그룹이 존재해야 한다.
    - 메뉴 상품이 존재해야 한다.
    - 상품이 존재해야 한다.
    - 메뉴의 가격은 상품의 가격의 총 합보다 작아야 한다.

#### 메뉴 그룹
- 메뉴 그룹을 생성한다.

### Query
- 테이블 전체 조회
- 상품 전체 조회
- 주문 전체 조회
- 메뉴 그룹 전체 조회
- 메뉴 전체 조회
    
## 테스트
- [x] 테이블
    - [x] 테이블 인수 테스트
    - [x] 테이블 서비스 테스트
- [x] 테이블 그룹
    - [x] 테이블 그룹 인수 테스트
    - [x] 테이블 그룹 서비스 테스트
- [x] 상품
    - [x] 상품 인수 테스트
    - [x] 상품 서비스 테스트
- [x] 주문
    - [x] 주문 인수 테스트
    - [x] 주문 서비스 테스트
- [x] 메뉴
    - [x] 메뉴 인수 테스트
    - [x] 메뉴 서비스 테스트
- [x] 메뉴 그룹
    - [x] 메뉴 그룹 인수 테스트
    - [x] 메뉴 그룹 서비스 테스트

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
