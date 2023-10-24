package kitchenpos.order.domain;

class OrderTest {

//    private final Menu pizza = new Menu(1L, "pizza", BigDecimal.TEN, 1L,
//            List.of(new MenuProduct(1L, 3)));

//    @Test
//    void 빈_테이블이_주문_생성_시_예외_발생() {
//        assertThatThrownBy(() -> new Order(1L, OrderStatus.COOKING, LocalDateTime.now()))
//                .isInstanceOf(IllegalArgumentException.class);
//    }
//
//    @Test
//    void Order의_OrderLineItems_와_주문하려는_메뉴_개수가_다르면_예외발생() {
//        Order order = new Order(new OrderTable(1L, 3, false), OrderStatus.COOKING, LocalDateTime.now());
//        order.addOrderLineItem(new OrderLineItem(pizza, 3));
//        assertThatThrownBy(() -> order.checkEqualMenuCount(2))
//                .isInstanceOf(IllegalArgumentException.class);
//    }
//
//    @Test
//    void Order의_OrderLineItems_와_주문하려는_메뉴_개수가_같으면_정상() {
//        Order order = new Order(new OrderTable(1L, 3, false), OrderStatus.COOKING, LocalDateTime.now());
//        order.addOrderLineItem(new OrderLineItem(pizza, 3));
//        assertDoesNotThrow(() -> order.checkEqualMenuCount(1));
//    }
//
//    @Test
//    void 주문의_상태가_COMPLETION_인지_확인한다() {
//        Order order = new Order(new OrderTable(1L, 3, false), OrderStatus.COMPLETION, LocalDateTime.now());
//        assertThat(order.isCompleted()).isTrue();
//    }
//
//    @Test
//    void 주문의_상태가_COMPLETION이_아닌지_확인한다() {
//        Order order = new Order(new OrderTable(1L, 3, false), OrderStatus.COOKING, LocalDateTime.now());
//        assertThat(order.isCompleted()).isFalse();
//    }
}
