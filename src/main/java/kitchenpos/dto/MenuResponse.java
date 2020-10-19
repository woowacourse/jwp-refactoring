package kitchenpos.dto;

public class MenuResponse {
    private Long id;

    private MenuResponse() {
    }

    public MenuResponse(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
