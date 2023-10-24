package kitchenpos.application;

import kitchenpos.domain.order.OrderTable;
import kitchenpos.domain.order.TableGroup;
import kitchenpos.domain.order.repository.OrderRepository;
import kitchenpos.domain.order.repository.OrderTableRepository;
import kitchenpos.domain.order.repository.TableGroupRepository;
import kitchenpos.domain.order.service.TableGroupService;
import kitchenpos.domain.order.service.dto.TableGroupCreateRequest;
import kitchenpos.domain.order.service.dto.TableGroupResponse;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
            given(orderRepository.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).willReturn(false);

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

        @Test
        void 주문상태가_요리와_식사중이면_그룹을_해제할_수_없다() {
            // given
            final long tableGroupId = 1L;

            final OrderTable spyOrderTable1 = spy(new OrderTable(3, true));
            final OrderTable spyOrderTable2 = spy(new OrderTable(5, true));

            given(orderTableRepository.findAllByTableGroupId(tableGroupId)).willReturn(List.of(spyOrderTable1, spyOrderTable2));
            given(spyOrderTable1.getId()).willReturn(1L);
            given(spyOrderTable2.getId()).willReturn(1L);
            given(orderRepository.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).willReturn(true);

            // when, then
            assertThatThrownBy(() -> tableGroupService.ungroup(tableGroupId))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
