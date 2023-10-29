package table.application.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;

public class OrderTableRequest {

    private final long id;

    @JsonCreator
    public OrderTableRequest(final long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
