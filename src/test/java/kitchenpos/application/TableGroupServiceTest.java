package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.List;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static kitchenpos.fixture.FixtureFactory.단체_지정_생성;
import static kitchenpos.fixture.FixtureFactory.주문_생성;
import static kitchenpos.fixture.FixtureFactory.주문_테이블_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@Transactional
@SpringBootTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class TableGroupServiceTest {

    @Autowired
    private TableGroupDao tableGroupDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private TableGroupService tableGroupService;

    @Nested
    class 단쳬_지정_저장 {

        @Test
        void 단쳬_지정을_저장할_수_있다() {
            // given
            final OrderTable firstOrderTable = 주문_테이블_생성(null, 0, true);
            final OrderTable secondOrderTable = 주문_테이블_생성(null, 0, true);

            final OrderTable firstSavedOrderTable = orderTableDao.save(firstOrderTable);
            final OrderTable secondSavedOrderTable = orderTableDao.save(secondOrderTable);

            final TableGroup expected = 단체_지정_생성(null, List.of(
                    firstSavedOrderTable, secondSavedOrderTable
            ));

            // when
            final TableGroup actual = tableGroupService.create(expected);

            // then
            assertAll(
                    () -> assertThat(actual.getOrderTables()).hasSize(2),
                    () -> assertThat(actual.getId()).isNotNull()
            );
        }

        @Test
        void 주문_테이블이_없다면_예외가_발생한다() {
            // given
            final TableGroup expected = new TableGroup();

            // expect
            assertThatThrownBy(() -> tableGroupService.create(expected))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_테이블이_2개_미만이면_예외가_발생한다() {
            // given
            final TableGroup expected = 단체_지정_생성(null, List.of(
                    주문_테이블_생성(null, 1, true)
            ));

            // expect
            assertThatThrownBy(() -> tableGroupService.create(expected))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_테이블이_저장되지_않은_경우_예외가_발생한다() {
            // given
            final OrderTable firstOrderTable = 주문_테이블_생성(null, 3, true);
            final OrderTable secondOrderTable = 주문_테이블_생성(null, 3, true);

            final TableGroup expected = 단체_지정_생성(LocalDateTime.now(), List.of(
                    firstOrderTable, secondOrderTable
            ));

            // expect
            assertThatThrownBy(() -> tableGroupService.create(expected))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 저장된_단체_지정_크기가_실제_사이즈와_다르면_예외가_발생한다() {
            // given
            final OrderTable firstOrderTable = 주문_테이블_생성(null, 3, true);
            final OrderTable secondOrderTable = 주문_테이블_생성(null, 3, true);

            orderTableDao.save(firstOrderTable);

            final TableGroup expected = 단체_지정_생성(LocalDateTime.now(), List.of(
                    firstOrderTable, secondOrderTable
            ));

            // expect
            assertThatThrownBy(() -> tableGroupService.create(expected))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_테이블이_빈_상태가_아니라면_예외가_발생한다() {
            // given
            final OrderTable firstOrderTable = orderTableDao.save(주문_테이블_생성(null, 3, false));
            final OrderTable secondOrderTable = orderTableDao.save(주문_테이블_생성(null, 3, false));

            final TableGroup expected = 단체_지정_생성(LocalDateTime.now(), List.of(
                    firstOrderTable, secondOrderTable
            ));

            // expect
            assertThatThrownBy(() -> tableGroupService.create(expected))
                    .isInstanceOf(IllegalArgumentException.class);
        }

    }

    @Test
    void 단체_지정을_해제할_수_있다() {
        // given
        final OrderTable firstOrderTable = orderTableDao.save(주문_테이블_생성(null, 3, false));
        final OrderTable secondOrderTable = orderTableDao.save(주문_테이블_생성(null, 3, false));

        final TableGroup tableGroup = 단체_지정_생성(LocalDateTime.now(), List.of(
                firstOrderTable, secondOrderTable
        ));
        final TableGroup expected = tableGroupDao.save(tableGroup);

        // expect
        assertDoesNotThrow(() -> tableGroupService.ungroup(expected.getId()));
    }

    @Test
    void 단체_지정_해제시_식사_중인_주문이_있을_경우_예외가_발생한다() {
        // given
        final OrderTable firstOrderTable = orderTableDao.save(주문_테이블_생성(null, 1, true));
        final OrderTable secondOrderTable = orderTableDao.save(주문_테이블_생성(null, 2, true));
        final Order order = 주문_생성(firstOrderTable.getId(), OrderStatus.COOKING.name(), LocalDateTime.now(), null);
        orderDao.save(order);

        final TableGroup expected = tableGroupService.create(단체_지정_생성(LocalDateTime.now(), List.of(
                firstOrderTable, secondOrderTable
        )));

        // expect
        assertThatThrownBy(() -> tableGroupService.ungroup(expected.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }

}
