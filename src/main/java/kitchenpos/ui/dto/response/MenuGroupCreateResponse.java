package kitchenpos.ui.dto.response;

public class MenuGroupCreateResponse {

    private Long id;
    private String name;

    private MenuGroupCreateResponse() {
    }

    public MenuGroupCreateResponse(final Long id, final String name) {
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
