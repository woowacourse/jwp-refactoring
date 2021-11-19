# 키친포스

## 요구 사항

### 메뉴 그룹 
#### [생성] <br>
사용자는 새로운 메뉴 그룹을 생성할 수 있다. 
- 입력값 : 메뉴 그룹 이름

#### [조회] <br>
사용자는 메뉴 그룹 전체 리스트를 조회할 수 있다.
- 조회값 : 메뉴 그룹 리스트 
- 상세값 : 메뉴 그룹 ID, 메뉴 그룹 이름

### 메뉴
#### [생성] <br>
사용자는 새로운 메뉴를 등록할 수 있다. 
- 입력값 : 메뉴 이름, 가격, 메뉴 그룹 ID, 메뉴 상품 리스트
- 예외 
  - 메뉴 가격이 개별 상품 가격의 합보다 비싸면 예외가 발생한다. 
  - 존재하지 않은 메뉴 그룹 ID면 예외가 발생한다.

#### [조회] <br>
사용자는 전체 메뉴 리스트를 조회할 수 있다. 
- 조회값: 메뉴 리스트
- 상세값 : 메뉴 ID, 이름, 가격, 메뉴 그룹 ID, 메뉴 상품 리스트 

### 주문
#### [생성] <br>
사용자는 새로운 주문을 넣을 수 있다. 
- 입력값 : 주문 테이블 ID, 주문 항목 및 수량
- 상세 요구사항
    - 주문에 주문 테이블 ID를 지정한다. 
    - 주문에 현재 주문 상태를 지정한다. 
    - 주문에 주문 시각을 현재로 지정한다.
    - 주문을 저장한다.
- 예외  
    - 만일 주문 항목이 비어있다면 예외를 던진다. 
    - 주문 항목의 메뉴가 데이터베이스에 없다면 예외를 던진다.
    - 주문 테이블이 데이터베이스에 없다면 예외를 던진다.
    - 주문 테이블이 비어있는 테이블이라면 예외를 던진다.

#### [조회] 
사용자는 전체 주문 리스트를 조회할 수 있다. 
- 조회값 : 주문 내용
- 상세값 : 주문 ID, 주문 테이블 ID, 주문 상태, 주문 시각, 주문 항목 리스트

#### [수정]
사용자는 주문의 주문 상태를 변경할 수 있다. 
- 입력값: 주문 ID, 주문 상태
- 예외
    - 만일 주문 ID가 유효하지 않다면 예외가 발생한다. 
    - 만일 입력 주문 상태가 completion 이라면 예외가 발생한다. 

### 상품 
#### [생성]
사용자는 새로운 상품을 추가할 수 있다. 
- 입력값: 상품 이름, 가격
- 예외 
    - 만일 가격이 null 이거나 0 보다 작으면 예외가 발생한다.  

#### [조회]
사용자는 전체 상품 리스트를 조회할 수 있다. 
- 조회값: 상품 이름, 상품 가격 리스트 

### 테이블 그룹
#### [생성]
사용자는 테이블 그룹을 생성하여 단체를 지정할 수 있다. 
- 입력값: 테이블 생성 시각, 주문 테이블 리스트
- 예외 
    - 만일 주문 테이블이 비었거나 1 이하라면 예외가 발생한다.
    - 만일 주문 테이블 중 존재하지 않는 테이블이 있다면 예외가 발생한다. 
    - 만일 주문 테이블이 빈 테이블이 아니라면 예외가 발생한다. 
    - 만일 주문 테이블이 이미 단체 테이블로 지정이 되어 있다면 예외가 발생한다. 
    
#### [삭제]
사용자는 테이블 그룹을 해제할 수 있다. 
- 입력값 : 테이블 그룹 ID
- 예외 
    - 만일 테이블 그룹에 속한 주문 테이블 중 요리중이거나 식사 중인 테이블이 있다면 예외가 발생한다. 
    
