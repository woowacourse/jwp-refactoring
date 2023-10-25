package kitchenpos.ui;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.domain.menuproduct.MenuProduct;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.ordertable.OrderTable;
import kitchenpos.domain.product.Product;
import kitchenpos.ui.request.OrderCreateRequest;
import kitchenpos.ui.request.OrderLineItemCreateRequest;
import kitchenpos.ui.response.OrderResponse;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
public class OrderIntegrationTest extends IntegrationTest {

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Nested
    class 주문_등록_시 {

        @Test
        void 주문을_정상적으로_등록한다() {
            // given
            final var product = new Product("후라이드", BigDecimal.valueOf(16000));
            productDao.save(product);
            final var menuProduct = new MenuProduct(product, 1L);
            final var menuGroup = menuGroupDao.save(new MenuGroup("치킨"));
            final var menu = menuDao.save(new Menu("후라이드", BigDecimal.valueOf(16000), List.of(menuProduct), menuGroup));
            final var orderTable = orderTableDao.save(new OrderTable(1, false));

            final var request = new OrderCreateRequest(
                    orderTable.getId(),
                    List.of(new OrderLineItemCreateRequest(menu.getId(), 1))
            );

            // when
            final var response = restTemplate.postForEntity("/api/orders", request, OrderResponse.class);

            // then
            assertAll(
                    () -> assertThat(response.getBody().getId()).isNotNull(),
                    () -> assertThat(response.getBody().getOrderTableId()).isEqualTo(request.getOrderTableId()),
                    () -> assertThat(response.getBody().getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
                    () -> assertThat(response.getBody().getOrderLineItems()).hasSize(1)
            );
        }

        @Test
        void 주문을_등록할_때_주문_테이블이_존재하지_않으면_예외가_발생한다() {
            // given
            final var product = new Product("후라이드", BigDecimal.valueOf(16000));
            productDao.save(product);
            final var menuProduct = new MenuProduct(product, 1L);
            final var menuGroup = menuGroupDao.save(new MenuGroup("치킨"));
            final var menu = menuDao.save(new Menu("후라이드", BigDecimal.valueOf(16000), List.of(menuProduct), menuGroup));

            final var request = new OrderCreateRequest(
                    Long.MAX_VALUE,
                    List.of(new OrderLineItemCreateRequest(menu.getId(), 1))
            );

            // when
            final var response = restTemplate.postForEntity("/api/orders", request, String.class);

            // then
            assertThat(response.getStatusCodeValue()).isBetween(400, 500);
        }

        @Test
        void 주문을_등록할_때_주문_테이블이_비어있으면_예외가_발생한다() {
            // given
            final var orderTable = orderTableDao.save(new OrderTable(0, true));
            final var product = new Product("후라이드", BigDecimal.valueOf(16000));
            productDao.save(product);
            final var menuProduct = new MenuProduct(product, 1L);
            final var menuGroup = menuGroupDao.save(new MenuGroup("치킨"));
            final var menu = menuDao.save(new Menu("후라이드", BigDecimal.valueOf(16000), List.of(menuProduct), menuGroup));

            final var request = new OrderCreateRequest(
                    orderTable.getId(),
                    List.of(new OrderLineItemCreateRequest(menu.getId(), 1))
            );

            // when
            final var response = restTemplate.postForEntity("/api/orders", request, String.class);

            // then
            assertThat(response.getStatusCodeValue()).isBetween(400, 500);
        }
    }
}
