package kitchenpos.application;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


class MenuServiceTest extends ServiceTest {

    @Autowired
    private MenuService menuService;

    @Nested
    class 메뉴_생성 {

        @ParameterizedTest
        @ValueSource(longs = {-100, -1})
        void 가격은_음수일_수_없다(long price) {
            //given
            MenuGroup 메뉴_그룹 = 메뉴_그룹_생성();

            Menu 메뉴 = new Menu();
            메뉴.setMenuGroupId(메뉴_그룹.getId());
            메뉴.setPrice(BigDecimal.valueOf(price));
            메뉴.setMenuProducts(상품_만들기());

            //expect
            assertThatThrownBy(() -> menuService.create(메뉴))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 가격은_null일_수_없다() {
            //given
            MenuGroup 메뉴_그룹 = 메뉴_그룹_생성();

            Menu 메뉴 = new Menu();
            메뉴.setMenuGroupId(메뉴_그룹.getId());
            메뉴.setPrice(null);
            메뉴.setMenuProducts(상품_만들기());

            //expect
            assertThatThrownBy(() -> menuService.create(메뉴))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 그룹이_존재하지_않으면_예외가_발생한다() {
            //given
            Menu 메뉴 = new Menu();
            메뉴.setMenuGroupId(1000000000000000L);
            List<MenuProduct> 상품 = 상품_만들기();
            메뉴.setMenuProducts(상품);

            //expect
            assertThatThrownBy(() -> menuService.create(메뉴))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 메뉴에_속하는_상품이_없으면_예외가_발생한다() {
            //given
            Menu 메뉴 = new Menu();
            메뉴.setMenuGroupId(메뉴_그룹_생성().getId());
            메뉴.setPrice(BigDecimal.valueOf(1000));
            메뉴.setMenuProducts(emptyList());

            //expect
            assertThatThrownBy(() -> menuService.create(메뉴))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    private MenuGroup 메뉴_그룹_생성() {
        MenuGroup 저장할_그룹 = new MenuGroup();
        저장할_그룹.setName("메뉴그룹");
        return menuGroupDao.save(저장할_그룹);
    }

    private List<MenuProduct> 상품_만들기() {
        var 상품_아이디 = productDao.findAll().get(0).getId();
        MenuProduct 메뉴_상품 = new MenuProduct();
        메뉴_상품.setProductId(상품_아이디);
        return List.of(메뉴_상품);
    }

}
