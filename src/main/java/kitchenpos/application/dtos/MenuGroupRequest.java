package kitchenpos.application.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class MenuGroupRequest {
    private String name;

    public MenuGroupRequest(String name) {
        this.name = name;
    }
}
