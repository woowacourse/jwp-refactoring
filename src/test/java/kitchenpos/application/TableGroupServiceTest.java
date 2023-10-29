package kitchenpos.application;

import kitchenpos.ServiceTest;
import kitchenpos.tablegroup.application.TableGroupService;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.table.dto.request.OrderTableIdRequest;
import kitchenpos.tablegroup.dto.request.TableGroupCreateRequest;
import kitchenpos.tablegroup.dto.response.TableGroupResponse;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.table.domain.repository.OrderTableRepository;
import kitchenpos.tablegroup.domain.repository.TableGroupRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableNumberOfGuests;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@ServiceTest
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
    class 단체_지정을_등록한다 {
        @Test
        void 단체_지정이_정상적으로_등록된다() {
            final List<OrderTableIdRequest> orderTables = new ArrayList<>();
            for (int i = 0; i < 2; i++) {
                final OrderTable savedOrderTable = orderTableRepository.save(new OrderTable(null, new OrderTableNumberOfGuests(0), true));
                orderTables.add(new OrderTableIdRequest(savedOrderTable.getId()));
            }

            final TableGroupCreateRequest request = new TableGroupCreateRequest(orderTables);
            final TableGroupResponse savedTableGroup = tableGroupService.create(request);

            assertSoftly(softly -> {
                assertThat(savedTableGroup.getId()).isNotNull();
                assertThat(savedTableGroup.getOrderTables()).hasSize(2);
            });
        }

        @Test
        void 단체_지정시_2개_보다_작은_테이블을_입력하면_예외가_발생한다() {
            final OrderTable savedOrderTable = orderTableRepository.save(new OrderTable(null, new OrderTableNumberOfGuests(0), true));

            final TableGroupCreateRequest tableGroup = new TableGroupCreateRequest(List.of(new OrderTableIdRequest(savedOrderTable.getId())));

            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 단체_지정되어있는_테이블을_단체_지정하면_예외가_발생한다() {
            final List<OrderTableIdRequest> orderTables = new ArrayList<>();
            for (int i = 0; i < 2; i++) {
                final OrderTable savedOrderTable = orderTableRepository.save(new OrderTable(null, new OrderTableNumberOfGuests(0), true));
                orderTables.add(new OrderTableIdRequest(savedOrderTable.getId()));
            }
            final TableGroupCreateRequest tableGroup = new TableGroupCreateRequest(orderTables);
            tableGroupService.create(tableGroup);

            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class 단체_지정을_해제한다 {
        @Test
        void 단체_지정을_정상적으로_해제한다() {
            final List<OrderTableIdRequest> orderTables = new ArrayList<>();
            for (int i = 0; i < 2; i++) {
                final OrderTable savedOrderTable = orderTableRepository.save(new OrderTable(null, new OrderTableNumberOfGuests(0), true));
                orderTables.add(new OrderTableIdRequest(savedOrderTable.getId()));
            }

            final TableGroupCreateRequest tableGroupCreateRequest = new TableGroupCreateRequest(orderTables);
            final TableGroupResponse tableGroupResponse = tableGroupService.create(tableGroupCreateRequest);

            tableGroupService.ungroup(tableGroupResponse.getId());
            final TableGroup tableGroup = tableGroupRepository.findById(tableGroupResponse.getId())
                    .orElseThrow(IllegalArgumentException::new);

            final List<OrderTable> changedOrderTableIds = orderTableRepository.findAllByTableGroup(tableGroup);

            assertThat(changedOrderTableIds).isEmpty();
        }

        @ParameterizedTest
        @ValueSource(strings = {"COOKING", "MEAL"})
        void 테이블이_조리_혹은_식사_상태일때_단체_지정을_해제하면_예외가_발생한다(final String status) {
            final List<OrderTableIdRequest> orderTables = new ArrayList<>();
            for (int i = 0; i < 2; i++) {
                final OrderTable savedOrderTable = orderTableRepository.save(new OrderTable(null, new OrderTableNumberOfGuests(0), true));
                orderRepository.save(new Order(savedOrderTable, OrderStatus.valueOf(status), LocalDateTime.now()));
                orderTables.add(new OrderTableIdRequest(savedOrderTable.getId()));
            }

            final TableGroupCreateRequest tableGroup = new TableGroupCreateRequest(orderTables);
            final TableGroupResponse savedTableGroup = tableGroupService.create(tableGroup);

            assertThatThrownBy(() -> tableGroupService.ungroup(savedTableGroup.getId()))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
