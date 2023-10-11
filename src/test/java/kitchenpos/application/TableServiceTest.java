package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.helper.IntegrationTestHelper;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.fixture.OrderFixture.주문_생성;
import static kitchenpos.fixture.OrderTableFixture.주문_테이블_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class TableServiceTest extends IntegrationTestHelper {

    @Autowired
    private TableService tableService;

    @Autowired
    private OrderDao orderDao;

    @Test
    void 주문_테이블을_생성한다() {
        // given
        OrderTable orderTable = 주문_테이블_생성(null, 10, false);

        // when
        OrderTable result = tableService.create(orderTable);

        // then
        assertSoftly(softly -> {
            softly.assertThat(result.getNumberOfGuests()).isEqualTo(10);
            softly.assertThat(result.isEmpty()).isEqualTo(false);
        });
    }

    @Test
    void 모든_주문_테이블을_반환한다() {
        // given
        OrderTable orderTable = 주문_테이블_생성(null, 10, false);
        OrderTable savedOrderTable = tableService.create(orderTable);

        // when
        List<OrderTable> result = tableService.list();

        // then
        assertSoftly(softly -> {
            softly.assertThat(result).hasSize(1);
            softly.assertThat(result.get(0)).usingRecursiveComparison()
                    .isEqualTo(savedOrderTable);
        });
    }

    @Test
    void 주문_테이블을_빈_상태로_변경한다() {
        // given
        OrderTable orderTable = tableService.create(주문_테이블_생성(null, 1, false));
        OrderTable changedTable = 주문_테이블_생성(null, 1, true);

        // when
        OrderTable changedOrderTable = tableService.changeEmpty(orderTable.getId(), changedTable);

        // then
        assertThat(changedOrderTable.isEmpty()).isTrue();
    }

    @Test
    void 주문_테이블을_빈_상태로_변경시에_주문_테이블이_없다면_예외를_발생한다() {
        // given
        Long invalidOrderTableId = -1L;

        OrderTable orderTable = tableService.create(주문_테이블_생성(null, 1, false));
        OrderTable changedTable = 주문_테이블_생성(null, 1, true);

        // when & then
        assertThatThrownBy(() -> tableService.changeEmpty(invalidOrderTableId, changedTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블을_빈_상태로_변경시에_밥을_먹는중이라면_예외를_발생시킨다() {
        // given
        OrderTable orderTable = tableService.create(주문_테이블_생성(null, 1, false));
        OrderTable changedTable = 주문_테이블_생성(null, 1, true);
        orderDao.save(주문_생성(orderTable.getId(), COOKING.name(), LocalDateTime.now(), null));

        // when & then
        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), changedTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블의_손님_수를_변경한다() {
        // given
        OrderTable orderTable = tableService.create(주문_테이블_생성(null, 1, false));
        OrderTable changedTable = 주문_테이블_생성(null, 10, true);

        // when
        OrderTable result = tableService.changeNumberOfGuests(orderTable.getId(), changedTable);

        // then
        assertThat(result.getNumberOfGuests()).isEqualTo(changedTable.getNumberOfGuests());
    }

    @Test
    void 변경하려는_주문_테이블의_손님_수가_0보다_작다면_예외를_발생시킨다() {
        // given
        OrderTable orderTable = tableService.create(주문_테이블_생성(null, 1, false));
        OrderTable changedTable = 주문_테이블_생성(null, -1, true);

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), changedTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 인원_변경하려는_주문_테이블이_빈_테이블이면_예외를_발생시킨다() {
        // given
        OrderTable orderTable = tableService.create(주문_테이블_생성(null, 0, true));

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