### 테이블
#### [생성]
사용자는 새로운 테이블을 생성할 수 있다. 
- 입력값 : 손님 수, 비어있는 여부
- 상세 요구사항
    - 처음 생성된 테이블의 테이블 그룹 ID는 Null 이다.
    
#### [조회]
사용자는 전체 테이블 리스트를 조회할 수 있다. 
- 조회값 : 테이블 ID, 테이블 그룹 ID, 손님 수, 비어있는 여부 

#### [수정]
사용자는 테이블이 비어있는 여부를 수정할 수 있다. 
- 입력값 : 비어있는 여부 (true/false)
- 예외 
    - 만일 테이블 그룹으로 지정된 테이블 이라면 예외가 발생한다. 
    - 만일 주문 상태가 요리중 혹은 식사중 이라면 에외가 발생한다.
    
#### [수정]
사용자는 테이블의 손님 수를 수정할 수 있다. 
- 입력값 : 손님 수 
- 예외 
    - 만일 손님의 수가 음수라면 예외가 발생한다.
    - 만일 주문 테이블의 ID가 유효하지 않다면 에외가 발생한다. 
    - 만일 주문 테이블이 비어있는 테이블이라면 예외가 발생한다.

#### [기타]
- 테이블 변경 순서는 다음과 같다.
  - 테이블이 채워질 때 
    - 테이블 empty true -> false 로 수정
    - 먼저 테이블 인원 수 0 -> 양수로 수정 
    -  Order 주문 생성 
  - 테이블이 비워질 때 
    - 주문 상태 COMPLETION으로 변경
    - 테이블 인원 수 양수 -> 0 으로 수정 
    - 테이블 empty false -> true 로 수정

## 도메인 리팩터링
- [ ] setter 삭제
- [ ] 필요 생성자 만들기
- [ ] 필요시 equals & hashcode 오버라이드 하기 

## 서비스 리팩터링
### DTO로 LayerArchitecture 지키기

- Request DTO 생성
  - [x] MenuRequestDto
  - [x] MenuGroupRequestDto
  - [x] OrderRequestDto
  - [x] OrderStatusRequestDto
    - [x] Order 
  - [x] ProductRequestDto
  - [x] TableGroupRequestDto
  - [x] OrderTableRequestDto
  - [ ] MenuProductDto
  - [x] TableEmptyRequestDto
    - [x] OrderTable 
  - [x] TableGuestRequestDto
    - [x] OrderTable 

- Response DTO 생성
- [x] MenuResponseDto
- [x] MenuGroupResponseDto
- [x] List<MenuGroup>
- [x] List<Menu>
- [x] OrderResponseDto
- [x] List<Order>
- [x] ProductResponseDto
- [x] List<Product>
- [x] TableGroupResponseDto
- [x] OrderTableResponseDto
- [x] List<OrderTable>

<br>

### 비지니스 로직 Domain 책임으로 옮기기
#### Menu
**도메인 담당**
- 메뉴 생성 로직
  - Price 유효성 검사
    - [ ] null 이거나 0 보다 작으면 예외
  - [ ] MenuProducts 가격 합보다 Price가 크면 예외


**서비스 흐름**
- 메뉴 생성 로직
  - [ ] MenuGroup 유효성
  - [ ] Menu에 속한 MenuProducts 구하기
  - [ ] 새로운 Menu 저장
  - [ ] 소속 MenuProducts 저장
    - [ ] MenuProducts의 MenuId 지정

#### Order
**도메인 담당**
- 주문 생성 로직
  - [ ] OrderLineItems가 Empty 라면 예외
  - [ ] 주문 첫 생성시 초기값 세팅
    - [ ] 주문 상태: COOKING
    - [ ] 주문 시각: now()

- OrderStatus 변경 로직
  - [ ] Order의 status가 COMPLETION 이라면 예외

