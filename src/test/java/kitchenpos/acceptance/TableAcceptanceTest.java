package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kitchenpos.acceptance.OrderAcceptanceTest.OrderLineItemForTest;
import kitchenpos.domain.OrderStatus;
import kitchenpos.dto.menu.MenuResponse;
import kitchenpos.dto.menugroup.MenuGroupResponse;
import kitchenpos.dto.order.OrderResponse;
import kitchenpos.dto.product.ProductResponse;
import kitchenpos.dto.table.TableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

class TableAcceptanceTest extends AcceptanceTest{

    /**
     * Feature: 테이블을 관리한다.
     *
     * Scenario: 테이블들을 준비한다.
     *           손님을 받은 테이블을 주문 가능 상태로 바꾸고, 해당 테이블의 손님 수를 입력한다.
     *           손님이 떠난 테이블을 주문 불가능 상태(empty)로 바꾸고,
     *           해당 테이블의 손님 수를 0명으로 한다.
     *
     * When 테이블들을 생성한다. Then 테이블들이 생성된다.
     *
     * When 모든 테이블을 조회한다. Then 모든 테이블이 조회된다.
     *
     * Given 테이블들이 생성되어있다.
     * When 어떤 테이블의 empty 여부를 true 에서 false 로 바꾼다.
     *      (주문 불가능한 테이블을 주문 가능한 상태로 바꾼다.)
     * Then 테이블의 empty 여부가 false 로 바뀐다.
     *
     * Given 테이블들이 생성되어있고, empty 여부가 false 인 테이블이 있다.
     * When empty=false 인 테이블 의 손님 수를 0 보다 큰 수로 바꾼다.
     * then 테이블의 손님 수가 바뀐다.
     *
     * Feature 손님이 나간 테이블 비우기
     *
     * Given 테이블들이 생성되어있고, empty 여부가 false 이면서 손님수가 0 이상인 테이블이 있다.
     * When 이 테이블의 손님 수를 0으로 바꾼다.
     * then 테이블의 손님 수가 0으로 바뀐다.
     *
     * When 어떤 테이블의 empty 여부를 true 에서 false 로 바꾼다.
     *      (주문 가능한 상태의 테이블을 주문 불가능한 상태로 바꾼다.)
     * Then 테이블의 empty 여부가 false 에서 true 로 바뀐다.
     */
    @Test
    @DisplayName("테이블을 관리한다.")
    void manageTable() {
        // 생성 (create)
        TableResponse tableA = createTable(5, false);
        TableResponse tableB = createTable(0, true);

        assertThat(tableA.getId()).isNotNull();
        assertThat(tableA.getTableGroupId()).isNull();
        assertThat(tableA.isEmpty()).isFalse();
        assertThat(tableA.getNumberOfGuests()).isEqualTo(5);

        assertThat(tableB.getId()).isNotNull();
        assertThat(tableB.getTableGroupId()).isNull();
        assertThat(tableB.isEmpty()).isTrue();
        assertThat(tableB.getNumberOfGuests()).isEqualTo(0);

        // 조회 (inquiry)
        List<TableResponse> tables = findTables();
        assertThat(doesTableExistInTables(tableA.getId(), tables)).isTrue();
        assertThat(doesTableExistInTables(tableB.getId(), tables)).isTrue();

        // change empty to false
        tableB = changeEmptyToFalse(tableB);
        assertThat(tableB.isEmpty()).isFalse();

        // change number of guests to n > 0
        tableB = changeNumberOfGuests(tableB, 2);
        assertThat(tableB.getNumberOfGuests()).isEqualTo(2);

        // change number of guests to 0
        tableB = changeNumberOfGuests(tableB, 0);
        assertThat(tableB.getNumberOfGuests()).isEqualTo(0);

        // change empty to true
        tableB = changeEmptyToTrue(tableB);
        assertThat(tableB.isEmpty()).isTrue();
    }

