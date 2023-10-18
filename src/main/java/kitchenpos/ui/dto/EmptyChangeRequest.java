package kitchenpos.ui.dto;

public class EmptyChangeRequest {

    private boolean isEmpty;

    public EmptyChangeRequest() {
    }

    public EmptyChangeRequest(boolean isEmpty) {
        this.isEmpty = isEmpty;
    }

    public boolean isEmpty() {
        return isEmpty;
    }
}
