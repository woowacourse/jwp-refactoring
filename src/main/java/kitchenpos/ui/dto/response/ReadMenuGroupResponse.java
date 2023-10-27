package kitchenpos.ui.dto.response;

import kitchenpos.application.dto.ReadMenuGroupDto;

public class ReadMenuGroupResponse {

    private final Long id;
    private final String name;

    public ReadMenuGroupResponse(final ReadMenuGroupDto dto) {
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
