# 👩‍🍳 헌치의 키친포스

## 주요 요구 사항 🚀

- [x] kitchenpos 패키지의 코드를 보고 키친포스의 요구 사항을 README.md에 작성한다.
- [x] 정리한 키친포스의 요구 사항을 토대로 테스트 코드를 작성한다.
- [x] 정리한 키친포스의 요구 사항을 토대로 예외사항에 대한 테스트 코드를 작성한다.
- [x] 모든 Business Object에 대한 테스트 코드를 작성한다.
- [x] @SpringBootTest를 이용한 통합 테스트 코드 또는 @ExtendWith(MockitoExtension.class)를 이용한 단위 테스트 코드를 작성한다.
- [x] 이번 과정에서는 Lombok 없이 미션을 진행해 본다.

---

## 기능 요구 사항 ⚙️

### ✨ 메뉴 그룹

> 메뉴들의 대분류표, 태그라고 생각하면 된다.
>
> 예를 들어, "추천메뉴" 등이 있다.

```http request
POST {{host}}/api/menu-groups
Content-Type: application/json

{
  "name": "추천메뉴"
}
```

#### 💬 필요 기능

- [x] 메뉴 그룹을 추가한다
- [x] 메뉴 그룹 목록을 조회한다

### ✨ 메뉴

> 주문가능한 메뉴 단위이다.
>
> 예를 들어, "후라이드+후라이드" 등이 있다.

메뉴 그룹(태그), 메뉴에 포함된 상품이 함께 조회된다.

```http request
POST {{host}}/api/menus
Content-Type: application/json

{
  "name": "후라이드+후라이드",
  "price": 19000,
  "menuGroupId": 1,
  "menuProducts": [
    {
      "productId": 1,
      "quantity": 2
    }
  ]
}
```

#### 💬 필요 기능

- [x] 메뉴를 추가한다.
    - [x] [예외] 메뉴 그룹은 DB에 등록되어야 한다.
    - [x] [예외] 메뉴 속 상품들은 모두 DB에 등록되어야 한다.
    - [x] [예외] 메뉴 가격은 0원 이상이어야 한다.
    - [x] [예외] 메뉴 가격은 내부 모든 상품가격보다 낮아야 한다.
- [x] 메뉴 목록을 조회한다.

### ✨ 주문

> 말그대로 메뉴에 대한 주문이다.
>
> 주문하는 테이블, 메뉴, 개수가 포함된다.
>
> 주문 상태는 조리중->음식->완료 세가지가 있다.

```http request
POST {{host}}/api/orders
Content-Type: application/json

{
  "orderTableId": 1,
  "orderLineItems": [
    {
      "menuId": 1,
      "quantity": 1
    }
  ]
}
```

```http request
PUT {{host}}/api/orders/1/order-status
Content-Type: application/json

{
  "orderStatus": "MEAL"
}
```

#### 💬 필요 기능

- [x] 주문을 추가한다
    - [x] [예외] 하나 이상의 메뉴를 주문해야 한다.
    - [x] [예외] 주문한 메뉴들은 모두 DB에 등록되어야 한다.
    - [x] [예외] 주문 테이블은 DB에 등록되어야 한다.
    - [x] [예외] 주문 테이블은 손님이 존재해야 한다.
- [x] 주문 목록을 조회한다
- [x] 특정 주문의 주문 상태를 변경한다
    - [x] [예외] 주문이 완료되었을 시, 주문 상태를 변경할 수 없다.

### ✨ 메뉴 상품

> 메뉴에 포함되는 상품이다.
>
> 구체적으로는 메뉴에는 세트메뉴가 있고, 세트 안에는 여러 상품(음식)이 포함될 수 있다.

```http request
POST {{host}}/api/products
Content-Type: application/json

{
  "name": "강정치킨",
  "price": 17000
}
```

#### 💬 필요 기능

- [x] 특정 메뉴 상품을 추가한다
    - [x] [예외] 상품 가격은 0 이상이어야 한다.
    - [x] [예외] 상품 이름은 중복되지 않아야 한다.
