package kitchenpos.dto.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class MenuGroupRequest {

    @NotNull(message = "이름을 입력해 주세요")
    @Size(max = 255, message = "이름은 255자를 초과할 수 없습니다.")
    private final String name;

    public MenuGroupRequest(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
