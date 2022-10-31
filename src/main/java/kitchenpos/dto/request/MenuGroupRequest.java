package kitchenpos.dto.request;

import kitchenpos.domain.menu.MenuGroup;

public class MenuGroupRequest {

    private Long id;

    private String name;

    protected MenuGroupRequest() {
    }

    public MenuGroupRequest(String name) {
        this.name = name;
    }

    public MenuGroupRequest(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public MenuGroup toDomain() {
        return new MenuGroup(id, name);
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "MenuGroup{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
