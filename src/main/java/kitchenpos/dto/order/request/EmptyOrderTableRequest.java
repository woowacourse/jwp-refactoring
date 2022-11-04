package kitchenpos.dto.order.request;

public class EmptyOrderTableRequest {

    private boolean isEmpty;

    private EmptyOrderTableRequest() {
    }

    public EmptyOrderTableRequest(final boolean isEmpty) {
        this.isEmpty = isEmpty;
    }

    public boolean isEmpty() {
        return isEmpty;
    }
}
