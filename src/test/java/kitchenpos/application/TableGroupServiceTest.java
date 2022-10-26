package kitchenpos.application;

import static kitchenpos.KitchenPosFixtures.삼인용_테이블;
import static kitchenpos.KitchenPosFixtures.오인용_테이블;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.repository.OrderRepository;
import kitchenpos.ui.dto.request.OrderTableCreateRequest;
import kitchenpos.ui.dto.request.TableGroupCreateRequest;
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
    private OrderRepository orderRepository;
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
        final var tableGroupRequest = new TableGroupCreateRequest(List.of(tableA.getId(), tableB.getId()));
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
        @DisplayName("orderTables의 size가 2보다 작으면 예외가 발생한다")
        @Test
        void should_fail_when_orderTables_size_is_less_then_two() {
            // given
            final var tableGroupRequest = new TableGroupCreateRequest(List.of(tableA.getId()));

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("orderTableId가 유효하지 않으면 예외가 발생한다")
        @Test
        void should_fail_when_orderTableId_is_invalid() {
            // given
            final var tableGroupRequest = new TableGroupCreateRequest(List.of(-1L, -2L));

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("그룹화 대상 테이블 중 하나라도 이미 다른 그룹에 속해있으면 예외가 발생한다")
        @Test
        void should_fail_when_any_grouping_target_table_is_already_assigned_to_another_group() {
            // given
            final var tableGroupRequest = new TableGroupCreateRequest(List.of(tableA.getId(), tableB.getId()));
            tableGroupService.create(tableGroupRequest);
            final var tableC = tableService.create(new OrderTableCreateRequest(0, true));

            // when
            final var request = new TableGroupCreateRequest(List.of(tableA.getId(), tableC.getId()));

            // then
            assertThatThrownBy(() -> tableGroupService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("그룹화 대상 테이블 중 하나라도 주문 가능 상태(empty=false)이면 예외가 발생한다")
        @Test
        void should_fail_when_any_grouping_target_table_is_not_empty_status() {
            // given
            final var emptyTrueTableId = tableService.create(new OrderTableCreateRequest(0, true)).getId();
            final var emptyFalseTableId = tableService.create(new OrderTableCreateRequest(0, false)).getId();
            final var tableGroupRequest = new TableGroupCreateRequest(List.of(emptyTrueTableId, emptyFalseTableId));

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("단체로 지정된 테이블들을 그룹 해제할 수 있다")
    @Test
    void ungroup() {
        // given
        final var tableGroupRequest = new TableGroupCreateRequest(List.of(tableA.getId(), tableB.getId()));
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
                    () -> assertThatThrownBy(() -> tableGroupService.ungroup(null))
                            .isInstanceOf(IllegalArgumentException.class),
                    () -> assertThatThrownBy(() -> tableGroupService.ungroup(-1L))
                            .isInstanceOf(IllegalArgumentException.class)
            );
        }

        @DisplayName("그룹에 속한 테이블 중 하나라도 조리시작(COOKING)인 주문이 있으면 예외가 발생한다")
        @Test
        void ungroup_should_fail_when_the_group_contains_table_has_order_with_cooking_status() {
            // given
            final TableGroup tableGroup = createTableGroup();
            orderRepository.save(new Order(tableA.getId(), OrderStatus.COOKING.name(), LocalDateTime.now(), null));

            // when & then
            assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("그룹에 속한 테이블 중 하나라도 식사중(MEAL)인 주문이 있으면 예외가 발생한다")
        @Test
        void ungroup_should_fail_when_the_group_contains_table_has_order_with_meal_status() {
            // given
            final TableGroup tableGroup = createTableGroup();
            orderRepository.save(new Order(tableA.getId(), OrderStatus.MEAL.name(), LocalDateTime.now(), null));

            // when & then
            assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        private TableGroup createTableGroup() {
            return tableGroupService.create(new TableGroupCreateRequest(List.of(tableA.getId(), tableB.getId())));
        }
    }
}
