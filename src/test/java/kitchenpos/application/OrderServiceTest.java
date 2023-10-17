package kitchenpos.application;

import static kitchenpos.domain.OrderStatus.COMPLETION;
import static kitchenpos.domain.OrderStatus.COOKING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import kitchenpos.application.request.OrderCreateRequest;
import kitchenpos.application.request.OrderLineItemCreateRequest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.persistence.MenuGroupRepository;
import kitchenpos.persistence.MenuRepository;
import kitchenpos.persistence.OrderRepository;
import kitchenpos.persistence.OrderTableRepository;
import kitchenpos.persistence.ProductRepository;
import kitchenpos.support.ServiceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class OrderServiceTest extends ServiceTest {

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private OrderService orderService;

    private Menu menu;

    @BeforeEach
    void createMenu() {
        Product product = productRepository.save(new Product("족발", BigDecimal.valueOf(1000.00)));
        Long menuGroupId = menuGroupRepository.save(new MenuGroup("세트")).getId();
        menu = menuRepository.save(
            new Menu("황족발", BigDecimal.valueOf(1800.00), menuGroupId, List.of(new MenuProduct(product, 2))));

    }

    @Nested
    class 메뉴_생성시 {

        @Test
        void 성공() {
            // given
            Long orderTableId = orderTableRepository.save(new OrderTable(5, false)).getId();
            var orderLineItem = new OrderLineItemCreateRequest(menu.getId(), 2);
            var request = new OrderCreateRequest(orderTableId, List.of(orderLineItem));

            // when
            Order actual = orderService.create(request);

            // then
            assertAll(
                () -> assertThat(actual.getId()).isPositive(),
                () -> assertThat(actual.getOrderLineItems())
                    .allSatisfy(it -> assertThat(it.getId()).isPositive())
            );
        }

        @Test
        void 오더라인_아이템이_없으면_예외() {
            // given
            Long orderTableId = orderTableRepository.save(new OrderTable(5, false)).getId();
            var request = new OrderCreateRequest(orderTableId, Collections.emptyList());

            // when && then
            assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문한_메뉴가_없는_메뉴가_있으면_예외() {
            // given
            Long orderTableId = orderTableRepository.save(new OrderTable(5, false)).getId();
            var orderLineItem = new OrderLineItemCreateRequest(-1L, 1);
            var request = new OrderCreateRequest(orderTableId, List.of(orderLineItem));

            // when && then
            assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문테이블이_비어있으면_예외() {
            // given
            Long orderTableId = orderTableRepository.save(new OrderTable(5, true)).getId();
            var orderLineItem = new OrderLineItemCreateRequest(menu.getId(), 2);
            var request = new OrderCreateRequest(orderTableId, List.of(orderLineItem));

            // when && then
            assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    void 주문_목록을_조회() {
        // given
        List<Order> expected = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            OrderTable orderTable = orderTableRepository.save(new OrderTable(5, false));
            expected.add(orderRepository.save(new Order(orderTable, COOKING, List.of(new OrderLineItem(1L, 5)))));
        }

        // when
        List<Order> actual = orderService.list();

        // then
        assertThat(actual).usingRecursiveComparison()
            .ignoringFields("orderTable")
            .isEqualTo(expected);
    }

    @Nested
    class 주문_상태_변경시 {

        @ParameterizedTest
        @CsvSource(value = {"COOKING : MEAL", "MEAL : COOKING"}, delimiter = ':')
        void 성공(OrderStatus originStatus, OrderStatus changedStatus) {
            // given
            OrderTable orderTableA = orderTableRepository.save(new OrderTable(5, false));
            Order order = new Order(orderTableA, originStatus, List.of(new OrderLineItem(1L, 5)));
            Long orderId = orderRepository.save(order).getId();

            // when
            Order actual = orderService.changeOrderStatus(orderId, changedStatus);

            // then
            assertThat(actual.getOrderStatus()).isEqualTo(changedStatus);
        }

        @Test
        void 바꿀려는_주문의_상태가_완료면_예외() {
            // given
            OrderTable orderTableA = orderTableRepository.save(new OrderTable(5, false));
            Order order = new Order(orderTableA, COMPLETION, List.of(new OrderLineItem(1L, 5)));
            Long orderId = orderRepository.save(order).getId();

            // when && then
            assertThatThrownBy(() -> orderService.changeOrderStatus(orderId, COOKING))
                .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
