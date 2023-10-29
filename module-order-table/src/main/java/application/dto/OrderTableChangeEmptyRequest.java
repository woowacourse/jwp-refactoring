package application.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderTableChangeEmptyRequest {

    private final boolean empty;

    @JsonCreator
    public OrderTableChangeEmptyRequest(@JsonProperty("isEmpty") final boolean isEmpty) {
        this.empty = isEmpty;
    }

    public boolean isEmpty() {
        return empty;
    }
}
