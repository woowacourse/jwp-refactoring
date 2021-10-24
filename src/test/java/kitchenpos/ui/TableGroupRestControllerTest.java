package kitchenpos.ui;

import org.junit.jupiter.api.DisplayName;

@DisplayName("TableGroupRestController 통합 테스트")
class TableGroupRestControllerTest extends IntegrationTest {

//    @Autowired
//    private OrderTableRepository orderTableRepository;
//
//    @Autowired
//    private TableGroupRepository tableGroupRepository;
//
//    @Autowired
//    private OrderRepository orderRepository;
//
//    @DisplayName("create 메서드는 TableGroup의 OrderTable 컬렉션 크기가 2 미만이면 예외가 발생한다.")
//    @Test
//    void create_order_table_smaller_than_two_exception_thrown() {
//        // given
//        TableGroup requestBody = new TableGroup();
//        requestBody.setOrderTables(Collections.emptyList());
//        OrderTable orderTable = new OrderTable();
//        requestBody.setOrderTables(Arrays.asList(orderTable));
//
//        // when, then
//        webTestClient.post()
//            .uri("/api/table-groups")
//            .contentType(MediaType.APPLICATION_JSON)
//            .accept(MediaType.APPLICATION_JSON)
//            .bodyValue(requestBody)
//            .exchange()
//            .expectStatus()
//            .isBadRequest()
//            .expectBody(String.class)
//            .value(response ->
//                assertThat(response).isEqualTo("TableGroup에 속한 OrderTable은 최소 2개 이상이어야합니다.")
//            );
//    }
//
//    @DisplayName("create 메서드는 TableGroup의 OrderTable 컬렉션이 DB에 저장되어있지 않았다면 예외가 발생한다.")
//    @Test
//    void create_order_table_not_persisted_exception_thrown() {
//        // given
//        TableGroup requestBody = new TableGroup();
//        OrderTable orderTable = new OrderTable();
//        OrderTable orderTable2 = new OrderTable();;
//        requestBody.setOrderTables(Arrays.asList(orderTable, orderTable2));
//        orderTable.setId(399L);
//        orderTable2.setId(999L);
//
//        // when, then
//        webTestClient.post()
//            .uri("/api/table-groups")
//            .contentType(MediaType.APPLICATION_JSON)
//            .accept(MediaType.APPLICATION_JSON)
//            .bodyValue(requestBody)
//            .exchange()
//            .expectStatus()
//            .isBadRequest()
//            .expectBody(String.class)
//            .value(response ->
//                assertThat(response).isEqualTo("요청한 OrderTable이 저장되어있지 않습니다.")
//            );
//    }
//
//    @DisplayName("create 메서드는 TableGroup의 OrderTable 중 일부가 비어있지 않다면 예외가 발생한다.")
//    @Test
//    void create_order_table_not_empty_exception_thrown() {
//        // given
//        TableGroup requestBody = new TableGroup();
//        OrderTable orderTable = new OrderTable();
//        OrderTable orderTable2 = new OrderTable();;
//        List<OrderTable> orderTables = Arrays.asList(orderTable, orderTable2);
//        requestBody.setOrderTables(orderTables);
//        orderTable.setId(1L);
//        orderTable2.setId(2L);
//        orderTable.setEmpty(true);
//        orderTable2.setEmpty(false);
//        orderTableRepository.save(orderTable);
//        orderTableRepository.save(orderTable2);
//
//        // when, then
//        webTestClient.post()
//            .uri("/api/table-groups")
//            .contentType(MediaType.APPLICATION_JSON)
//            .accept(MediaType.APPLICATION_JSON)
//            .bodyValue(requestBody)
//            .exchange()
//            .expectStatus()
//            .isBadRequest()
//            .expectBody(String.class)
//            .value(response ->
//                assertThat(response).isEqualTo("OrderTable 일부가 비어있지 않거나 특정 TableGroup에 이미 속해있습니다.")
//            );
//    }
//
//    @DisplayName("create 메서드는 TableGroup의 OrderTable 중 일부가 이미 특정 TableGroup에 속해있다면 예외가 발생한다.")
//    @Test
//    void create_order_table_already_in_table_group_exception_thrown() {
//        // given
//        TableGroup requestBody = new TableGroup();
//        OrderTable orderTable = new OrderTable();
//        OrderTable orderTable2 = new OrderTable();
//        TableGroup tableGroup = new TableGroup();
//        List<OrderTable> orderTables = Arrays.asList(orderTable, orderTable2);
//        requestBody.setOrderTables(orderTables);
//        orderTable.setId(1L);
//        orderTable2.setId(2L);
//        orderTable.setEmpty(true);
//        orderTable2.setEmpty(true);
//        tableGroup.setCreatedDate(LocalDateTime.now());
//        TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);
//        orderTable2.setTableGroupId(savedTableGroup.getId());
//        orderTableRepository.save(orderTable);
//        orderTableRepository.save(orderTable2);
//
//        // when, then
//        webTestClient.post()
//            .uri("/api/table-groups")
//            .contentType(MediaType.APPLICATION_JSON)
//            .accept(MediaType.APPLICATION_JSON)
//            .bodyValue(requestBody)
//            .exchange()
//            .expectStatus()
//            .isBadRequest()
//            .expectBody(String.class)
//            .value(response ->
//                assertThat(response).isEqualTo("OrderTable 일부가 비어있지 않거나 특정 TableGroup에 이미 속해있습니다.")
//            );
//    }
//
//    @DisplayName("create 메서드는 정상적인 경우 TableGroup을 저장하고 반환한다.")
//    @Test
//    void create_valid_condition_returns_table_group() {
//        // given
//        TableGroup requestBody = new TableGroup();
//        OrderTable orderTable = new OrderTable();
//        OrderTable orderTable2 = new OrderTable();
//        List<OrderTable> orderTables = Arrays.asList(orderTable, orderTable2);
//        requestBody.setOrderTables(orderTables);
//        orderTable.setId(1L);
//        orderTable2.setId(2L);
//
//        // when, then
//        webTestClient.post()
//            .uri("/api/table-groups")
//            .contentType(MediaType.APPLICATION_JSON)
//            .accept(MediaType.APPLICATION_JSON)
//            .bodyValue(requestBody)
//            .exchange()
//            .expectStatus()
//            .isCreated()
//            .expectHeader()
//            .valueEquals("location", "/api/table-groups/1")
//            .expectBody(TableGroup.class)
//            .value(response -> assertThat(response.getId()).isOne());
//    }
//
//    @DisplayName("ungroup 메서드는 TableGroup에 속한 OrderTable에 속한 Order들이, 조리 중이거나 식사 중이라면 예외가 발생한다.")
//    @Test
//    void ungroup_order_cooking_or_meal_exception_thrown() {
//        // given
//        create_valid_condition_returns_table_group();
//        Order order = new Order();
//        order.setOrderTableId(1L);
//        order.setOrderedTime(LocalDateTime.now());
//        order.setOrderStatus(OrderStatus.COOKING.name());
//        orderRepository.save(order);
//
//        // when, then
//        webTestClient.delete()
//            .uri("/api/table-groups/1")
//            .exchange()
//            .expectStatus()
//            .isBadRequest()
//            .expectBody(String.class)
//            .value(response ->
//                assertThat(response).isEqualTo("TableGroup에 속한 Order 중 일부가 조리중이거나 식사 중입니다.")
//            );
//    }
//
//    @DisplayName("ungroup 메서드는 정상적인 경우 group을 정상 해지한다.")
//    @Test
//    void ungroup_valid_condition_group_deleted() {
//        // given
//        create_valid_condition_returns_table_group();
//        Order order = new Order();
//        order.setOrderTableId(1L);
//        order.setOrderedTime(LocalDateTime.now());
//        order.setOrderStatus(OrderStatus.COMPLETION.name());
//        orderRepository.save(order);
//
//        // when, then
//        webTestClient.delete()
//            .uri("/api/table-groups/1")
//            .exchange()
//            .expectStatus()
//            .isNoContent();
//    }
}
