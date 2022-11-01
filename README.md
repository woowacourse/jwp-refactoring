# 키친포스

# Step1 키친 포스 도메인 이해하기

## 주요 도메인

> ### Product
상품은 `이름`과 `가격`을 가지고 있습니다.

- create (상품을 생성할 수 있습니다)
    - price 가 필수값입니다.
    - price > 0 조건을 만족해야합니다.
- findAll (상품 전체를 조회합니다.)

> ### Menu
메뉴는 `이름`, `가격`, `메뉴 그룹`, `메뉴 상품` 리스트를 가지고 있습니다. 약간 중국집에서 세트 1, 세트2 같이 여러 상품들을 조합해서 메뉴를 만드는 그런 느낌인 듯 합니다.

<img width="457" alt="스크린샷 2022-10-22 오후 4 10 38" src="https://user-images.githubusercontent.com/26570275/197325889-d0d5e8f0-18d4-473c-aa05-16cce2a1bb77.png">


---

- `create` (메뉴를 생성할 수 있습니다.)
    - price 가 필수값입니다.
    - price > 0 조건을 만족해야합니다.
    - 존재하는 menu group 이어야 합니다.
    - menu 가 가지고 있는 product 가 존재하는 상품이어야합니다.

- `findAll` (메뉴 전체를 조회합니다.)

> ### Menu Group
메뉴 그룹은 분류입니다. `이름` 을 가지고 있습니다.

- `create`(메뉴 그룹을 생성할 수 있습니다.)
- `findAll`(전체 메뉴 그룹을 조회할 수 있습니다.)

> ### MenuProduct
메뉴에 속하는 상품입니다. `메뉴 id`, `상품 id`, `개수`를 가지고 있습니다.


> ## Order
매장에서 발생하는 주문입니다.  `테이블 번호`, `주문 상태`, `주문 시간`, 주문에 속하는 수량이 있는 `메뉴`를 가지고 있습니다.

---

- `create`(주문을 생성할 수 있습니다.)
    - 주문 메뉴가 *null* 이면 예외를 반환합니다.
    - `없는 메뉴를 주문한 경우` 예외를 반환합니다.
    - 주문한 *테이블이 존재하지 않으면* 예외를 반환합니다.
- `findAll` (전체 주문을 조회할 수 있습니다.)
- `changeOrderStatus` (주문 상태를 변경할 수 있습니다.)
    - `없는 주문의 상태를 변경`하려고 하면 예외를 반환합니다.
    - `Completion 상태의 주문`인데, 변경을 시도할 경우 예외를 반환합니다.

> ## OrderLineItem
주문서 같은 느낌의 도메인 입니다. 주문 번호, 메뉴 번호, 수량을 가지고 있습니다.

> ## TableGroup
회식 때 처럼 여러 테이블을 한꺼번에 계산해야할 때, 해당 테이블은 한 그룹이다 명시하는 느낌의 도메인입니다.
`생성 시간`과 `테이블들` 을 가지고 있습니다.

- `create`(테이블 그룹을 생성합니다.)
    - 테이블이 널이면 예외를 반환합니다.
    - 테이블이 두개 이상이어야 합니다.
    - 테이블 List 와 DB 에서 찾아온 찾아온 테이블 객체의 수가 일치해야합니다.
- `ungroup`(테이블 그룹을 해지합니다.)
    - `주문 상태가 준비중`이나, `식사중`이면 `예외`를 반환합니다.

> ## OrderTable
매장에서 주문을 받는 내용에 대한 내용을 담은 도메인 입니다. `테이블 그룹`과, `손님 수`, `비어있는지` 확인합니다.

- `create`(오프라인 주문을 생성합니다.)
    - 테이블은 null 이 가능하다.
    - 테이블 초기화 시도시 존재하지않는 테이블이면 예외를 반환합니다.
    - 테이블 그룹이 *null* 이 아니면 예외를 반환합니다.
- `findAll`(전체 매장 주문에 대한 내용을 반환합니다.)
- `ChangeEmpty`(식사 종료 후 테이블을 정리합니다.)
    - 테이블에서 주문한 내역이 없으면 예외를 반환합니다.
    - `테이블 그룹이 없으면` 예외를 반환합니다.
    - 주문 상태가 `요리중이거나 식사중이 아니면` 예외를 반환합니다.
- `changeNumberOfGuests`(오프라인에서 식사하는 손님의 인원 수를 변경합니다.)
    - `인원 수가 0명 이상`이어야합니다.
    - 주문 내역이 없으면 예외를 반환합니다.

> ## 수달이 이해한 도메인 ✍🏻
키친 포스는 오프라인 주문을 받을 수 있는 서비스에요. 오프라인 기준으로는 매장에서 주문을 직접 받고 주문에 대한 상태관리를 해요. 상태에는 주문이 후 조리가 시작되고, 손님이 식사를하고, 계산하는 순서가 있겠죠?

