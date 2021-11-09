package kitchenpos.application.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderTableRequest {
    private Long id;

    public OrderTableRequest(Long id) {
        this.id = id;
    }
}
