package kitchenpos.ordertable.domain;

public interface OrderStatusChecker {

    boolean checkIncompleteOrders(Long orderTableId);

}
