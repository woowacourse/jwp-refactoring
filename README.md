# 키친포스

---

## 요구 사항 🚀
- [x] 메뉴 생성 기능
  - [x] 메뉴는 아이디, 이름, 가격, 메뉴 그룹 아이디, 메뉴 상품들의 리스트로 구성된다.
    - [x] 메뉴 상품은 일련번호, 메뉴 아이디, 상품 아이디, 개수로 구성된다. 
  - [x] 메뉴의 가격은 0보다 큰 수여야 한다. (소수점을 포함할 수 있다)
  - [x] 메뉴 그룹은 존재하는 메뉴 그룹을 참조해야 한다.
  - [x] 메뉴는 고유해야 한다.
  - [x] 가격은 실제 메뉴 상품들의 총 가격보다 작아야 한다.
    - [x] 메뉴 상품들의 총 가격을 계산하는 방법은 `상품 가격 * 메뉴 상품 개수` 이다.
  - [x] 정상적으로 생성되면 `201 CREATED`와 함께 메뉴를 반환한다. 


- [x] 메뉴 조회 기능
  - [x] 모든 메뉴를 조회한다.
  - [x] 정상적으로 조회하면 `200 OK`와 함께 메뉴 리스트를 반환한다. 


- [x] 상품 생성 기능
  - [x] 상품은 아이디, 이름, 가격으로 구성된다.
  - [x] 상품 가격은 0보다 큰 수여야 한다. (소수점을 포함할 수 있다)
  - [x] 정상적으로 생성되면 `201 CREATED`와 함께 상품을 반환한다.


- [x] 상품 조회 기능
  - [x] 모든 상품을 조회한다.
  - [x] 정상적으로 조회하면 `200 OK`와 함께 상품 리스트를 반환한다.


- [x] 주문 생성 기능
  - [x] 주문은 아이디, 주문 테이블 아이디, 주문 상태, 주문 시간, 주문 항목 리스트로 구성된다.
    - [x] 주문 항목은 일련번호, 주문 아이디, 메뉴 아이디, 수량으로 구성된다.
  - [x] 주문 항목은 적어도 하나 이상 존재해야 한다.
  - [x] 주문 항목의 메뉴는 모두 존재해야 한다.
  - [x] 주문 테이블은 존재해야 한다.
  - [x] 빈 테이블에서는 주문을 할 수 없다.
  - [x] 주문은 발행되면 주문 상태가 `COOKING`으로 설정된다.
  - [x] 정상적으로 생성되면 `201 CREATED`와 함께 주문을 반환한다.


- [x] 주문 조회 기능
  - [x] 모든 주문을 조회한다.
  - [x] 정상적으로 조회하면 `200 OK`와 함께 주문 리스트를 반환한다.


- [x] 주문 상태 변경 기능
  - [x] 해당하는 주문은 존재해야 한다.
  - [x] 이미 완료된 주문이면 상태를 변경할 수 없다.
  - [x] 정상적으로 변경하면 `200 OK`와 함께 주문을 반환한다.


- [x] 메뉴 그룹 생성 기능
  - [x] 메뉴 그룹은 아이디와 이름으로 구성된다.
  - [x] 정상적으로 생성되면 `201 CREATED`와 함께 메뉴 그룹을 반환한다.


- [x] 메뉴 그룹 조회 기능
  - [x] 모든 메뉴 그룹을 조회한다.
  - [x] 정상적으로 조회하면 `200 OK`와 함께 메뉴 그룹 리스트를 반환한다.


- [x] 테이블 생성 기능
  - [x] 테이블은 아이디, 테이블 그룹 아이디, 손님 수, 빈 테이블 여부로 구성된다.
  - [x] 정상적으로 생성되면 `201 CREATED`와 함께 테이블을 반환한다.


- [x] 테이블 조회 기능
  - [x] 모든 테이블을 조회한다.
  - [x] 정상적으로 조회하면 `200 OK`와 함께 테이블 리스트를 반환한다.


