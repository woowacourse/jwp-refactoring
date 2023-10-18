package kitchenpos.application;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
            final TableGroup expected = new TableGroup(LocalDateTime.now(), List.of(orderTable1, orderTable2));

            given(orderTableRepository.findAllByIdIn(anyList())).willReturn(List.of(orderTable1, orderTable2));

            final TableGroup spyExpected = spy(new TableGroup(expected.getCreatedDate(), new ArrayList<>()));
            given(tableGroupRepository.save(any(TableGroup.class))).willReturn(spyExpected);

            // when
            final TableGroup actual = tableGroupService.create(expected);

            // then
            assertThat(actual.getOrderTables()).containsExactly(orderTable1, orderTable2);
        }

        @Test
        void 주문_테이블이_null이면_예외가_발생한다() {
            // given
            final List<OrderTable> nullOrderTables = null;
            final TableGroup expected = new TableGroup(LocalDateTime.now(), nullOrderTables);

            // when, then
            assertThatThrownBy(() -> tableGroupService.create(expected))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_테이블이_크기가_2보다_작으면_예외가_발생한다() {
            // given
            final OrderTable orderTable = new OrderTable(3, true);
            final List<OrderTable> oneOrderTable = List.of(orderTable);
            final TableGroup expected = new TableGroup(LocalDateTime.now(), oneOrderTable);

            // when, then
            assertThatThrownBy(() -> tableGroupService.create(expected))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_테이블_수와_저장된_테이블_수가_일치하지_않으면_실패한다() {
            // given
            final OrderTable orderTable1 = new OrderTable(3, true);
            final OrderTable orderTable2 = new OrderTable(5, true);
            final TableGroup expected = new TableGroup(LocalDateTime.now(), List.of(orderTable1, orderTable2));

            given(orderTableRepository.findAllByIdIn(anyList())).willReturn(List.of(orderTable1));

            // when, then
            assertThatThrownBy(() -> tableGroupService.create(expected))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 저장된_주문_테이블이_비어있다면_실패한다() {
            // given
            final OrderTable notEmptyOrderTable = new OrderTable(3, false);
            final OrderTable emptyOrderTable = new OrderTable(5, true);
            final TableGroup expected = new TableGroup(LocalDateTime.now(), List.of(notEmptyOrderTable, emptyOrderTable));

            given(orderTableRepository.findAllByIdIn(anyList())).willReturn(List.of(notEmptyOrderTable, emptyOrderTable));

            // when, then
            assertThatThrownBy(() -> tableGroupService.create(expected))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 저장된_주문_테이블이_이미_그룹이면_실패한다() {
            // given
            final OrderTable alreadyHaveTableGroup = new OrderTable(3, true);
            final TableGroup tableGroup = tableGroup(LocalDateTime.now(), List.of(alreadyHaveTableGroup));
            alreadyHaveTableGroup.changeGroup(tableGroup);
            final OrderTable notHaveTableGroup = new OrderTable(5, true);
            final List<OrderTable> orderTables = new ArrayList<>(List.of(alreadyHaveTableGroup, notHaveTableGroup));
            final TableGroup expected = new TableGroup(LocalDateTime.now(), orderTables);

            given(orderTableRepository.findAllByIdIn(anyList())).willReturn(List.of(alreadyHaveTableGroup, notHaveTableGroup));

            // when, then
            assertThatThrownBy(() -> tableGroupService.create(expected))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class Ungroup {

        @Test
        void 그룹을_해제할_수_있다() {
            // given
            final long tableGroupId = 1L;
            final OrderTable spyOrderTable1 = spy(new OrderTable(3, false));
            final OrderTable spyOrderTable2 = spy(new OrderTable(5, false));

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

            final OrderTable spyOrderTable1 = spy(new OrderTable(3, false));
            final OrderTable spyOrderTable2 = spy(new OrderTable(5, false));

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
