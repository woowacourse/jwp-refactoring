package kitchenpos.menugroup.application.dto;

import com.fasterxml.jackson.annotation.JsonCreator;

public class MenuGroupCreateDto {

    private final String name;

    @JsonCreator
    public MenuGroupCreateDto(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
