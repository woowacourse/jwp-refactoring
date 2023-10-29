package kitchenpos.menugroup.dto;

public class MenuGroupCreateRequest {

    private String name;

    public MenuGroupCreateRequest(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
