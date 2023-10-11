# 키친포스

## 요구 사항

### 상품
- 상품은 다음과 같은 속성을 갖는다.
  - 이름
    - 이름은 1자에서 255자 사이이다.
  - 가격
    - 가격은 0 또는 양수이며 소숫점 2자리까지 표현할 수 있다.
    - 가격은 소숫점을 포함해 최대 19자리까지 가능하다.

- 상품을 등록할 수 있다.

- 상품의 목록을 조회할 수 있다.
  - 목록의 각 아이템은 id, 이름, 가격을 가진다.

### 메뉴 그룹
- 메뉴 그룹은 다음과 같은 속성을 갖는다.
  - 이름
    - 이름은 1자에서 255자 사이이다.
  
- 메뉴 그룹을 등록할 수 있다.
- 메뉴 그룹의 목록을 조회할 수 있다.
  - 목록의 각 아이템은 id, 이름을 가진다.

### 메뉴
- 메뉴는 다음과 같은 속성을 갖는다.
  - 이름
    - 이름은 1자에서 255자 사이이다.
  - 가격
    - 가격은 0 또는 양수이며 소숫점 2자리까지 표현할 수 있다.
    - 가격은 소숫점을 포함해 최대 19자리까지 가능하다.
    - 메뉴 가격은 모든 메뉴 상품 목록의 (상품 가격 * 상품의 양) 합보다 작거나 같아야 한다.
  - 메뉴 그룹
    - 모든 메뉴는 1개의 메뉴 그룹에 속한다.
    - 메뉴 그룹은 이미 등록된 메뉴 그룹이어야 한다.
  - 메뉴 상품 목록
    - 메뉴 상품 목록의 아이템은 1개 이상이다.
      - 메뉴 상품 목록의 각 아이템은 상품과 수량을 가진다.
      - 상품은 이미 등록된 상품이어야 한다.
      - 수량은 1 이상이어야 한다.

- 메뉴를 등록할 수 있다.

- 메뉴의 목록을 조회할 수 있다.

### 테이블
- 테이블은 다음과 같은 속성을 갖는다.
  - 번호
    - 테이블의 번호는 1 이상이다.
    - 테이블의 번호는 중복될 수 없다.
  - 비어 있는지 여부
    - 테이블이 등록되면 비어있는 상태이다.
  - 손님의 수
    - 손님의 수는 0 이상의 정수이다. 
    - 테이블이 비어있으면 손님의 수는 0이다.
  - 단체 지정
    - 지정된 단체가 없을 수도 있다.

- 테이블을 등록할 수 있다.

### 단체 지정
- 단체 지정은 다음과 같은 속성을 갖는다.
  - 번호
  - 단체 지정이 만들어진 시각
  - 단체 지정에 속한 테이블 목록
    - 단체 지정에 속한 테이블은 2개 이상이다.
    - 단체 지정에 속한 테이블은 등록된 테이블이어야 한다.
    - 단체 지정에 속한 테이블은 모두 비어 있는 상태여야 한다.
  
- 단체 지정을 등록할 수 있다.

### 주문
- 주문은 다음과 같은 속성을 갖는다.
  - 주문에 사용된 테이블
    - 주문에 사용된 테이블은 등록된 테이블이어야 한다.
  - 주문 상태
    - 주문 상태는 `조리중`, `식사중`, `계산완료` 중 하나이다.
  - 주문 시각
    - 주문 시각은 주문이 등록된 시각이다.
  - 주문 항목 목록
    - 주문 항목 목록의 주문 항목 아이템은 1개 이상이다.
      - 주문 항목 목록의 각 아이템은 메뉴와 수량을 가진다.
      - 메뉴는 이미 등록된 메뉴이어야 한다.
      - 수량은 1 이상이어야 한다.
  
- 주문을 등록할 수 있다.
  - 주문 등록 시 테이블의 상태는 비어있지 않은 상태여야한다.
    - 손님의 수는 필수 사항은 아니며 0 도 가능하다.
  
- 주문 상태를 변경할 수 있다.
  - 주문 상태가 `조리중`인 경우 `식사중`으로 변경할 수 있다.
  - 주문 상태가 `식사중`인 경우 `계산완료`로 변경할 수 있다.
  - 주문 상태가 `계산완료`인 경우 더 이상 상태를 변경할 수 없다.

- 주문을 조회할 수 있다.


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
