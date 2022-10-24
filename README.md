# 키친포스

## 요구 사항

키친 포스를 구현한다.

## 테이블 구조 파악하기

![](images/table-structure.png)

## 도메인 구조 파악하기

![](images/domain-structure.png)

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

### Product

 * 메뉴 관리에 기준이 되는 product이다. 
 * product의 name과 price를 가지고 있다.
 * Product 생성 시 price가 null인 경우 예외를 던진다.
 * Product 생성 시 price가 0미만인 경우 예외를 던진다.

### MenuGroup 

 * menuGroup은 menu의 묶음을 나타낸다.

### Menu

 * menu는 menuGroup에 속한 실제 주문 가능한 단위를 나타낸다. 
 * name, price, menuGroupId, MenuProducts를 가지고 있다.
 * Menu 생성 시 price가 null인 경우 예외를 던진다.
 * Menu 생성 시 price가 0미만인 경우 예외를 던진다.
 * Menu 생성 시 menu는 무조건 특정 menuGroup에 속해야 한다.
 * Menu 생성 시 menu의 price가 menu에 속한 menuProducts의 `price * quantity` 총합 보다 클 경우 예외를 던진다. 
 * Menu가 정상적으로 영속되면 menu에 속하는 menuProducts가 순차적으로 저장된다.

### MenuProduct

 * MenuProduct는 메뉴에 속하는 수량이 있는 product를 나타낸다. 
 * menuId, productId, quantity를 가지고 있다.
 * Menu와 Product의 중간 객체이다.
 * MenuProduct 생성 시 MenuId와 ProductId가 존재해야 한다.

### TableGroup

 * TableGroup은 통합 계산을 위해 개별 주문 테이블을 그룹화하기 위한 용도이다. 
 * createdDate, orderTables를 가지고 있다.
 * TableGroup 생성 시 orderTables가 비어있는 경우 예외를 던진다.
 * TableGroup 생성 시 orderTables의 사이즈가 2미만인 경우 예외를 던진다.
 * TableGroup 생성 시 orderTables는 모두 영속된 상태여야 한다.
 * TableGroup 생성 시 영속된 orderTables는 모두 비어 있어야 한다.
 * TableGroup 생성 시 영속된 orderTables는 TableGroupId를 가지고 있으면 안된다. 즉 어떤 TableGroup에도 속해 있지 않아야 한다.
 * TableGroup이 생성되면 소속된 orderTables에 tableGroupId를 설정한다.
 * TableGroup이 생성되면 그룹 내부에 존재하는 orderTable은 모두 비어있지 않은 상태로 영속 된다.

### OrderTable

 * OrderTable은 매장에서 주문이 발생하는 영역이다.
 * tableGroupId, numberOfGuests, empty를 가지고 있다. 
 * OrderTable 생성 시 tableGroupId는 없어도 된다.
 * OrderTable 생성 시 numberOfGuests관련 추가 제약 조건은 없다. (추후 추가하면 좋을 것 같다.)
 * OrderTable 생성 시 empty 관련 제약 조건은 없다. 최초에 비어있는 상태로 생성 될 것으로 추측한다.
 * numberOfGuests는 필수 사항은 아니며 주문은 0명으로 등록할 수 있다.
 * orderTable이 비어있는 경우(`isEmpty == true`) order를 진행할 수 없다. 즉 개별 테이블에서 설정하거나 TableGroup에 속해야 한다.
 * OrderTable은 empty를 변경할 수 있다.
   * 특정 TableGroup에 속한 table인 경우 empty를 변경할 수 없다.
   * OrderTable에 속한 Order 중 `COOKING`, `MEAL` 상태를 포함하는 경우 예외를 던진다.
 * OrderTable은 `numberOfGuests`를 변경할 수 있다.
   * numberOfGuests가 0미만인 경우 예외를 던진다.
   * 영속되지 않은 OrderTable인 경우 예외를 던진다.
   * 영속된 OrderTable이 비어있는 경우 예외를 던진다.

