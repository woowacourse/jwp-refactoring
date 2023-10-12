package kitchenpos.fixture;

import static kitchenpos.fixture.MenuGroupFixture.추천메뉴_그룹;
import static kitchenpos.fixture.MenuProductFixture.후추_치킨_2개;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;

public class MenuFixture {

    public static Menu 추천메뉴_후추칰힌_2개_메뉴() {
        return new Menu(
                "더블후추칰힌",
                BigDecimal.valueOf(19000),
                추천메뉴_그룹().getId(),
                List.of(후추_치킨_2개())
        );
    }

}