매장에서는 특수하게 한 사람이 여러 테이블을 계산할 수도 있어요. 더즈가 팀장이 되면 금요일 주말에 스모디 사람들에게 한턱 쏠때 처럼요. ^^ 그 때 관리를 하기 위한 테이블 그룹이라는 도메인도 있다는 점이
신기하네요.

음식점에서 포스기 서비스와 같은 느낌의 도메인이라고 이해했어요. 재밌네요 😀

---

## 용어 사전

| 한글명 | 영문명 | 설명 |
| --- | --- | --- |
| 상품 | product | 메뉴를 관리하는 기준이 되는 데이터 |
| 메뉴 그룹 | menu group | 메뉴 묶음, 분류 |
| 메뉴 | menu | 메뉴 그룹에 속하는 실제 주문 가능 단위 |
| 메뉴 그룹 | menu product | 메뉴에 속하는 수량이 있는 상품 |
| 금액 | amount | 가격 * 수량 |
| 주문 테이블 | order table | 매장에서 주문이 발생하는 영역 |
| 빈 테이블 | empty table | 주문을 등록할 수 없는 주문 테이블 |
| 주문 | order | 매장에서 발생하는 주문 |
| 주문 상태 | order status | 주문은 조리 ➜ 식사 ➜ 계산 완료 순서로 진행된다. |
| 방문한 손님 수 | number of guests | 필수 사항은 아니며 주문은 0명으로 등록할 수 있다. |
| 단체 지정 | table group | 통합 계산을 위해 개별 주문 테이블을 그룹화하는 기능 |
| 주문 항목 | order line item | 주문에 속하는 수량이 있는 메뉴 |
| 매장 식사 | eat in | 포장하지 않고 매장에서 식사하는 것 |

# 2단계 어지럽다 어지러워~  💁🏻‍♀️

## 첫번째

서비스 계층을 살펴 봤을 때, 역할과 책임을 확인해본다. 어디가 잘못되어있는지 마킹해본다.

## 두번째

getter, setter 를 줄여본다. 현재 객체의 변경 포인트가 분산되어 있다. 분산 포인트를 한곳으로 변경한다.

### 2- 1 여기서 고민점

이과정에서 DAO 가 엔티티를 생성하는 과정에서 엄청난 리팩터링이 요구된다. 또한 객체간 연관관계를 수동으로 맺어주고 있어서 그 로직 때문에 setter 사용하는 서비스가 많이 있다. 이부분을 어떻게 해결할 것인가
?

- 답은 `ORM` ? 꼭 ORM 일까 ? 어떻게든 ORM 없이 불편함을 느끼는 과정을 겪어볼까 했는데 이미 겪고 있는 것 같다. lombok 에 빌더를 사용해서 setter 사용을 줄여볼까 했는데, 현재
  제약사항으로 롬북도 금지되어 있다. 제한된 시간 내에서는 기존에 알고 있는 과정을 조금 줄이고, 퍼포먼스를 내야한다는 트레이드 오프를 선택하자.

### JPA 로 변경하는 여정

- [x] build.gradle에 JPA dependency 추가하기
- [x] application.yml에 JPA 설정 추가하기
- [x] JPA Entity 생성
  - [ ] 영속성 전이 사용해서 연관관계 맺도록 변경
  - [ ] Jpa Repository 사용하도록 변경 
- [ ] 기존 DB 와 변경된 내역에 대한 마이그레이션 필요, flyway 고려해보기
- [ ] 기존 Repository JPA CRUD 로 마이그레이션


### controller 변경에 따른 검증 테스트 코드 보강
- [x] MenuGroupRestController
- [x] MenuRestController
- [x] OrderRestController
- [x] ProductRestController
- [x] TableGroupRestController
- [x] TableRestController
= 총 15개 API 에 대한 테스트 필요

### 인수테스트 테스트 코드 보강
- [ ] MenuGroupRestController
  - `post: create /api/menu-groups`
  - `get: list /api/menu-groups`
- [ ] MenuRestController
  - `post: create /api/menus`
  - `get: get /api/menus`
- [ ] OrderRestController
  - `post: create /api/orders`
  - `get: list /api/orders`
  - `put: changeOrderStatus /api/orders/{orderId}/order-status`
- [ ] ProductRestController
  - `post: create /api/products`
  - `get: list /api/products`
- [ ] TableGroupRestController
  - `post: create /api/table-groups`
  - `delete: ungroup /api/table-groups/{tableGroupId}`
- [ ] TableRestController
  - `post: create /api/tables` 
  - `get: list /api/tables`
  - `put: changeEmpty /api/tables/{orderTableId}/empty`
  - `put: changeNumberOfGuests /api/tables/{orderTableId}/number-of-guests`
    = 총 15개 API 에 대한 테스트 필요


  
