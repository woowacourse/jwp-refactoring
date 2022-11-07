package kitchenpos.order.domain;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.menu.domain.MenuProduct;

public class OrderProducts {

    private final List<OrderProduct> orderProducts;

    public OrderProducts(final List<OrderProduct> orderProducts) {
        this.orderProducts = orderProducts;
    }

    public static OrderProducts createByMenuProducts(final List<MenuProduct> menuProducts) {
        final List<OrderProduct> orderProducts = new ArrayList<>();
        for (final MenuProduct menuProduct : menuProducts) {
            orderProducts.add(new OrderProduct(menuProduct));
        }
        return new OrderProducts(orderProducts);
    }

    public boolean isSameOrderMenuId(final Long orderMenuId) {
        return orderProducts.stream()
                .anyMatch(it -> it.getOrderMenuId().equals(orderMenuId));
    }

    public void setOrderMenuId(final Long orderMenuId) {
        orderProducts.forEach(it -> it.setOrderMenuId(orderMenuId));
    }

    public List<OrderProduct> getOrderProducts() {
        return orderProducts;
    }
}