- [x] 메뉴 상품 목록을 조회한다

### ✨ 단체 지정

> 이름이 헷갈릴 수 있는데, 계산을 통합해 진행하기 위해 주문한 테이블들을 묶어 그룹화한 것이다.

```http request
POST {{host}}/api/table-groups
Content-Type: application/json

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

#### 💬 필요 기능

- [x] 단체 지정을 추가한다
    - [x] [예외] 등록되는 테이블 수가 2개 이상이어야 한다.
    - [x] [예외] 등록되는 모든 테이블들은 비어있어야 한다.
    - [x] [예외] 등록되는 모든 테이블들은 기존 단체 지정이 없어야 한다.
- [x] 특정 단체 지정을 삭제한다
    - [x] [예외] 단체 지정 속 모든 테이블들의 주문이 있다면 완료 상태여야 한다.

### ✨ 테이블

> 테이블은 말그대로 주문 가능한 영역으로, 미리 세팅되어 있을 가능성이 높다.
>
> 즉, 주문마다 테이블이 만들어지지는 않는다.
>
> 테이블에는 앉을 수 있는 손님 수와, 빈 테이블인지 여부가 저장되어 있다.

```http request
POST {{host}}/api/tables
Content-Type: application/json

{
  "numberOfGuests": 0,
  "empty": true
}
```

#### 💬 필요 기능

- [x] 테이블을 추가한다
    - [x] [예외] 테이블 고객 수는 0명 이상이어야 한다.
- [x] 테이블 목록을 조회한다
- [x] 특정 테이블의 빈 테이블 여부를 변경한다
    - [x] [예외] 테이블은 단체지정이 없어야 한다.
    - [x] [예외] 테이블의 주문이 있다면 완료된 상태여야 한다.
- [x] 특정 테이블의 손님 수를 변경한다
    - [x] [예외] 테이블은 차있어야 한다.
    - [x] [예외] 테이블 고객 수는 0명 이상이어야 한다.

---

## 📁 용어 사전

| 한글명      | 영문명              | 설명                            |
|----------|------------------|-------------------------------|
| 상품       | product          | 메뉴를 관리하는 기준이 되는 데이터           |
| 메뉴 그룹    | menu group       | 메뉴 묶음, 분류                     |
| 메뉴       | menu             | 메뉴 그룹에 속하는 실제 주문 가능 단위        |
| 메뉴 상품    | menu product     | 메뉴에 속하는 수량이 있는 상품             |
| 금액       | amount           | 가격 * 수량                       |
| 주문 테이블   | order table      | 매장에서 주문이 발생하는 영역              |
| 빈 테이블    | empty table      | 주문을 등록할 수 없는 주문 테이블           |
| 주문       | order            | 매장에서 발생하는 주문                  |
| 주문 상태    | order status     | 주문은 조리 ➜ 식사 ➜ 계산 완료 순서로 진행된다. |
| 방문한 손님 수 | number of guests | 필수 사항은 아니며 주문은 0명으로 등록할 수 있다. |
| 단체 지정    | table group      | 통합 계산을 위해 개별 주문 테이블을 그룹화하는 기능 |
| 주문 항목    | order line item  | 주문에 속하는 수량이 있는 메뉴             |
| 매장 식사    | eat in           | 포장하지 않고 매장에서 식사하는 것           |

---

## 리팩토링 목록

### 비즈니스

- [x] 도메인 세터 삭제 및 생성자 수정
- [ ] 도메인과 Dto 분리

### 테스트

- [x] 테스트 픽스처 추가 분리
- [x] 테스트 픽스처 생성 방식 변경(Fixtures. 제거)
- [x] usingRecursiveComparison 등을 통한 필드값 검증
- [ ] DirtiesContext 제거 및 DatabaseCleaner 로 변경
- [ ] 일부 테스트코드 도메인 테스트로 이동
- [x] @SuppressWarnings("NonAsciiCharacters") 추가
