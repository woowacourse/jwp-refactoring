<p align="center">
    <img src="./docs/woowacourse.png" alt="우아한테크코스" width="250px">
</p>

# 레거시 코드 리팩터링 - Kitchen POS

---

![Generic badge](https://img.shields.io/badge/Level4-Kitchen_POS-green.svg)
![Generic badge](https://img.shields.io/badge/test-0_passed-blue.svg)
![Generic badge](https://img.shields.io/badge/version-1.0.0-brightgreen.svg)

> 우아한테크코스 웹 백엔드 4기, 레거시 코드 리팩터링 - Kitchen POS 저장소입니다.

<br><br>

## 요구 사항

- [x] kitchenpos 패키지의 코드를 보고 키친포스의 요구 사항을 README.md에 작성한다.
    - 미션을 진행함에 있어 [이 문서](https://dooray.com/htmls/guides/markdown_ko_KR.html)를 적극 활용한다.
- [ ] 정리한 키친포스의 요구 사항을 토대로 테스트 코드를 작성한다.
    - [ ] 모든 Business Object에 대한 테스트 코드를 작성한다.
    - [ ] @SpringBootTest를 이용한 통합 테스트 코드 또는 @ExtendWith(MockitoExtension.class)를 이용한 단위 테스트 코드를 작성한다.
    - [ ] Controller에 대한 테스트 코드 작성은 권장하지만 필수는 아니다.
    - 미션을 진행함에 있어 아래 문서를 적극 활용한다.
        - [Testing in Spring Boot - Baeldung](https://www.baeldung.com/spring-boot-testing)
        - [Exploring the Spring Boot TestRestTemplate](https://www.baeldung.com/spring-boot-testresttemplate)

<br><br>

## 요구사항 정리

---

- [API 분석](./docs/221018%20레거시%20코드%20리팩터링.pdf)
- [ERD 분석](./docs/221018-erd.png)
- [엔드포인트별 비즈니스 로직 요구사항 분석](./docs/221018%20요구사항%20분석.md)

<br>

## 요구사항 요약

### Product

- 프로덕트 생성과 조회가 가능하다.
- 프로덕트 생성 시 name과 price를 받는다.
    - 유효성 검사
        - [x] name은 null이 아니어야 하고, 유효한 문자가 1개 이상 존재해야 한다.
        - [x] price는 null이거나 0 미만일 수 없다.

### Table

- 테이블 생성과 조회, 주문 가능 여부 수정, 고객 인원 수정이 가능하다.
- 테이블 생성 시 numberOfGuests, empty를 받는다.
    - empty 는 주문 가능 여부를 나타낸다. 기본값은 false, 주문 불가 이다.
    - 유효성 검사
        - [x] numberOfGuests는 0 이상이어야 한다. 기본값은 0이다.
- 테이블 주문 가능 여부 수정 시 empty를 받는다
    - 유효성 검사
        - [x] PathVariable로 전달받은 tableId로 테이블을 조회한다. 없으면 예외이다.
        - [x] 조회된 테이블에 그룹 아이디가 존재하지 않아야 한다. 즉, 단체로 묶여있는 테이블은 주문 가능 여부 수정이 불가하다.
        - [x] 조회된 테이블에 조리시작(COOKING) 또는 식사중(MEAL) 상태인 주문이 없어야 한다. 즉, 계산 완료(COMPLETION) 상태여야 수정할 수 있다.
- 테이블 고객 인원 수정 시 numberOfGuests 를 받는다
    - 유효성 검사
        - [x] numberOfGuests 는 0 이상이어야 한다.
        - [x] PathVariable로 전달된 tableId로 테이블을 조회한다. 없으면 예외이다.
        - [x] 조회된 테이블이 주문 불가 상태일 경우(empty=true) 예외이다. 주문 불가 테이블에 대한 인원 수정 요청은 잘못된 요청이다.

### Menu

- 메뉴 생성, 조회가 가능하다.
- 메뉴 생성 시 name, price, menuGroupId, menuProducts를 받는다. menuProducts는 productId와 quantity를 담은 배열이다.
    - 유효성 검사
        - [x] price는 null이거나 음수일 수 없다
        - [x] menuGroupId는 실재하는 유효한 아이디여야 한다. 즉, 메뉴 생성 시점부터 메뉴는 특정 메뉴 그룹에 속해야 한다.
        - [x] menuProducts는 null이거나 빈 배열이 아니어야 한다.
        - [x] price는 menuProducts의 총합계액 보다 클 수 없다. 즉, 낱개의 합 보다 더 비싸게 가격을 책정할 수 없다. 0 <= price <= menuProducts 총 합계액

### Table Groups

- 테이블 그룹은 테이블을 묶어서 단체 손님으로 설정하는 것을 의미한다.
- 테이블 그룹의 생성과 삭제가 가능하다.
- 테이블 그룹 생성 시 orderTables 내에 배열로 id를 받는다. id는 테이블 id이다.
    - 유효성 검사
        - [x] 배열로 전달된 모든 orderTables 배열이 null이거나 2보다 작을 경우 예외이다
        - [x] 배열로 전달된 테이블 아이디로 조회했을 때 모든 아이디가 실재하는 유효한 값이어야 한다.
        - [x] 모든 테이블이 다른 그룹에 이미 할당되어 있지 않아야 한다. tableGroupId가 null이어야 한다.
        - [x] 모든 테이블이 주문 불가 상태(empty=true) 상태여야 한다.
    - 테이블 그룹으로 묶여질 때 모든 테이블의 주문 가능 여부, empty가 true로 변경된다.
- 테이블 그룹 삭제 시 PathVariable로 테이블 그룹 아이디를 받는다.
    - 유효성 검사
        - [x] 테이블 그룹 아이디로 조회된 모든 테이블에 조리 시작(COOKING) 또는 식사중(MEAL) 상태인 주문이 존재하지 않아야 한다
    - 테이블 그룹 삭제 시 모든 테이블의 tableGroupId를 null로 수정한다

### Menu Groups

- 메뉴 그룹은 개별 메뉴가 소속되는 곳이다. 가령 `한 마리 메뉴` 라는 메뉴 그룹에 `후라이드 한 마리`가 속하는 식이다.
- 메뉴 그룹 생성, 조회가 가능하다
- 메뉴 그룹 생성 시 name을 받는다.
    - 유효성 검사
        - [x] name은 null이 아니어야 하고, 공백이 아닌 문자가 1개 이상 있어야 한다.

### Order

- 주문 생성, 전체 조회, 주문 상태 수정이 가능하다
- 주문 생성 시 orderTableId, orderLineItems 배열을 받고, orderLineItems의 요소는 menuId, quantity로 구성된다.
    - 유효성 검사
        - [x] orderLineItems 배열이 null이거나 비어있으면 안된다.
        - [x] orderLineItems 배열 내 모든 menuId가 실재하는 유효한 메뉴 아이디여야 한다.
        - [x] orderTableId로 조회한 테이블의 empty가 false, 즉 주문 가능한 상태여야 한다.
- 주문 상태 수정 시 PathVariable로 orderId를 받고, RequestBody로 orderStatus를 받는다
    - 유효성 검사
        - [x] PathVariable로 전달된 orderId로 주문을 조회했을 때 존재하지 않으면 예외이다.
        - [x] 주문의 상태가 이미 `계산 완료(COMPLETION)` 상태일 경우 상태를 변경할 수 없다.

<br><br>

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

<br><br>
