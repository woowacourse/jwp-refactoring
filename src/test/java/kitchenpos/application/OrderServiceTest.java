package kitchenpos.application;

import static java.time.LocalDateTime.now;
import static kitchenpos.fixture.Fixture.menuFixture;
import static kitchenpos.fixture.Fixture.menuGroupFixture;
import static kitchenpos.fixture.Fixture.menuProductFixture;
import static kitchenpos.fixture.Fixture.orderFixture;
import static kitchenpos.fixture.Fixture.orderTableFixture;
import static kitchenpos.fixture.Fixture.productFixture;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.fixture.Fixture;

@SpringBootTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;
    @Autowired
    private TableService tableService;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private ProductDao productDao;

    private Order order;
    private OrderLineItem orderLineItem;
    private OrderTable orderTable;

    @BeforeEach
    void setUp() {
        final MenuGroup boonsik = menuGroupDao.save(menuGroupFixture("분식"));
        final Product productD = productDao.save(productFixture(null, "떡볶이", BigDecimal.TEN));

        final List<MenuProduct> menuProducts = List.of(
                menuProductFixture(null, productD.getId(), 2)
        );

        final Menu menu = menuDao.save(menuFixture("떡순튀", new BigDecimal(31), boonsik.getId(), menuProducts));
        orderTable = tableService.create(orderTableFixture(null, 4, false));
        orderLineItem = Fixture.orderLineItemFixture(null, menu.getId(), 1);
        order = orderFixture(orderTable.getId(), "COOKING", now(), List.of(orderLineItem));
    }


    @Nested
    @DisplayName("주문 생성 테스트")
    class OrderCreatedTest {
        @Test
        @DisplayName("주문 생성 - 정상")
        void createOrderWithValidData() {
            // Given
            orderService.create(order);

            // When
            Order createdOrder = orderService.create(order);

            assertSoftly(softly -> {
                softly.assertThat(createdOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
                softly.assertThat(createdOrder.getOrderLineItems().size()).isEqualTo(order.getOrderLineItems().size());
                softly.assertThat(createdOrder.getOrderedTime()).isNotNull();
                softly.assertThat(createdOrder.getId()).isNotNull();
            });
        }

        @Test
        @DisplayName("주문 항목이 비어있을 때 예외 발생")
        void createOrderWithEmptyLineItemsShouldThrowException() {
            // Given
            order.setOrderLineItems(List.of());

            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("주문 항목이 비었어요");
        }

        @Test
        @DisplayName("메뉴 ID가 존재하지 않을 때 예외 발생")
        void createOrderWithNonExistentMenuShouldThrowException() {
            orderLineItem.setMenuId(9999999L);
            order.setOrderLineItems(List.of(orderLineItem));

            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("없는 메뉴에요");
        }

        @Test
        @DisplayName("테이블이 비어있을 때 예외 발생")
        void createOrderWithEmptyTableShouldThrowException() {
            // Given
            orderTable = tableService.create(orderTableFixture(null, 4, true));
            order.setOrderTableId(orderTable.getId());

            // when & then
            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("주문 상태 변경 테스트")
    class ChangedOrderStatusTest {

        @Test
        @DisplayName("주문의 상태를 변경한다.")
        void changeOrderStatus() {
            //given
            final Order savedOrder = orderService.create(order);
            savedOrder.setOrderStatus("MEAL");

            //when
            final Order changedOrder = orderService.changeOrderStatus(savedOrder.getId(), savedOrder);

            //then
            assertSoftly(softAssertions -> {
                assertThat(changedOrder.getId()).isEqualTo(savedOrder.getId());
                assertThat(changedOrder.getOrderStatus()).isEqualTo("MEAL");
            });
        }

        @Test
        @DisplayName("주문의 상태가 COMPLETION 인 경우 예외가 발생한다.")
        void changedOrderStatusWithComplication() {
            //given
            final Order savedOrder = orderService.create(order);
            savedOrder.setOrderStatus("COMPLETION");
            final Order chagedOrder = orderService.changeOrderStatus(savedOrder.getId(), savedOrder);

            //when & then
            assertThatThrownBy(() -> orderService.changeOrderStatus(chagedOrder.getId(), chagedOrder))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("더 이상 상태를 변경할 수 없습니다");
        }
    }
}
