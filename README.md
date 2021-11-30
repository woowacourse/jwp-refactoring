# 키친포스

## 요구 사항

### 상품

- [x] 상품을 등록할 수 있다.
    - [x] 상품의 가격이 BigDecimal로 정상 입력되어야 한다.
    - [x] 상품의 가격은 0원 이상이어야 한다.
- [x] 상품 목록을 조회할 수 있다.

### 메뉴

- [x] 메뉴를 등록할 수 있다.
    - [x] 메뉴의 가격은 BigDeciaml로 정상 입력되어야 한다.
    - [x] 메뉴의 가격은 0원 이상이어야 한다.
    - [x] 메뉴 가격은 메뉴를 구성하는 총 상품의 가격 합보다 작아야 한다.
    - [x] 해당하는 메뉴 그룹이 존재해야 한다.
    - [x] 메뉴를 구성하는 상품이 모두 존재해야 한다.
- [x] 메뉴 목록을 조회할 수 있다.

### 메뉴 그룹

- [x] 메뉴 그룹을 등록할 수 있다.
- [x] 메뉴 그룹의 목록을 조회할 수 있다.

### 테이블

- [x] 주문 테이블을 등록할 수 있다.
- [x] 주문 테이블 목록을 조회할 수 있다.
- [x] 주문 테이블을 빈 테이블 상태를 수정할 수 있다.
    - [x] 존재하는 주문 테이블이어야 한다.
    - [x] 테이블이 단체 지정되어 있으면 불가하다.
    - [x] 조리 중이거나, 식사중인 경우 불가하다.
- [x] 주문 테이블의 손님 수를 수정한다.
    - [x] 손님 수는 0명 이상이어야 한다.
    - [x] 존재하는 주문 테이블이어야 한다.
    - [x] 빈 테이블 상태이면 안 된다.

## 테이블 단체 지정

- [x] 단체 지정을 등록할 수 있다.
    - [x] 단체 지정할 주문 테이블의 수가 2개 이상이어야 한다.
    - [x] 입력 받은 주문 테이블이 모두 저장된 테이블이어야 한다.
    - [x] 저장된 주문 테이블들 각각은 빈 테이블이어야 한다.
    - [x] 저장된 주문 테이블들 각각은 단체 지정되어 있으면 안된다.
    - [x] 현재 시간을 생성 시간에 추가해 준다.
    - [x] 주문 테이블을 순회하며, 각각에 단체 지정을 해주고, 주문을 할 수 있는 테이블 상태로 수정한다.

- [x] 단체 지정을 취소할 수 있다.
    - [x] 조리중이거나 식사중인 테이블이 포함되어 있으면 안 된다.
    - [x] 각 주문 테이블의 단체 지정을 해지하고, 주문을 할 수 있는 테이블 상태로 수정한다.

### 주문

- [x] 주문을 등록할 수 있다.
    - [x] 주문에 포함된 메뉴들이 존재해야 한다.(not Empty)
    - [x] 같은 메뉴의 주문 항목이 중복된 주문은 등록할 수 없다.
    - [x] 저장된 메뉴와 요청한 메뉴들의 종류가 같은지 검증한다.
    - [x] 존재하는 주문 테이블이어야 한다.
    - [x] 주문 테이블은 빈 테이블 상태가 아니여야 한다.
    - [x] 주문의 상태를 조리중으로 바꾼다.
    - [x] 주문 시간을 추가로 저장한다.
    - [x] 각각의 주문 항목을 저장한다.
- [x] 주문 목록을 조회할 수 있다.
    - [x] 각각의 주문에 대해 주문 항목을 함께 조회한다.
- [x] 주문의 상태를 바꿀 수 있다.
    - [x] 존재하는 주문이어야 한다.
    - [x] 주문이 완료된 상태이면 불가하다.

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

## ISSUE

### Step1

- 다른 Service에 의존적인 테스트 코드를 작성해도 될까?
    - TableService 중 TableGroup에 포함됬는지 여부에 따라 Empty상태 변경의 예외 조건이 있다. Service간의 의존이 명확하게 되지 않은 지금
      시점에서는 우선 Dao를 호출하여 진행해 보도록 한다.
