package kitchenpos.order.application;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.application.OrderService;
import kitchenpos.common.ServiceTest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.OrderChangeStatusRequest;
import kitchenpos.dto.OrderCreateRequest;
import kitchenpos.dto.OrderLineItemInOrderDto;
import kitchenpos.dto.OrderResponse;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderLineItemRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
class OrderServiceTest extends ServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private OrderLineItemRepository orderLineItemRepository;

    @Nested
    class create_성공_테스트 {

        @Test
        void 주문_생성을_할_수_있다() {
            // given
            tableGroupRepository.save(TableGroup.create());
            final var orderTable = orderTableRepository.save(new OrderTable(1L, 3, false));
            final var menuGroup = menuGroupRepository.save(new MenuGroup("메뉴_그룹_이름"));
            menuRepository.save(new Menu("메뉴_이름", BigDecimal.valueOf(0), menuGroup, List.of()));
            final var orderLineitem = new OrderLineItem(1L, "메뉴_이름", BigDecimal.valueOf(0), 5L);
            orderRepository.save(new Order(orderTable, OrderStatus.COOKING, List.of(orderLineitem)));
            orderLineItemRepository.save(orderLineitem);

            final var orderLineItemRequest = new OrderLineItemInOrderDto(1L, "메뉴_이름", BigDecimal.ZERO, 5L);
            final var request = new OrderCreateRequest(1L, List.of(orderLineItemRequest));

            // when
            final var actual = orderService.create(request);

            // then
            assertThat(actual.getId()).isExactlyInstanceOf(Long.class);
        }
    }

    @Nested
    class create_실패_테스트 {

        @Test
        void 존재하지_않는_주문_테이블을_사용하면_예외를_반환한다() {
            // given
            tableGroupRepository.save(TableGroup.create());
            final var orderTable = orderTableRepository.save(new OrderTable(1L, 3, false));
            final var menuGroup = menuGroupRepository.save(new MenuGroup("메뉴_그룹_이름"));
            menuRepository.save(new Menu("메뉴_이름", BigDecimal.valueOf(0), menuGroup, List.of()));
            final var orderLineItem = new OrderLineItem(1L, "메뉴_이름", BigDecimal.valueOf(0), 5);
            orderRepository.save(new Order(orderTable, OrderStatus.COOKING, List.of(orderLineItem)));

            final var orderLineItemRequest = new OrderLineItemInOrderDto(1L, "메뉴_이름", BigDecimal.ZERO, 5L);
            final var orderCreateRequest = new OrderCreateRequest(2L, List.of(orderLineItemRequest));

            // when & then
            assertThatThrownBy(() -> orderService.create(orderCreateRequest))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("[ERROR] 존재하지 않는 주문 테이블입니다.");
        }

        @Test
        void 존재하지_않는_메뉴를_사용하면_예외를_반환한다() {
            // given
            tableGroupRepository.save(TableGroup.create());
            orderTableRepository.save(new OrderTable(1L, 3, false));

            final var orderLineItemRequest = new OrderLineItemInOrderDto(999L, "메뉴_이름", BigDecimal.ZERO, 5L);
            final var request = new OrderCreateRequest(1L, List.of(orderLineItemRequest));

            // when & then
            assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("[ERROR] 존재하지 않는 메뉴입니다.");
        }
    }

    @Nested
    class list_성공_테스트 {

        @Test
        void 주문_목록이_존재하지_않으면_빈_값을_반환한다() {
            // given & when
            final var actual = orderService.list();

            // then
            assertThat(actual).isEmpty();
        }

        @Test
        void 주문이_하나_이상_존재하면_주문_목록을_반환한다() {
            // given
            tableGroupRepository.save(TableGroup.create());
            final var orderTable = orderTableRepository.save(new OrderTable(1L, 5, false));
            final var menuGroup = menuGroupRepository.save(new MenuGroup("메뉴_그룹_이름"));
            menuRepository.save(new Menu("메뉴_이름", BigDecimal.valueOf(0), menuGroup, List.of()));
            final var orderLineItem = new OrderLineItem(1L, "메뉴_이름", BigDecimal.valueOf(0), 5);
            final var order = orderRepository.save(new Order(orderTable, OrderStatus.COOKING, List.of(orderLineItem)));

            final var expected = List.of(OrderResponse.from(order));

            // when
            final var actual = orderService.list();

            // then
            assertThat(actual).usingRecursiveComparison()
                    .isEqualTo(expected);
        }
    }

    @Nested
    class list_실패_테스트 {
    }

    @Nested
    class changeOrderStatus_성공_테스트 {

        @Test
        void 주문_상태_변경을_할_수_있다() {
            // given
            tableGroupRepository.save(TableGroup.create());
            final var orderTable = orderTableRepository.save(new OrderTable(1L, 5, false));
            final var menuGroup = menuGroupRepository.save(new MenuGroup("메뉴_그룹_이름"));
            menuRepository.save(new Menu("메뉴_이름", BigDecimal.valueOf(0L), menuGroup, List.of()));
            final var orderLineItem = new OrderLineItem(1L, "메뉴_이름", BigDecimal.valueOf(0), 5L);
            orderRepository.save(new Order(orderTable, OrderStatus.COOKING, List.of(orderLineItem)));
            orderLineItemRepository.save(orderLineItem);

            final var request = new OrderChangeStatusRequest("COOKING");

            // when
            final var actual = orderService.changeOrderStatus(1L, request);
            final var expected = OrderResponse.from(orderRepository.findById(1L).get());

            // then
            assertThat(actual).usingRecursiveComparison()
                    .isEqualTo(expected);
        }
    }

    @Nested
    class changeOrderStatus_실패_테스트 {

        @Test
        void 존재하지_않는_주문의_상태를_변경하면_에러를_반환한다() {
            // given
            final var request = new OrderChangeStatusRequest("COOKING");

            // when & then
            assertThatThrownBy(() -> orderService.changeOrderStatus(1L, request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("[ERROR] 존재하지 않는 주문입니다.");
        }
    }
}
