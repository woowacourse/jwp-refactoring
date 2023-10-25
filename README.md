# 키친포스
## 요구 사항
### Products
- [x] 상품을 생성한다.
  - [x] 가격이 Null이면 예외가 발생한다.
  - [x] 가격이 0보다 낮으면 예외가 발생한다.
- [x] 상품 목록을 조회한다.

### MenuGroups
- [x] 메뉴 그룹을 생성한다.
- [x] 메뉴 그룹 목록을 조회한다.

### Menus
- [x] 메뉴를 생성한다.
  - [x] 메뉴의 가격이 Null이면 예외가 발생한다.
  - [x] 메뉴의 가격이 0보다 작으면 예외가 발생한다.
  - [x] 메뉴의 MenuGroupId가 존재하지 않는다면 예외가 발생한다.
  - [x] MenuProduct 중에서 MenuProduct의 ProductId를 가진 상품이 존재하지 않는다면 예외가 발생한다.
  - [x] 메뉴의 가격이 메뉴에 속한 제품들의 가격 합보다 큰 경우 예외가 발생한다.
    - 이 검증은 메뉴의 가격과 제품들의 가격을 일치시키고 메뉴의 가격이 제품들의 가격을 초과하는지 확인하기 위함
    - 또한 같은 메뉴의 상품을 여러개 주문할 경우를 대비해 메뉴 상품의 수량을 더한다.
    - 예를 들어 Menu(치킨+치즈볼), Product(치킨, 치즈볼), MenuProduct(치킨 * 1, 치즈볼 *1) 일 때
    - Menu(치킨+치즈볼)의 가격이 MenuProducts의 가격을 넘으면 안된다는 것이다.
  - [x] 메뉴를 생성하고 MenuProduct의 연관관계를 설정한다.
- [x] 메뉴 목록을 조회한다.

### Orders
- [x] 주문을 생성한다.
  - [x] 주문에 주문 항목이 존재하지 않을경우 예외가 발생한다.
  - [x] 주문 항목의 메뉴 개수와 주문한 메뉴의 개수가 다를 경우 예외가 발생한다.
    - 주문한 메뉴가 존재하지 않을 수도 있음
  - [x] 주문 테이블이 존재하지 않는다면 예외가 발생한다.
  - [x] 주문 테이블이 비어(empty)있다면 예외가 발생한다.
  - [x] OrderTableId를 설정한다.
  - [x] OrderStatus를 COOKING으로 설정한다.
  - [x] OrderedTime을 현재 시간으로 설정한다.
  - [x] 주문 항목들(OrderLineItems)의 orderId를 설정한다.
  - [x] 주문 항목들을 저장한다.
  - [x] 저장한 주문 항목들을 반환한다.
- [x] 주문 목록을 반환한다.
- [x] 주문 상태를 변경한다.
  - [x] 입력받은 orderId가 존재하지 않는다면 예외가 발생한다.
  - [x] 저장된 주문이 이미 완료되었다면 예외가 발생한다.

### TableGroups
- [x] 통합 계산을 위해 TableGroups(단체 설정)을 생성한다.
  - [x] 주문이 없으면 예외가 발생한다.
  - [x] 주문이 한 개면 예외가 발생한다.
  - [x] 저장된 주문 테이블 개수와 TableGroups의 주문 개수가 다르면 예외가 발생한다.
  - [x] 저장된 주문 테이블이 비어있지(empty) 않으면 예외가 발생한다.
  - [x] 저장된 주문 테이블의 TableGroupId가 null이 아니라면 예외가 발생한다.
  - [x] TableGroups를 저장하고
    - [x] tableGroupId를 설정한다.
    - [x] empty를 false로 설정한다.
- [x] 통합 계산을 취소한다.
  - [x] 통합 계산 주문들 중 하나라도 COOKING 또는 MEAL이면 예외가 발생한다.
  - [x] 주문의 tableGrouId를 null로 설정한다.
  - [x] 주문의 setEmpty를 false로 설정한다.

