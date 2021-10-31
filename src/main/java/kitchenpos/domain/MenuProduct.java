package kitchenpos.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class MenuProduct {
    private Long seq;
    private Long menuId;
    private Long productId;
    private long quantity;

}
