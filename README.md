# 키친포스

## 요구 사항

> | 한글명 | 영문명 | 설명 |

### 상품

> | 상품 | product | 메뉴를 관리하는 기준이 되는 데이터 |

- [x] 상품을 등록할 수 있다.
- [x] 상품의 가격이 올바르지 않으면 등록할 수 없다.
    - [x] 상품의 가격은 0원 이상이어야 한다.
- [x] 상품 목록을 조회할 수 있다.

### 메뉴 그룹

> | 메뉴 그룹 | menu group | 메뉴 묶음, 분류 |

- [x] 메뉴 그룹을 생성할 수 있다.
- [x] 메뉴 그룹 목록을 조회할 수 있다.

### 메뉴

> | 메뉴 | menu | 메뉴 그룹에 속하는 실제 주문 가능 단위 |

- [x] 메뉴를 등록할 수 있다.
- [x] 메뉴 정보가 올바르지 않으면 등록할 수 없다.
    - [x] 메뉴의 가격은 0원 이상이어야 한다.
    - [x] 메뉴의 가격은 비어있을 수 없다.
    - [x] 메뉴 그룹이 존재해야한다.
    - [x] 메뉴 상품이 존재해야한다.
    - [x] 메뉴 가격은 메뉴 상품 가격의 합보다 클 수 없다.
- [x] 메뉴 목록을 조회한다.

### 주문 테이블

> | 주문 테이블 | order table | 매장에서 주문이 발생하는 영역 |

- [x] 주문 테이블을 생성할 수 있다.
- [x] 주문 테이블 목록을 조회할 수 있다.
- [x] 주문 테이블이 빈 테이블인지에 대한 정보를 수정할 수 있다.
    - [x] 주문 테이블 정보가 올바르지 않으면 상태를 변경할 수 없다.
        - [x] 주문 테이블이 존재해야한다.
        - [x] 주문 테이블 그룹이 존재해야한다.
        - [x] 주문 테이블 조리, 식사중이면 안된다.
- [x] 주문 테이블의 방문한 손님 수를 수정할 수 있다.
    - [x] 주문 테이블의 정보가 올바르지 않으면 수정할 수 없다.
        - [x] 방문한 손님 수는 0명 이상이어야 한다.
        - [x] 주문 테이블이 존재해야한다.
        - [x] 주문 테이블은 비어있을 수 없다.

### 단체 지정

> | 단체 지정 | table group | 통합 계산을 위해 개별 주문 테이블을 그룹화하는 기능 |

- [x] 테이블 그룹을 생성할 수 있다.
    - [x] 테이블 그룹 정보가 올바르지 않으면 생성할 수 없다.
        - [x] 주문 테이블이 2개 이상이여야한다.
        - [x] 주문 테이블은 모두 존재해야한다.
        - [x] 주문 테이블은 모두 빈 테이블이여야한다.
- [x] 테이블 그룹을 제거한다.
    - [x] 테이블 그룹 정보가 올바르지 않으면 제거할 수 없다.
        - [x] 테이블 그룹에 속한 주문 테이블은 모두 조리, 식사중이면 안된다.

### 주문

> | 주문 | order | 매장에서 발생하는 주문 |

- [x] 주문을 생성한다.
  - [x] 주문 정보가 올바르지 않으면 주문을 생성할 수 없다.
      - [x] 주문 항목의 개수는 0보다 커야한다.
      - [x] 주문 항목 개수와 실제 메뉴의 개수는 일치해야한다.
      - [x] 주문 테이블이 존재해야한다.
      - [x] 빈 테이블이면 주문할 수 없다.
- [x] 주문 목록을 조회한다.
- [x] 주문 상태를 변경한다.
  - [x] 주문 상태가 올바르지 않으면 상태를 변경할 수 없다.
      - [x] 주문 상태가 계산 완료 상태변 변경할 수 없다.

## 용어 사전

| 한글명      | 영문명              | 설명                            |
|----------|------------------|-------------------------------|
| 상품       | product          | 메뉴를 관리하는 기준이 되는 데이터           |
| 메뉴 그룹    | menu group       | 메뉴 묶음, 분류                     |
| 메뉴       | menu             | 메뉴 그룹에 속하는 실제 주문 가능 단위        |
| 메뉴 상품    | menu product     | 메뉴에 속하는 수량이 있는 상품             |
| 금액       | amount           | 가격 * 수량                       |
| 주문 테이블   | order table      | 매장에서 주문이 발생하는 영역              |
| 빈 테이블    | empty table      | 주문을 등록할 수 없는 주문 테이블           |
| 주문       | order            | 매장에서 발생하는 주문                  |
| 주문 상태    | order status     | 주문은 조리 ➜ 식사 ➜ 계산 완료 순서로 진행된다. |
| 방문한 손님 수 | number of guests | 필수 사항은 아니며 주문은 0명으로 등록할 수 있다. |
| 단체 지정    | table group      | 통합 계산을 위해 개별 주문 테이블을 그룹화하는 기능 |
| 주문 항목    | order line item  | 주문에 속하는 수량이 있는 메뉴             |
| 매장 식사    | eat in           | 포장하지 않고 매장에서 식사하는 것           |
