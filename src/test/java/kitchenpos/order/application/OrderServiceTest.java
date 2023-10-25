package kitchenpos.order.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Collections;
import java.util.List;
import kitchenpos.config.ServiceTest;
import kitchenpos.dto.OrderLineItemDto;
import kitchenpos.fixture.OrderTableFixture;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.repository.MenuGroupRepository;
import kitchenpos.order.application.request.OrderCreateRequest;
import kitchenpos.order.application.request.OrderStatusUpdateRequest;
import kitchenpos.order.application.response.OrderResponse;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.repository.OrderTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
@ServiceTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuRepository menuRepository;

    private Menu menu;

    private OrderTable orderTable;

    @BeforeEach
    void setUp() {
        MenuGroup menuGroup = menuGroupRepository.save(MenuGroup.builder().name("인형").build());
        menu = menuRepository.save(Menu.builder().menuGroupId(menuGroup.getId()).name("벨리곰").price(10_000).build());
        orderTable = orderTableRepository.save(OrderTableFixture.createOrderTable(null, false, 5));
    }

    @Nested
    class 주문을_생성할_때 {

        @Test
        void 정상적으로_생성한다() {
            OrderLineItemDto orderLineItemDto = new OrderLineItemDto(menu.getId(), 1);
            OrderCreateRequest orderCreateRequest = new OrderCreateRequest(orderTable.getId(),
                    List.of(orderLineItemDto));

            OrderResponse response = orderService.create(orderCreateRequest);

            assertAll(
                    () -> assertThat(response.getId()).isPositive(),
                    () -> assertThat(response.getOrderTableId()).isEqualTo(orderTable.getId()),
                    () -> assertThat(response.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
                    () -> assertThat(response.getOrderLineItemDtos().get(0).getMenuId()).isEqualTo(menu.getId()),
                    () -> assertThat(response.getOrderLineItemDtos().get(0).getQuantity()).isEqualTo(1)
            );
        }

        @Test
        void 주문_아이템이_없으면_예외가_발생한다() {
            OrderCreateRequest orderCreateRequest = new OrderCreateRequest(orderTable.getId(), Collections.emptyList());

            assertThatThrownBy(() -> orderService.create(orderCreateRequest))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("주문의 메뉴가 존재하지 않습니다.");
        }

        @Test
        void 주문_메뉴를_찾을_수_없으면_예외가_발생한다() {
            Long wrongMenuId = -1L;
            OrderLineItemDto orderLineItemDto = new OrderLineItemDto(wrongMenuId, 1);
            OrderCreateRequest orderCreateRequest = new OrderCreateRequest(orderTable.getId(),
                    List.of(orderLineItemDto));

            assertThatThrownBy(() -> orderService.create(orderCreateRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_테이블을_찾을_수_없으면_예외가_발생한다() {
            OrderLineItemDto orderLineItemDto = new OrderLineItemDto(menu.getId(), 1);
            OrderCreateRequest orderCreateRequest = new OrderCreateRequest(-1L, List.of(orderLineItemDto));

            assertThatThrownBy(() -> orderService.create(orderCreateRequest))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("주문 테이블이 존재하지 않습니다.");
        }

        @Test
        void 주문_테이블이_EMPTY_상태이면_예외가_발생한다() {
            OrderTable emptyOrderTable = orderTableRepository.save(OrderTableFixture.createOrderTable(null, true, 10));
            OrderLineItemDto orderLineItemDto = new OrderLineItemDto(menu.getId(), 1);
            OrderCreateRequest orderCreateRequest = new OrderCreateRequest(emptyOrderTable.getId(),
                    List.of(orderLineItemDto));

            assertThatThrownBy(() -> orderService.create(orderCreateRequest))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("EMPTY 상태인 테이블에 주문할 수 없습니다.");
        }
    }

    @Test
    void 모든_주문_엔티티를_조회한다() {
        OrderLineItemDto orderLineItemDto = new OrderLineItemDto(menu.getId(), 1);
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(orderTable.getId(),
                List.of(orderLineItemDto));
        orderService.create(orderCreateRequest);

        List<OrderResponse> orders = orderService.list();

        assertThat(orders).hasSizeGreaterThanOrEqualTo(1);
    }

    @Nested
    class 주문_상태를_변경할_때 {

        @Test
        void 정상적으로_변경한다() {
            OrderLineItemDto orderLineItemDto = new OrderLineItemDto(menu.getId(), 1);
            OrderCreateRequest orderCreateRequest = new OrderCreateRequest(orderTable.getId(),
                    List.of(orderLineItemDto));
            OrderResponse orderResponse = orderService.create(orderCreateRequest);

            OrderResponse updatedOrderResponse = orderService.changeOrderStatus(
                    orderResponse.getId(),
                    new OrderStatusUpdateRequest("COMPLETION")
            );

            assertThat(updatedOrderResponse.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name());
        }

        @Test
        void 주문_상태가_완료인_주문의_상태를_변경하면_예외가_발생한다() {
            OrderLineItemDto orderLineItemDto = new OrderLineItemDto(menu.getId(), 1);
            OrderCreateRequest orderCreateRequest = new OrderCreateRequest(orderTable.getId(),
                    List.of(orderLineItemDto));
            OrderResponse orderResponse = orderService.create(orderCreateRequest);
            orderService.changeOrderStatus(orderResponse.getId(), new OrderStatusUpdateRequest("COMPLETION"));

            assertThatThrownBy(() -> orderService.changeOrderStatus(orderResponse.getId(), new OrderStatusUpdateRequest("COMPLETION")))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("완료된 주문의 상태를 변경할 수 없습니다.");
        }

        @Test
        void 주문을_찾을_수_없으면_예외가_발생한다() {
            Long wrongOrderId = -1L;

            assertThatThrownBy(() -> orderService.changeOrderStatus(wrongOrderId, new OrderStatusUpdateRequest("COMPLETION")))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("일치하는 주문을 찾을 수 없습니다.");
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"hello", "gray", "done"})
        void 존재하는_주문_상태가_아니면_예외가_발생한다(String status) {
            OrderLineItemDto orderLineItemDto = new OrderLineItemDto(menu.getId(), 1);
            OrderCreateRequest orderCreateRequest = new OrderCreateRequest(orderTable.getId(),
                    List.of(orderLineItemDto));
            OrderResponse orderResponse = orderService.create(orderCreateRequest);

            assertThatThrownBy(() -> orderService.changeOrderStatus(orderResponse.getId(), new OrderStatusUpdateRequest(status)))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("일치하는 주문 상태를 찾을 수 없습니다.");
        }
    }
}
