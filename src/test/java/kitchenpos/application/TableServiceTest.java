package kitchenpos.application;

import static kitchenpos.fixture.OrderFixture.주문_생성;
import static kitchenpos.fixture.OrderTableFixture.주문_테이블_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Collections;
import java.util.List;
import kitchenpos.config.ServiceTest;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
@SuppressWarnings("NonAsciiCharacters")
class TableServiceTest {

    @Autowired
    OrderTableDao orderTableDao;

    @Autowired
    OrderDao orderDao;

    @Autowired
    TableGroupDao tableGroupDao;

    @Autowired
    TableService tableService;

    @Test
    void create_메서드는_orderTable을_전달하면_orderTable을_저장하고_반환한다() {
        // given
        final OrderTable orderTable = 주문_테이블_생성(0, false);

        // when
        final OrderTable actual = tableService.create(orderTable);

        // then
        assertThat(actual.getId()).isPositive();
    }

    @Test
    void list_메서드는_저장한_모든_orderTable을_반환한다() {
        // given
        final OrderTable persistOrderTable = orderTableDao.save(주문_테이블_생성(0, false));

        // when
        final List<OrderTable> actual = tableService.list();

        // then
        assertAll(
                () -> assertThat(actual).hasSize(1),
                () -> assertThat(actual.get(0).getId()).isEqualTo(persistOrderTable.getId())
        );
    }

    @Test
    void changeEmpty_메서드는_변경할_orderTableId와_변경한_값을_가진_orderTable을_전달하면_empty를_변경한다() {
        // given
        final OrderTable persistOrderTable = orderTableDao.save(주문_테이블_생성(true));
        final OrderTable emptyOrderTable = 주문_테이블_생성(false);

        // when
        final OrderTable actual = tableService.changeEmpty(persistOrderTable.getId(), emptyOrderTable);

        // then
        assertThat(actual.isEmpty()).isEqualTo(emptyOrderTable.isEmpty());
    }

    @Test
    void changeEmpty_메서드는_존재하지_않는_orderTableId를_전달하면_예외가_발생한다() {
        // given
        final OrderTable emptyOrderTable = 주문_테이블_생성(false);

        // when & then
        assertThatThrownBy(() -> tableService.changeEmpty(-999L, emptyOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest(name = "orderStatus가 {0}이면 예외가 발생한다.")
    @ValueSource(strings = {"COOKING", "MEAL"})
    void changeEmpty_메서드는_변경할_orderTableId의_orderStatu가_COMPLETION이_아니면_예외가_발생한다(final String invalidOrderStatus) {
        // given
        final OrderTable persistOrderTable = orderTableDao.save(주문_테이블_생성(false));
        orderDao.save(주문_생성(persistOrderTable.getId(), Collections.emptyList(), invalidOrderStatus));
        final OrderTable emptyOrderTable = 주문_테이블_생성(false);

        // when & then
        assertThatThrownBy(() -> tableService.changeEmpty(persistOrderTable.getId(), emptyOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void changeNumberOfGuests_메서드는_변경할_orderTableId와_변경할_값을_가진_orderTable을_전달하면_numberOfGuests를_변경한다() {
        // given
        final OrderTable persistOrderTable = orderTableDao.save(주문_테이블_생성(0, false));
        final OrderTable numberOfGuestsOrderTable = 주문_테이블_생성(1, false);

        // when
        final OrderTable actual = tableService.changeNumberOfGuests(
                persistOrderTable.getId(),
                numberOfGuestsOrderTable
        );

        // then
        assertThat(actual.getNumberOfGuests()).isEqualTo(numberOfGuestsOrderTable.getNumberOfGuests());
    }

    @ParameterizedTest(name = "numberOfGuests가 {0}이면 예외가 발생한다.")
    @ValueSource(ints = {-1, -2, -3})
    void changeNumberOfGuests_메서드는_0_이하의_numberOfGuests를_전달하면_예외가_발생한다(final int invalidNumberOfGuests) {
        // given
        final OrderTable persistOrderTable = orderTableDao.save(주문_테이블_생성(0, false));
        final OrderTable invalidNumberOfGuestsOrderTable = 주문_테이블_생성(invalidNumberOfGuests, false);

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(
                persistOrderTable.getId(),
                invalidNumberOfGuestsOrderTable)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void changeNumberOfGuests_메서드는_존재하지_않는_orderTableId를_전달하면_예외가_발생한다() {
        // given
        final OrderTable numberOfGuestsOrderTable = 주문_테이블_생성(1, false);

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(-999L, numberOfGuestsOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void changeNumberOfGuests_메서드는_orderTableId가_비어_있다면_예외가_발생한다() {
        // given
        final OrderTable invalidPersistOrderTable = orderTableDao.save(주문_테이블_생성(true));
        final OrderTable numberOfGuestsOrderTable = 주문_테이블_생성(1, false);

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(
                invalidPersistOrderTable.getId(),
                numberOfGuestsOrderTable)
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
