package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kitchenpos.OrderFixtures;
import kitchenpos.TableGroupFixtures;
import kitchenpos.application.dto.request.OrderTableCreateRequest;
import kitchenpos.application.dto.response.OrderTableResponse;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.support.ServiceTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class TableServiceTest {

    private final TableService tableService;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    @Autowired
    public TableServiceTest(
            TableService tableService,
            OrderRepository orderRepository,
            OrderTableRepository orderTableRepository,
            TableGroupRepository tableGroupRepository
    ) {
        this.tableService = tableService;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Test
    void create() {
        // given
        OrderTableCreateRequest request = new OrderTableCreateRequest(3, true);
        // when
        OrderTableResponse createdOrderTable = tableService.create(request);
        // then
        assertThat(createdOrderTable.getId()).isNotNull();
    }

    @Test
    void list() {
        // given & when
        List<OrderTableResponse> orderTables = tableService.list();
        // then
        int defaultSize = 8;
        assertThat(orderTables).hasSize(defaultSize);
    }

    @Test
    void changeEmpty() {
        // given
        OrderTableResponse orderTable = tableService.create(new OrderTableCreateRequest(0, true));

        // when
        OrderTableResponse changedOrderTable = tableService.changeEmpty(orderTable.getId(), false);

        // then
        assertThat(changedOrderTable.isEmpty()).isFalse();
    }

    @Test
    void changeEmptyWithTableGroupId() {
        // given
        TableGroup tableGroup = tableGroupRepository.save(TableGroupFixtures.createTableGroup());
        OrderTable orderTable = orderTableRepository.save(new OrderTable(tableGroup, 3, false));

        // when & then
        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), true))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void changeEmptyWithCookingStatus() {
        // given
        Order order = OrderFixtures.createOrder();
        orderRepository.save(order);
        // when & then
        assertThatThrownBy(() -> tableService.changeEmpty(order.getOrderTable().getId(), false))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void changeNumberOfGuests() {
        // given
        OrderTableResponse orderTable = tableService.create(new OrderTableCreateRequest(2, false));

        // when
        OrderTableResponse changedOrderTable = tableService.changeNumberOfGuests(orderTable.getId(), 4);

        // then
        assertThat(changedOrderTable.getNumberOfGuests()).isEqualTo(4);
    }

    @Test
    void changeNumberOfGuestsWithNegativeNumber() {
        // given
        OrderTableResponse orderTable = tableService.create(new OrderTableCreateRequest(2, false));

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), -1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void changeNumberOfGuestsWithEmpty() {
        // given
        OrderTableResponse orderTable = tableService.create(new OrderTableCreateRequest(0, true));

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), 2))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void changeNumberOfGuestsWithInvalidOrderTableId() {
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(999L, 2))
                .hasCauseInstanceOf(IllegalArgumentException.class);
    }
}