### Table
- [x] 주문 테이블(OrderTable)을 저장한다.
  - [x] 주문 테이블의 id를 null로 설정한다.
  - [x] 주문 테이블의 tableGroupId를 null로 설정한다.
- [x] 주문 목록을 반환한다.
- [x] 주문 테이블을 empty로 변경한다.
  - [x] 주문 테이블이 존재하지 않는다면 예외가 발생한다.
  - [x] 주문 테이블의 tableGroupId가 존재한다면 예외가 발생한다.
  - [x] 주문 상태가 COOKING 또는 MEAL이라면 예외가 발생한다.
- [x] 손님 수를 변경한다.
  - [x] 주문 테이블의 손님 수가 0보다 작다면 예외가 발생한다.
  - [x] 주문 테이블이 존재하지 않는다면 예외가 발생한다.
  - [x] 주문 테이블이 비어있다면(empty) 예외가 발생한다.

## 용어 사전

| 한글명      | 영문명 | 설명 |
|----------| --- | --- |
| 상품       | product | 메뉴를 관리하는 기준이 되는 데이터 |
| 메뉴 그룹    | menu group | 메뉴 묶음, 분류 |
| 메뉴       | menu | 메뉴 그룹에 속하는 실제 주문 가능 단위 |
| 메뉴 상품    | menu product | 메뉴에 속하는 수량이 있는 상품 |
| 금액       | amount | 가격 * 수량 |
| 주문 테이블   | order table | 매장에서 주문이 발생하는 영역 |
| 빈 테이블    | empty table | 주문을 등록할 수 없는 주문 테이블 |
| 주문       | order | 매장에서 발생하는 주문 |
| 주문 상태    | order status | 주문은 조리 ➜ 식사 ➜ 계산 완료 순서로 진행된다. |
| 방문한 손님 수 | number of guests | 필수 사항은 아니며 주문은 0명으로 등록할 수 있다. |
| 단체 지정    | table group | 통합 계산을 위해 개별 주문 테이블을 그룹화하는 기능 |
| 주문 항목    | order line item | 주문에 속하는 수량이 있는 메뉴 |
| 매장 식사    | eat in | 포장하지 않고 매장에서 식사하는 것 |

## 의존성 리팩터링
* 변경 전 : Menu(N) -> (1)MenuGroup (단방향, 직접 참조)
* 변경 후 : Menu(N) -> (1)MenuGroup (단방향, 간접 참조)
  * Menu는 MenuGroup을 간접 참조한다.
    * Menu가 생성될 때 MenuGroup이 생성되지 않는다. 
    * Menu가 삭제될 때 MenuGroup이 삭제되지 않아야 한다.
    * Menu가 속한 MenuGroup을 알 수 있어야 한다.

* 변경 전 : Menu(1) <-> (N)MenuProduct (양방향, 직접 참조)
* 변경 후 : Menu(1) <- (N)MenuProduct (단방향, 직접 참조)
  * 양방향 사이클(Cycle) 제거
    * Menu가 MenuProduct를 의존하지 않는다.
  * MenuProduct는 Menu를 직접 참조한다.
    * MenuProduct 매핑 테이블은 Menu가 생성될 때 같이 생성된다.
    * MenuProduct 매핑 테이블은 Menu가 삭제될 때 같이 삭제된다.

* 변경 전 : MenuProduct(N) -> (1)Product (단방향, 직접 참조)
* 변경 전 : MenuProduct(N) -> (1)Product (단방향, 간접 참조)
  * MenuProduct는 Product를 간접 참조한다.
    * MenuProduct가 생성될 때 Product가 생성되지 않는다.
    * MenuProduct가 삭제될 때 Product가 삭제되지 않는다.
    * 메뉴와는 별도의 도메인이기 때문에 지연 로딩을 사용하여 트랜잭션의 깊이(depth)를 늘리지 않는다.

* 