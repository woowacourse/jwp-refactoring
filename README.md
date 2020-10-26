# 키친포스
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


## 요구 사항
* **Menu**
  * MenuGroup에 속하는 실제 주문 가능 단위
    * MenuGroup에 반드시 속해야 한다.
  * Menu를 생성할 수 있다.
  * 가격은 0원 이상이다.
  * Menu 안에 있는 MenuProduct 가격 합보다 Menu 가격이 크면 안된다.

* **MenuGroup**
  * 메뉴 묶음 (분류)
  * MenuGroup을 생성할 수 있다.
  * MenuGroup을 조회할 수 있다.

* **MenuProduct**
  * Menu 안에 속하는 제품 정보
  * MenuProduct을 생성할 수 있다.
  * MenuProduct을 조회할 수 있다.
  * 가격은 0원 이상이다.

* **Order**
  * Order를 생성할 수 있다.
  * 주문 시 OrderTable을 등록해야 한다.
    * OrderTable은 empty 상태이면 안된다.
  * OrderLineItem에 있는 Menu 정보는 중복되면 안된다.
  * OrderStatus가 존재한다.
  * OrderStatus를 변경할 수 있다.
  * Order 목록을 조회할 수 있다.

* **OrderTable**
  * 주문이 발생한 테이블 정보
  * OrderTable을 생성할 수 있다.
  * 손님은 0명 이상이다.
  * 빈 상태의 Table에서는 손님을 받을 수 없다.
  * 빈 상태의 Table에서는 손님의 수를 변경할 수 없다.

* **OrderLineItem**
  * 하나의 Order에 존재하는 Menu 정보이다.
    * Menu의 수량

* **OrderStatus**
  * 주문의 상태
  * COOKING, MEAL, COMPLETION

* **TableGroup**
  * 통합 계산을 위해 Table을 그룹화한 정보
  * 그룹하기
    * Table 개수는 2개 이상이다.
    * 등록할 때 Table은 Empty 상태여야 한다.
    * 하나의 Table은 여러 TableGroup에 중복될 수 없다.
  * 분리하기
    * 주문이 완료되지 않은 상태라면 분리할 수 없다.
      * COOKING, MEAL

  
-[x] 테스트 코드를 작성한다.
    - 모든 Business Object에 대한 테스트 코드를 작성한다.
    - @SpringBootTest를 이용한 통합 테스트 코드 또는 @ExtendWith(MockitoExtension.class)를 이용한 단위 테스트 코드를 작성한다.
    - Controller는 권장하지만 필수는 아니다.