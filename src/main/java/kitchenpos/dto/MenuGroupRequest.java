package kitchenpos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MenuGroupRequest {

    private String name;

    public String getName() {
        return name;
    }
}
