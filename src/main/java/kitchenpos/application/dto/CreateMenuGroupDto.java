package kitchenpos.application.dto;

import lombok.Getter;

@Getter
public class CreateMenuGroupDto {

    private final String name;

    public CreateMenuGroupDto(String name) {
        this.name = name;
    }
}
