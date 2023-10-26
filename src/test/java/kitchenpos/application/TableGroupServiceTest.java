package kitchenpos.application;

import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItems;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.order.repository.OrderRepository;
import kitchenpos.domain.order.service.dto.TableGroupCreateRequest;
import kitchenpos.domain.order.service.dto.TableGroupResponse;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.TableGroup;
import kitchenpos.domain.table.repository.OrderTableRepository;
import kitchenpos.domain.table.repository.TableGroupRepository;
import kitchenpos.domain.table.service.TableGroupService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static java.time.LocalDateTime.now;
import static kitchenpos.application.fixture.TableGroupFixture.tableGroup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.spy;

@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

    @InjectMocks
    private TableGroupService tableGroupService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private TableGroupRepository tableGroupRepository;

    @Nested
    class Create {

        @Test
        void 단체_지정을_할_수_있다() {
            // given
            final OrderTable orderTable1 = new OrderTable(3, true);
            final OrderTable orderTable2 = new OrderTable(5, true);
            final TableGroup expected = tableGroup(now(), List.of(orderTable1, orderTable2));

            given(orderTableRepository.findAllByIdIn(anyList())).willReturn(List.of(orderTable1, orderTable2));

            final TableGroup spyExpected = spy(tableGroup(expected.getCreatedDate(), new ArrayList<>()));
            given(tableGroupRepository.save(any(TableGroup.class))).willReturn(spyExpected);

            // when
            final TableGroupCreateRequest request = new TableGroupCreateRequest(List.of(1L, 2L));
            final TableGroupResponse actual = tableGroupService.create(request);

            // then
            assertAll(
                    () -> assertThat(actual.getCreatedDate()).isEqualTo(spyExpected.getCreatedDate())
            );
        }
    }

    @Nested
    class Ungroup {

        @Test
        void 그룹을_해제할_수_있다() {
            // given
            final long tableGroupId = 1L;
            final OrderTable spyOrderTable1 = spy(new OrderTable(3, true));
            final OrderTable spyOrderTable2 = spy(new OrderTable(5, true));

            given(orderTableRepository.findAllByTableGroupId(tableGroupId)).willReturn(List.of(spyOrderTable1, spyOrderTable2));
            given(spyOrderTable1.getId()).willReturn(1L);
            given(spyOrderTable2.getId()).willReturn(1L);

            // when
            tableGroupService.ungroup(tableGroupId);

            // then
            assertAll(
                    () -> assertThat(spyOrderTable1.getTableGroup()).isNull(),
                    () -> assertThat(spyOrderTable1.isEmpty()).isFalse(),
                    () -> assertThat(spyOrderTable2.getTableGroup()).isNull(),
                    () -> assertThat(spyOrderTable2.isEmpty()).isFalse()
            );
        }

        @ParameterizedTest
        @EnumSource(value = OrderStatus.class, names = {"MEAL", "COOKING"})
        void 주문상태가_요리와_식사중이면_그룹을_해제할_수_없다(final OrderStatus orderStatus) {
            // given
            final long tableGroupId = 1L;
            final OrderTable spyOrderTable = spy(new OrderTable(3, true));

            final List<OrderTable> ordertables = List.of(spyOrderTable);
            given(orderTableRepository.findAllByTableGroupId(tableGroupId)).willReturn(ordertables);
            given(spyOrderTable.getId()).willReturn(1L);

            final Long orderTableId = 1L;
            final Order order = new Order(orderTableId, orderStatus, now(), new OrderLineItems());
            given(orderRepository.findAllByOrderTableIds(anyList())).willReturn(List.of(order));

            // when, then
            assertThatThrownBy(() -> tableGroupService.ungroup(tableGroupId))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