- [x] 테이블 상태 변경 기능
  - [x] 빈 테이블 혹은 주문 테이블로 변경하는 기능
    - [x] 해당하는 테이블은 존재해야 한다.
    - [x] 테이블 그룹이 존재하면 변경할 수 없다.
    - [x] 해당 테이블의 주문 상태가 `COOKING`이거나 `MEAL`이면 안된다.
    - [x] 정상적으로 변경하면 `200 OK`와 함께 테이블을 반환한다.
  - [x] 손님 수 변경 기능
    - [x] 손님 수는 0명 이상으로만 변경할 수 있다.
    - [x] 테이블은 존재해야 한다.
    - [x] 빈 테이블에 대해 손님 수는 변경할 수 없다.
    - [x] 정상적으로 변경하면 `200 OK`와 함께 테이블을 반환한다.


- [x] 테이블 그룹 생성 기능
  - [x] 테이블 그룹은 아이디, 생성 날짜, 테이블 리스트로 구성된다.
  - [x] 테이블은 두 개 이상 지정할 수 있다.
  - [x] 테이블은 모두 실제 존재하는 테이블이어야 한다.
  - [x] 빈 테이블이면서 그룹에 포함되지 않은 테이블만 그룹으로 지정할 수 있다.
  - [x] 그룹으로 지정된 테이블은 주문 테이블로 변경된다.
  - [x] 정상적으로 생성하면 `201 CREATED`와 함께 테이블 그룹을 반환한다.


- [x] 테이블 그룹 해체 기능
  - [x] 해당하는 아이디에 맞는 테이블 그룹을 해체한다.
  - [x] 주문 상태가 `COOKING`이거나 `MEAL`이면서 주문이 이미 존재하는 경우 해체할 수 없다.
  - [x] 정상적으로 해체한 경우 `204 NO CONTENT`를 반환한다.

---
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
### step1 기록
-  트랜잭션 스크립트 패턴으로 작성된 서비스를 테스트하려니 테스트가 어렵다.
  - 모든 서비스 로직이 뭉쳐있기 때문에 기능별로 독립적인 테스트가 어려워진다.

### step2 기록
- 한 번에 모든 것들을 바꾸기는 어렵다.
  - 현재 규모에서도 바꾸기 어려운데, 실무 레거시 코드를 접하게 되면 더 어려울 것이다.
  - 따라서 점진적인 리팩토링이 중요하다는 생각이 든다. 
    - 한 번에 변경하는 단위를 최소화하자.
- 모든 비즈니스 규칙을 검증하기에는 서비스 테스트로는 부족하다는 생각이 든다.
  - 각각의 서비스 메소드는 잘 작동하는 것 같지만, 이들이 조합되었을 때의 검증이 필요함.
- 명세도 없고, 기존 레거시 코드만으로 기능을 유추해야 하는 상황이다.
  - 이런 상황에서도 테스트 코드가 존재하기 때문에 변경한 코드가 기존 레거시와 동일한 기능을 한다는 것을 보장받을 수 있는 것 같다.

### step3 기록
- 리팩토링 진행 순서는 패키지 분리 -> 패키지 의존성 사이클 제거 -> 클래스 의존성 사이클 제거
  - 패키지를 분리하는 기준은 '같이 변경되고 같은 제약사항을 공유하는 도메인들끼리' 묶는 것
- JPA를 사용하면서 단방향 일대다 관계로 구성하고 싶지만, INSERT + UPDATE 구조이기 때문에 NOT NULL 제약조건을 위반한다.
  - 어떻게 할 수 있을까? 스키마를 수정해야 할까?
- 양방향 연관관계를 제거하고(다중성이 적은 방향으로) ID 참조를 통해 도메인 경계 간의 격리성을 강화하니 서비스에 비즈니스 로직이 새어나오게 되었다.
  - 서로 다른 바운디드 컨텍스트의 협력이기 때문에 서비스에 드러나는게 어찌 보면 당연하기도 한 것 같다.


- 의존성 리팩토링 전

<img src = images/before.png>

- 의존성 리팩토링 후

<img src = images/after.png>
