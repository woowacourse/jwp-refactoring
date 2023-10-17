package kitchenpos.application;

import java.util.List;

import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.ui.dto.table.CreateOrderTableRequest;
import kitchenpos.ui.dto.table.UpdateTableGuestRequest;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
@SpringBootTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class TableServiceTest {

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private TableService tableService;

    @Test
    void 주문_테이블을_저장할_수_있다() {
        // given
        final CreateOrderTableRequest createOrderTableRequest = new CreateOrderTableRequest(3, true);

        // when
        final OrderTable actual = tableService.create(createOrderTableRequest);

        // then
        assertThat(actual).isNotNull();
    }

    @Test
    void 저장된_주문_테이블을_모두_가져올_수_있다() {
        // when
        final List<OrderTable> expected = tableService.list();

        // then
        assertThat(expected).hasSize(8);
    }

    @Nested
    class 손님_수_변경시 {

        @Test
        void 주문_테이블의_저장된_손님_수를_변경할_수_있다() {
            // given
            final OrderTable orderTable = new OrderTable(null, null, 0, false);
            final OrderTable expected = orderTableDao.save(orderTable);
            final UpdateTableGuestRequest updateTableGuestRequest = new UpdateTableGuestRequest(5);

            // when
            final OrderTable actual = tableService.changeNumberOfGuests(expected.getId(), updateTableGuestRequest);

            // then
            assertThat(actual.getNumberOfGuests()).isEqualTo(5);
        }

        @Test
        void 주문_테이블_게스트_수가_0보다_작다면_예외가_발생한다() {
            // given
            final OrderTable orderTable = new OrderTable(null, null, 0, false);
            final OrderTable expected = orderTableDao.save(orderTable);
            final UpdateTableGuestRequest updateTableGuestRequest = new UpdateTableGuestRequest(-1);

            // expect
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(expected.getId(), updateTableGuestRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 존재하지_않는_주문_테이블_식별자를_인자로_받으면_예외가_발생한다() {
            // given
            final Long notExistOrderTableId = null;
            final UpdateTableGuestRequest updateTableGuestRequest = new UpdateTableGuestRequest(-1);

            // expect
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(notExistOrderTableId, updateTableGuestRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_테이블이_비어있다면_예외가_발생한다() {
            // given
            final OrderTable orderTable = new OrderTable(null, null, 0, true);
            final OrderTable expected = orderTableDao.save(orderTable);
            final UpdateTableGuestRequest updateTableGuestRequest = new UpdateTableGuestRequest(-1);

            // expected
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(expected.getId(), updateTableGuestRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }

    }

}
