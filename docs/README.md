## TODO

### Step3
#### 좋은 의존성을 위한 기본 가이드
- 양방향 의존성 피하기
- 다중성이 적은 방향 선택하기
- 필요없는 의존성은 두지 않기
- 패키지 사이의 의존성 사이클 제거하기
    - 중간 객체 만들기
    - 인터페이스나 추상클래스를 사용하여 의존성 역전
    - 새로운 패키지로 분리

#### 객체 참조(강한 결합)으로 인해 발생하는 문제 해결 방법
- Repository를 통한 탐색으로 변경(약한 결합도)
    - 함께 생성되고, 함께 삭제되는 객체들이 아니면 객체가 아닌 id값으로 의존성을 맺는다.
    - 도메인 제약사항을 공유하는 객체들끼리는 객체 참조를 사용한다.

- 약한 결합으로 변경시 리팩터링 가이드
    - 객체를 직접 참조하는 로직을 다른 객체로 옮기기(절차지향적으로)
    - 도메인 이벤트 퍼블리싱 적용

#### TODO
- [x] Package by Feature로 구조 변경
- [x] 의존성 그려보기
- [x] 의존성 관점에서 리팩터링
    - [x] 메뉴(Menu)의 이름, 가격 변경시 주문 항목(OrderLineItem)에 영향을 미치지 않게
        - [x] 메뉴 수정 api 추가
    - [x] 클래스 사이 의존 관계 단방향으로 변경(pr에 이유 작성)
        ~~- Menu <-> MenuProduct 단방향으로 변경~~
        ~~- Order <-> OrderLineItem 단방향으로 변경~~
        ~~- OrderTable <-> TableGroup 단방향으로 변경~~
    - [x] 패키지 사이 의존 관계 단방향으로 변경
        - [x] Order <-> OrderTable 단방향으로 변경
    - [x] 패키지 간 연관 관계 Repository를 통한 탐색(약한 결합도)으로 변경
        - [x] Menu -> MenuGroup 객체 참조를 id로 변경
        - [x] MenuProduct -> Product 객체 참조를 id로 변경
        - [x] OrderLineItem -> Menu 객체 참조를 id로 변경
        - [x] Order -> OrderTable 객체 참조를 id로 변경

### Step2
- [x] ddl-auto 값 validate 로 주기
  
- [x] JdbcTemplate -> Jpa
    - [x] MenuProduct
    - [x] Product
    - [x] MenuGroup
    - [x] Menu
    - [x] OrderLineItem
    - [x] OrderTable
    - [x] Order
    - [x] TableGroup
  
- [x] 도메인 필드 DB의존적이지 않게 변경(id값이 아닌 객체를 가지도록)
    - [x] MenuProduct
        - [x] Menu
        - [x] Product
    - [x] Menu
        - [x] MenuGroup
        - [x] MenuProduct
    - [x] Order
        - [x] OrderTable 
        - [x] OrderLineItem
        - [x] OrderStatus
    - [x] OrderLineItem
        - [x] Order 
        - [x] Menu
    - [x] OrderTable
        - [x] TableGroup
    - [x] TableGroup
        - [x] OrderTable
    
- [x] 비즈니스 로직 도메인으로 옮기기
  * TDD로 진행
  - [x] Product
  - [x] Menu
  - [x] OrderTable
  - [x] Order
  - [x] TableGroup

- [x] controller - service 사이 dto 사용
  
- [x] 도메인에서 setter 제거
  
- [x] 테스트 Fixture 사용하여 중복 줄이기
