package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.ServiceTest;
import kitchenpos.application.CreateOrderCommand.OrderLineItemRequest;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.domain.OrderValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
@ServiceTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @MockBean(name = "orderValidator")
    private OrderValidator orderValidator;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @BeforeEach
    void setUp() {
        doNothing().when(orderValidator).validate(any(Order.class));
    }


    @Nested
    class 주문_생성 {

        @Test
        void 성공() {
            //given
            OrderLineItemRequest 주문상품 = 주문_상품_초기화();
            OrderTable 테이블 = 비어있지_않은_테이블_생성();

            CreateOrderCommand 커맨드 = new CreateOrderCommand(테이블.getId(), List.of(주문상품));

            //when
            OrderDto 실제주문 = orderService.create(커맨드);

            //then
            assertThat(orderRepository.existsById(실제주문.getId())).isTrue();
        }

    }

    private OrderTable 비어있지_않은_테이블_생성() {
        final var 테이블 = new OrderTable(4, false);
        return orderTableRepository.save(테이블);
    }

    private OrderLineItemRequest 주문_상품_초기화() {
        final var 메뉴 = menuRepository.findAll().get(0);
        return new OrderLineItemRequest(메뉴.getId(), 1L);
    }

    @Test
    void 주문_리스트_조회() {
        //given
        var 존재하는_주문_아이디 = orderRepository.findAll().stream()
                .map(Order::getId)
                .collect(Collectors.toList());

        //when
        List<OrderDto> 주문_리스트 = orderService.list();

        //then
        assertThat(주문_리스트).extracting(OrderDto::getId)
                .containsAll(존재하는_주문_아이디);
    }

    @Nested
    class 주문_상태_변경 {

        private OrderDto 주문_생성() {
            OrderLineItemRequest 주문상품 = 주문_상품_초기화();
            OrderTable 테이블 = 비어있지_않은_테이블_생성();

            CreateOrderCommand 커맨드 = new CreateOrderCommand(테이블.getId(), List.of(주문상품));

            return orderService.create(커맨드);
        }

        @Test
        void 성공() {
            //given
            OrderDto 주문 = 주문_생성();

            ChangeOrderStatusCommand 커맨드 = new ChangeOrderStatusCommand(주문.getId(),
                    OrderStatus.MEAL.name());

            //when
            OrderDto 실제주문 = orderService.changeOrderStatus(커맨드);

            //then
            assertThat(실제주문.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
        }

        @Test
        void 주문이_존재하지_않으면_예외가_발생한다() {
            OrderTable 테이블_엔티티 = new OrderTable(4, false);
            OrderTable 테이블 = orderTableRepository.save(테이블_엔티티);
            orderTableRepository.delete(테이블);

            ChangeOrderStatusCommand 커맨드 = new ChangeOrderStatusCommand(테이블.getId(),
                    OrderStatus.COOKING.name());
            //expect
            assertThatThrownBy(() -> orderService.changeOrderStatus(커맨드))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_상태가_COMPLETION이면_예외가_발생한다() {
            //given
            Order 생성된_주문 = orderRepository.getById(주문_생성().getId());
            생성된_주문.startMeal();
            생성된_주문.completeOrder();
            orderRepository.save(생성된_주문);

            ChangeOrderStatusCommand 커맨드 = new ChangeOrderStatusCommand(생성된_주문.getId(),
                    OrderStatus.COMPLETION.name());

            //expect
            assertThatThrownBy(() -> orderService.changeOrderStatus(커맨드))
                    .isInstanceOf(IllegalArgumentException.class);
        }

    }

}