    /**
     * Feature: 테이블 생성에 있어서 테이블 id 와 테이블 그룹 id는 사용자가 지정할 수 없다.
     *
     * When 테이블 생성 요청을 하는데, 테이블 id 와 테이블 그룹 id를 지정해서 요청한다.
     * Then 테이블 id는 사용자 요청을 무시하고 자동으로 정해지고,
     *      테이블 그룹 id 도 사용자 요청을 무시하는데 항상 null 로만 생성된다.
     */
    @Test
    @DisplayName("테이블 생성시 테이블 id와 그룹 id 지정 시도")
    void createTable_ExceptionalCase() {
        TableResponse table = createTableWithTableId(12_000L, 10L, 0, true);

        assertThat(table.getId()).isNotNull();
        assertThat(table.getId()).isNotEqualTo(12_000L);
        assertThat(table.getTableGroupId()).isNull();
    }

    /**
     * Feature: 주문이 들어가서 식사까지 완료된 테이블을 빈 테이블로 바꾼다.
     *
     * Given 주문이 들어가서 식사까지 완료된 테이블이 있다.
     * When 이 테이블을 비워달라고 요청한다. Then 테이블이 비워진다.
     */
    @Test
    @DisplayName("테이블 비어있는지 표시하기 - 주문한 테이블이 식사까지 완료했을 경우")
    void changeEmptyOfTableInOrderCompletion() {
        // 1. given
        // 1.1. 영업준비
        TableResponse tableA = createTable(0, true);

        MenuGroupResponse 세트메뉴_그룹 = createMenuGroup("세트 메뉴");
        MenuGroupResponse 음료수_그룹 = createMenuGroup("음료수");

        List<ProductResponse> 치킨세트_구성상품들 = new ArrayList<>();

        치킨세트_구성상품들.add(createProduct("후라이드 치킨", 10_000));
        치킨세트_구성상품들.add(createProduct("감자 튀김", 4_000));
        치킨세트_구성상품들.add(createProduct("매운 치즈 떡볶이", 5_000));

        MenuResponse 치킨_세트 = createMenu("치킨 세트", 치킨세트_구성상품들, 16_000L, 세트메뉴_그룹.getId());

        ProductResponse beerProduct = createProduct("맥주 500CC", 4_000);
        MenuResponse 맥주 = createMenu("맥주 500cc", Collections.singletonList(beerProduct),
            beerProduct.getPrice().longValue(), 음료수_그룹.getId());

        // 1.2. tableA 에 손님이 앉아서 주문함
        changeEmptyToFalse(tableA);
        List<OrderLineItemForTest> orderLineItems = new ArrayList<>();

        orderLineItems.add(new OrderLineItemForTest(치킨_세트.getId(), 1));
        orderLineItems.add(new OrderLineItemForTest(맥주.getId(), 4));

        OrderResponse order = requestOrder(tableA, orderLineItems);

        // 1.3. 주문 상태를 completion 으로 바꾸기
        changeOrderStatusTo(OrderStatus.COMPLETION, order);

        // 2. when & then
        changeEmptyToTrue(tableA);
    }

