package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.stream.Collectors;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.dao.fakedao.InMemoryOrderDao;
import kitchenpos.dao.fakedao.InMemoryOrderTableDao;
import kitchenpos.dao.fakedao.InMemoryTableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableFactory;
import kitchenpos.domain.TableGroupFactory;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class TableGroupServiceTest {

    private OrderDao fakeOrderDao;
    private OrderTableDao fakeOrderTableDao;
    private TableGroupDao fakeTableGroupDao;

    @BeforeEach
    void setUp() {
        fakeOrderDao = new InMemoryOrderDao();
        fakeOrderTableDao = new InMemoryOrderTableDao();
        fakeTableGroupDao = new InMemoryTableGroupDao();
    }

    @Nested
    class 단체_지정_등록시 {

        @Test
        void 단체_지정이_속한_테이블이_두_개_미만일_경우_예외가_발생한다() {
            // given
            final var tableOne = fakeOrderTableDao.save(OrderTableFactory.createOrderTableOf(0, true));

            final var tableGroup = TableGroupFactory.createTableGroupTableOf(tableOne);
            final var tableGroupService = new TableGroupService(fakeOrderDao, fakeOrderTableDao, fakeTableGroupDao);

            //when
            final ThrowingCallable throwingCallable = () -> tableGroupService.create(tableGroup);

            // then
            assertThatThrownBy(throwingCallable).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 단체_지정에_속한_테이블이_등록되지_않은_테이블일_경우_예외가_발생한다() {
            // given
            final var tableOne = fakeOrderTableDao.save(OrderTableFactory.createOrderTableOf(0, true));
            final var tableTwo = OrderTableFactory.createOrderTableOf(0, true);
            tableTwo.setId(Long.MAX_VALUE);
            final var tableGroup = TableGroupFactory.createTableGroupTableOf(tableOne, tableTwo);
            final var tableGroupService = new TableGroupService(fakeOrderDao, fakeOrderTableDao, fakeTableGroupDao);

            //when
            final ThrowingCallable throwingCallable = () -> tableGroupService.create(tableGroup);

            // then
            assertThatThrownBy(throwingCallable).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 단체_지정에_속한_테이블이_비어있지_않을_경우_예외가_발생한다() {
            // given
            final var tableOne = fakeOrderTableDao.save(OrderTableFactory.createOrderTableOf(0, false));
            final var tableTwe = fakeOrderTableDao.save(OrderTableFactory.createOrderTableOf(0, true));
            final var tableGroup = TableGroupFactory.createTableGroupTableOf(tableOne, tableTwe);
            final var tableGroupService = new TableGroupService(fakeOrderDao, fakeOrderTableDao, fakeTableGroupDao);

            //when
            final ThrowingCallable throwingCallable = () -> tableGroupService.create(tableGroup);

            // then
            assertThatThrownBy(throwingCallable).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 단체_지정이_속한_테이블이_두_개_이상일_경우_정상_등록된다() {
            // given
            final var tableOne = fakeOrderTableDao.save(OrderTableFactory.createOrderTableOf(0, true));
            final var tableTwe = fakeOrderTableDao.save(OrderTableFactory.createOrderTableOf(0, true));
            final var tableGroup = TableGroupFactory.createTableGroupTableOf(tableOne, tableTwe);
            final var tableGroupService = new TableGroupService(fakeOrderDao, fakeOrderTableDao, fakeTableGroupDao);

            //when
            final var saved = tableGroupService.create(tableGroup);

            final var tableGroupIds = saved.getOrderTables().stream()
                                           .map(OrderTable::getTableGroupId)
                                           .collect(Collectors.toSet());
            // then
            assertAll(
                    () -> assertThat(saved.getId()).isNotNull(),
                    () -> assertThat(saved.getOrderTables()).hasSize(2),
                    () -> assertThat(tableGroupIds).containsExactly(1L)
            );
        }
    }
}
