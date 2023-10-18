package kitchenpos.domain;

import kitchenpos.domain.vo.Price;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThatCode;

class MenuProductTest {

    @DisplayName("[SUCCESS] 생성한다.")
    @Test
    void success_create() {
        final Product product = new Product("테스트용 상품명", new Price("10000"));
        final MenuGroup menuGroup = new MenuGroup("테스트용 메뉴그룹명");
        final Menu menu = new Menu("테스트용 메뉴명", new Price("10000"), menuGroup, new ArrayList<>());

        assertThatCode(() -> new MenuProduct(menu, product, 10))
                .doesNotThrowAnyException();
    }
}