    /**
     * Feature: 주문이 들어가있는 테이블은 empty 를 true 로 전환할 수 없다.
     *
     * Given 주문이 들어간 테이블이 있다.
     * When 요리중인 테이블의 empty 를 true 로 전환하길 시도한다.
     * Then 500 에러 응답이 온다.
     *
     * Given 주문이 들어간 뒤 음식이 제공되어 식사중인 테이블이 있다.
     * When 식사중인 테이블의 empty 를 true 로 전환하길 시도한다.
     * Then 500 에러 응답이 온다.
     */
    @Test
    @DisplayName("테이블 비어있는지 표시하기 - 주문이 들어가서 요리중인 테이블일 경우 예외처리")
    void changeEmptyOfTableInOrder() {
        // given
        // 1. 영업준비
        TableResponse tableA = createTable(0, true);

        MenuGroupResponse 세트메뉴_그룹 = createMenuGroup("세트 메뉴");
        MenuGroupResponse 음료수_그룹 = createMenuGroup("음료수");

        List<ProductResponse> 치킨세트_구성상품들 = new ArrayList<>();

        치킨세트_구성상품들.add(createProduct("후라이드 치킨", 10_000));
        치킨세트_구성상품들.add(createProduct("감자 튀김", 4_000));
        치킨세트_구성상품들.add(createProduct("매운 치즈 떡볶이", 5_000));

        MenuResponse 치킨_세트 = createMenu("치킨 세트", 치킨세트_구성상품들, 16_000L, 세트메뉴_그룹.getId());

        ProductResponse beerProduct = createProduct("맥주 500CC", 4_000);
        MenuResponse 맥주 = createMenu("맥주 500cc", Collections.singletonList(beerProduct),
            beerProduct.getPrice().longValue(), 음료수_그룹.getId());

        // 2. tableA 에 손님이 앉아서 주문함
        changeEmptyToFalse(tableA);
        List<OrderLineItemForTest> orderLineItems = new ArrayList<>();

        orderLineItems.add(new OrderLineItemForTest(치킨_세트.getId(), 1));
        orderLineItems.add(new OrderLineItemForTest(맥주.getId(), 4));

        OrderResponse order = requestOrder(tableA, orderLineItems);

        // when & then
        failToChangeEmptyToTrue(tableA);

        // given
        changeOrderStatusTo(OrderStatus.MEAL, order);

        // when & then
        failToChangeEmptyToTrue(tableA);
    }

    /**
     * Feature: empty=true 인 테이블의 손님 수를 0 보다 큰 수로 바꿀 수 없다.
     *
     * Given 손님이 없는(비어있는) 테이블이 있다.
     * When 이 테이블의 손님 수를 0보다 큰 수로 바꿔달라고 요청한다.
     * Then 500 에러 응답이 온다.
     */
    @Test
    @DisplayName("테이블 손님 수 변경 - 테이블이 empty=true 인 경우 예외처리")
    void changeNumberOfGuests_ExceptionalCase() {
        TableResponse tableA = createTable(0, true);
        failToChangeNumberOfGuests(tableA, 5);
    }

    private List<TableResponse> findTables() {
        return given()
            .when()
                .get("/api/tables")
            .then()
                .statusCode(HttpStatus.OK.value())
                .log().all()
                .extract()
                .jsonPath()
                .getList("", TableResponse.class);
    }

    private boolean doesTableExistInTables(Long tableId, List<TableResponse> tables) {
        return tables.stream()
            .anyMatch(orderTable -> orderTable.getId().equals(tableId));
    }

    private TableResponse changeEmptyToTrue(TableResponse table) {
        Map<String, Object> body = new HashMap<>();
        body.put("empty", true);

        return given()
                .body(body)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
            .when()
                .put("/api/tables/" + table.getId() + "/empty")
            .then()
                .statusCode(HttpStatus.OK.value())
                .extract().as(TableResponse.class);
    }

    private void failToChangeEmptyToTrue(TableResponse table) {
        Map<String, Object> body = new HashMap<>();
        body.put("empty", true);

        given()
            .body(body)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
        .when()
            .put("/api/tables/" + table.getId() + "/empty")
        .then()
            .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    private TableResponse createTableWithTableId(Long tableId, Long tableGroupId, int numberOfGuests,
        boolean empty) {
        Map<String, Object> body = new HashMap<>();

        body.put("id", tableId);
        body.put("tableGroupId", tableGroupId);
        body.put("numberOfGuests", numberOfGuests);
        body.put("empty", empty);

        return sendCreateTableRequest(body);
    }

    private void failToChangeNumberOfGuests(TableResponse table, int numberOfGuests) {
        Map<String, Object> body = new HashMap<>();
        body.put("numberOfGuests", numberOfGuests);

        given()
            .body(body)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
        .when()
            .put("/api/tables/" + table.getId() + "/number-of-guests")
        .then()
            .statusCode(HttpStatus.BAD_REQUEST.value());
    }
}
