package kitchenpos.exception;

public class OrderTableNotFoundException extends BusinessException{
    public OrderTableNotFoundException(Long orderTableId) {
        super(String.format("%d table is not exist!", orderTableId));
    }
}
