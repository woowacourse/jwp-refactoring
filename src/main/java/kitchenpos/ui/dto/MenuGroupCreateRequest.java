package kitchenpos.ui.dto;

public class MenuGroupCreateRequest {
    private Long id;
    private String name;

    private MenuGroupCreateRequest() {
    }

    public MenuGroupCreateRequest(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
