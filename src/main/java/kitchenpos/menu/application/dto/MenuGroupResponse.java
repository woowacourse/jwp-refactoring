package kitchenpos.menu.application.dto;

public final class MenuGroupResponse {

    private final Long id;
    private final String name;

    public MenuGroupResponse(final Long id, final String name) {
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
