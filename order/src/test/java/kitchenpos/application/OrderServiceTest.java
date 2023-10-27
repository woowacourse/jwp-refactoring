package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import javax.persistence.EntityManager;
import kitchenpos.dto.OrderCreateRequest;
import kitchenpos.dto.OrderCreateRequest.OrderLineItemDto;
import kitchenpos.dto.OrderResponse;
import kitchenpos.dto.OrderStatusRequest;
import kitchenpos.exception.CannotMakeOrderWithEmptyTableException;
import kitchenpos.exception.MenuNotFoundException;
import kitchenpos.exception.OrderNotFoundException;
import kitchenpos.exception.OrderTableNotFoundException;
import kitchenpos.exception.RequestOrderLineItemIsEmptyException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.Menu.ProductIdAndQuantity;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.Order.MenuIdQuantityAndPrice;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.exception.OrderStatusNotChangeableException;
import kitchenpos.product.domain.Product;
import kitchenpos.table.domain.OrderTable;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@Transactional
@SpringBootTest
class OrderServiceTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private OrderService orderService;

    @Nested
    class 주문_생성_테스트 {

        @Test
        void 주문을_정상_생성한다() {
            // given
            OrderTable orderTable = new OrderTable(4, false);
            MenuGroup menuGroup = new MenuGroup("menuGroup");
            Product product = Product.of("product", new BigDecimal(2500));
            em.persist(orderTable);
            em.persist(menuGroup);
            em.persist(product);
            Menu menu = Menu.of("menu", new BigDecimal(10_000), menuGroup.getId(),
                    List.of(new ProductIdAndQuantity(product.getId(), 4L)));
            em.persist(menu);
            em.flush();
            em.clear();
            OrderCreateRequest request = new OrderCreateRequest(orderTable.getId(),
                    List.of(new OrderLineItemDto(menu.getId(), 4L)));

            // when
            OrderResponse response = orderService.create(request);

            // then
            SoftAssertions.assertSoftly(softly -> {
                assertThat(response.getOrderTableId()).isEqualTo(orderTable.getId());
                assertThat(response.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
                assertThat(response.getOrderLineItems()).hasSize(1);
            });
        }

        @Test
        void 요청에_주문_항목_정보가_없으면_예외를_반환한다() {
            // given
            OrderCreateRequest request = new OrderCreateRequest(1L, Collections.emptyList());

            // when, then
            assertThrows(RequestOrderLineItemIsEmptyException.class,
                    () -> orderService.create(request));
        }

        @Test
        void 요청에_해당하는_주문_테이블이_존재하지_않으면_예외를_반환한다() {
            // given
            OrderCreateRequest request = new OrderCreateRequest(-1L,
                    List.of(new OrderLineItemDto(10L, 10L)));

            // when, then
            assertThrows(OrderTableNotFoundException.class, () -> orderService.create(request));
        }

        @Test
        void 요청의_주문_테이블이_빈_테이블이면_예외를_반환한다() {
            // given
            OrderTable orderTable = new OrderTable(0, true);
            em.persist(orderTable);
            em.flush();
            em.clear();
            OrderCreateRequest request = new OrderCreateRequest(orderTable.getId(),
                    List.of(new OrderLineItemDto(1L, 4L)));

            // when, then
            assertThrows(CannotMakeOrderWithEmptyTableException.class,
                    () -> orderService.create(request));
        }

        @Test
        void 요청의_주문_항목에_해당하는_메뉴가_존재하지_않으면_예외를_반환한다() {
            // given
            OrderTable orderTable = new OrderTable(4, false);
            em.persist(orderTable);
            em.flush();
            em.clear();
            OrderCreateRequest request = new OrderCreateRequest(orderTable.getId(),
                    List.of(new OrderLineItemDto(-1L, 4L)));

            // when, then
            assertThrows(MenuNotFoundException.class, () -> orderService.create(request));
        }
    }

    @Test
    void 주문을_전체_조회한다() {
        assertThat(orderService.list()).isInstanceOf(List.class);
    }

    @Nested
    class 주문_상태_변경_테스트 {

        @Test
        void 주문의_상태를_변경한다() {
            // given
            MenuGroup menuGroup = new MenuGroup("menuGroup");
            Product product = Product.of("product", new BigDecimal(2500));
            em.persist(menuGroup);
            em.persist(product);
            OrderTable orderTable = new OrderTable(4, false);
            em.persist(orderTable);
            Menu menu = Menu.of("menu", new BigDecimal(10_000), menuGroup.getId(),
                    List.of(new ProductIdAndQuantity(product.getId(), 4L)));
            em.persist(menu);
            Order order = Order.of(orderTable.getId(), OrderStatus.COOKING, LocalDateTime.now(),
                    List.of(new MenuIdQuantityAndPrice(menu.getId(), 1L, menu.getPrice())));
            em.persist(order);
            em.flush();
            em.clear();
            OrderStatusRequest request = new OrderStatusRequest(
                    OrderStatus.COOKING.name());
            // when
            OrderResponse response = orderService.changeOrderStatus(order.getId(), request);

            // then
            SoftAssertions.assertSoftly(softly -> {
                assertThat(response.getId()).isEqualTo(order.getId());
                assertThat(response.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
            });
        }

        @Test
        void 변경하려는_주문_id에_해당하는_주문이_존재하지_않으면_예외를_반환한다() {
            // given
            OrderStatusRequest request = new OrderStatusRequest(OrderStatus.MEAL.name());
            // when, then
            assertThrows(OrderNotFoundException.class,
                    () -> orderService.changeOrderStatus(-1L, request));
        }

        @Test
        void 변경하려는_주문의_주문_상태가_계산_완료인_경우_예외를_반환한다() {
            // given
            MenuGroup menuGroup = new MenuGroup("menuGroup");
            Product product = Product.of("product", new BigDecimal(2500));
            em.persist(menuGroup);
            em.persist(product);
            OrderTable orderTable = new OrderTable(4, false);
            em.persist(orderTable);
            Menu menu = Menu.of("menu", new BigDecimal(10_000), menuGroup.getId(),
                    List.of(new ProductIdAndQuantity(product.getId(), 4L)));
            em.persist(menu);
            Order order = Order.of(orderTable.getId(), OrderStatus.COMPLETION, LocalDateTime.now(),
                    List.of(new MenuIdQuantityAndPrice(menu.getId(), 1L, menu.getPrice())));
            em.persist(order);
            em.flush();
            em.clear();
            OrderStatusRequest request = new OrderStatusRequest(
                    OrderStatus.MEAL.name());

            // when, then
            assertThrows(OrderStatusNotChangeableException.class,
                    () -> orderService.changeOrderStatus(order.getId(), request));
        }
    }
}
