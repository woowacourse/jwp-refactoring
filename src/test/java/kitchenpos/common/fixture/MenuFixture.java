package kitchenpos.common.fixture;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import kitchenpos.domain.Menu;

@SuppressWarnings("NonAsciiCharacters")
public class MenuFixture {

    public static Menu 새_메뉴(Long menuId, Long menuGroupId) {
        return new Menu(
                menuId,
                "menuName",
                BigDecimal.valueOf(1).setScale(2, RoundingMode.HALF_UP),
                menuGroupId,
                List.of()
        );
    }

    public static Menu 새_메뉴(Long menuGroupId) {
        return new Menu(
                "menuName",
                BigDecimal.valueOf(1).setScale(2, RoundingMode.HALF_UP),
                menuGroupId,
                List.of()
        );
    }
}
