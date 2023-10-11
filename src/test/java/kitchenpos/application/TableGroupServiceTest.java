package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import java.util.Collections;
import java.util.List;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @Mock
    private TableGroupDao tableGroupDao;

    @InjectMocks
    private TableGroupService tableGroupService;

    @Nested
    class 단체_지정_생성_테스트 {

        @Test
        void 단체_지정을_정상_생성한다() {
            // given
            TableGroup tableGroup = new TableGroup();

            OrderTable orderTable1 = new OrderTable();
            orderTable1.setId(1L);
            orderTable1.setEmpty(true);
            OrderTable orderTable2 = new OrderTable();
            orderTable2.setId(2L);
            orderTable2.setEmpty(true);
            tableGroup.setOrderTables(List.of(orderTable1, orderTable2));

            List<OrderTable> savedOrderTables = List.of(orderTable1, orderTable2);

            TableGroup savedTableGroup = new TableGroup();

            given(orderTableDao.findAllByIdIn(anyList())).willReturn(savedOrderTables);
            given(tableGroupDao.save(any(TableGroup.class))).willReturn(savedTableGroup);

            // when, then
            assertThat(tableGroupService.create(tableGroup)).isInstanceOf(TableGroup.class);
        }

        @Test
        void 단체_지정에_테이블이_포함되지_않은_경우_예외를_반환한다() {
            // given, when, then
            assertThrows(IllegalArgumentException.class,
                    () -> tableGroupService.create(new TableGroup()));
        }

        @Test
        void 단체_지정에_둘_미만의_테이블이_포함된_경우_예외를_반환한다() {
            // given
            TableGroup tableGroup = new TableGroup();
            tableGroup.setOrderTables(List.of(new OrderTable()));

            // when, then
            assertThrows(IllegalArgumentException.class,
                    () -> tableGroupService.create(tableGroup));
        }

        @Test
        void 단체_지정하는_테이블_id가_존재하지_않는_경우_예외를_반환한다() {
            // given
            TableGroup tableGroup = new TableGroup();
            tableGroup.setOrderTables(List.of(new OrderTable(), new OrderTable()));

            given(orderTableDao.findAllByIdIn(anyList())).willReturn(Collections.emptyList());

            // when, then
            assertThrows(IllegalArgumentException.class,
                    () -> tableGroupService.create(tableGroup));
        }

        @Test
        void 단체_지정하는_테이블이_빈_테이블인_경우_예외를_반환한다() {
            // given
            TableGroup tableGroup = new TableGroup();

            OrderTable orderTable1 = new OrderTable();
            orderTable1.setEmpty(true);
            OrderTable orderTable2 = new OrderTable();
            orderTable2.setEmpty(false);
            List<OrderTable> orderTables = List.of(orderTable1, orderTable2);
            tableGroup.setOrderTables(orderTables);

            given(orderTableDao.findAllByIdIn(anyList())).willReturn(orderTables);

            // when, then
            assertThrows(IllegalArgumentException.class,
                    () -> tableGroupService.create(tableGroup));
        }

        @Test
        void 단체_지정하는_테이블이_이미_단체_지정된_경우_예외를_반환한다() {
            // given
            TableGroup tableGroup = new TableGroup();

            OrderTable orderTable1 = new OrderTable();
            orderTable1.setEmpty(false);
            orderTable1.setTableGroupId(null);
            OrderTable orderTable2 = new OrderTable();
            orderTable2.setEmpty(false);
            orderTable2.setTableGroupId(1L);
            List<OrderTable> orderTables = List.of(orderTable1, orderTable2);
            tableGroup.setOrderTables(orderTables);

            given(orderTableDao.findAllByIdIn(anyList())).willReturn(orderTables);

            // when, then
            assertThrows(IllegalArgumentException.class,
                    () -> tableGroupService.create(tableGroup));
        }
    }

    @Test
    void 단체_지정을_정상_해제한다() {
        // given
        OrderTable orderTable1 = new OrderTable();
        orderTable1.setId(1L);
        OrderTable orderTable2 = new OrderTable();
        orderTable2.setId(2L);
        List<OrderTable> orderTables = List.of(orderTable1, orderTable2);

        given(orderTableDao.findAllByTableGroupId(anyLong())).willReturn(orderTables);
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).willReturn(
                false);

        // when, then
        assertDoesNotThrow(() -> tableGroupService.ungroup(1L));
    }
}
