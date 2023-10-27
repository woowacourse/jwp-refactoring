package kitchenpos.menugroup.application.dto;

public class MenuGroupResponse {
    private Long id;
    private String name;

    private MenuGroupResponse(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public static MenuGroupResponse of(final Long id, final String name) {
       return new MenuGroupResponse(id, name);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
