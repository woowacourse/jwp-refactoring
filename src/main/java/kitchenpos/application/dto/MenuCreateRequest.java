package kitchenpos.application.dto;

import java.util.List;

public class MenuCreateRequest {

    private String name;
    private Long menuGroupId;
    private List<Long> menuProductIds;

    public MenuCreateRequest() {
    }

    public MenuCreateRequest(final String name, final Long menuGroupId, final List<Long> menuProductIds) {
        this.name = name;
        this.menuGroupId = menuGroupId;
        this.menuProductIds = menuProductIds;
    }

    public String getName() {
        return name;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<Long> getMenuProductIds() {
        return menuProductIds;
    }
}
