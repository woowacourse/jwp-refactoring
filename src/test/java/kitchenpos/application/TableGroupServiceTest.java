package kitchenpos.application;

import static java.util.Collections.emptyList;
import static kitchenpos.fixture.OrderFixture.createOrder;
import static kitchenpos.fixture.OrderTableFixture.createOrderTable;
import static kitchenpos.fixture.TableGroupFixture.createTableGroup;
import static kitchenpos.fixture.TableGroupFixture.createTableGroupRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

public class TableGroupServiceTest extends AbstractServiceTest {
    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private TableGroupDao tableGroupDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private TableGroupService tableGroupService;

    @DisplayName("단체 지정을 할 수 있다.")
    @Test
    void create() {
        List<OrderTable> orderTables = Arrays.asList(
            orderTableDao.save(createOrderTable(null, true, 0, null)),
            orderTableDao.save(createOrderTable(null, true, 0, null))
        );
        TableGroup tableGroup = createTableGroupRequest(orderTables);

        TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        assertAll(
            () -> assertThat(savedTableGroup.getId()).isNotNull(),
            () -> assertThat(savedTableGroup).isEqualToIgnoringGivenFields(savedTableGroup, "id"),
            () -> assertThat(savedTableGroup.getOrderTables())
                .extracting(OrderTable::getTableGroupId).containsOnly(savedTableGroup.getId())
        );
    }

    @DisplayName("2개 이상의 빈 테이블이 아닌 경우 단체 지정할 수 없다.")
    @Test
    void create_throws_exception() {
        List<OrderTable> orderTables = Arrays.asList(
            orderTableDao.save(createOrderTable(null, false, 0, null)),
            orderTableDao.save(createOrderTable(null, false, 0, null))
        );
        TableGroup tableGroup = createTableGroupRequest(orderTables);

        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> tableGroupService.create(tableGroup));
    }

    @DisplayName("단체 지정을 해제할 수 있다.")
    @Test
    void ungroup() {
        List<OrderTable> orderTables = Arrays.asList(
            orderTableDao.save(createOrderTable(null, true, 0, null)),
            orderTableDao.save(createOrderTable(null, true, 0, null))
        );
        TableGroup tableGroup = tableGroupDao
            .save(createTableGroup(null, LocalDateTime.now(), orderTables));

        tableGroupService.ungroup(tableGroup.getId());

        List<OrderTable> foundOrderTables = orderTableDao.findAllByTableGroupId(tableGroup.getId());

        assertThat(foundOrderTables).isEmpty();
    }

    @DisplayName("주문 상태가 조리 또는 식사인 경우 단체 지정을 해제할 수 없다.")
    @ParameterizedTest
    @MethodSource("provideOrderStatus")
    void ungroup_throws_exception(OrderStatus orderStatus) {
        List<OrderTable> orderTables = Arrays.asList(
            orderTableDao.save(createOrderTable(null, true, 0, null)),
            orderTableDao.save(createOrderTable(null, true, 0, null))
        );
        TableGroup tableGroup = tableGroupService
            .create(createTableGroupRequest(orderTables));

        Order order = orderDao
            .save(createOrder(null, LocalDateTime.now(), emptyList(), orderStatus,
                orderTables.get(0).getId()));

        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()));
    }

    private static Stream<Arguments> provideOrderStatus() {
        return Stream.of(
            Arguments.of(OrderStatus.COOKING),
            Arguments.of(OrderStatus.MEAL)
        );
    }
}
