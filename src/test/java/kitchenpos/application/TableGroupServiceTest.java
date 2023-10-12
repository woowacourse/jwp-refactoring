package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroupFactory;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class TableGroupServiceTest {

    private final OrderDao orderDao = mock(OrderDao.class);
    private final OrderTableDao orderTableDao = mock(OrderTableDao.class);
    private final TableGroupDao tableGroupDao = mock(TableGroupDao.class);

    @Nested
    class 단체_지정_등록시 {

        @Test
        void 단체_지정이_속한_테이블이_두_개_미만일_경우_예외가_발생한다() {
            // given
            final var tableGroup = TableGroupFactory.createTableGroupTableOf(true, 1L, 2L);
            final var tableGroupService = new TableGroupService(orderDao, orderTableDao, tableGroupDao);

            //when
            final ThrowingCallable throwingCallable = () -> tableGroupService.create(tableGroup);

            // then
            assertThatThrownBy(throwingCallable).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 단체_지정에_속한_테이블이_등록되지_않은_테이블일_경우_예외가_발생한다() {
            // given
            final var tableGroup = TableGroupFactory.createTableGroupTableOf(true, 1L, 2L, 3L);
            final var tableGroupService = new TableGroupService(orderDao, orderTableDao, tableGroupDao);
            when(orderTableDao.findAllByIdIn(List.of(1L, 2L, 3L))).thenReturn(List.of());

            //when
            final ThrowingCallable throwingCallable = () -> tableGroupService.create(tableGroup);

            // then
            assertThatThrownBy(throwingCallable).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 단체_지정에_속한_테이블이_비어있지_않을_경우_예외가_발생한다() {
            // given
            final var tableGroup = TableGroupFactory.createTableGroupTableOf(false, 1L, 2L, 3L);
            final var tableGroupService = new TableGroupService(orderDao, orderTableDao, tableGroupDao);
            when(orderTableDao.findAllByIdIn(List.of(1L, 2L, 3L))).thenReturn(tableGroup.getOrderTables());

            //when
            final ThrowingCallable throwingCallable = () -> tableGroupService.create(tableGroup);

            // then
            assertThatThrownBy(throwingCallable).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 단체_지정이_속한_테이블이_두_개_이상일_경우_정상_등록된다() {
            // given
            final var tableGroup = TableGroupFactory.createTableGroupTableOf(true, 1L, 2L, 3L);
            final var tableGroupService = new TableGroupService(orderDao, orderTableDao, tableGroupDao);
            when(orderTableDao.findAllByIdIn(List.of(1L, 2L, 3L))).thenReturn(tableGroup.getOrderTables());
            when(tableGroupDao.save(tableGroup)).thenReturn(TableGroupFactory.empty(1L));

            //when
            final var saved = tableGroupService.create(tableGroup);

            final var tableGroupIds = saved.getOrderTables().stream()
                                           .map(OrderTable::getTableGroupId)
                                           .collect(Collectors.toSet());
            // then
            assertAll(
                    () -> assertThat(saved.getId()).isNotNull(),
                    () -> assertThat(saved.getOrderTables()).hasSize(3),
                    () -> assertThat(tableGroupIds).containsExactly(1L)
            );
        }
    }
}
