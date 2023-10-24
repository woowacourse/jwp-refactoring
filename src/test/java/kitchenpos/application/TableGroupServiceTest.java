package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.TableGroupRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class TableGroupServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private TableGroupDao tableGroupDao;

    @Autowired
    private OrderDao orderDao;

    private List<OrderTable> dummyTables;
    private List<Long> dummyTableIds;

    @BeforeEach
    void setUp() {
        dummyTables = List.of(orderTableDao.save(new OrderTable()), orderTableDao.save(new OrderTable()));
        dummyTableIds = dummyTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }

    @Nested
    class create_메서드는 {

        @Test
        void 주문_테이블_그룹을_생성한다() {
            // given
            final TableGroupRequest request = new TableGroupRequest(dummyTableIds);

            // when
            final TableGroup createdTableGroup = tableGroupService.create(request);

            // then
            assertSoftly(
                    softly -> {
                        softly.assertThat(createdTableGroup.getId()).isNotNull();
                        softly.assertThat(createdTableGroup.getCreatedDate()).isNotNull();
                        softly.assertThat(createdTableGroup.getOrderTables())
                                .extracting(OrderTable::getTableGroupId)
                                .containsExactly(createdTableGroup.getId(), createdTableGroup.getId());
                        softly.assertThat(createdTableGroup.getOrderTables())
                                .extracting(OrderTable::isEmpty)
                                .containsExactly(false, false);
                    }
            );
        }

        @Test
        void 주문_테이블_그룹이_비어있으면_예외가_발생한다() {
            // given
            final TableGroupRequest request = new TableGroupRequest(Collections.emptyList());

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 포함된_주문_테이블이_2개미만이면_예외가_발생한다() {
            // given
            final TableGroupRequest request = new TableGroupRequest(List.of(dummyTableIds.get(0)));

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 존재하지_않는_주문_테이블이_포함되어_있으면_예외가_발생한다() {
            // given
            final long nonExistTableId = 99L;
            final OrderTable invalidTable = new OrderTable(nonExistTableId, 1L, 5, true);
            final TableGroupRequest request = new TableGroupRequest(
                    List.of(dummyTableIds.get(0), invalidTable.getId()));

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 중복인_주문_테이블이_포함되어_있으면_예외가_발생한다() {
            // given
            final TableGroupRequest request = new TableGroupRequest(
                    List.of(dummyTableIds.get(0), dummyTableIds.get(0)));

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_가능한_주문_테이블이_포함되어_있으면_예외가_발생한다() {
            // given
            final OrderTable invalidTable = orderTableDao.save(new OrderTable(null, 5, false));
            final TableGroupRequest request = new TableGroupRequest(
                    List.of(dummyTableIds.get(0), invalidTable.getId()));

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 이미_다른_그룹에_속한_주문_테이블이_포함되어_있으면_예외가_발생한다() {
            // given
            final TableGroup otherTableGroup = new TableGroup(LocalDateTime.now(), Collections.emptyList());
            final OrderTable invalidTable = orderTableDao.save(new OrderTable());
            relationTablesWithTableGroup(otherTableGroup, List.of(invalidTable));
            final TableGroupRequest request = new TableGroupRequest(
                    List.of(dummyTableIds.get(0), invalidTable.getId()));

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }


    @Nested
    class ungroup_메서드는 {
        @Test
        void 주문_테이블_그룹을_해제한다() {
            // given
            final TableGroup tableGroup = new TableGroup(LocalDateTime.now(), Collections.emptyList());
            relationTablesWithTableGroup(tableGroup, dummyTables);

            // when
            tableGroupService.ungroup(1L);

            // then
            final List<OrderTable> tables = orderTableDao.findAllByIdIn(List.of(
                    dummyTables.get(0).getId(),
                    dummyTables.get(1).getId())
            );
            assertSoftly(
                    softly -> {
                        softly.assertThat(tables)
                                .extracting(OrderTable::isEmpty)
                                .containsExactly(false, false);
                        softly.assertThat(tables)
                                .extracting(OrderTable::getTableGroupId)
                                .containsExactly(null, null);
                    }
            );
        }

        @Test
        void 주문_상태가_식사중인_주문_테이블이_포함되어_있으면_예외가_발생한다() {
            // given
            final OrderTable invalidTable = orderTableDao.save(new OrderTable(null, 0, false));
            orderDao.save(new Order(invalidTable.getId(), OrderStatus.MEAL.name(), LocalDateTime.now(),
                    List.of(new OrderLineItem(null, 1L, 1))));
            final TableGroup tableGroup = new TableGroup(LocalDateTime.now(), Collections.emptyList());
            relationTablesWithTableGroup(tableGroup, List.of(dummyTables.get(0), invalidTable));

            // when & then
            assertThatThrownBy(() -> tableGroupService.ungroup(1L))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_상태가_조리중인_주문_테이블이_포함되어_있으면_예외가_발생한다() {
            // given
            final OrderTable invalidTable = orderTableDao.save(new OrderTable(null, 0, false));
            orderDao.save(new Order(invalidTable.getId(), OrderStatus.COOKING.name(), LocalDateTime.now(),
                    List.of(new OrderLineItem(null, 1L, 1))));
            final TableGroup tableGroup = new TableGroup(LocalDateTime.now(), Collections.emptyList());
            relationTablesWithTableGroup(tableGroup, List.of(dummyTables.get(0), invalidTable));

            // when & then
            assertThatThrownBy(() -> tableGroupService.ungroup(1L))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    void relationTablesWithTableGroup(final TableGroup tableGroup, final List<OrderTable> orderTables) {
        if (!tableGroup.getOrderTables().isEmpty()) {
            throw new IllegalArgumentException();
        }
        final List<OrderTable> createdOrderTables = orderTables.stream()
                .map(orderTable -> orderTableDao.save(orderTable))
                .collect(Collectors.toList());
        tableGroup.setOrderTables(createdOrderTables);
        final long tableGroupId = tableGroupDao.save(tableGroup).getId();
        createdOrderTables.forEach(table -> {
            table.setTableGroupId(tableGroupId);
            orderTableDao.save(table);
        });
    }
}
