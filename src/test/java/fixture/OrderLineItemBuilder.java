//package fixture;
//
//import kitchenpos.domain.OrderLineItem;
//
//public class OrderLineItemBuilder {
//    private Long seq;
//    private Long orderId;
//    private Long menuId;
//    private long quantity;
//
//    public static OrderLineItemBuilder init() {
//        OrderLineItemBuilder builder = new OrderLineItemBuilder();
//        builder.seq = 1L;
//        builder.orderId = 1L;
//        builder.menuId = 1L;
//        builder.quantity = 2L;
//        return builder;
//    }
//
//    public OrderLineItemBuilder seq(Long seq) {
//        this.seq = seq;
//        return this;
//    }
//
//    public OrderLineItemBuilder orderId(Long orderId) {
//        this.orderId = orderId;
//        return this;
//    }
//
//    public OrderLineItemBuilder menuId(Long menuId) {
//        this.menuId = menuId;
//        return this;
//    }
//
//    public OrderLineItemBuilder quantity(long quantity) {
//        this.quantity = quantity;
//        return this;
//    }
//
//    public OrderLineItem build() {
//        final OrderLineItem orderLineItem = new OrderLineItem(
//                this.seq,
//                this.orderId,
//                this.menuId,
//                this.quantity
//        );
//        return orderLineItem;
//    }
//}
