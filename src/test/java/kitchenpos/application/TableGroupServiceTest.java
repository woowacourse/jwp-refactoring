package kitchenpos.application;

import static kitchenpos.fixture.TableFixture.비어있는_주문_테이블;
import static kitchenpos.fixture.TableFixture.전체_주문_테이블;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.OrderDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.repository.OrderTableRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class TableGroupServiceTest extends ServiceIntegrationTest {

    @Autowired
    private TableGroupService tableGroupService;
    @Autowired
    private TableService tableService;
    @Autowired
    private OrderTableRepository orderTableRepository;
    @Autowired
    private OrderDao orderDao;

    @Nested
    @DisplayName("Table Group을 추가한다.")
    class Create {

        @Test
        @DisplayName("테이블 그룹을 정상적으로 생성한다.")
        void success() {
            //given
            final TableGroup tableGroup = new TableGroup();

            //when
            final TableGroup savedTableGroup = saveTableGroupSuccessfully(tableGroup, 전체_주문_테이블());

            //then
            tableGroup.getOrderTables().forEach(orderTable -> {
                orderTable.setTableGroup(savedTableGroup);
                orderTable.setEmpty(false);
            });
            assertThat(savedTableGroup)
                .usingRecursiveComparison()
                .ignoringFields("id", "orderTables.id")
                .isEqualTo(tableGroup);
        }

        @Test
        @DisplayName("ordertable 이 비어있는 경우 예외처리한다.")
        void throwExceptionOrderTablesAreEmpty() {
            final TableGroup tableGroup = new TableGroup();
            tableGroup.setOrderTables(Collections.emptyList());

            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("저장되지 않은 tableGroup로 요청하는 경우 Exception을 throw한다.")
        void throwExceptionOrderTablesAreNotFound() {
            final TableGroup tableGroup = new TableGroup();
            tableGroup.setOrderTables(전체_주문_테이블());

            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("tableGroup안에 orderTable이 비어있지 않은 경우 Exception을 throw한다.")
        void throwExceptionOrderTablesAreNotEmpty() {
            final List<OrderTable> orderTables = 전체_주문_테이블();
            orderTables.forEach(orderTable -> orderTable.setEmpty(false));
            final TableGroup tableGroup = new TableGroup();
            tableGroup.setOrderTables(orderTables);

            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("TableGroup을 해제했다.")
    class UnGroup {

        @Test
        @DisplayName("정상적으로 tableGroup을 해제한다.")
        void success() {
            //given
            final TableGroup tableGroup = new TableGroup();
            final TableGroup savedTableGroup = saveTableGroupSuccessfully(tableGroup, 전체_주문_테이블());

            //when
            tableGroupService.ungroup(savedTableGroup.getId());

            //then
            final List<Long> savedOrderTableIds = savedTableGroup.getOrderTables().stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
            final List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(
                savedOrderTableIds);

            assertThat(savedOrderTables)
                .extracting(OrderTable::getTableGroupId, OrderTable::isEmpty)
                .containsExactly(
                    tuple(null, false), tuple(null, false), tuple(null, false), tuple(null, false),
                    tuple(null, false), tuple(null, false), tuple(null, false), tuple(null, false)
                );
        }

        @Test
        @DisplayName("해제하려는 TableGroup에 속한 Table의 주문 상태가 완료상태가 아닌경우 예외처리")
        void throwExceptionIfOrderIsNotCompletion() {
            //given
            final TableGroup tableGroup = new TableGroup();
            final OrderTable savedOrderTable = orderTableRepository.save(비어있는_주문_테이블());
            final OrderTable savedOrderTable2 = orderTableRepository.save(비어있는_주문_테이블());
            saveOrderMeal(savedOrderTable);
            tableGroup.setOrderTables(List.of(savedOrderTable, savedOrderTable2));
            final TableGroup savedTableGroup = tableGroupService.create(tableGroup);

            //when
            assertThatThrownBy(() -> tableGroupService.ungroup(savedTableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class);
        }

        private void saveOrderMeal(final OrderTable savedOrderTable) {
            final Order order = new Order();
            order.setOrderStatus(OrderStatus.MEAL.name());
            order.setOrderTableId(savedOrderTable.getId());
            order.setOrderedTime(LocalDateTime.now());
            orderDao.save(order);
        }
    }

    private TableGroup saveTableGroupSuccessfully(final TableGroup tableGroup,
        final List<OrderTable> orderTables) {
        final List<OrderTable> savedOrderTables = orderTables.stream()
            .map(tableService::create)
            .collect(Collectors.toList());

        tableGroup.setOrderTables(savedOrderTables);

        final TableGroup savedTableGroup = tableGroupService.create(tableGroup);
        return savedTableGroup;
    }
}
