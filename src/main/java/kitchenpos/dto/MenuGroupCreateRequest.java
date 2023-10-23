package kitchenpos.dto;

public class MenuGroupCreateRequest {

    private final String name;

    public MenuGroupCreateRequest(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
