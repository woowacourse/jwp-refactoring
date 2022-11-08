package kitchenpos.menu.ui.dto.menugroup;

public class MenuGroupCreateResponse {

    private Long id;
    private String name;

    public MenuGroupCreateResponse() {
    }

    public MenuGroupCreateResponse(Long id, String name) {
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
