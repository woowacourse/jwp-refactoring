package kitchenpos.application;

import static kitchenpos.TableGroupFixtures.createTableGroup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import kitchenpos.OrderTableFixtures;
import kitchenpos.application.dto.request.TableGroupCreateRequest;
import kitchenpos.application.dto.response.TableGroupResponse;
import kitchenpos.dao.OrderRepository;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.dao.TableGroupRepository;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.support.ServiceTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class TableGroupServiceTest {

    private TableGroupService tableGroupService;
    private TableGroupRepository tableGroupRepository;
    private OrderTableRepository orderTableRepository;
    private OrderRepository orderRepository;

    @Autowired
    public TableGroupServiceTest(
            TableGroupService tableGroupService,
            TableGroupRepository tableGroupRepository,
            OrderTableRepository orderTableRepository,
            OrderRepository orderRepository
    ) {
        this.tableGroupService = tableGroupService;
        this.tableGroupRepository = tableGroupRepository;
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
    }

    @Test
    void create() {
        // given
        List<Long> orderTableIds = List.of(1L, 2L);
        TableGroupCreateRequest request = new TableGroupCreateRequest(orderTableIds);

        // when
        TableGroupResponse response = tableGroupService.create(request);

        // then
        assertThat(response.getId()).isNotNull();
    }

    @Test
    void createWithEmptyOrderTable() {
        // given
        TableGroupCreateRequest request = new TableGroupCreateRequest(new ArrayList<>());

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void createWithOneOrderTable() {
        // given
        TableGroupCreateRequest request = new TableGroupCreateRequest(List.of(1L));

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void createWithWrongOrderTable() {
        // given
        long wrongOrderTableId = 999L;
        TableGroupCreateRequest request = new TableGroupCreateRequest(List.of(1L, wrongOrderTableId));

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void createWithNotEmptyOrderTable() {
        // given
        OrderTable emptyOrderTable = orderTableRepository.save(new OrderTable(1L, null, 2, false));
        TableGroupCreateRequest request = new TableGroupCreateRequest(List.of(emptyOrderTable.getId(), 2L));

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void ungroup() {
        // given
        TableGroup savedTableGroup = tableGroupRepository.save(createTableGroup());

        // when & then
        assertThatCode(() -> tableGroupService.ungroup(savedTableGroup.getId()))
                .doesNotThrowAnyException();
    }

    @Test
    void ungroupWithCookingStatus(@Autowired EntityManager entityManager) {
        // given
        TableGroup savedTableGroup = tableGroupRepository.save(createTableGroup());
        int any = 0;
        OrderTable orderTable = savedTableGroup.getOrderTables().get(any);

        Order order = new Order(orderTable, OrderStatus.COOKING, LocalDateTime.now(), null);
        orderRepository.save(order);
        entityManager.flush();
        entityManager.clear();

        // when & then
        assertThatThrownBy(() -> tableGroupService.ungroup(savedTableGroup.getId()))
                .isInstanceOf(IllegalStateException.class);
    }
}
