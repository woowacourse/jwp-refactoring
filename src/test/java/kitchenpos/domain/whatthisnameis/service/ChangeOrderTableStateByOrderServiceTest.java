package kitchenpos.domain.whatthisnameis.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.config.IntegrationTest;
import kitchenpos.domain.common.Price;
import kitchenpos.domain.exception.InvalidOrderStatusCompletionException;
import kitchenpos.domain.exception.InvalidTableGroupException;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.MenuProducts;
import kitchenpos.domain.menu.repository.MenuRepository;
import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.domain.menugroup.repository.MenuGroupRepository;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.order.repository.OrderRepository;
import kitchenpos.domain.ordertable.OrderTable;
import kitchenpos.domain.ordertable.repository.OrderTableRepository;
import kitchenpos.domain.ordertable.service.ChangeOrderTableStateService;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.repository.ProductRepository;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ChangeOrderTableStateByOrderServiceTest {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    MenuRepository menuRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    MenuGroupRepository menuGroupRepository;

    @Autowired
    OrderTableRepository orderTableRepository;

    @Autowired
    ChangeOrderTableStateService changeOrderTableStateService;

    @Test
    void changeEmpty_메서드는_호출하면_orderTable의_상태를_변경한다() {
        // given
        final Menu persistMenu = persistMenu();
        final OrderTable persistOrderTable = orderTableRepository.save(new OrderTable(0, true));
        persistOrder(persistOrderTable, persistMenu, OrderStatus.COMPLETION);
        persistOrderTable.group(1L);

        // when
        changeOrderTableStateService.changeEmpty(persistOrderTable, false);

        // then
        assertThat(persistOrderTable.isEmpty()).isFalse();
    }

    @Test
    void changeEmpty_메서드는_group이_없다면_예외가_발생한다() {
        // given
        final Menu persistMenu = persistMenu();
        final OrderTable persistOrderTable = orderTableRepository.save(new OrderTable(0, true));
        persistOrder(persistOrderTable, persistMenu, OrderStatus.COMPLETION);

        // when & then
        assertThatThrownBy(() -> changeOrderTableStateService.changeEmpty(persistOrderTable, false))
                .isInstanceOf(InvalidTableGroupException.class);
    }

    @ParameterizedTest(name = "OrderStauts가 {0}이라면 예외가 발생한다")
    @ValueSource(strings = {"COOKING", "MEAL"})
    void changeEmpty_메서드는_order가_COMPLETION이_아니라면_예외가_발생한다(final String invalidOrderStatusName) {
        // given
        final Menu persistMenu = persistMenu();
        final OrderTable persistOrderTable = orderTableRepository.save(new OrderTable(0, true));
        final OrderStatus invalidOrderStatus = OrderStatus.valueOf(invalidOrderStatusName);
        persistOrder(persistOrderTable, persistMenu, invalidOrderStatus);
        persistOrderTable.group(1L);

        // when & then
        assertThatThrownBy(() -> changeOrderTableStateService.changeEmpty(persistOrderTable, false))
                .isInstanceOf(InvalidOrderStatusCompletionException.class);
    }

    private Menu persistMenu() {
        final MenuGroup persistMenuGroup = menuGroupRepository.save(new MenuGroup("메뉴 그룹"));
        final Product persistProduct = productRepository.save(new Product("상품", BigDecimal.TEN));
        final MenuProduct persistMenuProduct = new MenuProduct(persistProduct.getId(), 1L);
        final Price price = new Price(BigDecimal.TEN);

        return menuRepository.save(
                new Menu(
                        "메뉴",
                        BigDecimal.TEN,
                        persistMenuGroup.getId(),
                        MenuProducts.of(price, price, List.of(persistMenuProduct))
                )
        );
    }

    private Order persistOrder(
            final OrderTable persistOrderTable,
            final Menu persistMenu,
            final OrderStatus orderStatus
    ) {
        final OrderLineItem persistOrderLineItem = new OrderLineItem(persistMenu.getId(), 1L);

        return orderRepository.save(
                new Order(persistOrderTable.getId(), orderStatus, LocalDateTime.now(), List.of(persistOrderLineItem))
        );
    }
}
