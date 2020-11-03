package kitchenpos.ui.dto;

import java.beans.ConstructorProperties;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE, onConstructor_ = @ConstructorProperties("name"))
@Getter
public class MenuGroupCreateRequest {
    @NotBlank
    @Valid
    private final String name;
}
