package kitchenpos.domain;

import kitchenpos.domain.vo.Name;
import kitchenpos.domain.vo.Price;
import kitchenpos.domain.vo.Quantity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThatCode;

class MenuProductTest {

    @DisplayName("[SUCCESS] 생성한다.")
    @Test
    void success_create() {
        final Product product = new Product(new Name("테스트용 상품명"), new Price("10000"));
        final MenuGroup menuGroup = new MenuGroup(new Name("테스트용 메뉴그룹명"));
        final Menu menu = new Menu(new Name("테스트용 메뉴명"), new Price("10000"), menuGroup, new ArrayList<>());

        assertThatCode(() -> new MenuProduct(menu, product, new Quantity(10)))
                .doesNotThrowAnyException();
    }
}
