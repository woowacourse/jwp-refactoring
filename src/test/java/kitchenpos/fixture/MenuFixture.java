package kitchenpos.fixture;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Objects;

import kitchenpos.domain.Menu;
import kitchenpos.dto.request.MenuCreateRequest;
import kitchenpos.dto.request.MenuProductCreateRequest;

public class MenuFixture {
    public static final Long ID1 = 1L;
    public static final String NAME = "chicken";

    public static MenuCreateRequest createRequest(Long price, Long menuGroupId,
        MenuProductCreateRequest... menuProducts) {
        if (Objects.isNull(price)) {
            return new MenuCreateRequest(NAME, null, menuGroupId,
                Arrays.asList(menuProducts));
        }
        return new MenuCreateRequest(NAME, BigDecimal.valueOf(price), menuGroupId,
            Arrays.asList(menuProducts));
    }

    public static Menu createWithoutId(Long menuGroupId, Long price) {
        return new Menu(NAME, BigDecimal.valueOf(price), menuGroupId);
    }

    public static Menu createWithId(Long id, Long menuGroupId, Long price) {
        return new Menu(id, NAME, BigDecimal.valueOf(price), menuGroupId);
    }

    public static Menu createNullPrice(Long menuGroupId) {
        return new Menu(NAME, null, menuGroupId);
    }
}