**서비스 흐름**
- 주문 생성 로직
  - [ ] OrderLineItem의 MenuId가 존재하지 않는 Id라면 예외
  - [ ] 주문한 orderTable이 없다면 예외
  - [ ] 주문한 orderTable이 비었다면 예외
  - [ ] Order 및 OrderLineItems 저장
    - [ ] OrderLineItems의 orderId를 세팅한 후 저장

- [ ] OrderStatus 변경 로직
  - [ ] 저장된 Order 불러오기
  - [ ] 도메인에서 유효성 검사 후 새로운 OrderStatus 지정
  - [ ] 변경내용 저장

#### Product
**도메인 담당**
- 프로덕트 생성 로직
  - [ ] 가격이 Null, 혹은 0 보다 작다면 예외

#### TableGroup
**도메인 담당**
- 테이블 그룹 생성 로직
  - [ ] 소속 테이블이 비었거나 개수가 2보다 작다면 예외
- [ ] 소속 테이블 중 비어있지 않거나 TableGroup이 이미 지정된 곳이 있다면 예외

- 테이블 그룹 해제 로직
  - [ ] 소속 테이블 중 COOKING, MEAL인 테이블이 있다면 예외

**서비스 담당**
- 테이블 그룹 생성 로직
  - [ ] 소속 테이블 중 존재하지 않는 테이블이 있다면 예외
- 초기값 세팅 (메서드 호출하고 도메인에서 한번에 처리하도록 구현)
  - [ ] 소속 테이블의 tableGroupId, empty 여부 세팅 및 저장
  - [ ] 생성 시간 now()로 세팅

- 테이블 그룹 해제 로직
  - [ ] 그룹 해제 설정
    - [ ] 테이블 GroupId null
    - [ ] empty 여부 false로 변경

#### Table
**도메인 담당**
- 테이블 Empty 여부 변경 로직
  - [ ] TableGroupId가 null이 아니라면 예외

- 테이블의 손님 수 변경 로직
  - [ ] 변경하려는 손님수가 0 이하라면 예외
  - [ ] 변경하려는 테이블이 빈 테이블이라면 예외


**서비스 담당**
- 테이블 Empty 여부 변경 로직
  - [ ] 수정하려는 OrderTable이 없다면 예외
  - [ ] 속한 OrderTable의 Order가 COOKING이나 MEAL이라면 예외
  - [ ] 테이블의 empty를 지정하고 저장

- 테이블의 손님 수 변경 로직
  - [ ] 수정하려는 OrderTable이 없다면 예외
  - [ ] 테이블의 손님 수를 지정하고 저장

### JPA로 리팩토링
- [ ] 기존 JdbcTemplate을 JPA로 변경

### 테스트코드 수정
- [ ] 기존에 DTO를 사용하지 않던 테스트코드 인자를 수정
- [ ] JPA Repository 테스트코드 추가
- [ ] 단위테스트 추가

### 비지니스 로직 Domain 책임으로 옮기기
#### Menu
**도메인 담당**
- 메뉴 생성 로직
  - Price 유효성 검사
    - [ ] null 이거나 0 보다 작으면 예외
  - [ ] MenuProducts 가격 합보다 Price가 크면 예외


**서비스 흐름**
- 메뉴 생성 로직
  - [ ] MenuGroup 유효성
  - [ ] Menu에 속한 MenuProducts 구하기
  - [ ] 새로운 Menu 저장
  - [ ] 소속 MenuProducts 저장
    - [ ] MenuProducts의 MenuId 지정

#### Order
**도메인 담당**
- 주문 생성 로직
  - [ ] OrderLineItems가 Empty 라면 예외
  - [ ] 주문 첫 생성시 초기값 세팅
    - [ ] 주문 상태: COOKING
    - [ ] 주문 시각: now()

- OrderStatus 변경 로직
  - [ ] Order의 status가 COMPLETION 이라면 예외

**서비스 흐름**
- 주문 생성 로직
  - [ ] OrderLineItem의 MenuId가 존재하지 않는 Id라면 예외
  - [ ] 주문한 orderTable이 없다면 예외
  - [ ] 주문한 orderTable이 비었다면 예외
  - [ ] Order 및 OrderLineItems 저장
    - [ ] OrderLineItems의 orderId를 세팅한 후 저장

