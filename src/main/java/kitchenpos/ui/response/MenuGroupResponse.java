package kitchenpos.ui.response;

public class MenuGroupResponse {

    private final long id;
    private final String name;

    public MenuGroupResponse(final long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
