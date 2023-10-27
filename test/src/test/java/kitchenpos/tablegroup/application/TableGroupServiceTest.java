package kitchenpos.tablegroup.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import exception.EntityNotFoundException;
import java.util.List;
import kitchenpos.table.OrderTableFactory;
import kitchenpos.table.dao.InMemoryOrderTableDao;
import kitchenpos.tablegroup.dao.InMemoryTableGroupDao;
import ordertable.domain.OrderTable;
import ordertable.repository.OrderTableDao;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import tablegroup.application.TableGroupService;
import tablegroup.repository.TableGroupDao;
import tablegroup.ui.request.SimpleIdRequest;
import tablegroup.ui.request.TableGroupCreateRequest;

@SuppressWarnings("NonAsciiCharacters")
class TableGroupServiceTest {

    private OrderTableDao fakeOrderTableDao;
    private TableGroupDao fakeTableGroupDao;

    @BeforeEach
    void setUp() {
        fakeOrderTableDao = new InMemoryOrderTableDao();
        fakeTableGroupDao = new InMemoryTableGroupDao();
    }

    @Nested
    class 단체_지정_등록시 {

        @Test
        void 단체_지정이_속한_테이블이_두_개_미만일_경우_예외가_발생한다() {
            // given
            final var tableOne = fakeOrderTableDao.save(OrderTableFactory.createOrderTableOf(0, true));

            final var tableGroup = new TableGroupCreateRequest(List.of(new SimpleIdRequest(tableOne.getId())));
            final var tableGroupService = new TableGroupService(fakeOrderTableDao, fakeTableGroupDao);

            //when
            final ThrowingCallable throwingCallable = () -> tableGroupService.create(tableGroup);

            // then
            assertThatThrownBy(throwingCallable).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 단체_지정에_속한_테이블이_등록되지_않은_테이블일_경우_예외가_발생한다() {
            // given
            final var tableOne = fakeOrderTableDao.save(OrderTableFactory.createOrderTableOf(0, true));
            final var tableTwo = new OrderTable(Long.MAX_VALUE, null, 0, true);
            final var tableGroup = new TableGroupCreateRequest(List.of(
                    new SimpleIdRequest(tableOne.getId()),
                    new SimpleIdRequest(tableTwo.getId())));
            final var tableGroupService = new TableGroupService(fakeOrderTableDao, fakeTableGroupDao);

            //when
            final ThrowingCallable throwingCallable = () -> tableGroupService.create(tableGroup);

            // then
            assertThatThrownBy(throwingCallable).isInstanceOf(EntityNotFoundException.class);
        }

        @Test
        void 단체_지정에_속한_테이블이_비어있지_않을_경우_예외가_발생한다() {
            // given
            final var tableOne = fakeOrderTableDao.save(OrderTableFactory.createOrderTableOf(0, false));
            final var tableTwo = fakeOrderTableDao.save(OrderTableFactory.createOrderTableOf(0, true));
            final var tableGroup = new TableGroupCreateRequest(List.of(
                    new SimpleIdRequest(tableOne.getId()),
                    new SimpleIdRequest(tableTwo.getId())));
            final var tableGroupService = new TableGroupService(fakeOrderTableDao, fakeTableGroupDao);

            //when
            final ThrowingCallable throwingCallable = () -> tableGroupService.create(tableGroup);

            // then
            assertThatThrownBy(throwingCallable).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 단체_지정이_속한_테이블이_두_개_이상일_경우_정상_등록된다() {
            // given
            final var tableOne = fakeOrderTableDao.save(OrderTableFactory.createOrderTableOf(0, true));
            final var tableTwo = fakeOrderTableDao.save(OrderTableFactory.createOrderTableOf(0, true));
            final var tableGroup = new TableGroupCreateRequest(List.of(
                    new SimpleIdRequest(tableOne.getId()),
                    new SimpleIdRequest(tableTwo.getId())));
            final var tableGroupService = new TableGroupService(fakeOrderTableDao, fakeTableGroupDao);

            //when
            final var saved = tableGroupService.create(tableGroup);
            // then
            assertAll(
                    () -> assertThat(saved.getId()).isNotNull(),
                    () -> assertThat(saved.getOrderTables()).hasSize(2)
            );
        }
    }
}
