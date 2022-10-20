package kitchenpos.application;

import static kitchenpos.KitchenPosFixtures.삼인용_테이블;
import static kitchenpos.KitchenPosFixtures.오인용_테이블;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.dao.OrderDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.ui.dto.request.OrderTableCreateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class TableGroupServiceTest extends ServiceTest {
    private static OrderTable table() {
        return new OrderTable(null, 0, true);
    }

    @Autowired
    private TableGroupService tableGroupService;
    @Autowired
    private TableService tableService;
    @Autowired
    private OrderDao orderDao;
    private OrderTable tableA;
    private OrderTable tableB;

    @BeforeEach
    void setUpTables() {
        tableA = tableService.create(삼인용_테이블);
        tableB = tableService.create(오인용_테이블);
    }

    @DisplayName("테이블 그룹을 지정할 수 있다")
    @Test
    void create() {
        // given
        final var orderTables = List.of(
                new OrderTable(tableA.getId(), null, 0, true),
                new OrderTable(tableB.getId(), null, 0, true)
        );
        final var tableGroupRequest = new TableGroup(null, orderTables);
        final var beforeRequest = LocalDateTime.now();

        // when
        final var tableGroup = tableGroupService.create(tableGroupRequest);
        final var afterRequest = LocalDateTime.now();

        // then
        assertAll(
                () -> assertThat(tableGroup.getId()).isEqualTo(1L),
                () -> assertThat(tableGroup.getCreatedDate()).isAfter(beforeRequest),
                () -> assertThat(tableGroup.getCreatedDate()).isBefore(afterRequest),
                () -> assertThat(tableGroup.getOrderTables())
                        .extracting("tableGroupId")
                        .containsExactly(tableGroup.getId(), tableGroup.getId()),
                () -> assertThat(tableGroup.getOrderTables())
                        .extracting("empty")
                        .containsExactly(false, false)
        );
    }

    @DisplayName("TableGroupService의 create 메서드는")
    @Nested
    class CreateTableGroup {
        @DisplayName("orderTables가 null이면 예외가 발생한다")
        @Test
        void should_fail_when_orderTables_is_null() {
            // given
            final var tableGroupRequest = new TableGroup(null, null);

            // when & then
            assertThrows(
                    IllegalArgumentException.class,
                    () -> tableGroupService.create(tableGroupRequest)
            );
        }

        @DisplayName("orderTables의 size가 2보다 작으면 예외가 발생한다")
        @Test
        void should_fail_when_orderTables_size_is_less_then_two() {
            // given
            final var tableGroupRequest = new TableGroup(null, List.of(table()));
            insertOrderTables(tableGroupRequest);

            // when & then
            assertThrows(
                    IllegalArgumentException.class,
                    () -> tableGroupService.create(tableGroupRequest)
            );
        }

        @DisplayName("orderTableId가 유효하지 않으면 예외가 발생한다")
        @Test
        void should_fail_when_orderTableId_is_invalid() {
            // given
            final var tableGroupRequest = new TableGroup(null, List.of(table(), table()));

            // when & then
            assertThrows(
                    IllegalArgumentException.class,
                    () -> tableGroupService.create(tableGroupRequest)
            );
        }

        @DisplayName("그룹화 대상 테이블 중 하나라도 이미 다른 그룹에 속해있으면 예외가 발생한다")
        @Test
        void should_fail_when_any_grouping_target_table_is_already_assigned_to_another_group() {
            // given
            final var tableGroupRequest = new TableGroup(null,
                    List.of(new OrderTable(1L, 0, true), table()));

            // when & then
            assertThrows(
                    IllegalArgumentException.class,
                    () -> tableGroupService.create(tableGroupRequest)
            );
        }

        @DisplayName("그룹화 대상 테이블 중 하나라도 주문 가능 상태(empty=false)이면 예외가 발생한다")
        @Test
        void should_fail_when_any_grouping_target_table_is_not_empty_status() {
            // given
            final var tableGroupRequest = new TableGroup(null,
                    List.of(new OrderTable(null, 0, false), table()));

            // when & then
            assertThrows(
                    IllegalArgumentException.class,
                    () -> tableGroupService.create(tableGroupRequest)
            );
        }
    }

    private void insertOrderTables(final TableGroup tableGroupRequest) {
        for (OrderTable orderTable : tableGroupRequest.getOrderTables()) {
            tableService.create(new OrderTableCreateRequest(orderTable.getNumberOfGuests(), orderTable.isEmpty()));
        }
    }

    @DisplayName("단체로 지정된 테이블들을 그룹 해제할 수 있다")
    @Test
    void ungroup() {
        // given
        final var orderTables = List.of(
                new OrderTable(tableA.getId(), null, 0, true),
                new OrderTable(tableB.getId(), null, 0, true)
        );
        final var tableGroupRequest = new TableGroup(null, orderTables);
        final var tableGroup = tableGroupService.create(tableGroupRequest);

        // when
        tableGroupService.ungroup(tableGroup.getId());
        final var tables = tableService.list();

        // then
        assertAll(
                () -> assertThat(tables).extracting("id").containsExactly(tableA.getId(), tableB.getId()),
                () -> assertThat(tables).extracting("tableGroupId").containsExactly(null, null),
                () -> assertThat(tables).extracting("empty").containsExactly(false, false)
        );
    }

    @DisplayName("TableGroupService의 unGroup 메서드는")
    @Nested
    class UnGroup {
        @DisplayName("매개변수로 전달한 그룹 아이디로 테이블을 조회하지 못하면 예외가 발생한다")
        @Test
        void ungroup_should_fail_when_tableGroupId_is_invalid() {
            // given
            final TableGroup tableGroup = createTableGroup();

            // when & then
            assertAll(
                    () -> assertThat(tableGroup.getId()).isEqualTo(1L),
                    () -> assertThrows(
                            IllegalArgumentException.class,
                            () -> tableGroupService.ungroup(null)
                    ),
                    () -> assertThrows(
                            IllegalArgumentException.class,
                            () -> tableGroupService.ungroup(-1L)
                    )
            );
        }

        @DisplayName("그룹에 속한 테이블 중 하나라도 조리시작(COOKING)인 주문이 있으면 예외가 발생한다")
        @Test
        void ungroup_should_fail_when_the_group_contains_table_has_order_with_cooking_status() {
            // given
            final TableGroup tableGroup = createTableGroup();
            orderDao.save(new Order(tableA.getId(), OrderStatus.COOKING.name(), LocalDateTime.now(), null));

            // when & then
            assertThrows(
                    IllegalArgumentException.class,
                    () -> tableGroupService.ungroup(tableGroup.getId())
            );
        }

        @DisplayName("그룹에 속한 테이블 중 하나라도 식사중(MEAL)인 주문이 있으면 예외가 발생한다")
        @Test
        void ungroup_should_fail_when_the_group_contains_table_has_order_with_meal_status() {
            // given
            final TableGroup tableGroup = createTableGroup();
            orderDao.save(new Order(tableA.getId(), OrderStatus.MEAL.name(), LocalDateTime.now(), null));

            // when & then
            assertThrows(
                    IllegalArgumentException.class,
                    () -> tableGroupService.ungroup(tableGroup.getId())
            );
        }

        private TableGroup createTableGroup() {
            final var orderTables = List.of(
                    new OrderTable(tableA.getId(), null, 0, true),
                    new OrderTable(tableB.getId(), null, 0, true)
            );

            return tableGroupService.create(new TableGroup(null, orderTables));
        }
    }
}
