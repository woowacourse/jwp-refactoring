package kitchenpos.table.domain;

import static kitchenpos.support.fixtures.DomainFixtures.MENU1_NAME;
import static kitchenpos.support.fixtures.DomainFixtures.MENU1_PRICE;
import static kitchenpos.support.fixtures.DomainFixtures.MENU_GROUP_NAME1;
import static kitchenpos.support.fixtures.DomainFixtures.PRODUCT1_NAME;
import static kitchenpos.support.fixtures.DomainFixtures.PRODUCT1_PRICE;
import static kitchenpos.support.fixtures.DomainFixtures.PRODUCT2_NAME;
import static kitchenpos.support.fixtures.DomainFixtures.PRODUCT2_PRICE;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.infrastructure.OrderEmptyValidatorImpl;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class OrderEmptyValidatorTest {

    private OrderEmptyValidator orderEmptyValidator;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private OrderRepository orderRepository;

    @BeforeEach
    void setUp() {
        orderEmptyValidator = new OrderEmptyValidatorImpl(orderRepository);
    }

    @ParameterizedTest
    @EnumSource(value = OrderStatus.class, names = {"MEAL", "COOKING"})
    @DisplayName("완료 상태가 아니면 예외가 발생한다.")
    void notCompletedFailed(final OrderStatus orderStatus) {
        Menu menu = createMenu(MENU1_NAME, MENU1_PRICE);
        OrderTable orderTable = orderTableRepository.save(new OrderTable(10, true));
        List<OrderLineItem> orderLineItems = List.of(new OrderLineItem(menu.getId(), 1),
                new OrderLineItem(menu.getId(), 2));
        orderRepository.save(new Order(orderTable.getId(), orderStatus, LocalDateTime.now(), orderLineItems));

        assertThatThrownBy(() -> orderEmptyValidator.validateOrderStatus(orderTable.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("현재 조리 / 식사 중입니다.");
    }

    private Menu createMenu(final String name, final BigDecimal price) {
        MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup(MENU_GROUP_NAME1));
        Product product1 = productRepository.save(new Product(PRODUCT1_NAME, PRODUCT1_PRICE));
        Product product2 = productRepository.save(new Product(PRODUCT2_NAME, PRODUCT2_PRICE));

        List<MenuProduct> menuProducts = List.of(new MenuProduct(null, product1.getId(), 1),
                new MenuProduct(null, product2.getId(), 1));
        return menuRepository.save(new Menu(name, price, menuGroup.getId(), menuProducts));
    }
}
