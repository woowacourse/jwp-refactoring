package kitchenpos;

import kitchenpos.order.Order;
import kitchenpos.order.OrderRepository;
import kitchenpos.order.OrderStatus;
import kitchenpos.ordertable.OrderTable;
import kitchenpos.ordertable.OrderTableService;
import kitchenpos.ordertable.ui.OrderTableDto;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@ServiceTest
class TableServiceTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderTableService tableService;

    @Test
    void 테이블을_등록할_수_있다() {
        OrderTableDto orderTableRequest = new OrderTableDto(null, 0, false);

        OrderTable savedOrderTable = tableService.create(orderTableRequest);

        assertSoftly(softly -> {
            softly.assertThat(savedOrderTable.getId()).isNotNull();
            softly.assertThat(savedOrderTable).extracting("numberOfGuests", "empty")
                    .containsOnly(orderTableRequest.getNumberOfGuests(), orderTableRequest.isEmpty());
        });
    }

    @Test
    void 테이블_목록을_조회한다() {
        OrderTable orderTable1 = tableService.create(new OrderTableDto(1, false));
        OrderTable orderTable2 = tableService.create(new OrderTableDto(0, true));

        List<OrderTable> tableList = tableService.list();

        assertThat(tableList).hasSize(2)
                .extracting("id")
                .containsOnly(orderTable1.getId(), orderTable2.getId());
    }

    @Nested
    class 테이블_상태_변경 {
        @Test
        void 테이블을_빈_테이블로_변경한다() {
            OrderTable savedOrderTable = tableService.create(new OrderTableDto(0, false));

            OrderTableDto changeOrderTable = new OrderTableDto(0, true);
            OrderTable result = tableService.changeEmpty(savedOrderTable.getId(), changeOrderTable);

            assertThat(result.isEmpty()).isTrue();
        }

        @Test
        void 테이블이_존재하지_읺으면_상태를_변경할_수_없다() {
            OrderTableDto notExistTable = new OrderTableDto(10, false);
            long notExistTableId = Long.MIN_VALUE;

            assertThatThrownBy(() -> tableService.changeEmpty(notExistTableId, notExistTable))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("테이블이 존재하지 않습니다. 빈 테이블로 변경할 수 없습니다.");
        }

        @ParameterizedTest
        @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
        void 테이블의_주문상태가_조리_또는_식사인_주문_테이블은_빈_테이블로_변경할_수_없다(OrderStatus orderStatus) {
            OrderTable savedOrderTable = tableService.create(new OrderTableDto(1, false));
            Order alreadyCookingOrMealOrder = new Order(savedOrderTable.getId(), orderStatus.name(), LocalDateTime.now());
            orderRepository.save(alreadyCookingOrMealOrder);

            OrderTableDto changeOrderTable = new OrderTableDto(0, true);

            assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTable.getId(), changeOrderTable))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("테이블의 주문이 조리, 혹은 식사 상태이면 빈 테이블로 변경할 수 없습니다.");
        }
    }

    @Nested
    class 방문한_손님_등록 {
        @Test
        void 테이블에_방문한_손님_수를_등록한다() {
            OrderTable orderTable = tableService.create(new OrderTableDto(0, false));

            OrderTableDto changeOrderTable = new OrderTableDto(1, true);
            tableService.changeNumberOfGuests(orderTable.getId(), changeOrderTable);

            assertThat(orderTable.getNumberOfGuests()).isOne();
        }

        @Test
        void 방문한_손님_수가_0명_미만이면_등록할_수_없다() {
            OrderTable orderTable = tableService.create(new OrderTableDto(0, false));
            int invalidNumberOfGuest = -1;
            OrderTableDto changeOrderTable = new OrderTableDto(invalidNumberOfGuest, true);

            assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), changeOrderTable))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("방문한 손님 수는 0명 이상이어야 합니다.");
        }

        @Test
        void 테이블이_존재하지_않으면_방문한_손님_수를_등록할_수_없다() {
            OrderTableDto notExistTable = new OrderTableDto(1, false);

            Long notExistTableId = Long.MIN_VALUE;
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(notExistTableId, notExistTable))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("테이블이 존재하지 않습니다. 방문한 손님 수를 등록할 수 없습니다.");
        }
    }
}
