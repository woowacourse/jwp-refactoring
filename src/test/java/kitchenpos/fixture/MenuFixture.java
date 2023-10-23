package kitchenpos.fixture;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.ui.dto.MenuProductRequest;
import kitchenpos.ui.dto.MenuRequest;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static kitchenpos.fixture.FixtureUtil.listAllInDatabaseFrom;

@SuppressWarnings("NonAsciiCharacters")
public abstract class MenuFixture {

    @InDatabase
    public static Menu 후라이드치킨() {
        return new Menu(1L, "후라이드치킨", new BigDecimal("16000.00"), MenuGroupFixture.한마리메뉴(),
                List.of(new MenuProduct(1L, ProductFixture.후라이드(), 1L))
        );
    }

    @InDatabase
    public static Menu 양념치킨() {
        return new Menu(2L, "양념치킨", new BigDecimal("16000.00"), MenuGroupFixture.한마리메뉴(),
                List.of(new MenuProduct(2L, ProductFixture.양념치킨(), 1L))
        );
    }

    @InDatabase
    public static Menu 반반치킨() {
        return new Menu(3L, "반반치킨", new BigDecimal("16000.00"), MenuGroupFixture.한마리메뉴(),
                List.of(new MenuProduct(3L, ProductFixture.반반치킨(), 1L))
        );
    }

    @InDatabase
    public static Menu 통구이() {
        return new Menu(4L, "통구이", new BigDecimal("16000.00"), MenuGroupFixture.한마리메뉴(),
                List.of(new MenuProduct(4L, ProductFixture.통구이(), 1L))
        );
    }

    @InDatabase
    public static Menu 간장치킨() {
        return new Menu(5L, "간장치킨", new BigDecimal("17000.00"), MenuGroupFixture.한마리메뉴(),
                List.of(new MenuProduct(5L, ProductFixture.간장치킨(), 1L))
        );
    }

    @InDatabase
    public static Menu 순살치킨() {
        return new Menu(6L, "순살치킨", new BigDecimal("17000.00"), MenuGroupFixture.한마리메뉴(),
                List.of(new MenuProduct(6L, ProductFixture.순살치킨(), 1L))
        );
    }

    public static Menu 후라이드_두마리() {
        return new Menu(
                "후라이드_두마리",
                new BigDecimal("32000.00"),
                MenuGroupFixture.두마리메뉴(),
                List.of(new MenuProduct(
                        ProductFixture.후라이드(),
                        2L
                ))
        );
    }

    public static MenuRequest 후라이드_두마리_REQUEST() {
        var menu = 후라이드_두마리();
        return new MenuRequest(
                menu.getName(),
                menu.getPrice(),
                menu.getMenuGroup().getId(),
                menu.getMenuProducts().stream()
                        .map(it -> new MenuProductRequest(
                                it.getProductId(),
                                it.getQuantity()
                        ))
                        .collect(Collectors.toList())
        );
    }

    public static List<Menu> listAllInDatabase() {
        return listAllInDatabaseFrom(MenuFixture.class, Menu.class);
    }
}