- OrderService의 경우, 그 계층 구조가 명확한데 이 때에도 Repository만을 사용해서 테스트를 진행해야 할까?
    - 다른 Service 테스트에 경우에는 서비스간의 의존성을 끊기 위해 Dao를 가지고 데이터를 생성해서 진행했다. OrderServiceTest에서 필요한 데이터를
      생성해주기 위해서, 다른 Service를 사용하는 것이 옳을까? OrderServiceTest가 다른 서비스의 의존적이여야 하는 것이 당연한 것일까? 다른 서비스의
      수정의 결과가 OrderServiceTest에 영향을 주는 것이 바람직한가?
    - 다른 Service를 사용하지 않았을 때 발생할 수 있는 단점으로는, 의도하지 않은 형식의 데이터위에서 OrderService의 테스트가 진행될 수 있다는 점이다. 즉,
      어떤 형식의 데이터가 저장이 되는지를 고려하여 테스트 데이터를 작성해야 한다. 발생하는 작업을 모두 일일히 진행해줘야 한다.

### Step2

- 기존의 도메인의 구조를 유지한 채로 리팩토링 vs 도메인의 연관관계를 결정하고 나서 리팩토링.
    - 어느정도 도메인의 구조를 정하고, 선택을 해야 하는 문제에 대해서는 그 구조를 기반하여 작성하고자 한다.

- Repository와 Dao를 동시에 사용하게 되었을 때 문제점 발생. repository는 영속성 컨택스트로 저장, Dao는 직접 DB 조회. flush를 하지 않은 상태이기
  때문에, null을 조회하게 됨. Product를 JpaRepository로 저장한 후 flush 진행 -> dao로 조회했을 때, 정상적으로 동작.

- 객체의 생명주기
    - Product
    - Menu, MenuProduct

### Step3

- TableValidator를 interface로 구현하고 이 구현체를 Order부분에 위치 (아래의 의존관계를 풀어주기 위해)
    - TableValidator -> Order의 의존성이 존재 (Order -> OrderTable -> Order)
    - TableGroupValidator -> Order의 의존성이 존재 (Order -> OrderTable -> TableGroup -> Order)

### Step4

- `@RequestBody`의 기본 생성자가 없어서, 에러. 다른 부분에서는 왜 정상 동작할까?
- `@OneToMany` 단방향의 경우 update쿼리가 추가로 발생하는데, 현재의 schema는 NotNull 옵션이 적용되어있기 때문에, 불가능.
    - 양방향으로 뚫어서 이 부분을 해결. mappedBy를 어디서 하냐도 영향을 주는 듯.
    - 양방향으로 뚫었을 때 (Menu, MenuProduct), 한 쪽에 수정을 배제한다면, 편의 메소드는 의미가 없을까?

- Money를 도입하게 되었을 때, update 구문이 한번 더 나가게 됨. -> Money객체가 equals가 선언되어 있지 않아 발생했던 문제.
```log
Hibernate: 
    insert 
    into
        product
        (id, name, price) 
    values
        (null, ?, ?)
2021-11-30 01:43:53.835 TRACE 215952 --- [    Test worker] o.h.type.descriptor.sql.BasicBinder      : binding parameter [1] as [VARCHAR] - [product]
2021-11-30 01:43:53.836 TRACE 215952 --- [    Test worker] o.h.type.descriptor.sql.BasicBinder      : binding parameter [2] as [NUMERIC] - [1000]
Hibernate: 
    update
        product 
    set
        name=?,
        price=? 
    where
        id=?
2021-11-30 01:43:53.849 TRACE 215952 --- [    Test worker] o.h.type.descriptor.sql.BasicBinder      : binding parameter [1] as [VARCHAR] - [product]
2021-11-30 01:43:53.849 TRACE 215952 --- [    Test worker] o.h.type.descriptor.sql.BasicBinder      : binding parameter [2] as [NUMERIC] - [1000]
2021-11-30 01:43:53.850 TRACE 215952 --- [    Test worker] o.h.type.descriptor.sql.BasicBinder      : binding parameter [3] as [BIGINT] - [1]
```
