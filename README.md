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

- [ ] kitchenpos 패키지의 코드를 보고 키친포스의 요구 사항을 README.md에 작성한다.
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
