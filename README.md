# 키친포스

## 요구 사항
<details>
<summary>도메인 구조</summary>
<div markdown="1">

<img src="https://user-images.githubusercontent.com/28749734/197745454-e7874650-ccc2-484c-9ae4-d9ce255751a7.png" width="70%">

</div>
</details>

### Product
- 상품 생성 기능
    - 상품명과 가격을 입력받아 새로운 상품을 생성한다.
    - 가격은 `null`이거나 0 미만일 수 없다.
- 상품 목록 조회 기능
    - 모든 상품에 대한 id, 상품명, 가격을 조회한다.

### Menu Group
- 메뉴 그룹 생성 기능
  - 이름을 입력받아 새로운 메뉴 그룹을 생성한다.
- 메뉴 그룹 목록 조회 기능
  - 모든 메뉴 그룹에 대한 id와 메뉴 그룹명을 조회한다.

### Menu
- 메뉴 생성 기능
  - 메뉴명, 가격, MenuGroup id, MenuProduct 목록을 입력받아 새로운 메뉴를 생성한다. 
  - 가격은 `null`이거나 0 미만일 수 없다.
  - 가격은 MenuProduct들의 price * quantity 총합 이하여야한다.
  - MenuGroup의 id는 유효해야한다.
  - MenuProduct 목록의 각 productId는 유효해야한다.
- 메뉴 목록 조회 기능
  - 모든 메뉴에 대한 id, 메뉴명, 메뉴 가격, 메뉴에 해당하는 그룹 id, 메뉴에 속하는 수량이 있는 상품 목록을 조회한다. 

### Order Table
- 주문 테이블 생성 기능
  - 방문한 손님 수, 빈 테이블 여부, 단체 지정 id를 입력받아 새로운 주문 테이블을 생성한다.
  - 단체로 지정하지 않은 주문 테이블의 경우 단체 지정 id를 생략 가능하다.
  - 손님 수를 입력하지 않는 경우 기본 값인 0으로 생성된다.
  - 빈 테이블 여부를 입력하지 않는 경우 기본 값인 `false`로 생성된다.
- 주문 테이블 목록 조회 기능
  - 모든 주문 테이블에 대한 id, 단체 지정 id, 방문한 손님 수, 빈 테이블 여부를 조회한다.
  - 단체로 지정하지 않은 주문 테이블의 경우 단체 지정 id는 `null`이다.
- 주문 테이블의 빈 테이블 여부 수정 기능
  - id에 해당하는 주문 테이블은 유효해야한다.
  - 주문 테이블이 그룹화된 경우 수정할 수 없다.
  - 주문 테이블이 `COOKING` 또는 `MEAL` 상태인 경우 수정할 수 없다.
- id에 해당하는 주문 테이블의 손님 수 수정 기능
  - 손님 수는 0 미만일 수 없다.
  - id에 해당하는 주문 테이블은 유효해야한다.
  - 주문 테이블은 empty 상태가 아니어야한다.

### Table Group
- 개별 주문 테이블을 그룹으로 생성하는 기능
  - 입력받은 주문 테이블 목록을 그룹화한다.
    - 그룹화된 주문 테이블은 테이블 그룹 id가 부여되고, `emtpy` 상태가 아니다.
  - 주문 테이블은 2개 이상부터 그룹화할 수 있다.
  - 각 주문 테이블의 id는 유효해야한다.
  - 각 주문 테이블이 `empty` 상태가 아니거나 이미 그룹화된 경우 그룹으로 생성할 수 없다.
- 그룹을 해제하는 기능
  - 테이블 그룹 id에 해당하는 주문 테이블의 그룹을 해제한다.
    - 그룹이 해제된 주문 테이블은 `empty` 상태가 되며 테이블 그룹 id가 `null`이다.
  - 그룹에 속한 주문 테이블이 `COOKING` 또는 `MEAL` 상태인 경우 그룹을 해제할 수 없다.

### Order
- 주문 생성 기능
  - 주문 테이블 id와 주문 항목 목록을 입력받아 주문을 생성한다.
    - 생성된 주문의 주문 항목들의 주문은 `COOKING` 상태이다. 
  - 주문 항목은 1개 이상 입력해야한다.
  - 각 주문 항목의 메뉴는 서로 달라야한다.
  - 주문 테이블 id는 유효해야하고, `empty` 상태가 아니어야한다.
- 주문 목록 조회 기능
  - 모든 주문에 대한 id, 주문 테이블 id, 주문 상태, 주문 시간, 주문 항목을 조회한다.
- 주문 상태 수정 기능
  - 주문 id와 주문 상태를 입력받아 주문 항목들의 상태를 일괄적으로 변경한다.
  - 주문 id는 유효해야한다.
  - 주문 id에 해당하는 주문 상태가 이미 `COMPLETION`인 경우 변경할 수 없다.

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

# 레거시 코드 리팩터링 미션 요구사항

## 1단계 - 테스트를 통한 코드 보호
- [x] `kitchenpos` 패키지의 코드를 보고 키친포스의 요구 사항을 `README.md`에 작성
- [x] 모든 Business Object에 대한 테스트 코드를 작성

## 2단계 - 서비스 리팩터링
- [ ] 도메인 setter 메서드 제거
- [x] 핵심 비즈니스 로직을 도메인 객체가 담당하도록 변경
  - [x] `Product` 도메인에 비즈니스 로직 위임 및 단위 테스트 작성
  - [x] `MenuGroup` 도메인에 비즈니스 로직 위임 및 단위 테스트 작성
  - [x] `Menu` 도메인에 비즈니스 로직 위임 및 단위 테스트 작성
  - [x] `MenuProduct` 도메인에 비즈니스 로직 위임 및 단위 테스트 작성
  - [x] `Order` 도메인에 비즈니스 로직 위임 및 단위 테스트 작성
  - [x] `OrderLineItem` 도메인에 비즈니스 로직 위임 및 단위 테스트 작성
  - [x] `OrderTable` 도메인에 비즈니스 로직 위임 및 단위 테스트 작성
  - [x] `TableGroup` 도메인에 비즈니스 로직 위임 및 단위 테스트 작성
- [ ] Spring Data JPA 마이그레이션
  - 데이터베이스 스키마 변경 및 마이그레이션에 따른 형상 관리 
