package kitchenpos.application;

import kitchenpos.application.request.tablegroup.OrderTableIdRequest;
import kitchenpos.application.request.tablegroup.TableGroupRequest;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static kitchenpos.fixture.OrderFixture.newOrder;
import static kitchenpos.fixture.OrderTableFixture.newOrderTable;
import static kitchenpos.fixture.TableGroupFixture.newTableGroup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
@SpringBootTest
class TableGroupServiceTest {

    private static final int ORDER_TABLE_COUNT_LIMIT = 2;

    private final TableGroupService tableGroupService;
    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;
    private final TableGroupDao tableGroupDao;

    @Autowired
    public TableGroupServiceTest(final TableGroupService tableGroupService, final OrderDao orderDao,
                                 final OrderTableDao orderTableDao, final TableGroupDao tableGroupDao) {
        this.tableGroupService = tableGroupService;
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
        this.tableGroupDao = tableGroupDao;
    }

    @Nested
    @ServiceTest
    class CreateTest {

        @DisplayName("단체 지정을 한다")
        @Test
        void create() {
            orderTableDao.save(newOrderTable(null, 1, true));
            orderTableDao.save(newOrderTable(null, 1, true));

            final var request = new TableGroupRequest(new OrderTableIdRequest(1L), new OrderTableIdRequest(2L));
            final var actual = tableGroupService.create(request);

            assertThat(actual.getId()).isPositive();
        }

        @DisplayName("2개 이상의 주문 테이블을 지정해야 한다")
        @Test
        void createWithEmptyOrSingleOrderTable() {
            orderTableDao.save(newOrderTable(null, 1, true));
            orderTableDao.save(newOrderTable(null, 1, true));
            final var request = new TableGroupRequest(new OrderTableIdRequest(1L));

            assertThatThrownBy(() -> tableGroupService.create(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("2개 이상의 주문 테이블을 지정해야 합니다.");
        }

        @DisplayName("주문 테이블은 중복되지 않아야 한다")
        @Test
        void createWithDuplicatedOrderTables() {
            orderTableDao.save(newOrderTable(null, 1, true));
            orderTableDao.save(newOrderTable(null, 1, true));

            final var request = new TableGroupRequest(new OrderTableIdRequest(1L),new OrderTableIdRequest(1L));

            assertThatThrownBy(() -> tableGroupService.create(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("중복되는 주문 테이블이 있습니다.");
        }

        @DisplayName("비어있는 주문 테이블이어야 한다")
        @Test
        void createWithNonEmptyOrderTable() {
            orderTableDao.save(newOrderTable(null, 1, false));
            orderTableDao.save(newOrderTable(null, 1, true));

            final var request = new TableGroupRequest(new OrderTableIdRequest(1L), new OrderTableIdRequest(2L));

            assertThatThrownBy(() -> tableGroupService.create(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("비어있지 않은 주문 테이블이 존재합니다.");
        }

        @DisplayName("단체 지정되지 않은 주문 테이블이어야 한다")
        @Test
        void createWithAlreadyGroupAssignedOrderTable() {
            orderTableDao.save(newOrderTable(1L, 1, true));
            orderTableDao.save(newOrderTable(null, 1, true));

            final var request = new TableGroupRequest(new OrderTableIdRequest(1L), new OrderTableIdRequest(2L));

            assertThatThrownBy(() -> tableGroupService.create(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("비어있지 않은 주문 테이블이 존재합니다.");
        }
    }

    private List<OrderTable> saveOrderTableAsTimes(final int times) {
        return Collections.nCopies(times, newOrderTable(null, 1, true))
                .stream()
                .map(orderTableDao::save)
                .collect(Collectors.toUnmodifiableList());
    }

    @Nested
    @ServiceTest
    class UngroupTest {

        @DisplayName("단체 지정을 해제한다")
        @Test
        void ungroup() {
            final var expected = saveOrderTableAsTimes(ORDER_TABLE_COUNT_LIMIT);
            final var savedTableGroup = tableGroupDao.save(newTableGroup(LocalDateTime.now(), expected));

            final var savedOrderTableIds = expected.stream()
                    .map(OrderTable::getId)
                    .collect(Collectors.toUnmodifiableList());

            savedOrderTableIds.forEach(orderTableId -> orderDao.save(
                    newOrder(orderTableId, OrderStatus.COMPLETION, LocalDateTime.now()))
            );

            tableGroupService.ungroup(savedTableGroup.getId());

            final var actual = orderTableDao.findAllByIdIn(savedOrderTableIds);
            assertThat(actual).hasSize(expected.size());
        }

        @DisplayName("지정된 주문 테이블의 모든 계산이 완료되어 있어야 한다")
        @ParameterizedTest
        @ValueSource(strings = {"COOKING", "MEAL"})
        void ungroupWithUnreadyOrderTable(final OrderStatus orderStatus) {
            final var tableGroupId = tableGroupDao.save(newTableGroup()).getId();
            orderTableDao.save(newOrderTable(tableGroupId, 1, true));
            orderTableDao.save(newOrderTable(tableGroupId, 1, true));
            orderDao.save(newOrder(tableGroupId, orderStatus, LocalDateTime.now()));

            assertThatThrownBy(() -> tableGroupService.ungroup(tableGroupId))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("계산이 완료되지 않은 테이블이 존재합니다.");
        }
    }
}
