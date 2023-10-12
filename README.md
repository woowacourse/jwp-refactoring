# 키친포스

## 요구 사항

- [x] 상품
  - 상품을 등록한다
    - 가격은 비워둘 수 없다
    - 가격은 0보다 작아선 안된다
    - 저장된 상품을 반환한다
      - Id를 포함한다
  - 모든 상품들을 가져온다

- [x] 메뉴
  - 메뉴를 생성한다
    - 가격은 비워둘 수 없다
    - 가격은 0원보다 작아선 안된다
    - 기존 `메뉴 상품`을 사용해야 한다
    - 메뉴 상품들의 가격 총합은 상품들의 원래 가격보다 커선 안된다
    - 기존 메뉴 그룹을 사용해야 한다
    - 저장한 메뉴를 반환한다
      - Id를 포함한다
  - 모든 메뉴들을 가져온다

- [x] 메뉴 그룹(분류)
  - `메뉴 그룹`을 등록한다
  - 모든 `메뉴 그룹`들을 가져온다

- [x] 테이블
  - 테이블을 등록한다
    - 테이블 id, 테이블 그룹 id를 직접 지정할 수 없다
  - 모든 테이블을 가져온다
  - 테이블이 비었는지 여부를 변경한다
    - 기존 테이블이어야 한다
    - 테이블이 조리중이거나, 식사중이면 안된다
    - `테이블 그룹`이 없어야 한다
    - 테이블을 비우거나 채운다.
    - 저장한 테이블을 반환한다
  - 손님 수를 변경한다
    - 손님 수는 0 이상이어야 한다
    - 기존 테이블을 사용해야 한다
    - 빈 테이블이 아니어야 한다
    - 저장한 테이블을 반환한다

- [x] 테이블 그룹
  - `테이블 그룹`을 등록한다
    - 두 테이블 이상이어야 한다
    - 기존 테이블을 사용해야 한다
    - 테이블들은 비어있어야 한다
    - `테이블 그룹`의 생성 시각을 기록한다
    - 테이블들의 그룹을 지정한다
    - 테이블들을 이용중으로 바꾼다
    - 저장한 `테이블 그룹`을 반환한다
  - 그룹을 해제한다
    - 조리중이거나 식사중이면 안된다
    - 테이블들의 `테이블 그룹`을 제거한다
    - 테이블들을 비운다.

- 주문
  - 메뉴를 주문한다
    - 주문 항목이 있어야 한다
    - 주문 항목의 메뉴는 기존 메뉴여야 한다
    - 주문 Id를 직접 지정할 수 없다
    - 기존 테이블을 사용해야 한다
    - 빈 테이블이면 안된다
    - 주문한 테이블을 지정한다
    - 조리중으로 바꾼다
    - 주문 시각을 기록한다
    - 저장한 주문을 반환한다
  - 모든 주문들을 가져온다
  - 주문 상태를 바꾼다
    - 기존 주문을 사용해야 한다 
    - 주문이 `계산 완료` 상태이면 안된다
    - 변경한 주문을 반환한다

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

## 용어 사전 (자세히)

- MenuGroup
  - 메뉴의 분류. 한식, 일식, 중식 등
- OrderLineItem
  - 주방(조리 라인)에서 처리해야 할 항목들
  - Line은 처리 창구(조리 라인)의 의미로 쓰인 것으로 보인다.
