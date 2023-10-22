package kitchenpos.legacy.application;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.anyList;
import static org.mockito.BDDMockito.given;

import java.util.List;
import java.util.stream.IntStream;
import kitchenpos.legacy.dao.OrderDao;
import kitchenpos.legacy.dao.OrderTableDao;
import kitchenpos.legacy.dao.TableGroupDao;
import kitchenpos.legacy.domain.OrderTable;
import kitchenpos.legacy.domain.TableGroup;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class LegacyTableGroupServiceTest {

    @InjectMocks
    LegacyTableGroupService tableGroupService;

    @Mock
    OrderDao orderDao;

    @Mock
    OrderTableDao orderTableDao;

    @Mock
    TableGroupDao tableGroupDao;

    @Nested
    class create {

        @ParameterizedTest
        @ValueSource(ints = {0, 1})
        void 주문_테이블_목록의_갯수가_1개_이하이면_예외(int count) {
            // given
            TableGroup tableGroup = new TableGroup();
            List<OrderTable> orderTables = IntStream.range(0, count)
                .mapToObj(i -> new OrderTable())
                .collect(toList());
            tableGroup.setOrderTables(orderTables);

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_테이블_목록으로_조회한_테이블_목록의_개수가_조회한_테이블_목록의_개수와_맞지_않으면_예외() {
            // given
            TableGroup tableGroup = new TableGroup();
            OrderTable orderTable1 = new OrderTable();
            orderTable1.setId(1L);
            OrderTable orderTable2 = new OrderTable();
            orderTable2.setId(2L);
            tableGroup.setOrderTables(List.of(orderTable1, orderTable2));

            // when
            given(orderTableDao.findAllByIdIn(anyList()))
                .willReturn(List.of(new OrderTable()));

            // then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_테이블_목록에서_비어있는_테이블이_있으면_예외() {
            // given
            TableGroup tableGroup = new TableGroup();
            OrderTable orderTable1 = new OrderTable();
            orderTable1.setId(1L);
            OrderTable orderTable2 = new OrderTable();
            orderTable2.setId(2L);
            orderTable2.setEmpty(true);
            tableGroup.setOrderTables(List.of(orderTable1, orderTable2));
            given(orderTableDao.findAllByIdIn(anyList()))
                .willReturn(tableGroup.getOrderTables());

            // when
            orderTable1.setEmpty(false);

            // then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_테이블_목록에서_테이블_그룹에_등록된_테이블이_있으면_예외() {
            // given
            TableGroup tableGroup = new TableGroup();
            OrderTable orderTable1 = new OrderTable();
            orderTable1.setId(1L);
            orderTable1.setEmpty(true);
            OrderTable orderTable2 = new OrderTable();
            orderTable2.setId(2L);
            orderTable2.setEmpty(true);
            tableGroup.setOrderTables(List.of(orderTable1, orderTable2));
            given(orderTableDao.findAllByIdIn(anyList()))
                .willReturn(tableGroup.getOrderTables());

            // when
            orderTable1.setTableGroupId(1L);

            // then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 성공() {
            // given
            TableGroup tableGroup = new TableGroup();
            OrderTable orderTable1 = new OrderTable();
            orderTable1.setId(1L);
            orderTable1.setEmpty(true);
            OrderTable orderTable2 = new OrderTable();
            orderTable2.setId(2L);
            orderTable2.setEmpty(true);
            tableGroup.setOrderTables(List.of(orderTable1, orderTable2));
            given(orderTableDao.findAllByIdIn(anyList()))
                .willReturn(tableGroup.getOrderTables());
            TableGroup savedTableGroup = new TableGroup();
            savedTableGroup.setId(1L);
            savedTableGroup.setOrderTables(tableGroup.getOrderTables());
            given(tableGroupDao.save(any(TableGroup.class)))
                .willReturn(savedTableGroup);

            // when
            TableGroup actual = tableGroupService.create(tableGroup);

            // then
            List<OrderTable> actualOrderTables = actual.getOrderTables();
            assertThat(actualOrderTables)
                .map(OrderTable::getTableGroupId)
                .allMatch(tableGroupId -> tableGroupId.equals(1L));
        }
    }

    @Nested
    class ungroup {

        @Test
        void 주문_테이블의_주문의_상태가_COMPLETION이_아니면_예외() {
            // given
            Long tableGroupId = 1L;

            // when
            given(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList()))
                .willReturn(true);

            // then
            assertThatThrownBy(() -> tableGroupService.ungroup(tableGroupId))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 성공() {
            // given
            Long tableGroupId = 1L;
            given(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList()))
                .willReturn(false);

            // when
            assertThatNoException().isThrownBy(() -> tableGroupService.ungroup(tableGroupId));
        }
    }
}
