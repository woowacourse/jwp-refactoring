package kitchenpos.order.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.presentation.dto.ChangeEmptyRequest;
import kitchenpos.order.presentation.dto.ChangeNumberOfGuestsRequest;
import kitchenpos.order.presentation.dto.CreateOrderTableRequest;
import kitchenpos.support.NewTestSupporter;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@Sql("/truncate.sql")
@SpringBootTest
class OrderTableServiceTest {

    @Autowired
    private OrderTableService orderTableService;

    @Autowired
    private NewTestSupporter newTestSupporter;

    @Test
    void 주문_테이블을_생성한다() {
        // given
        final CreateOrderTableRequest createOrderTableRequest = new CreateOrderTableRequest(0, false);

        // when
        final OrderTable orderTable = orderTableService.create(createOrderTableRequest);

        // then
        assertThat(orderTable).isNotNull();
    }

    @Test
    void 주문_테이블에_대해_전체_조회한다() {
        // given
        final OrderTable orderTable = newTestSupporter.createOrderTable();

        // when
        final List<OrderTable> orderTables = orderTableService.list();

        // then
        assertThat(orderTables.get(0)).isEqualTo(orderTable);
    }

    @Test
    void 주문_테이블의_empty_상태를_변경한다() {
        // given
        final OrderTable orderTable = newTestSupporter.createOrderTable(false);
        final ChangeEmptyRequest changeEmptyRequest = new ChangeEmptyRequest(true);

        // when
        final OrderTable actual = orderTableService.changeEmpty(orderTable.getId(),
                                                                     changeEmptyRequest);

        // then
        assertThat(actual.isEmpty()).isTrue();
    }

    @Test
    void 주문_테이블의_방문_손님_수를_변경한다() {
        // given
        final OrderTable orderTable = newTestSupporter.createOrderTable();
        final ChangeNumberOfGuestsRequest changeNumberOfGuestsRequest = new ChangeNumberOfGuestsRequest(100);

        // when
        final OrderTable actual = orderTableService.changeNumberOfGuests(orderTable.getId(),
                                                                              changeNumberOfGuestsRequest);

        // then
        assertThat(actual.getNumberOfGuests()).isEqualTo(100);
    }
}
