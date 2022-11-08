package kitchenpos.menu.ui.dto.menugroup;

public class MenuGroupListResponse {

    private Long id;
    private String name;

    public MenuGroupListResponse() {
    }

    public MenuGroupListResponse(Long id, String name) {
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
