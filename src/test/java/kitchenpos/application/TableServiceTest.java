package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.application.dto.MenuCreateRequest;
import kitchenpos.application.dto.MenuGroupRequest;
import kitchenpos.application.dto.MenuGroupResponse;
import kitchenpos.application.dto.MenuProductCreateRequest;
import kitchenpos.application.dto.MenuResponse;
import kitchenpos.application.dto.OrderCreateRequest;
import kitchenpos.application.dto.OrderLineItemRequest;
import kitchenpos.application.dto.OrderResponse;
import kitchenpos.application.dto.OrderTableChangeEmptyRequest;
import kitchenpos.application.dto.OrderTableChangeNumberOfGuestsRequest;
import kitchenpos.application.dto.OrderTableCreateRequest;
import kitchenpos.application.dto.OrderTableResponse;
import kitchenpos.application.dto.OrderUpdateRequest;
import kitchenpos.application.dto.ProductCreateRequest;
import kitchenpos.application.dto.ProductResponse;
import kitchenpos.domain.OrderStatus;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class TableServiceTest {

    @Nested
    class 테이블_생성 extends IntegrationTest {
        @Test
        void 요청을_할_수_있다() {
            // given
            final OrderTableCreateRequest orderTable = new OrderTableCreateRequest(2, false);

            // when
            final OrderTableResponse extract = tableService.create(orderTable);

            // then
            assertThat(extract).isNotNull();
        }
    }

    @Nested
    class 테이블_목록 extends IntegrationTest {
        @Test
        void 요청을_할_수_있다() {
            // given
            final OrderTableCreateRequest orderTable = new OrderTableCreateRequest(2, false);
            tableService.create(orderTable);

            // when
            final List<OrderTableResponse> extract = tableService.list();

            // then
            assertThat(extract).hasSize(1);
        }
    }

    @Nested
    class 테이블_상태_변경 extends IntegrationTest {
        @ParameterizedTest(name = "[{index}] 주문의 상태가 {0} 인 경우")
        @CsvSource(value = {"COOKING", "MEAL"})
        void 요청에서_테이블이_비어있는_상태로_변경할때_주문의_상태가_완료상태가_아니면_예외가_발생한다(final OrderStatus orderStatus) {
            // given
            final MenuGroupResponse menuGroup = menuGroupService.create(new MenuGroupRequest("1인 메뉴"));
            final ProductResponse product = productService.create(new ProductCreateRequest("짜장면", BigDecimal.valueOf(1000)));
            final MenuResponse menu = menuService.create(new MenuCreateRequest("짜장면", BigDecimal.valueOf(1000), menuGroup.getId(),
                List.of(new MenuProductCreateRequest(product.getId(), 1))));
            final OrderTableResponse orderTable = tableService.create(new OrderTableCreateRequest(2, false));
            final OrderResponse order = orderService.create(new OrderCreateRequest(orderTable.getId(), List.of(new OrderLineItemRequest(menu.getId(), 1))));
            orderService.changeOrderStatus(order.getId(), new OrderUpdateRequest(orderStatus.name()));

            // when & then
            assertThatThrownBy(
                () -> tableService.changeEmpty(orderTable.getId(), new OrderTableChangeEmptyRequest(false)))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 요청에서_테이블의_손님의_수를_변경할_수_있다() {
            // given
            final OrderTableResponse orderTable = tableService.create(new OrderTableCreateRequest(2, false));

            // when
            final OrderTableResponse extract = tableService.changeNumberOfGuests(orderTable.getId(), new OrderTableChangeNumberOfGuestsRequest(3));

            // then
            assertThat(extract.getNumberOfGuests()).isEqualTo(3);
        }

        @Test
        void 요청에서_테이블의_손님의_수를_0원_미만으로_변경하면_예외가_발생한다() {
            // given
            final OrderTableResponse orderTable = tableService.create(new OrderTableCreateRequest(2, false));

            // when & then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), new OrderTableChangeNumberOfGuestsRequest(-1)))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 요청에서_비어있는_테이블에_손님의_수를_변경할_경우_예외가_발생한다() {
            // given
            final OrderTableResponse orderTable = tableService.create(new OrderTableCreateRequest(2, false));
            tableService.changeEmpty(orderTable.getId(), new OrderTableChangeEmptyRequest(true));

            // when & then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), new OrderTableChangeNumberOfGuestsRequest(1)))
                .isInstanceOf(IllegalArgumentException.class);
        }
    }
}