package kitchenpos.order.domain.service;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

import java.util.List;
import java.util.Optional;
import kitchenpos.menu.domain.repository.MenuRepository;
import kitchenpos.menu.supports.MenuFixture;
import kitchenpos.order.domain.model.Order;
import kitchenpos.order.domain.model.OrderLineItem;
import kitchenpos.order.supports.OrderFixture;
import kitchenpos.order.supports.OrderLineItemFixture;
import kitchenpos.table.domain.model.OrderTable;
import kitchenpos.table.domain.repository.OrderTableRepository;
import kitchenpos.table.supports.OrderTableFixture;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class OrderValidatorTest {

    @Mock
    MenuRepository menuRepository;

    @Mock
    OrderTableRepository orderTableRepository;

    @InjectMocks
    OrderValidator orderValidator;

    @Test
    void 주문_테이블은_DB에_존재해야한다() {
        // given
        Long orderTableId = 1L;
        Order order = OrderFixture.fixture().orderTableId(orderTableId).build();

        given(orderTableRepository.findById(orderTableId))
            .willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> orderValidator.validate(order))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("존재하지 않는 주문 테이블입니다.");
    }

    @Test
    void 빈_주문_테이블에_주문_생성_불가() {
        // given
        Long orderTableId = 1L;
        Order order = OrderFixture.fixture().orderTableId(orderTableId).build();
        OrderTable orderTable = OrderTableFixture.fixture().id(orderTableId).empty(true).build();

        given(orderTableRepository.findById(orderTableId))
            .willReturn(Optional.of(orderTable));

        // when & then
        assertThatThrownBy(() -> orderValidator.validate(order))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("빈 주문 테이블입니다.");
    }

    @Test
    void 주문_항목의_메뉴들은_전부_DB에_존재해야한다() {
        // given
        Long orderTableId = 1L;
        OrderTable orderTable = OrderTableFixture.fixture().id(orderTableId).empty(false).build();
        List<OrderLineItem> orderLineItems = List.of(
            OrderLineItemFixture.fixture().menuId(1L).build(),
            OrderLineItemFixture.fixture().menuId(2L).build()
        );
        Order order = OrderFixture.fixture().orderTableId(orderTableId).orderLineItems(orderLineItems).build();

        given(orderTableRepository.findById(orderTableId))
            .willReturn(Optional.of(orderTable));
        given(menuRepository.findAllById(eq(List.of(1L, 2L))))
            .willReturn(List.of(MenuFixture.fixture().id(1L).build()));

        // when & then
        assertThatThrownBy(() -> orderValidator.validate(order))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("올바르지 않은 메뉴입니다.");
    }

    @Test
    void 주문_생성_검증_성공() {
        // given
        Long orderTableId = 1L;
        OrderTable orderTable = OrderTableFixture.fixture().id(orderTableId).empty(false).build();
        List<OrderLineItem> orderLineItems = List.of(
            OrderLineItemFixture.fixture().menuId(1L).build(),
            OrderLineItemFixture.fixture().menuId(2L).build()
        );
        Order order = OrderFixture.fixture().orderTableId(orderTableId).orderLineItems(orderLineItems).build();

        given(orderTableRepository.findById(orderTableId))
            .willReturn(Optional.of(orderTable));
        given(menuRepository.findAllById(eq(List.of(1L, 2L))))
            .willReturn(List.of(
                MenuFixture.fixture().id(1L).build(),
                MenuFixture.fixture().id(2L).build()));

        // when & then
        assertThatNoException()
            .isThrownBy(() -> orderValidator.validate(order));
    }
}
