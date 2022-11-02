package kitchenpos.application;

import static kitchenpos.OrderFixtures.*;
import static kitchenpos.OrderTableFixtures.*;
import static kitchenpos.TableGroupFixtures.createTableGroup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import kitchenpos.OrderFixtures;
import kitchenpos.OrderTableFixtures;
import kitchenpos.application.dto.request.TableGroupCreateRequest;
import kitchenpos.application.dto.request.TableGroupIdRequest;
import kitchenpos.application.dto.response.TableGroupResponse;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.tablegroup.TableGroup;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
import kitchenpos.support.ServiceTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class TableGroupServiceTest {

    private final TableGroupService tableGroupService;
    private final TableGroupRepository tableGroupRepository;
    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;

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
        List<TableGroupIdRequest> orderTableIds = List.of(new TableGroupIdRequest(1L), new TableGroupIdRequest(2L));
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
        List<TableGroupIdRequest> orderTableIds = List.of(new TableGroupIdRequest(1L));
        TableGroupCreateRequest request = new TableGroupCreateRequest(orderTableIds);

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void createWithWrongOrderTable() {
        // given
        long wrongOrderTableId = 999L;
        List<TableGroupIdRequest> orderTableIds = List.of(new TableGroupIdRequest(wrongOrderTableId));
        TableGroupCreateRequest request = new TableGroupCreateRequest(orderTableIds);

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(request))
                .hasCauseInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void createWithNotEmptyOrderTable() {
        // given
        OrderTable emptyOrderTable = orderTableRepository.save(new OrderTable(1L, null, 2, false));
        List<TableGroupIdRequest> orderTableIds = List.of(
                new TableGroupIdRequest(emptyOrderTable.getId()),
                new TableGroupIdRequest(2L)
        );
        TableGroupCreateRequest request = new TableGroupCreateRequest(orderTableIds);

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void ungroup() {
        // given
        OrderTable orderTableA = orderTableRepository.save(createOrderTable());
        OrderTable orderTableB = orderTableRepository.save(createOrderTable());
        TableGroup savedTableGroup = tableGroupRepository.save(
                createTableGroup(orderTableA.getId(), orderTableB.getId())
        );

        // when & then
        assertThatCode(() -> tableGroupService.ungroup(savedTableGroup.getId()))
                .doesNotThrowAnyException();
    }

    @Test
    void ungroupWithCookingStatus(@Autowired EntityManager entityManager) {
        // given
        OrderTable orderTableA = createOrderTable();
        orderTableRepository.save(orderTableA);
        orderTableA.addOrder(
                orderRepository.save(createOrder(orderTableA.getId(), OrderStatus.MEAL)).getId(),
                OrderStatus.MEAL
        );

        OrderTable orderTableB = createOrderTable();
        orderTableRepository.save(orderTableB);
        orderTableB.addOrder(
                orderRepository.save(createOrder(orderTableA.getId(), OrderStatus.COMPLETION)).getId(),
                OrderStatus.COMPLETION
        );

        TableGroup tableGroup = tableGroupRepository.save(
                createTableGroup(orderTableA.getId(), orderTableB.getId())
        );
        orderTableA.group(tableGroup.getId());
        orderTableB.group(tableGroup.getId());

        entityManager.flush();
        entityManager.clear();

        // when & then
        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
                .isInstanceOf(IllegalStateException.class);
    }
}
