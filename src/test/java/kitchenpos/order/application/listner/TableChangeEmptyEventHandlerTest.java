package kitchenpos.order.application.listner;

import static kitchenpos.order.domain.exception.OrderExceptionType.ORDER_IS_NOT_COMPLETION;
import static kitchenpos.support.fixture.OrderFixture.createOrderLineItem;
import static kitchenpos.support.fixture.TableFixture.비어있지_않는_주문_테이블_DTO;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.menu.application.dto.MenuDto;
import kitchenpos.order.application.dto.OrderDto;
import kitchenpos.order.application.dto.OrderLineItemDto;
import kitchenpos.order.domain.exception.OrderException;
import kitchenpos.support.ServiceIntegrationTest;
import kitchenpos.table.application.TableChangeEmptyEvent;
import kitchenpos.table.application.dto.OrderTableDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class TableChangeEmptyEventHandlerTest extends ServiceIntegrationTest {

    @Autowired
    private TableChangeEmptyEventHandler eventHandler;

    @Test
    @DisplayName("order가 cooking이나 meal 상태인 경우 예외처리")
    void throwExceptionTableIsEmpty() {
        //given
        final OrderTableDto orderTableDto = 비어있지_않는_주문_테이블_DTO();
        final OrderTableDto savedOrderTableDto = tableService.create(orderTableDto);
        createOrderSuccessfully(savedOrderTableDto);

        final TableChangeEmptyEvent event = new TableChangeEmptyEvent(savedOrderTableDto.getId());

        //when
        assertThatThrownBy(
            () -> eventHandler.handle(event)
        ).isInstanceOf(OrderException.class)
            .hasMessage(ORDER_IS_NOT_COMPLETION.getMessage());
    }

    private void createOrderSuccessfully(final OrderTableDto orderTableDto) {
        final MenuDto menuDto = createMenu();
        final OrderLineItemDto orderLineItemDto = createOrderLineItem(menuDto.getId(), 1L);

        final OrderDto orderDto = new OrderDto(
            null,
            orderTableDto.getId(),
            null,
            LocalDateTime.now(),
            List.of(orderLineItemDto)
        );
        orderService.create(orderDto);
    }
}