- [ ] OrderStatus 변경 로직
  - [ ] 저장된 Order 불러오기
  - [ ] 도메인에서 유효성 검사 후 새로운 OrderStatus 지정
  - [ ] 변경내용 저장

#### Product
**도메인 담당**
- 프로덕트 생성 로직
  - [ ] 가격이 Null, 혹은 0 보다 작다면 예외

#### TableGroup
**도메인 담당**
- 테이블 그룹 생성 로직
  - [ ] 소속 테이블이 비었거나 개수가 2보다 작다면 예외
- [ ] 소속 테이블 중 비어있지 않거나 TableGroup이 이미 지정된 곳이 있다면 예외

- 테이블 그룹 해제 로직
  - [ ] 소속 테이블 중 COOKING, MEAL인 테이블이 있다면 예외

**서비스 담당**
- 테이블 그룹 생성 로직
  - [ ] 소속 테이블 중 존재하지 않는 테이블이 있다면 예외
- 초기값 세팅 (메서드 호출하고 도메인에서 한번에 처리하도록 구현)
  - [ ] 소속 테이블의 tableGroupId, empty 여부 세팅 및 저장
  - [ ] 생성 시간 now()로 세팅

- 테이블 그룹 해제 로직
  - [ ] 그룹 해제 설정
    - [ ] 테이블 GroupId null
    - [ ] empty 여부 false로 변경

#### Table
**도메인 담당**
- 테이블 Empty 여부 변경 로직
  - [ ] TableGroupId가 null이 아니라면 예외

- 테이블의 손님 수 변경 로직
  - [ ] 변경하려는 손님수가 0 이하라면 예외
  - [ ] 변경하려는 테이블이 빈 테이블이라면 예외


**서비스 담당**
- 테이블 Empty 여부 변경 로직
  - [ ] 수정하려는 OrderTable이 없다면 예외
  - [ ] 속한 OrderTable의 Order가 COOKING이나 MEAL이라면 예외
  - [ ] 테이블의 empty를 지정하고 저장

- 테이블의 손님 수 변경 로직
  - [ ] 수정하려는 OrderTable이 없다면 예외
  - [ ] 테이블의 손님 수를 지정하고 저장

### JPA로 리팩토링
- [ ] 기존 JdbcTemplate을 JPA로 변경

### 테스트코드 수정
- [ ] 기존에 DTO를 사용하지 않던 테스트코드 인자를 수정
- [ ] JPA Repository 테스트코드 추가
- [ ] 단위테스트 추가

## 생각할 거리 
- DTO를 어느 layer까지 사용해야 할까 ?
  - 완벽한 단방향 layer를 하기 위해서는 application과 presentation 에서 각기 다른 Dto를 사용하는 것이 맞다.
  - 하지만 지나치게 많은 DTO파일을 생성해야하며 DTO 포장하는 등의 작업이 매우 번거로우며 비지니스 코드보다 오래걸릴 수 있다. 
  - DTO를 하나만 쓴다면 다음 두 가지 경우 중 무엇이 적합할까? 
    1. controller에서 DTO <-> Domain 변환
      - 도메인이 presentation layer에 노출이 되지만 단방향을 유지할 수 있다.
      - presentation layer에서 도메인이 노출될 수 있다. 
    2. service에서 DTO <-> Domain 변환
      - presentation layer에 속하는 dto가 application layer에 역방향으로 참조된다. 
      - 자주 바뀌는 presentation의 변화에 application layer가 영향을 받을 수 있다. 
  - 결론: 현재 테스트코드의 서비스가 모두 도메인을 인자로 받고 있는 점, 모듈이 분리될 수 있으므로 단방향을 유지하고자 하는 점에 의해 1번을 선택하도록 한다. 
  
<br>

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
