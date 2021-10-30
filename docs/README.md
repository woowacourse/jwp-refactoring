## TODO

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

- [ ] controller - service 사이 dto 사용  
- [ ] 도메인에서 setter 제거
- [ ] 테스트 Fixture 사용하여 중복 줄이기
- [ ] DB 마이그레이션?
