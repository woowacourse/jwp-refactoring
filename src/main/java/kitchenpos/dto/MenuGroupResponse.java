package kitchenpos.dto;

public class MenuGroupResponse {
    private Long id;

    public MenuGroupResponse() {
    }

    public MenuGroupResponse(Long id, String name) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
