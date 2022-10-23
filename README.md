# 키친포스

<details>
<summary>용어 사전</summary>

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

</details>

<details>
<summary>1단계 - 테스트를 통한 코드 보호</summary>

## 요구 사항

- [ ] kitchenpos 패키지의 코드를 보고 요구 사항을 README.md에 작성
    - [마크다운(Markdown) - Dooray!](https://dooray.com/htmls/guides/markdown_ko_KR.html)
- [ ] 요구 사항을 토대로 모든 Business Object에 대한 테스트 코드 작성
    - [Testing in Spring Boot - Baeldung](https://www.baeldung.com/spring-boot-testing)
    - [Exploring the Spring Boot TestRestTemplate](https://www.baeldung.com/spring-boot-testresttemplate)
- [ ] Lombok 사용하지 않기
    - [Lombok 사용상 주의점(Pitfall)](https://kwonnam.pe.kr/wiki/java/lombok/pitfall)

---

## 구현 기능 목록

### 상품(product)

- 등록
    - 가격이 null이거나 0보다 작으면 등록할 수 없다.
- 목록 조회

### 메뉴 그룹(menu group)

- 등록
- 목록 조회

### 메뉴(menu)

- 등록
    - 가격이 null이면 등록할 수 없다.
    - 가격이 0보다 작으면 등록할 수 없다.
    - 메뉴 그룹이 존재하지 않으면 등록할 수 없다.
    - 존재하지 않는 상품이 포함되어 있으면 등록할 수 없다.
    - 메뉴 금액이 각 상품 금액의 합보다 크면 등록할 수 없다.
- 목록 조회

### 주문(order)

- 등록
    - 주문 항목이 존재하지 않는다면 등록할 수 없다.
    - 존재하지 않는 메뉴가 포함되어 있으면 등록할 수 없다.
    - 주문 테이블이 존재하지 않는다면 등록할 수 없다.
    - 주문 테이블이 빈 테이블이면 등록할 수 없다.
- 목록 조회
- 상태 변경
    - 존재하지 않는 주문이면 변경할 수 없다.
    - 현재 상태가 `COMPLETION`이면 변경할 수 없다.

### 테이블(table)

- 등록
- 목록 조회
- 빈 테이블로 변경
    - 존재하지 않는 주문 테이블이면 변경할 수 없다.
    - 단체 지정에 포함된 테이블이면 변경할 수 없다.
    - 주문 상태가 `COOKING`이거나 `MEAL`이면 변경할 수 없다.
- 방문한 손님 수 변경
  - 손님의 수가 0보다 작으면 변경할 수 없다.
  - 존재하지 않는 주문 테이블이면 변경할 수 없다.
  - 빈 테이블이면 변경할 수 없다.

### 단체 지정(table group)

- 등록
  - 주문 테이블이 존재하지 않거나 2개보다 적으면 등록할 수 없다.
  - 존재하지 않는 주문 테이블이 포함되어 있다면 등록할 수 없다.
  - 비어있지 않은 테이블이나 이미 다른 단체에 지정된 테이블이 포함되어 있다면 등록할 수 없다.
- 단체 해제
  - 주문 상태가 `COOKING`이거나 `MEAL`인 테이블이 존재한다면 해제할 수 없다.

</details>
