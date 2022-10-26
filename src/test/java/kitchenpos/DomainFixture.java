package kitchenpos;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.Name;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.menu.domain.Price;
import kitchenpos.menu.domain.Product;
import kitchenpos.menu.domain.Quantity;

public class DomainFixture {

    public static final Quantity 한개 = new Quantity(1);
    public static final Quantity 두개 = new Quantity(2);
    public static final Name 뿌링클_치즈볼 = new Name("뿌링클+치즈볼");

    public static final Product 뿌링클 = new Product(new Name("뿌링클"), Price.valueOf(18_000));
    public static final Product 치즈볼 = new Product(new Name("치즈볼"), Price.valueOf(5_000));

    public static final MenuGroup 세트_메뉴 = new MenuGroup(new Name("세트 메뉴"));
    public static final MenuGroup 인기_메뉴 = new MenuGroup(new Name("인기 메뉴"));

//    public static final Menu 뿌링클과_치즈볼 = new Menu(new Name("뿌링클+치즈볼"), Price.valueOf(23_000), 0L, List.of());
//
//    public static Menu 메뉴_세팅(final Menu menu, final long menuGroupId, final long... productIds) {
//        menu.setMenuGroupId(menuGroupId);
//        final List<MenuProduct> menuProducts = Arrays.stream(productIds)
//                .mapToObj(DomainFixture::메뉴_상품_생성)
//                .collect(Collectors.toList());
//        menu.setMenuProducts(menuProducts);
//        return menu;
//    }

    public static Menu 뿌링클_치즈볼_메뉴_생성(final long menuGroupId, final Product... products) {
        return new Menu(
                뿌링클_치즈볼, Price.valueOf(23_000), menuGroupId,
                Arrays.stream(products)
                        .map(product -> new MenuProduct(product, 한개))
                        .collect(Collectors.toList())
        );
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
