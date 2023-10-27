package kitchenpos.menu.event;

import kitchenpos.config.ApplicationTestConfig;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuHistory;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProductHistories;
import kitchenpos.menu.domain.Product;
import kitchenpos.menu.domain.vo.Name;
import kitchenpos.menu.domain.vo.Price;
import kitchenpos.menu.domain.vo.Quantity;
import kitchenpos.order.application.event.OrderPreparedEvent;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

class MenuHistoryEventListenerTest extends ApplicationTestConfig {

    private MenuHistoryEventListener menuHistoryEventListener;

    @BeforeEach
    void setUp() {
        menuHistoryEventListener = new MenuHistoryEventListener(
                orderRepository,
                menuRepository,
                menuHistoryRepository
        );
    }

    @DisplayName("[SUCCESS] 메뉴를 기록한다.")
    @Test
    void success_menuHistoryEvent() {
        // given
        final Product savedProduct = productRepository.save(
                new Product(new Name("테스트용 상품명"), Price.from("10000"))
        );

        final MenuGroup savedMenuGroup = menuGroupRepository.save(
                new MenuGroup(new Name("테스트용 메뉴 그룹명"))
        );
        final Menu menu = Menu.withEmptyMenuProducts(
                new Name("테스트용 메뉴명"),
                Price.from("10000"),
                savedMenuGroup
        );
        menu.addMenuProducts(List.of(
                MenuProduct.withoutMenu(savedProduct, new Quantity(10))
        ));
        final Menu savedMenu = menuRepository.save(menu);

        final OrderTable savedOrderTable = orderTableRepository.save(
                OrderTable.withoutTableGroup(10, true)
        );

        final Order savedOrder = orderRepository.save(
                new Order(
                        savedOrderTable.getId(),
                        new OrderLineItems(List.of(
                                new OrderLineItem(savedMenu.getId(), new Quantity(10))
                        ))
                )
        );

        // when
        menuHistoryEventListener.menuHistoryEvent(new OrderPreparedEvent(savedOrder.getId()));

        // then
        final MenuProductHistories expectedMenuProductHistories = MenuProductHistories.from(menu.getMenuProducts());

        final List<MenuHistory> actual = menuHistoryRepository.findAll();
        assertSoftly(softly -> {
            softly.assertThat(actual).hasSize(1);
            final MenuHistory actualMenuHistory = actual.get(0);

            softly.assertThat(actualMenuHistory.getId()).isPositive();
            softly.assertThat(actualMenuHistory.getName()).isEqualTo(savedMenu.getName());
            softly.assertThat(actualMenuHistory.getPrice()).isEqualTo(savedMenu.getPrice());
            softly.assertThat(actualMenuHistory.getMenuProductHistories())
                    .usingRecursiveComparison()
                    .ignoringExpectedNullFields()
                    .isEqualTo(expectedMenuProductHistories);
        });
    }
}
