package ordertable.ui.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import javax.validation.constraints.NotNull;

public class OrderTableChangeEmptyRequest {

    @NotNull
    private final Boolean empty;

    @JsonCreator
    public OrderTableChangeEmptyRequest(Boolean empty) {
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }
}
