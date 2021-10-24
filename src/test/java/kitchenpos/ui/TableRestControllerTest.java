package kitchenpos.ui;

import org.junit.jupiter.api.DisplayName;

@DisplayName("TableRestController 통합 테스트")
class TableRestControllerTest extends IntegrationTest {

//    @Autowired
//    private TableGroupRepository tableGroupRepository;
//
//    @Autowired
//    private OrderTableRepository orderTableRepository;
//
//    @Autowired
//    private OrderRepository orderRepository;
//
//    @DisplayName("create 메서드는 OrderTable을 저장 및 반환한다.")
//    @Test
//    void create_saves_and_returns_order_table() {
//        // given
//        OrderTable requestBody = new OrderTable();
//
//        // when, then
//        webTestClient.post()
//            .uri("/api/tables")
//            .contentType(MediaType.APPLICATION_JSON)
//            .accept(MediaType.APPLICATION_JSON)
//            .bodyValue(requestBody)
//            .exchange()
//            .expectStatus()
//            .isCreated()
//            .expectHeader()
//            .valueEquals("location", "/api/tables/9");
//    }
//
//    @DisplayName("list 메서드는 OrderTable을 목록을 반환한다.")
//    @Test
//    void list_returns_order_table_list() {
//        // given, when, then
//        webTestClient.get()
//            .uri("/api/tables")
//            .accept(MediaType.APPLICATION_JSON)
//            .exchange()
//            .expectStatus()
//            .isOk()
//            .expectBody(new ParameterizedTypeReference<List<OrderTable>>() {})
//            .value(response -> assertThat(response).hasSize(8));
//    }
//
//    @DisplayName("changeEmpty 메서드는 ID에 해당하는 OrderTable이 없으면 예외가 발생한다.")
//    @Test
//    void changeEmpty_order_table_not_found_exception_thrown() {
//        // given
//        OrderTable requestBody = new OrderTable();
//        requestBody.setEmpty(true);
//
//        // given, when, then
//        webTestClient.put()
//            .uri("/api/tables/31231/empty")
//            .contentType(MediaType.APPLICATION_JSON)
//            .accept(MediaType.APPLICATION_JSON)
//            .bodyValue(requestBody)
//            .exchange()
//            .expectStatus()
//            .isBadRequest()
//            .expectBody(String.class)
//            .value(response ->
//                assertThat(response).isEqualTo("OrderTable이 존재하지 않습니다.")
//            );
//    }
//
//    @DisplayName("changeEmpty 메서드는 OrderTable이 이미 특정 TableGroup에 속했으면 예외가 발생한다.")
//    @Test
//    void changeEmpty_order_table_has_group_exception_thrown() {
//        // given
//        TableGroup tableGroup = new TableGroup();
//        tableGroup.setCreatedDate(LocalDateTime.now());
//        TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);
//        OrderTable orderTable = new OrderTable();
//        orderTable.setEmpty(false);
//        orderTable.setTableGroupId(savedTableGroup.getId());
//        OrderTable requestBody = orderTableRepository.save(orderTable);
//
//        // given, when, then
//        webTestClient.put()
//            .uri("/api/tables/{id}/empty", requestBody.getId())
//            .contentType(MediaType.APPLICATION_JSON)
//            .accept(MediaType.APPLICATION_JSON)
//            .bodyValue(orderTable)
//            .exchange()
//            .expectStatus()
//            .isBadRequest()
//            .expectBody(String.class)
//            .value(response ->
//                assertThat(response).isEqualTo("OrderTable이 속한 TableGroup이 존재합니다.")
//            );
//    }
//
//    @DisplayName("changeEmpty 메서드는 OrderTable에 속한 Order가 조리중 혹은 식사중이라면 예외가 발생한다.")
//    @ParameterizedTest
//    @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
//    void changeEmpty_order_status_cooking_or_meal_exception_thrown(OrderStatus orderStatus) {
//        // given
//        OrderTable orderTable = new OrderTable();
//        orderTable.setEmpty(false);
//        OrderTable requestBody = orderTableRepository.save(orderTable);
//        Order order = new Order();
//        order.setOrderedTime(LocalDateTime.now());
//        order.setOrderStatus(orderStatus.name());
//        order.setOrderTableId(requestBody.getId());
//        orderRepository.save(order);
//
//        // when, then
//        webTestClient.put()
//            .uri("/api/tables/{id}/empty", requestBody.getId())
//            .contentType(MediaType.APPLICATION_JSON)
//            .accept(MediaType.APPLICATION_JSON)
//            .bodyValue(requestBody)
//            .exchange()
//            .expectStatus()
//            .isBadRequest()
//            .expectBody(String.class)
//            .value(response ->
//                assertThat(response).isEqualTo("OrderTable에 속한 Order 중 일부가 조리 혹은 식사 중입니다.")
//            );
//    }
//
//    @DisplayName("changeEmpty 메서드는 정상적인 경우 OrderTable의 상태를 변경하고 반환한다.")
//    @Test
//    void changeEmpty_valid_condition_table_status_changed() {
//        // given
//        TableGroup tableGroup = new TableGroup();
//        tableGroup.setCreatedDate(LocalDateTime.now());
//        OrderTable orderTable = new OrderTable();
//        orderTable.setEmpty(false);
//        OrderTable requestBody = orderTableRepository.save(orderTable);
//        Order order = new Order();
//        order.setOrderedTime(LocalDateTime.now());
//        order.setOrderStatus(OrderStatus.COMPLETION.name());
//        order.setOrderTableId(requestBody.getId());
//        orderRepository.save(order);
//
//        // when, then
//        webTestClient.put()
//            .uri("/api/tables/{id}/empty", requestBody.getId())
//            .contentType(MediaType.APPLICATION_JSON)
//            .accept(MediaType.APPLICATION_JSON)
//            .bodyValue(requestBody)
//            .exchange()
//            .expectStatus()
//            .isOk()
//            .expectBody(OrderTable.class)
//            .value(response -> assertThat(response.isEmpty()).isFalse());
//    }
//
//    @DisplayName("changeNumberOfGuest 메서드는 변경하려는 손님 숫자가 음수면 예외가 발생한다.")
//    @Test
//    void changeNumberOfGuest_number_of_guests_negative_exception_thrown() {
//        // given
//        OrderTable requestBodye = new OrderTable();
//        requestBodye.setNumberOfGuests(-1);
//
//        // when, then
//        webTestClient.put()
//            .uri("/api/tables/1/number-of-guests")
//            .contentType(MediaType.APPLICATION_JSON)
//            .accept(MediaType.APPLICATION_JSON)
//            .bodyValue(requestBodye)
//            .exchange()
//            .expectStatus()
//            .isBadRequest()
//            .expectBody(String.class)
//            .value(response ->
//                assertThat(response).isEqualTo("변경하려는 손님 수는 음수일 수 없습니다.")
//            );
//    }
//
//    @DisplayName("changeNumberOfGuest 메서드는 OrderTable을 조회할 수 없는 경우 예외가 발생한다.")
//    @Test
//    void changeNumberOfGuest_order_table_not_found_exception_thrown() {
//        // given
//        OrderTable requestBody = new OrderTable();
//        requestBody.setNumberOfGuests(0);
//
//        // when, then
//        webTestClient.put()
//            .uri("/api/tables/9999/number-of-guests")
//            .contentType(MediaType.APPLICATION_JSON)
//            .accept(MediaType.APPLICATION_JSON)
//            .bodyValue(requestBody)
//            .exchange()
//            .expectStatus()
//            .isBadRequest()
//            .expectBody(String.class)
//            .value(response ->
//                assertThat(response).isEqualTo("OrderTable이 존재하지 않습니다.")
//            );
//    }
//
//    @DisplayName("changeNumberOfGuest 메서드는 조회된 OrderTable이 비어있는 경우 예외가 발생한다.")
//    @Test
//    void changeNumberOfGuest_saved_order_table_is_empty_exception_thrown() {
//        // given
//        OrderTable requestBody = new OrderTable();
//        requestBody.setId(1L);
//        requestBody.setEmpty(true);
//        orderTableRepository.save(requestBody);
//
//        // when, then
//        webTestClient.put()
//            .uri("/api/tables/1/number-of-guests")
//            .contentType(MediaType.APPLICATION_JSON)
//            .accept(MediaType.APPLICATION_JSON)
//            .bodyValue(requestBody)
//            .exchange()
//            .expectStatus()
//            .isBadRequest()
//            .expectBody(String.class)
//            .value(response ->
//                assertThat(response).isEqualTo("OrderTable이 비어있는 상태입니다.")
//            );
//    }
//
//    @DisplayName("changeNumberOfGuest 메서드는 정상적인 경우 손님 수를 변경한다.")
//    @Test
//    void changeNumberOfGuest_valid_condition_number_of_guests_changed() {
//        // given
//        OrderTable orderTable = new OrderTable();
//        orderTable.setId(1L);
//        orderTable.setEmpty(false);
//        orderTableRepository.save(orderTable);
//        OrderTable requestBody = new OrderTable();
//        requestBody.setNumberOfGuests(13);
//
//        // when, then
//        webTestClient.put()
//            .uri("/api/tables/1/number-of-guests")
//            .contentType(MediaType.APPLICATION_JSON)
//            .accept(MediaType.APPLICATION_JSON)
//            .bodyValue(requestBody)
//            .exchange()
//            .expectStatus()
//            .isOk()
//            .expectBody(OrderTable.class)
//            .value(response -> assertThat(response.getNumberOfGuests()).isEqualTo(13));
//    }
}
