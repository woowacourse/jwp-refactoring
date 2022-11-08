package kitchenpos.core.order.domain;

import java.math.BigDecimal;

public class OrderMenu {

    private final Long menuId;
    private final long quantity;
    private final String name;
    private final BigDecimal price;

    public OrderMenu(final Long menuId, final long quantity, final String name, final BigDecimal price) {
        this.menuId = menuId;
        this.quantity = quantity;
        this.name = name;
        this.price = price;
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final OrderMenu orderMenu = (OrderMenu) o;

        if (getQuantity() != orderMenu.getQuantity()) {
            return false;
        }
        if (getMenuId() != null ? !getMenuId().equals(orderMenu.getMenuId()) : orderMenu.getMenuId() != null) {
            return false;
        }
        if (getName() != null ? !getName().equals(orderMenu.getName()) : orderMenu.getName() != null) {
            return false;
        }
        if (getPrice() == null && orderMenu.getPrice() != null) {
            return false;
        }
        if (getPrice() != null && orderMenu.getPrice() == null) {
            return false;
        }
        return orderMenu.getPrice() == null || getPrice().compareTo(orderMenu.getPrice()) == 0;
    }

    @Override
    public int hashCode() {
        int result = getMenuId() != null ? getMenuId().hashCode() : 0;
        result = 31 * result + (int) (getQuantity() ^ (getQuantity() >>> 32));
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        result = 31 * result + (getPrice() != null ? getPrice().hashCode() : 0);
        return result;
    }
}
