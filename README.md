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

## 규칙
### 테스트 규칙
- Api 테스트는 클라이언트와의 요청, 응답을 검증하는 mock 테스트이다.

### 요청, 응답 DTO 규칙
- 요청과 응답 DTO는 ui 패키지에 위치한다.
- 클라이언트 <-> controller 간의 요청과 응답으로 간주한다.
- 만약 controller <-> service 간의 DTO가 필요하면 application 패키지에 DTO를 생성한다.

## API 수정 사항
_23/10/19_
### `GET /api/menus` 
API 응답에 menuProducts 안에 불필요한 menuId 제거
#### 변경 전


#### 변경 후

<br>

### `POST /api/table-groups` 
API 요청에 id를 객체로 감싸놓은 것을 언박싱
#### 변경 전
```json
{
  "orderTables": [
    {
      "id": 1
    },
    {
      "id": 2
    }
  ]
}
```
#### 변경 후
```json
{
  "orderTableIds": [1, 2]
}
```

<br>

### `GET /api/orders`
API 응답에 orderLineItems 안에 불필요한 orderId 제거
#### 변경 전


#### 변경 후

<br>

### `PUT /api/orders/{id}/order-status`
API 응답에 orderLineItems 안에 불필요한 orderId 제거
#### 변경 전


#### 변경 후
