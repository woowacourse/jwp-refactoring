package kitchenpos.domain.table.service;

import kitchenpos.table.OrderTable;
import kitchenpos.table.OrderTableValidator;
import kitchenpos.table.OrderTables;
import kitchenpos.table.TableGroup;
import kitchenpos.table.repository.OrderTableRepository;
import kitchenpos.table.repository.TableGroupRepository;
import kitchenpos.table.service.TableGroupService;
import kitchenpos.table.service.dto.TableGroupCreateRequest;
import kitchenpos.table.service.dto.TableGroupResponse;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static java.time.LocalDateTime.now;
import static kitchenpos.domain.fixture.TableGroupFixture.tableGroup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.spy;

@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

    @InjectMocks
    private TableGroupService tableGroupService;

    @Mock
    private OrderTableValidator orderTableValidator;

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
            final TableGroup expected = tableGroup(now());

            given(orderTableRepository.findAllByIdIn(anyList())).willReturn(List.of(orderTable1, orderTable2));

            final TableGroup spyExpected = spy(tableGroup(expected.getCreatedDate()));
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

            willDoNothing().given(orderTableValidator).validate(any(OrderTables.class));
            given(orderTableRepository.findAllByTableGroupId(tableGroupId)).willReturn(List.of(spyOrderTable1, spyOrderTable2));

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
    }
}
