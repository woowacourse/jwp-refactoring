package kitchenpos.domain;

public class OrderLineItem {

    private Long seq;
    private Order order;
    private Menu menu;
    private long quantity;

    public OrderLineItem() {}

    public OrderLineItem(Menu menu, long quantity) {
        this.menu = menu;
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public Order getOrder() {
        return order;
    }

    public Menu getMenu() {
        return menu;
    }

    public long getQuantity() {
        return quantity;
    }
}
