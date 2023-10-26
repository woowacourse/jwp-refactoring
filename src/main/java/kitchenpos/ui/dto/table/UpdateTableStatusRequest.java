package kitchenpos.ui.dto.table;

public class UpdateTableStatusRequest {

    private final Boolean isEmpty;

    public UpdateTableStatusRequest(final Boolean isEmpty) {
        this.isEmpty = isEmpty;
    }

    public Boolean getEmpty() {
        return isEmpty;
    }
}
