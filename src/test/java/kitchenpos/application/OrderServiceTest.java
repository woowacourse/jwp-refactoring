package kitchenpos.application;

import static kitchenpos.fixture.MenuGroupFixtures.한마리메뉴_그룹;
import static kitchenpos.fixture.OrderTableFixtures.빈_테이블1;
import static kitchenpos.fixture.OrderTableFixtures.주문_테이블9;
import static kitchenpos.fixture.OrderTableFixtures.테이블_생성;
import static kitchenpos.fixture.ProductFixtures.후라이드_상품;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.order.application.dto.OrderLineItemRequest;
import kitchenpos.order.application.dto.OrderRequest;
import kitchenpos.order.application.dto.OrderStatusUpdateRequest;
import kitchenpos.application.support.IntegrationTest;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.repository.MenuGroupRepository;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.repository.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.repository.OrderTableRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
public class OrderServiceTest {

    @Autowired
    private OrderService sut;
    
    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Nested
    @DisplayName("주문 등록")
    class CreateTest {

        @DisplayName("정상적인 경우 주문을 등록할 수 있다.")
        @Test
        void createOrder() {
            final OrderTable orderTable = orderTableRepository.save(테이블_생성(주문_테이블9.getNumberOfGuests(), 주문_테이블9.isEmpty()));
            final Product product = productRepository.findById(후라이드_상품.getId()).get();
            final MenuProduct menuProduct = new MenuProduct(product, 5L);
            final MenuGroup menuGroup = menuGroupRepository.findById(한마리메뉴_그룹.getId()).get();
            final Menu menu = menuRepository.save(new Menu("한마리메뉴", BigDecimal.TEN, menuGroup, List.of(menuProduct)));

            final OrderRequest orderRequest = new OrderRequest(orderTable.getId(),
                    List.of(new OrderLineItemRequest(menu.getId(), 5L)));
            final Order actual = sut.create(orderRequest);

            assertAll(
                    () -> assertThat(actual.getId()).isNotNull(),
                    () -> assertThat(actual.getOrderTable().getId()).isEqualTo(orderTable.getId()),
                    () -> assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.COOKING),
                    () -> assertThat(actual.getOrderLineItems()).hasSize(1)
            );
        }

        @DisplayName("주문 항목이 비어있으면 등록할 수 없다.")
        @Test
        void createOrderWithNullOrderLineItem() {
            final OrderTable orderTable = orderTableRepository.save(테이블_생성(주문_테이블9.getNumberOfGuests(), 주문_테이블9.isEmpty()));

            final OrderRequest orderRequest = new OrderRequest(orderTable.getId(), List.of());

            assertThatThrownBy(() -> sut.create(orderRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문 항목에 쓰여진 메뉴가 존재하지 않으면 등록할 수 없다.")
        @Test
        void createOrderWithNotExistMenu() {
            final Long 존재하지_않는_메뉴_ID = -1L;

            final OrderTable orderTable = orderTableRepository.save(테이블_생성(주문_테이블9.getNumberOfGuests(), 주문_테이블9.isEmpty()));

            final OrderRequest orderRequest = new OrderRequest(orderTable.getId(),
                    List.of(new OrderLineItemRequest(존재하지_않는_메뉴_ID, 5L)));
            assertThatThrownBy(() -> sut.create(orderRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("해당 주문이 속한 주문 테이블이 존재하지 않으면 등록할 수 없다.")
        @Test
        void createOrderWithNotExistOrderTable() {
            final Long 존재하지_않는_주문_테이블_ID = -1L;
            final Product product = productRepository.findById(후라이드_상품.getId()).get();
            final MenuProduct menuProduct = new MenuProduct(product, 5L);
            final MenuGroup menuGroup = menuGroupRepository.findById(한마리메뉴_그룹.getId()).get();
            final Menu menu = menuRepository.save(new Menu("한마리메뉴", BigDecimal.TEN, menuGroup, List.of(menuProduct)));

            final OrderRequest orderRequest = new OrderRequest(존재하지_않는_주문_테이블_ID,
                    List.of(new OrderLineItemRequest(menu.getId(), 5L)));

            assertThatThrownBy(() -> sut.create(orderRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("해당 주문이 속한 주문 테이블이 빈 테이블이면 등록할 수 없다.")
        @Test
        void createOrderWithEmptyTable() {
            final Product product = productRepository.findById(후라이드_상품.getId()).get();
            final MenuProduct menuProduct = new MenuProduct(product, 5L);
            final MenuGroup menuGroup = menuGroupRepository.findById(한마리메뉴_그룹.getId()).get();
            final Menu menu = menuRepository.save(new Menu("한마리메뉴", BigDecimal.TEN, menuGroup, List.of(menuProduct)));

            final OrderRequest orderRequest = new OrderRequest(빈_테이블1.getId(),
                    List.of(new OrderLineItemRequest(menu.getId(), 5L)));

            assertThatThrownBy(() -> sut.create(orderRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("주문 목록을 조회할 수 있다.")
    @Test
    void getOrders() {
        final Product product = productRepository.findById(후라이드_상품.getId()).get();
        final MenuProduct menuProduct1 = new MenuProduct(product, 5L);
        final MenuGroup menuGroup = menuGroupRepository.findById(한마리메뉴_그룹.getId()).get();
        final Menu menu1 = menuRepository.save(new Menu("한마리메뉴", BigDecimal.TEN, menuGroup, List.of(menuProduct1)));

        final MenuProduct menuProduct2 = new MenuProduct(product, 5L);
        final Menu menu2 = menuRepository.save(new Menu("한마리메뉴", BigDecimal.TEN, menuGroup, List.of(menuProduct2)));

        final OrderTable orderTable = orderTableRepository.save(테이블_생성(5, false));

        final OrderRequest orderRequest = new OrderRequest(orderTable.getId(),
                List.of(new OrderLineItemRequest(menu1.getId(), 5L), new OrderLineItemRequest(menu2.getId(), 5L)));
        sut.create(orderRequest);

        assertThat(sut.list()).hasSize(1);
    }

    @DisplayName("주문이 존재하지 않으면 변경할 수 없다.")
    @Test
    void changeOrderStatusWithNotExistOrder() {
        final Long 존재하지_않는_주문_ID = -1L;

        final OrderStatusUpdateRequest request = new OrderStatusUpdateRequest(OrderStatus.COOKING.name());

        assertThatThrownBy(() -> sut.changeOrderStatus(존재하지_않는_주문_ID, request))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