### Order

 * Order는 매장에서 발생하는 주문이다. 
 * orderTableId, orderStatus, orderedTime, orderLineItems를 가지고 있다. 
 * Order 생성 시 orderLineItems가 비어 있는 경우 예외를 던진다.
 * Order 생성 시 orderLineItems에 속한 menu가 영속되지 않은 메뉴를 포함하는 경우 예외를 던진다.
 * Order 생성 시 존재하지 않는 OrderTable인 경우 예외를 던진다.
 * Order 생성 시 비어 있는 OrderTable인 경우 예외를 던진다.
 * Order 생성 시 특정 OrderTable에 속해야 한다.
 * Order 생성 시 OrderStatus는 `COOKING`이다.
 * Order 생성 시 OrderedTime은 현재 서버 시간(LocalDateTime)으로 설정된다.
 * Order가 생성되면 orderLineItem은 orderId 값을 세팅한 뒤 영속된다.
 * Order는 OrderStatus를 변경할 수 있다.
   * 존재하지 않는 Order인 경우 예외를 던진다.
   * Order의 OrderStatus가 `COMPLETION`인 경우 예외를 던진다.

### OrderStatus

 * OrderStatus는 order의 상태를 나타낸다.
 * 주문은 `조리(COOKING)` ➜ `식사(MEAL)` ➜ `계산 완료(COMPLETION)` 순서로 진행된다.

### OrderLineItem

 * OrderLineItem은 orderId, menuId, quantity를 가지고 있다.
 * Order와 Menu의 중간 객체이다.
 * OrderLineItem 생성 시 OrderId와 MenuId가 존재해야 한다.

## 비즈니스 로직 파악하기

### ProductService

 * [x] product를 생성한다.
   * [x] price가 null인 경우 예외를 던진다.
   * [x] price가 0미만인 경우 예외를 던진다.
 * [x] product list를 조회한다.

### MenuGroupService

 * [x] menuGroup을 생성한다.
 * [x] menuGroup list를 조회한다.

### MenuService

 * [x] menu를 생성한다.
   * [x] price가 null인 경우 예외를 던진다.
   * [x] price가 0미만인 경우 예외를 던진다.
   * [x] 존재하지 않는 menuGroupId인 경우 예외를 던진다.
   * [x] price가 menu에 속한 product의 총 price보다 큰 경우 예외를 던진다.
 * [x] menu list를 조회한다.

### OrderService

 * [x] order를 생성한다.
    * [x] orderLineItems가 비어있는 경우 예외를 던진다.
    * [x] orderLineItems의 사이즈가 실제 menu에 포함된 개수가 알치하지 않는 경우 예외를 던진다.
    * [x] orderTable이 비어있는 경우 예외를 던진다.
 * [x] order list를 조회한다.
 * [x] order의 상태를 변경한다.
   * [x] order의 상태가 `COMPLETION`인 경우 예외를 던진다.

### TableService

 * [x] orderTable을 생성한다.
 * [x] orderTable list를 조회한다.
 * [x] orderTable의 empty를 변경한다.
   * [x] tableGroupId가 null이 아닌 경우 예외를 던진다.
   * [x] orderTable에 속한 order 중 `COOKING`, `MEAL`이 존재하는 경우 예외를 던진다.
 * [x] orderTable의 `changeNumberOfGuests`를 변경한다.
   * [x] numberOfGuests가 0미만인 경우 예외를 던진다.
   * [x] 저장된 orderTable이 비어있는 경우 예외를 던진다.

### TableGroupService

 * [x] tableGroup을 생성한다.
   * [x] orderTables가 비어있으면 예외를 던진다.
   * [x] orderTables의 사이즈가 2미만인 경우 예외를 던진다.
   * [x] orderGroup이 가진 orderTables의 사이즈와 저장된 orderTables의 사이즈가 다른 경우 예외를 던진다.
   * [x] 저장된 orderTables 중 비어있지 않은 table이 존재하는 경우 예외를 던진다.
   * [x] 저장된 orderTables 중 tableGroupId가 null이 아닌 경우 예외를 던진다.
 * [x] tableGroup을 해제한다.
   * [x] orderTables의 orderStatus가 `COOKING`, `MEAL`인 경우 예외를 던진다.

## 테스트 코드 작성 규칙

### application 계층

 * `@SpringBootTest`를 활용한 통합 테스트를 진행한다. 기본적으로 도메인 별 `Fixtures`를 추가하여 해당 데이터를 기반으로 테스트를 진행한다.
 * `@Sql` 애노테이션을 활용하여 테스트 격리를 진행한다.
