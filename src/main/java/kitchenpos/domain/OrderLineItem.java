package kitchenpos.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class OrderLineItem {
    private Long seq;
    private Long orderId;
    private Long menuId;
    private long quantity;

}
