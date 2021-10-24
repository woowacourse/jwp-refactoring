package kitchenpos.ui;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Collections;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.domain.repository.TableGroupRepository;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.ui.dto.request.TableGroupRequest;
import kitchenpos.ui.dto.response.TableGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

@DisplayName("TableGroupRestController 통합 테스트")
class TableGroupRestControllerTest extends IntegrationTest {

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private OrderRepository orderRepository;

    @DisplayName("create 메서드는 TableGroup의 OrderTable 컬렉션 크기가 2 미만이면 예외가 발생한다.")
    @Test
    void create_order_table_smaller_than_two_exception_thrown() {
        // given
        TableGroupRequest request = new TableGroupRequest(Collections.emptyList());

        // when, then
        webTestClient.post()
            .uri("/api/table-groups")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .exchange()
            .expectStatus()
            .isBadRequest()
            .expectBody(String.class)
            .value(response ->
                assertThat(response).isEqualTo("TableGroup에 속한 OrderTable은 최소 2개 이상이어야합니다.")
            );
    }

    @DisplayName("create 메서드는 TableGroup의 OrderTable 컬렉션이 DB에 저장되어있지 않았다면 예외가 발생한다.")
    @Test
    void create_order_table_not_persisted_exception_thrown() {
        // given
        TableGroupRequest request = new TableGroupRequest(Arrays.asList(399L, 999L));

        // when, then
        webTestClient.post()
            .uri("/api/table-groups")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .exchange()
            .expectStatus()
            .isBadRequest()
            .expectBody(String.class)
            .value(response ->
                assertThat(response).isEqualTo("요청한 OrderTable이 저장되어있지 않습니다.")
            );
    }

    @DisplayName("create 메서드는 TableGroup의 OrderTable 중 일부가 비어있지 않다면 예외가 발생한다.")
    @Test
    void create_order_table_not_empty_exception_thrown() {
        // given
        TableGroupRequest request = new TableGroupRequest(Arrays.asList(7L, 8L));

        // when, then
        webTestClient.post()
            .uri("/api/table-groups")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .exchange()
            .expectStatus()
            .isBadRequest()
            .expectBody(String.class)
            .value(response ->
                assertThat(response).isEqualTo("OrderTable이 비어있지 않거나 특정 TableGroup에 이미 속해있습니다.")
            );
    }

    @DisplayName("create 메서드는 TableGroup의 OrderTable 중 일부가 이미 특정 TableGroup에 속해있다면 예외가 발생한다.")
    @Test
    void create_order_table_already_in_table_group_exception_thrown() {
        // given
        TableGroupRequest request = new TableGroupRequest(Arrays.asList(9L, 10L));
        OrderTable orderTable1 = new OrderTable(10, true);
        OrderTable orderTable2 = new OrderTable(10, true);
        TableGroup tableGroup = tableGroupRepository.save(new TableGroup());
        orderTable1.toTableGroup(tableGroup);
        orderTableRepository.saveAll(Arrays.asList(orderTable1, orderTable2));

        // when, then
        webTestClient.post()
            .uri("/api/table-groups")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .exchange()
            .expectStatus()
            .isBadRequest()
            .expectBody(String.class)
            .value(response ->
                assertThat(response).isEqualTo("OrderTable이 비어있지 않거나 특정 TableGroup에 이미 속해있습니다.")
            );
    }

    @DisplayName("create 메서드는 정상적인 경우 TableGroup을 저장하고 반환한다.")
    @Test
    void create_valid_condition_returns_table_group() {
        // given
        TableGroupRequest request = new TableGroupRequest(Arrays.asList(9L, 10L));
        OrderTable orderTable1 = new OrderTable(10, true);
        OrderTable orderTable2 = new OrderTable(10, true);
        orderTableRepository.saveAll(Arrays.asList(orderTable1, orderTable2));

        // when, then
        webTestClient.post()
            .uri("/api/table-groups")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .exchange()
            .expectStatus()
            .isCreated()
            .expectHeader()
            .valueEquals("location", "/api/table-groups/1")
            .expectBody(TableGroupResponse.class)
            .value(response -> assertThat(response.getId()).isOne());
    }

    @DisplayName("ungroup 메서드는 TableGroup에 속한 OrderTable에 속한 Order들이, 조리 중이거나 식사 중이라면 예외가 발생한다.")
    @Test
    void ungroup_order_cooking_or_meal_exception_thrown() {
        // given
        create_valid_condition_returns_table_group();
        OrderTable orderTable = orderTableRepository.findById(9L).get();
        Order order = new Order(orderTable, OrderStatus.COOKING);
        orderRepository.save(order);

        // when, then
        webTestClient.delete()
            .uri("/api/table-groups/1")
            .exchange()
            .expectStatus()
            .isBadRequest()
            .expectBody(String.class)
            .value(response ->
                assertThat(response).isEqualTo("OrderTable에 속한 Order 중 일부가 조리 혹은 식사 중입니다.")
            );
    }

    @DisplayName("ungroup 메서드는 정상적인 경우 group을 정상 해지한다.")
    @Test
    void ungroup_valid_condition_group_deleted() {
        // given
        create_valid_condition_returns_table_group();
        OrderTable orderTable = orderTableRepository.findById(9L).get();
        Order order = new Order(orderTable, OrderStatus.COMPLETION);
        orderRepository.save(order);

        // when, then
        webTestClient.delete()
            .uri("/api/table-groups/1")
            .exchange()
            .expectStatus()
            .isNoContent();
    }
}
