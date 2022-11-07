package kitchenpos.order.domain;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.Menus;

public class OrderMenus {

    private List<OrderMenu> orderMenus;

    public OrderMenus(final List<OrderMenu> orderMenus) {
        this.orderMenus = orderMenus;
    }

    public static OrderMenus createByMenus(final Menus menus) {
        final List<OrderMenu> orderMenus = new ArrayList<>();
        for (final Menu menu : menus.getMenus()) {
            orderMenus.add(new OrderMenu(menu));
        }
        return new OrderMenus(orderMenus);
    }

    public List<OrderMenu> getOrderMenus() {
        return orderMenus;
    }

    public int size() {
        return orderMenus.size();
    }

    public Long findIdByMenuId(final Long menuId) {
        return orderMenus.stream()
                .filter(it -> it.hasSameMenuId(menuId))
                .findFirst()
                .map(OrderMenu::getId)
                .get();
    }

    public void setOrderProducts(final OrderProducts orderProducts) {
        for (final OrderMenu orderMenu : orderMenus) {
            setOrderProducts(orderProducts, orderMenu);
        }
    }

    private static void setOrderProducts(final OrderProducts orderProducts, final OrderMenu orderMenu) {
        if (orderProducts.isSameOrderMenuId(orderMenu.getId())) {
            orderMenu.setOrderProducts(orderProducts);
        }
    }
}
