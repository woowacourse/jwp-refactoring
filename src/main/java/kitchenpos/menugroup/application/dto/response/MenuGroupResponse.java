package kitchenpos.menugroup.application.dto.response;

public class MenuGroupResponse {

    private Long id;
    private String name;

    public MenuGroupResponse(Long id, String name) {
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
