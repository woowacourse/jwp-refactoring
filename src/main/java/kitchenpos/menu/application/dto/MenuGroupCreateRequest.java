package kitchenpos.menu.application.dto;

public final class MenuGroupCreateRequest {

    private final String name;

    public MenuGroupCreateRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
