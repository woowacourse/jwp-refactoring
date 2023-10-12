package kitchenpos.fixture;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;

@SuppressWarnings("NonAsciiCharacters")
public class MenuFixture {

    public static Menu 메뉴_엔티티_A = createMenu(1L, "홈런볼", BigDecimal.valueOf(10_000L));
    public static Menu 메뉴_엔티티_B_가격_NULL = createMenu(2L, "새우볼", null);
    public static Menu 메뉴_엔티티_C_가격_음수 = createMenu(3L, "공룡볼", BigDecimal.valueOf(-1_000L));
    public static Menu 메뉴_엔티티_D_가격_합계_오류 = createMenu(4L, "감자볼", BigDecimal.valueOf(15_000L));

    private static Menu createMenu(Long id, String name, BigDecimal price) {
        MenuGroup 메뉴_그룹_엔티티_A = MenuGroupFixture.메뉴_그룹_엔티티_A;
        MenuProduct 메뉴_상품_엔티티_A = MenuProductFixture.메뉴_상품_엔티티_A;

        Menu menu = new Menu();
        menu.setId(id);
        menu.setName(name);
        menu.setPrice(price);
        menu.setMenuGroupId(메뉴_그룹_엔티티_A.getId());
        menu.setMenuProducts(List.of(메뉴_상품_엔티티_A));
        return menu;
    }
}
