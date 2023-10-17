package kitchenpos.fixture;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;

@SuppressWarnings("NonAsciiCharacters")
public class MenuFixture {

    public static Menu 메뉴_엔티티_A = createMenu(1L, "홈런볼", 10_000L);
    public static Menu 메뉴_엔티티_B_가격_NULL = createMenu(2L, "새우볼", 0);
    public static Menu 메뉴_엔티티_C_가격_음수 = createMenu(3L, "공룡볼", -1_000L);
    public static Menu 메뉴_엔티티_D_가격_합계_오류 = createMenu(4L, "감자볼", 15_000L);

    private static Menu createMenu(Long id, String name, long price) {
        return Menu.builder()
                .name(name)
                .price(price)
                .menuGroup(MenuGroup.builder().build())
                .build();
    }
}
