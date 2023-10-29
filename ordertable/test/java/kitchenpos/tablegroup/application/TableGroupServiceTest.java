package ordertable.test.java.kitchenpos.tablegroup.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.ServiceTest;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.exception.OrderTableUpdateException;
import kitchenpos.tablegroup.application.TableGroupService;
import kitchenpos.tablegroup.dto.TableRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class TableGroupServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private OrderRepository orderRepository;

    private List<OrderTable> dummyTables;
    private List<TableRequest> dummyTableRequests;

    @BeforeEach
    void setUp() {
        dummyTables = new ArrayList<>();
        dummyTables.addAll(List.of(orderTableRepository.save(new OrderTable()), orderTableRepository.save(new OrderTable())));
        dummyTableRequests = dummyTables.stream()
                .map(dummyTable -> new TableRequest(dummyTable.getId()))
                .collect(Collectors.toList());
    }

    @Nested
    class create_메서드는 {

        @Test
        void 주문_테이블_그룹을_생성한다() {
            // given
            dummyTables.forEach(table -> table.changeEmpty(true));
            orderTableRepository.saveAll(dummyTables);
            final TableGroupRequest request = new TableGroupRequest(dummyTableRequests);

            // when
            final TableGroup createdTableGroup = tableGroupService.create(request);

            // then
            assertSoftly(
                    softly -> {
                        softly.assertThat(createdTableGroup.getId()).isNotNull();
                        softly.assertThat(createdTableGroup.getCreatedDate()).isNotNull();
                        softly.assertThat(createdTableGroup.getOrderTables());
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
            final TableGroupRequest request = new TableGroupRequest(List.of(dummyTableRequests.get(0)));

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 존재하지_않는_주문_테이블이_포함되어_있으면_예외가_발생한다() {
            // given
            final long nonExistTableId = 99L;
            final TableGroupRequest request = new TableGroupRequest(
                    List.of(dummyTableRequests.get(0), new TableRequest(nonExistTableId)));

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 중복인_주문_테이블이_포함되어_있으면_예외가_발생한다() {
            // given
            final TableGroupRequest request = new TableGroupRequest(
                    List.of(dummyTableRequests.get(0), dummyTableRequests.get(0)));

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_가능한_주문_테이블이_포함되어_있으면_예외가_발생한다() {
            // given
            final OrderTable invalidTable = orderTableRepository.save(new OrderTable(null, 5, false));
            final TableGroupRequest request = new TableGroupRequest(
                    List.of(dummyTableRequests.get(0), new TableRequest(invalidTable.getId())));

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 이미_다른_그룹에_속한_주문_테이블이_포함되어_있으면_예외가_발생한다() {
            // given
            tableGroupRepository.save(new TableGroup(LocalDateTime.now(), dummyTables));
            final TableGroupRequest request = new TableGroupRequest(dummyTableRequests);

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
            final TableGroup tableGroup = tableGroupRepository.save(new TableGroup(LocalDateTime.now(), dummyTables));

            // when
            tableGroupService.ungroup(1L);

            // then
            final List<OrderTable> tables = orderTableRepository.findAllByIdIn(List.of(
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
            final OrderTable invalidTable = orderTableRepository.save(new OrderTable(null, 0, true));
            final TableGroup tableGroup = tableGroupService.create(
                    new TableGroupRequest(List.of(new TableRequest(invalidTable.getId()), new TableRequest(dummyTables.get(0).getId()))));
            orderRepository.save(new Order(invalidTable.getId(), OrderStatus.MEAL.name(), LocalDateTime.now(),
                    List.of(new OrderLineItem(null, 1L, 1))));

            // when & then
            assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
                    .isInstanceOf(OrderTableUpdateException.class);
        }

        @Test
        void 주문_상태가_조리중인_주문_테이블이_포함되어_있으면_예외가_발생한다() {
            // given
            final OrderTable invalidTable = orderTableRepository.save(new OrderTable(null, 0, true));
            final TableGroup tableGroup = tableGroupService.create(
                    new TableGroupRequest(List.of(new TableRequest(invalidTable.getId()), new TableRequest(dummyTables.get(0).getId()))));
            orderRepository.save(new Order(invalidTable.getId(), OrderStatus.COOKING.name(), LocalDateTime.now(),
                    List.of(new OrderLineItem(null, 1L, 1))));

            // when & then
            assertThatThrownBy(() -> tableGroupService.ungroup(1L))
                    .isInstanceOf(OrderTableUpdateException.class);
        }
    }
}
