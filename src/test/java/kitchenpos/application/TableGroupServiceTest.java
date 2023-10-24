package kitchenpos.application;

import kitchenpos.ServiceTest;
import kitchenpos.dao.OrderDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.request.OrderTableIdRequest;
import kitchenpos.dto.request.TableGroupRequest;
import kitchenpos.dto.response.TableGroupResponse;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
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
    private OrderDao orderDao;

    @Nested
    class 단체_지정을_등록한다 {
        @Test
        void 단체_지정이_정상적으로_등록된다() {
            final List<OrderTableIdRequest> orderTables = new ArrayList<>();
            for (int i = 0; i < 2; i++) {
                final OrderTable savedOrderTable = orderTableRepository.save(new OrderTable(null, null, 0, true));
                orderTables.add(new OrderTableIdRequest(savedOrderTable.getId()));
            }

            final TableGroupRequest request = new TableGroupRequest(orderTables);
            final TableGroupResponse savedTableGroup = tableGroupService.create(request);

            assertSoftly(softly -> {
                assertThat(savedTableGroup.getId()).isNotNull();
                assertThat(savedTableGroup.getOrderTables()).hasSize(2);
            });
        }

        @Test
        void 단체_지정시_2개_보다_작은_테이블을_입력하면_예외가_발생한다() {
            final OrderTable savedOrderTable = orderTableRepository.save(new OrderTable(null, null, 0, true));

            final TableGroupRequest tableGroup = new TableGroupRequest(List.of(new OrderTableIdRequest(savedOrderTable.getId())));

            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 단체_지정되어있는_테이블을_단체_지정하면_예외가_발생한다() {
            final List<OrderTableIdRequest> orderTables = new ArrayList<>();
            for (int i = 0; i < 2; i++) {
                final OrderTable savedOrderTable = orderTableRepository.save(new OrderTable(null, null, 0, true));
                orderTables.add(new OrderTableIdRequest(savedOrderTable.getId()));
            }
            final TableGroupRequest tableGroup = new TableGroupRequest(orderTables);
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
                final OrderTable savedOrderTable = orderTableRepository.save(new OrderTable(null, null, 0, true));
                orderTables.add(new OrderTableIdRequest(savedOrderTable.getId()));
            }

            final TableGroupRequest tableGroupRequest = new TableGroupRequest(orderTables);
            final TableGroupResponse tableGroupResponse = tableGroupService.create(tableGroupRequest);

            tableGroupService.ungroup(tableGroupResponse.getId());

            final List<OrderTable> changedOrderTableIds = orderTableRepository.findAllByTableGroupId(tableGroupResponse.getId());

            assertThat(changedOrderTableIds).isEmpty();
        }

        @ParameterizedTest
        @ValueSource(strings = {"COOKING", "MEAL"})
        void 테이블이_조리_혹은_식사_상태일때_단체_지정을_해제하면_예외가_발생한다(final String status) {
            final List<OrderTableIdRequest> orderTables = new ArrayList<>();
            for (int i = 0; i < 2; i++) {
                final OrderTable savedOrderTable = orderTableRepository.save(new OrderTable(null, null, 0, true));
                orderTables.add(new OrderTableIdRequest(savedOrderTable.getId()));
                orderDao.save(new Order(null, savedOrderTable.getId(), status, LocalDateTime.now(), List.of()));
            }

            final TableGroupRequest tableGroup = new TableGroupRequest(orderTables);
            final TableGroupResponse savedTableGroup = tableGroupService.create(tableGroup);

            assertThatThrownBy(() -> tableGroupService.ungroup(savedTableGroup.getId()))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
