package kitchenpos.exception;

public class OrderTableNotFoundException extends BusinessException {
    public OrderTableNotFoundException() {
        super("Table of table group request is not exist");
    }

    public OrderTableNotFoundException(Long orderTableId) {
        super(String.format("%d table is not exist!", orderTableId));
    }
}
