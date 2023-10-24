package kitchenpos.application;

import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTables;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.dto.TableGroupRequest;
import kitchenpos.domain.dto.TableGroupResponse;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.domain.repository.TableGroupRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import support.fixture.OrderBuilder;
import support.fixture.TableBuilder;
import support.fixture.TableGroupBuilder;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TableGroupServiceTest {

    @Autowired
    private TableGroupService tableGroupService;
    @Autowired
    private TableGroupRepository tableGroupRepository;
    @Autowired
    private OrderTableRepository orderTableRepository;
    @Autowired
    private OrderRepository orderRepository;

    @Nested
    @DisplayName("테이블 그룹 생성 테스트")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class CreateTableGroupTest {

        Stream<List<Long>> createWithEmptySmallerThenTwoTablesTest() {
            return Stream.of(
                    List.of(),
                    List.of(1L)
            );
        }

        @Test
        @DisplayName("테이블 그룹을 생성하고 테이블의 그룹 id를 설정하고 empty를 false로 설장한다.")
        void createTest() {
            // given
            final OrderTable table1 = orderTableRepository.save(new TableBuilder().build());
            final OrderTable table2 = orderTableRepository.save(new TableBuilder().build());

            final TableGroupRequest request = new TableGroupRequest(List.of(table1.getId(), table2.getId()));

            // when
            final TableGroupResponse expect = tableGroupService.create(request);

            // then
            final TableGroup tableGroup = tableGroupRepository.findById(expect.getId()).get();
            final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroup.getId());
            final List<Long> orderTableIds = orderTables.stream()
                    .map(OrderTable::getId)
                    .collect(Collectors.toList());

            final TableGroupResponse actual = new TableGroupResponse(tableGroup.getId(), orderTableIds, null);

            assertThat(actual)
                    .usingRecursiveComparison()
                    .ignoringFields("createdDate")
                    .isEqualTo(expect);
        }

        @ParameterizedTest
        @MethodSource
        @DisplayName("OrderTable이 2개 미만일 경우 IllegalArgumentException이 발생한다.")
        void createWithEmptySmallerThenTwoTablesTest(final List<Long> orderTableIds) {
            // given
            final TableGroupRequest request = new TableGroupRequest(orderTableIds);

            // when & then
            assertThrowsExactly(IllegalArgumentException.class,
                    () -> tableGroupService.create(request));
        }

        @Test
        @DisplayName("저장되지 않은 OrderTable이 존재할 경우 IllegalArgumentException이 발생한다.")
        void createTableWithNotSavedOrderTable() {
            // given
            final long savedId1 = 1L;
            final long savedId2 = 2L;
            final long invalidId = -1L;
            final TableGroupRequest request = new TableGroupRequest(List.of(savedId1, savedId2, invalidId));

            // when & then
            assertThrowsExactly(IllegalArgumentException.class,
                    () -> tableGroupService.create(request));
        }

        @Test
        @DisplayName("비어있지 않은 OrderTable이 존재할 경우 IllegalArgumentException이 발생한다.")
        void should_throw_when_not_empty_orderTable_exists() {
            // given
            final OrderTable orderTable1 = orderTableRepository.findById(1L).get();
            final OrderTable orderTable2 = orderTableRepository.findById(2L).get();

            orderTable1.setEmpty(false);
            orderTableRepository.save(orderTable1);

            final TableGroupRequest request = new TableGroupRequest(List.of(orderTable1.getId(), orderTable2.getId()));

            // when & then
            assertThrowsExactly(IllegalArgumentException.class,
                    () -> tableGroupService.create(request));
        }

        @Test
        @DisplayName("이미 테이블 그룹에 속한 테이블이 존재할 경우 IllegalArgumentException이 발생한다.")
        void should_throw_when_already_belong_to_tableGroup() {
            // given
            final OrderTable orderTable1 = orderTableRepository.save(new TableBuilder().build());
            final OrderTable orderTable2 = orderTableRepository.save(new TableBuilder().build());

            final TableGroup tableGroup = tableGroupRepository.save(new TableGroupBuilder()
                    .setOrderTables(List.of(orderTable1, orderTable2))
                    .build());

            orderTable1.setTableGroup(tableGroup);
            orderTable2.setTableGroup(tableGroup);

            orderTableRepository.save(orderTable1);
            orderTableRepository.save(orderTable2);

            final TableGroupRequest request = new TableGroupRequest(List.of(orderTable1.getId(), orderTable2.getId()));

            // when & then
            assertThrowsExactly(IllegalArgumentException.class,
                    () -> tableGroupService.create(request));
        }
    }

    @Nested
    @DisplayName("테이블 그룹 해제 테스트")
    class UngroupTest {

        @Test
        @DisplayName("테이블 그룹을 해제하면 그룹에 속하는 모든 테이블의 그룹 id를 null로 설정하고 empty를 true로 설정한다.")
        void ungroupTest() {
            // given
            final OrderTable orderTable1 = orderTableRepository.findById(1L).get();
            final OrderTable orderTable2 = orderTableRepository.findById(2L).get();

            final OrderTables orderTables = new OrderTables(List.of(orderTable1, orderTable2));
            final TableGroup tableGroup = tableGroupRepository.save(new TableGroup(orderTables));

            orderTable1.setEmpty(false);
            orderTable2.setEmpty(false);

            orderTable1.setTableGroup(tableGroup);
            orderTable2.setTableGroup(tableGroup);

            orderTableRepository.save(orderTable1);
            orderTableRepository.save(orderTable2);

            orderRepository.findAll().stream()
                    .filter(order -> List.of(1L, 2L).contains(order.getOrderTable().getId()))
                    .forEach(order -> {
                        order.updateOrderStatus(OrderStatus.COMPLETION);
                        orderRepository.save(order);
                    });

            // when
            tableGroupService.ungroup(tableGroup.getId());

            // then
            orderTableRepository.findById(orderTable1.getId()).ifPresentOrElse(
                    actual -> {
                        assertNull(actual.getTableGroup());
                        assertFalse(actual.isEmpty());
                    },
                    () -> fail("테이블이 존재하지 않습니다.")
            );

            orderTableRepository.findById(orderTable2.getId()).ifPresentOrElse(
                    actual -> {
                        assertNull(actual.getTableGroup());
                        assertFalse(actual.isEmpty());
                    },
                    () -> fail("테이블이 존재하지 않습니다.")
            );
        }

        @ParameterizedTest
        @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
        @DisplayName("주문 상태가 COOKING 또는 MEAL일 경우 IllegalArgumentException이 발생한다.")
        void should_throw_when_order_status_is_cooking_or_meal(final OrderStatus orderStatus) {
            // given
            final OrderTable table1 = orderTableRepository.save(new TableBuilder().build());
            final OrderTable table2 = orderTableRepository.save(new TableBuilder().build());

            final TableGroup tableGroup = tableGroupRepository.save(new TableGroupBuilder()
                    .setOrderTables(List.of(table1, table2))
                    .build());

            table1.setTableGroup(tableGroup);
            table2.setTableGroup(tableGroup);

            orderTableRepository.save(table1);
            orderTableRepository.save(table2);

            orderRepository.save(new OrderBuilder()
                    .setOrderTable(table1)
                    .setOrderStatus(orderStatus)
                    .build());
            orderRepository.save(new OrderBuilder()
                    .setOrderTable(table2)
                    .setOrderStatus(orderStatus)
                    .build());

            // when & then
            assertThrowsExactly(IllegalArgumentException.class,
                    () -> tableGroupService.ungroup(tableGroup.getId()));
        }
    }
}
