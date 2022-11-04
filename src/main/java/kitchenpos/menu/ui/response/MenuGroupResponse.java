package kitchenpos.menu.ui.response;

public class MenuGroupResponse {

    private final Long id;
    private final String name;

    public MenuGroupResponse(final long id, final String name) {
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
