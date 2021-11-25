package kitchenpos.order.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Collections;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.repository.MenuRepository;
import kitchenpos.order.exception.MenuNotFoundException;
import kitchenpos.order.exception.OrderTableEmptyException;
import kitchenpos.order.exception.OrderTableNotFoundException;
import kitchenpos.order.ui.request.OrderLineItemRequest;
import kitchenpos.order.ui.request.OrderRequest;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.repository.OrderTableRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@DisplayName("OrderValidator 통합 테스트")
@SpringBootTest
class OrderValidatorTest {

    private static final long MENU_GROUP_ID = 1L;

    @Autowired
    private OrderValidator orderValidator;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private MenuRepository menuRepository;

    @DisplayName("생성 요청을 검증할 때")
    @Nested
    class ValidateCreate {

        @DisplayName("OrderTableId 에 해당하는 테이블이 존재하지 않는 경우 예외가 발생한다.")
        @Test
        void orderTableNotFoundException() {
            // given
            Menu menu = menuRepository.save(new Menu("햄버거 단품 메뉴", BigDecimal.valueOf(3_000), MENU_GROUP_ID));

            OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(menu.getId(), 2L);
            OrderRequest request = new OrderRequest(Long.MAX_VALUE, Collections.singletonList(orderLineItemRequest));

            // when, then
            assertThatThrownBy(() -> orderValidator.validateOrder(request))
                .isExactlyInstanceOf(OrderTableNotFoundException.class);
        }

        @DisplayName("테이블이 비어있는 경우 예외가 발생한다.")
        @Test
        void orderTableEmptyFoundException() {
            // given
            Menu menu = menuRepository.save(new Menu("햄버거 단품 메뉴", BigDecimal.valueOf(3_000), MENU_GROUP_ID));
            OrderTable orderTable = orderTableRepository.save(new OrderTable(5, true));

            OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(menu.getId(), 2L);
            OrderRequest request = new OrderRequest(orderTable.getId(), Collections.singletonList(orderLineItemRequest));

            // when, then
            assertThatThrownBy(() -> orderValidator.validateOrder(request))
                .isExactlyInstanceOf(OrderTableEmptyException.class);
        }

        @DisplayName("MenuId 에 해당하는 메뉴가 존재하지 않는 경우 예외가 발생한다.")
        @Test
        void menuNotFoundException() {
            // given
            OrderTable orderTable = orderTableRepository.save(new OrderTable(5, false));

            OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(Long.MAX_VALUE, 2L);
            OrderRequest request = new OrderRequest(orderTable.getId(), Collections.singletonList(orderLineItemRequest));

            // when, then
            assertThatThrownBy(() -> orderValidator.validateOrder(request))
                .isExactlyInstanceOf(MenuNotFoundException.class);
        }
    }
}
