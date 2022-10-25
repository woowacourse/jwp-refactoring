package kitchenpos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.Product;

public class DomainFixture {

    public static final MenuGroup 인기_메뉴 = new MenuGroup();
    public static final MenuGroup 세트_메뉴 = new MenuGroup();

    static {
        인기_메뉴.setName("인기 메뉴");
        세트_메뉴.setName("세트 메뉴");
    }

    public static Product 뿌링클_생성() {
        var product = new Product();
        product.setName("뿌링클");
        product.setPrice(BigDecimal.valueOf(18_000));
        return product;
    }

    public static Product 치즈볼_생성() {
        var product = new Product();
        product.setName("치즈볼");
        product.setPrice(BigDecimal.valueOf(5_000));
        return product;
    }

    public static Menu 뿌링클_치즈볼_메뉴_생성() {
        var menu = new Menu();
        menu.setName("뿌링클+치즈볼");
        menu.setPrice(BigDecimal.valueOf(23_000));
        return menu;
    }

    public static Menu 메뉴_세팅(final Menu menu, final long menuGroupId, final long... productIds) {
        menu.setMenuGroupId(menuGroupId);
        final List<MenuProduct> menuProducts = Arrays.stream(productIds)
                .mapToObj(DomainFixture::메뉴_상품_생성)
                .collect(Collectors.toList());
        menu.setMenuProducts(menuProducts);
        return menu;
    }

    private static MenuProduct 메뉴_상품_생성(final long productId) {
        final var menuProduct = new MenuProduct();
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(1);
        return menuProduct;
    }

    public static Order 주문_생성(final long tableId, final long... menuIds) {
        final var order = new Order();
        order.setOrderTableId(tableId);
        order.setOrderStatus(OrderStatus.COOKING.name());
        order.setOrderedTime(LocalDateTime.now());
        final List<OrderLineItem> orderLineItems = Arrays.stream(menuIds)
                .mapToObj(menuId -> {
                    var orderLineItem = new OrderLineItem();
                    orderLineItem.setMenuId(menuId);
                    orderLineItem.setQuantity(1);
                    return orderLineItem;
                })
                .collect(Collectors.toList());
        order.setOrderLineItems(orderLineItems);
        return order;
    }
}
