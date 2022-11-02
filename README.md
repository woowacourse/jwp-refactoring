# 키친포스

## 기능 요구 사항

### 상품
- 이름과 가격으로 상품을 생성할 수 있다. 
  - 가격은 0원 보다 같거나 커야한다.
- 상품 목록을 조회할 수 있다. 

### 메뉴 그룹
- 이름으로 메뉴 그룹을 생성할 수 있다. 
- 메뉴 그룹 목록을 조회할 수 있다. 

### 메뉴
- 이름, 가격, 메뉴 그룹 id, list(상품 id, 상품 수량)로 메뉴를 생성할 수 있다. 
  - 가격은 0원 보다 같거나 커야한다.
  - 해당 id의 메뉴 그룹이 있어야 한다.
  - 해당 id의 상품이 있어야한다.
  - 각 상품의 금액을 더한 값 보다 메뉴의 가격이 같거나 작아야한다.
- 메뉴 목록을 조회할 수 있다. 

### 테이블
- 방문한 손님 수와 비었는지 여부로 테이블을 생성할 수 있다. 
- 테이블 목록을 조회할 수 있다. 
- 특정 id의 테이블의 비었는지 여부를 변경할 수 있다. 
  - 해당 id의 테이블이 있어야한다.
  - 테이블에 이미 지정된 단체 지정이 있으면 안된다.
  - 테이블의 주문 상태는 조리, 식사 중 하나면 안 된다. -> **모든 주문이 계산 완료여야 한다.**  
- 특정 id의 테이블의 방문한 손님 수를 변경할 수 있다. 
  - 방문한 손님 수는 0보다 같거나 커야한다.
  - 해당 id의 테이블이 있어야한다.

### 단체 지정
- 주문 테이블의 id 목록으로 단체 지정을 생성한다.
  - 주문 테이블의 id는 2개 이상이어야 한다.
- 특정 id의 단체 지정을 삭제할 수 있다. 
  - 해당 id의 단체 지정이 있어야 한다.
  - 단체 지정의 주문 상태는 조리, 식사 중 하나여야 한다.

### 주문
- 주분 테이블 id, 주문 항목 리스트(메뉴 id, 수량)로 주문을 생성할 수 있다. 
  - 주문 항목 리스트는 비어있으면 안된다.
  - 해당 id의 주문 테이블이 있어야한다.
  - 주문의 상태는 조리로 시작된다.
- 주문 목록을 조회할 수 있다. 
- 특정 id의 주문의 상태를 변경할 수 있다.
  - 상태에는 조리, 식사, 계산 완료이 있다.
  - 해당 id의 주문이 있어야한다.
  - 해당 id의 주문 상태가 이미 계산 완료면 안된다.

## 용어 사전

| 한글명 | 영문명 | 설명 |
| --- | --- | --- |
| 상품 | product | 메뉴를 관리하는 기준이 되는 데이터 |
| 메뉴 그룹 | menu group | 메뉴 묶음, 분류 |
| 메뉴 | menu | 메뉴 그룹에 속하는 실제 주문 가능 단위 |
| 메뉴 상품 | menu product | 메뉴에 속하는 수량이 있는 상품 |
| 금액 | amount | 가격 * 수량 |
| 주문 테이블 | order orderTable | 매장에서 주문이 발생하는 영역 |
| 빈 테이블 | empty orderTable | 주문을 등록할 수 없는 주문 테이블 |
| 주문 | order | 매장에서 발생하는 주문 |
| 주문 상태 | order status | 주문은 조리 ➜ 식사 ➜ 계산 완료 순서로 진행된다. |
| 방문한 손님 수 | number of guests | 필수 사항은 아니며 주문은 0명으로 등록할 수 있다. |
| 단체 지정 | orderTable group | 통합 계산을 위해 개별 주문 테이블을 그룹화하는 기능 |
| 주문 항목 | order line item | 주문에 속하는 수량이 있는 메뉴 |
| 매장 식사 | eat in | 포장하지 않고 매장에서 식사하는 것 |

## 클래스 다이어그램

```uml
MenuGroup "1" -- "*" Menu
Menu "1" -- "*" MenuProduct
MenuProduct "*" -- "1" Product

TableGroup "1" -- "*" OrderTable
OrderTable "1" -- "*" Order
Order "1" -- "*" OrderLineItem
OrderLineTiem "*" -- "1" Menu
```

## 미션 요구 사항

### 2단계: 서비스 리팩터링

- [X] 단위 테스트하기 어려운 코드와 단위 테스트 가능한 코드를 분리해 단위 테스트 가능한 코드에 대해 단위 테스트를 구현한다.
  - 도메인 모델에 비지니스 로직을 모으면 프레임워크나 외부 종속 없이도 테스트가 가능해진다.
- [X] Spring Data JPA 사용 시 `spring.jpa.hibernate.ddl-auto=validate` 옵션을 필수로 준다.

### 3단계: 의존성 리팩터링

> 이전 단계에서 객체 지향 설계를 의식하였다면 아래의 문제가 존재한다. 의존성 관점에서 설계를 검토해 본다.

- [ ] 메뉴의 이름과 가격이 변경되면 주문 항목도 함께 변경된다. 메뉴 정보가 변경되더라도 주문 항목이 변경되지 않게 구현한다.
- [X] 클래스 간의 방향도 중요하고 패키지 간의 방향도 중요하다. 클래스 사이, 패키지 사이의 의존 관계는 단방향이 되도록 해야 한다.
  - OrderTable이 TableGroup의 id를 참조하도록 변경한다. 
  - Order가 OrderTabled의 id를 참조하도록 변경한다. 
- [ ] 데이터베이스 스키마 변경 및 마이그레이션이 필요하다면 [다음 문서](https://meetup.toast.com/posts/173)를 적극 활용한다.

## 프로그래밍 요구 사항

- Lombok을 사용하지 않는다.
- 모든 기능을 TDD로 구현해 단위 테스트가 존재해야 한다.
- 함수(또는 메서드)의 길이가 10라인을 넘어가지 않도록 구현한다.
- 모든 원시 값과 문자열을 포장한다.
- 3개 이상의 인스턴스 변수를 가진 클래스를 쓰지 않는다.
