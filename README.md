# 키친포스

## 요구 사항

- 메뉴를 만들 수 있다.
    - 메뉴는 이름이 있어야 한다.
    - 메뉴는 가격이 있어야 한다.
        - **예외** 메뉴 가격은 정확한 값 이여야 한다.
        - **예외** 메뉴 가격은 0원 이상 이여야 한다.
    - 메뉴는 그룹이 있어야 한다.
    - 메뉴에 속하는 메뉴 상품이 있어야 한다.
        - **예외** 메뉴 상품 가격은 정확한 값 이여야 한다.
        - **예외** 메뉴 상품 가격의 합이 메뉴 가격 이상 이여야 한다.
- 메뉴들을 볼 수 있다.

--- 

- 메뉴 그룹을 만들 수 있다.
    - 메뉴 그룹은 이름이 있어야 한다.
- 메뉴 그룹들을 볼 수 있다.

---

- 주문을 할 수 있다.
    - 주문하는 테이블이 있어야 한다.
        - **예외** 테이블이 비어있으면 안된다.
    - 주문 상태가 있어야 한다.
        - 주문이 들어가면 조리 상태가 된다.
    - 주문한 시간이 있어야 한다.
    - 주문에 주문 항목이 있어야 한다.
        - **예외** 주문 항목에 있는 메뉴가 존재해야한다.
- 주문들을 볼 수 있다.
- 주문의 상태를 변경할 수 있다.
    - **예외** 이미 계산이 완료되었으면 변경할 수 없다.

---

- 상품을 등록할 수 있다.
    - 상품의 이름이 있어야 한다.
    - 상품의 가격이 있어야 한다.
        - **예외** 상품의 가격은 정확해야한다.
        - **예외** 상품의 가격은 0원 이상 이여야 한다.
- 상품들을 볼 수 있다.

---

- 주문 테이블을 만들 수 있다.
    - 주문 테이블에는 방문한 손님 수가 있어야 한다.
    - 주문 테이블이 빈 테이블인지 아닌지 알 수 있어야한다.
- 주문 테이블들을 볼 수 있다.
- 주문 테이블을 빈 테이블로 변경할 수 있다.
    - **예외** 존재하는 주문 테이블 이어야 한다.
    - **예외** 계산 완료되지 않은 주문 테이블은 빈 테이블로 변경할 수 없다.
- 주문 테이블의 방문한 손님 수를 변경할 수 있다.
    - **예외** 0명 미만으로 변경할 수 없다.
    - **예외** 저장되어 있지 않은 주문 테이블은 변경할 수 없다.
    - **예외** 빈 테이블은 변경할 수 없다.

---

- 주문 테이블들을 단체 지정할 수 있다.
    - **예외** 주문 테이블이 2개 이상 이어야 한다.
    - **예외** 단체 지정할 주문 테이블들이 모두 저장되어 있어야 한다.
- 주문 테이블들을 단체 지정에서 해제할 수 있다.
    - **예외** 이미 계산이 완료된 테이블은 해제할 수 없다.

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
