package kitchenpos.application.dto;

public class IdRequest {
    private Long id;

    private IdRequest() {
    }

    public IdRequest(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
