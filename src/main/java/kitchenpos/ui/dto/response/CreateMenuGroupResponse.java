package kitchenpos.ui.dto.response;

import kitchenpos.application.dto.CreateMenuGroupDto;

public class CreateMenuGroupResponse {

    private final Long id;
    private final String name;

    public CreateMenuGroupResponse(final CreateMenuGroupDto dto) {
        this.id = dto.getId();
        this.name = dto.getName();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
