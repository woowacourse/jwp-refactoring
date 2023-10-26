package kitchenpos.ordertable.presentation.dto;

public class EmptyChangeRequest {

    private boolean isEmpty;

    private EmptyChangeRequest() {
    }

    public EmptyChangeRequest(boolean isEmpty) {
        this.isEmpty = isEmpty;
    }

    public boolean isEmpty() {
        return isEmpty;
    }
}
