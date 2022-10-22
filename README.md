# 키친포스

## 요구 사항

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

---

# 1단계 - 테스트를 통한 코드 보호

## 미션 요구사항 1

- [ ] `kitchenpos` 패키지의 코드를 보고 키친포스의 요구사항을 `README.md` 에 작성한다.

## 미션 요구사항 2

- [ ] 정리한 키친포스의 요구사항을 토대로 테스트 코드를 작성한다.
- [ ] 모든 Business Object에 대한 테스트 코드를 작성한다.
- [ ] `@SpringBootTest` 를 이용한 통합 테스트는 `@ExtendWith(MockitoExtension.class)` 를 이용한 단위 테스트 코드를 작성한다.

---

## 프로그래밍 요구사항

Lombok 은 그 강력한 기능만큼 사용상 주의를 요한다.

- 무분별한 setter 메소드 사용
- 객체 간에 상호 참조하는 경우 무한 루프에 빠질 가능성
- [Lombok 사용상 주의점(Pitfall)](https://kwonnam.pe.kr/wiki/java/lombok/pitfall)

이번 미션에서는 Lombok 없이 미션을 진행한다.
