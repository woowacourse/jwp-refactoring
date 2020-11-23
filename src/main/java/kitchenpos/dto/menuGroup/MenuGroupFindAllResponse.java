package kitchenpos.dto.menuGroup;

import kitchenpos.domain.MenuGroup;

public class MenuGroupFindAllResponse {
    private Long id;
    private String name;

    protected MenuGroupFindAllResponse() {
    }

    public MenuGroupFindAllResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public MenuGroupFindAllResponse(MenuGroup menuGroup) {
        this(menuGroup.getId(), menuGroup.getName());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
