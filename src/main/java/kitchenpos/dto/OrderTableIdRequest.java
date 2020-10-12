package kitchenpos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderTableIdRequest {

    private Long id;

    public Long getId() {
        return id;
    }
}
