# 키친포스

## 요구 사항

### 메뉴 (Menu)
- 메뉴를 생성할 수 있다.
  - 메뉴의 이름, 가격, 메뉴 그룹 식별자, 메뉴 상품 목록을 받는다.
  - 가격이 null 또는 0 보다 작으면 예외를 던진다.
  - 메뉴 그룹 식별자에 대한 메뉴 그룹이 없으면 예외를 던진다.
  - 메뉴 상품의 상품 식별자로 상품을 조회하고 상품의 가격과 메뉴 상품의 수량을 곱한 총합 가격을 더한다.
    - 상품 식별자에 대한 상품이 없으면 예외를 던진다.
    - 총합 가격이 메뉴 가격과 맞지 않으면 예외를 던진다.
- 메뉴를 조회할 수 있다.

### 메뉴 그룹 (MenuGroup)

- 메뉴 그룹을 생성할 수 있다.
- 메뉴 그룹을 조회할 수 있다.

### 메뉴 상품 (MenuProduct)

- 메뉴를 생성할 때 함께 생성된다.
- 메뉴를 조회할 때 함께 조회된다.

### 주문 (Order)

- 주문을 생성할 수 있다.
  - 주문 테이블의 식별자, 주문 항목 목록을 받는다.
  - 주문 항목 목록이 비어있으면 예외를 던진다.
  - 주문 항목 목록의 메뉴 식별자의 갯수와 주문 항목 목록의 갯수가 맞지 않으면 예외를 던진다.
  - 주문 테이블 식별자로 주문 테이블을 조회하고, 없으면 예외를 던진다.
  - 주문 테이블이 비어있으면 예외를 던진다.
  - 주문을 `COOKING` 상태로 변경한다.
- 주문을 조회할 수 있다.
- 주문의 상태를 변경할 수 있다.
  - 주문의 식별자, 주문의 상태를 받는다.
  - 주문의 식별자로 주문을 찾고, 없으면 예외를 던진다.
  - 찾은 주문이 `COMPLETION` 상태이면 예외를 던진다.

### 주문 항목 (OrderLineItme)

- 주문을 생성할 때 함께 생성된다.
- 주문을 조회할 때 함께 조회된다.

### 주문 상태 (OrderStatus)

- `COOKING`, `MEAL`, `COMPLETION`을 가지는 Enum이다.

### 주문 테이블 (OrderTable)

- 주문 테이블을 생성할 수 있다.
  - 빈 테이블 유무, 방문한 손님수를 받는다.
- 주문 테이블을 조회할 수 있다.
- 주문 테이블은 빈 테이블로 변경할 수 있다.
  - 주문 테이블 식별자, 빈 테이블 유무를 받는다.
  - 주문 테이블 식별자로 주문 테이블을 조회하고, 없으면 예외를 던진다.
  - 테이블 그룹에 등록되어 있으면 예외를 던진다.
  - 주문 테이블의 주문 목록에 주문의 상태가 `COMPLETION`이 아니면 예외를 던진다.
- 주문 테이블은 방문한 손님 수를 변경할 수 있다.
  - 주문 테이블 식별자, 방문한 손님 수를 받는다.
  - 방문한 손님 수가 음수이면 예외를 던진다.
  - 주문 테이블 식별자로 주문 테이블을 조회하고, 없으면 예외를 던진다.
  - 주문 테이블이 비어있으면 예외를 던진다.

### 상품 (Product)

- 상품을 생성할 수 있다.
  - 상품의 이름, 가격을 받는다.
  - 가격이 null 또는 0 보다 작으면 예외를 던진다.
- 상품을 조회할 수 있다.

### 테이블 그룹 (TableGroup)

- 테이블 그룹을 생성할 수 있다.
  - 주문 테이블 목록을 받는다.
  - 주문 테이블 목록의 갯수가 1개 이하이면 예외를 던진다.
  - 주문 테이블 목록의 식별자로 주문 테이블 목록을 조회하고, 조회한 목록의 갯수와 식별자의 갯수가 맞지 않으면 예외를 던진다.
  - 주문 테이블 목록에서 비어있지 않거나, 테이블 그룹에 등록되어 있으면 예외를 던진다.
  - 주문 테이블 목록의 테이블 그룹을 등록하고, 빈 테이블 상태를 false로 설정한다.
- 테이블 그룹을 해제할 수 있다.
  - 테이블 그룹 식별자를 받는다.
  - 테이블 그룹의 식별자로 주문 테이블 목록을 조회한다.
  - 주문 테이블의 주문 목록에 주문의 상태가 `COMPLETION`이 아니면 예외를 던진다.
  - 주문 테이블 목록의 테이블 그룹 식별자를 null로 변경하고, 빈 테이블 상태를 false로 설정한다.

### 프로젝트 개선 목록

- [ ] 코드 서식을 정리한다.
  - [ ] 응용 계층에 `@Transactional`을 클래스 단에 적용, 조회에는 `readonly` 설정 
  - [ ] 표현 계층에 `@RequestMapping`을 클래스 단에 적용한다.
- [ ] 도메인 구조를 JPA 엔티티 기반으로 재설계 한다.
  - [ ] 비즈니스 로직을 도메인 객체로 최대한 이동한다.
  - [ ] setter를 삭제한다.
  - [ ] Dao를 Repository로 변경한다.
- [ ] 표현 계층에서 도메인에 대한 의존성을 제거한다.
- [ ] 표현 계층에서 API 버저닝을 적용한다.
- [ ] 예외를 명확하게 던지고, `@ControllerAdvice`를 적용한다.
- [ ] 표현 계층에서 응답의 형식을 정의하고 적용한다.

### 1단계 요구 사항

- [ ] 도메인 요구 사항 정리
- [ ] Controller 테스트 추가
- [ ] Service 테스트 추가

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
