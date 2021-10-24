package kitchenpos.ui;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.dao.OrderRepository;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.dao.TableGroupRepository;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.ui.dto.request.OrderTableRequest;
import kitchenpos.ui.dto.response.OrderTableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;

@DisplayName("TableRestController 통합 테스트")
class TableRestControllerTest extends IntegrationTest {

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private OrderRepository orderRepository;

    @DisplayName("create 메서드는 OrderTable을 저장 및 반환한다.")
    @Test
    void create_saves_and_returns_order_table() {
        // given
        OrderTableRequest request = new OrderTableRequest(10, true);

        // when, then
        webTestClient.post()
            .uri("/api/tables")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .exchange()
            .expectStatus()
            .isCreated()
            .expectHeader()
            .valueEquals("location", "/api/tables/9");
    }

    @DisplayName("list 메서드는 OrderTable을 목록을 반환한다.")
    @Test
    void list_returns_order_table_list() {
        // given, when, then
        webTestClient.get()
            .uri("/api/tables")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody(new ParameterizedTypeReference<List<OrderTableResponse>>() {})
            .value(response -> assertThat(response).hasSize(8));
    }

    @DisplayName("changeEmpty 메서드는 ID에 해당하는 OrderTable이 없으면 예외가 발생한다.")
    @Test
    void changeEmpty_order_table_not_found_exception_thrown() {
        // given
        OrderTableRequest request = new OrderTableRequest(10, true);

        // given, when, then
        webTestClient.put()
            .uri("/api/tables/31231/empty")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .exchange()
            .expectStatus()
            .isBadRequest()
            .expectBody(String.class)
            .value(response ->
                assertThat(response).isEqualTo("OrderTable이 존재하지 않습니다.")
            );
    }

    @DisplayName("changeEmpty 메서드는 OrderTable이 이미 특정 TableGroup에 속했으면 예외가 발생한다.")
    @Test
    void changeEmpty_order_table_has_group_exception_thrown() {
        // given
        TableGroup savedTableGroup = tableGroupRepository.save(new TableGroup());
        OrderTable orderTable = new OrderTable(savedTableGroup, 10, false);
        OrderTable savedOrderTable = orderTableRepository.save(orderTable);
        OrderTableRequest request = new OrderTableRequest(10, true);

        // given, when, then
        webTestClient.put()
            .uri("/api/tables/{id}/empty", savedOrderTable.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .exchange()
            .expectStatus()
            .isBadRequest()
            .expectBody(String.class)
            .value(response ->
                assertThat(response).isEqualTo("OrderTable이 속한 TableGroup이 존재합니다.")
            );
    }

    @DisplayName("changeEmpty 메서드는 OrderTable에 속한 Order가 조리중 혹은 식사중이라면 예외가 발생한다.")
    @ParameterizedTest
    @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
    void changeEmpty_order_status_cooking_or_meal_exception_thrown(OrderStatus orderStatus) {
        // given
        OrderTable orderTable = new OrderTable(10, false);
        OrderTable savedOrderTable = orderTableRepository.save(orderTable);
        Order order = new Order(orderTable, orderStatus);
        orderRepository.save(order);
        OrderTableRequest request = new OrderTableRequest(10, true);

        // when, then
        webTestClient.put()
            .uri("/api/tables/{id}/empty", savedOrderTable.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .exchange()
            .expectStatus()
            .isBadRequest()
            .expectBody(String.class)
            .value(response ->
                assertThat(response).isEqualTo("OrderTable에 속한 Order 중 일부가 조리 혹은 식사 중입니다.")
            );
    }

    @DisplayName("changeEmpty 메서드는 정상적인 경우 OrderTable의 상태를 변경하고 반환한다.")
    @Test
    void changeEmpty_valid_condition_table_status_changed() {
        // given
        OrderTable orderTable = new OrderTable(10, false);
        OrderTable savedOrderTable = orderTableRepository.save(orderTable);
        Order order = new Order(orderTable, OrderStatus.COMPLETION);
        orderRepository.save(order);
        OrderTableRequest request = new OrderTableRequest(10, true);

        // when, then
        webTestClient.put()
            .uri("/api/tables/{id}/empty", savedOrderTable.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody(OrderTableResponse.class)
            .value(response -> assertThat(response.isEmpty()).isTrue());
    }

    @DisplayName("changeNumberOfGuest 메서드는 변경하려는 손님 숫자가 음수면 예외가 발생한다.")
    @Test
    void changeNumberOfGuest_number_of_guests_negative_exception_thrown() {
        // given
        OrderTableRequest request = new OrderTableRequest(-1, true);

        // when, then
        webTestClient.put()
            .uri("/api/tables/8/number-of-guests")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .exchange()
            .expectStatus()
            .isBadRequest()
            .expectBody(String.class)
            .value(response ->
                assertThat(response).isEqualTo("변경하려는 손님 수는 음수일 수 없습니다.")
            );
    }

    @DisplayName("changeNumberOfGuest 메서드는 OrderTable을 조회할 수 없는 경우 예외가 발생한다.")
    @Test
    void changeNumberOfGuest_order_table_not_found_exception_thrown() {
        // given
        OrderTableRequest request = new OrderTableRequest(1, true);

        // when, then
        webTestClient.put()
            .uri("/api/tables/9999/number-of-guests")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .exchange()
            .expectStatus()
            .isBadRequest()
            .expectBody(String.class)
            .value(response ->
                assertThat(response).isEqualTo("OrderTable이 존재하지 않습니다.")
            );
    }

    @DisplayName("changeNumberOfGuest 메서드는 조회된 OrderTable이 비어있는 경우 예외가 발생한다.")
    @Test
    void changeNumberOfGuest_saved_order_table_is_empty_exception_thrown() {
        // given
        OrderTableRequest request = new OrderTableRequest(1, true);

        // when, then
        webTestClient.put()
            .uri("/api/tables/1/number-of-guests")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .exchange()
            .expectStatus()
            .isBadRequest()
            .expectBody(String.class)
            .value(response ->
                assertThat(response).isEqualTo("OrderTable이 비어있는 상태입니다.")
            );
    }

    @DisplayName("changeNumberOfGuest 메서드는 정상적인 경우 손님 수를 변경한다.")
    @Test
    void changeNumberOfGuest_valid_condition_number_of_guests_changed() {
        // given
        OrderTableRequest request = new OrderTableRequest(13, true);

        // when, then
        webTestClient.put()
            .uri("/api/tables/8/number-of-guests")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody(OrderTableResponse.class)
            .value(response -> assertThat(response.getNumberOfGuests()).isEqualTo(13));
    }
}
