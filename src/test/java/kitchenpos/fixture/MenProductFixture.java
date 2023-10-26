package kitchenpos.fixture;

import kitchenpos.menu.domain.MenuProduct;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class MenProductFixture {

    public static final MenuProduct 메뉴_상품_1000원_2개 = new MenuProduct(null, 2L);

    public static MenuProduct of(final Long quantity) {
        return new MenuProduct(null, quantity);
    }
}
