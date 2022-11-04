package kitchenpos.common.dto;

public class IdRequest {

    private Long id;

    public IdRequest(final Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    private IdRequest() {
    }
}
