package kitchenpos.application;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


class MenuServiceTest extends ServiceTest {

    @Autowired
    private MenuService menuService;

    @Nested
    class 메뉴_생성 {

        @Test
        void 가격은_음수일_수_없다() {
            //given
            MenuGroup 메뉴_그룹 = 메뉴_그룹_만들기();

            Menu 메뉴 = new Menu();
            메뉴.setName("메뉴명");
            메뉴.setMenuGroupId(메뉴_그룹.getId());
            메뉴.setPrice(BigDecimal.valueOf(-1));
            메뉴.setMenuProducts(상품_만들기());

            //expect
            assertThatThrownBy(() -> menuService.create(메뉴))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 가격은_null일_수_없다() {
            //given
            MenuGroup 메뉴_그룹 = 메뉴_그룹_만들기();

            Menu 메뉴 = new Menu();
            메뉴.setName("메뉴명");
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
            메뉴.setName("메뉴명");
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
            메뉴.setName("메뉴명");
            메뉴.setMenuGroupId(메뉴_그룹_만들기().getId());
            메뉴.setPrice(BigDecimal.valueOf(1000));
            메뉴.setMenuProducts(emptyList());

            //expect
            assertThatThrownBy(() -> menuService.create(메뉴))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @ParameterizedTest
        @ValueSource(longs = {0, 1})
        void 메뉴가격이_상품가격합_이하여야한다(Long subtrahend) {
            //given
            MenuGroup 메뉴_그룹 = 메뉴_그룹_만들기();
            Menu 메뉴 = new Menu();
            메뉴.setName("메뉴명");
            메뉴.setMenuGroupId(메뉴_그룹.getId());
            메뉴.setPrice(BigDecimal.valueOf(메뉴_상품가격합_구하기(메뉴) - subtrahend));

            //when
            Menu 생성된_메뉴 = menuService.create(메뉴);

            //then
            assertThat(생성된_메뉴.getId()).isNotNull();
        }

        @Test
        void 메뉴가격이_상품가격합보다_큰_경우_예외가_발생한다() {
            //given
            MenuGroup 메뉴_그룹 = 메뉴_그룹_만들기();
            Menu 메뉴 = new Menu();
            메뉴.setName("메뉴명");
            메뉴.setMenuGroupId(메뉴_그룹.getId());
            long 가격_합 = 메뉴_상품가격합_구하기(메뉴);

            메뉴.setPrice(BigDecimal.valueOf(가격_합 + 1));
            //expect
            assertThatThrownBy(() -> menuService.create(메뉴))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        private long 메뉴_상품가격합_구하기(final Menu 메뉴) {
            var 존재하는_상품_목록 = productDao.findAll().subList(0, 2);
            List<MenuProduct> 메뉴상품_목록 = 존재하는_상품_목록.stream()
                    .map(product -> {
                        MenuProduct 메뉴_상품 = new MenuProduct();
                        메뉴_상품.setProductId(product.getId());
                        메뉴_상품.setQuantity(1);
                        return 메뉴_상품;
                    }).collect(Collectors.toList());
            메뉴.setMenuProducts(메뉴상품_목록);
            long 가격_합 = 존재하는_상품_목록.stream()
                    .mapToLong(product -> product.getPrice().longValue())
                    .sum();
            return 가격_합;
        }

    }

    private MenuGroup 메뉴_그룹_만들기() {
        MenuGroup 저장할_그룹 = new MenuGroup("메뉴그룹");
        return menuGroupDao.save(저장할_그룹);
    }

    private List<MenuProduct> 상품_만들기() {
        var 상품_아이디 = productDao.findAll().get(0).getId();
        MenuProduct 메뉴_상품 = new MenuProduct();
        메뉴_상품.setProductId(상품_아이디);
        return List.of(메뉴_상품);
    }

    @Nested
    class 메뉴_목록_조회 {
        @Test
        void 메뉴_목록을_조회할_수_있다() {
            //given
            List<Long> 모든_메뉴_아이디 = menuDao.findAll().stream()
                    .map(Menu::getId)
                    .collect(Collectors.toList());

            //when
            List<Menu> 메뉴_목록 = menuService.list();

            //then
            assertThat(메뉴_목록).extracting(Menu::getId)
                    .containsAll(모든_메뉴_아이디);
        }
    }

}
