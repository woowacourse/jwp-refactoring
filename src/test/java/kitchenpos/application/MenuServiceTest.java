package kitchenpos.application;

import static kitchenpos.test.fixture.MenuFixture.메뉴;
import static kitchenpos.test.fixture.MenuGroupFixture.메뉴_그룹;
import static kitchenpos.test.fixture.MenuProductFixture.메뉴_상품;
import static kitchenpos.test.fixture.ProductFixture.상품;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuGroupRepository;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.domain.ProductRepository;
import kitchenpos.test.ServiceTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
class MenuServiceTest extends ServiceTest {

    @Autowired
    private MenuService menuService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Nested
    class 메뉴_추가_시 {

        @Test
        void 정상적인_메뉴라면_메뉴를_추가한다() {
            //given
            MenuGroup menuGroup = menuGroupRepository.save(메뉴_그룹("일식"));
            Product product = productRepository.save(상품("텐동", BigDecimal.valueOf(11000)));
            MenuProduct menuProduct = 메뉴_상품(null, product.getId(), 1);
            Menu menu = 메뉴("텐동", BigDecimal.valueOf(11000), menuGroup.getId(), List.of(menuProduct));

            //when
            Menu savedMenu = menuService.create(menu);

            //then
            assertSoftly(softly -> {
                softly.assertThat(savedMenu.getId()).isNotNull();
                softly.assertThat(savedMenu.getName()).isEqualTo("텐동");
                softly.assertThat(savedMenu.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(11000));
                softly.assertThat(savedMenu.getMenuGroupId()).isEqualTo(menuGroup.getId());
                softly.assertThat(savedMenu.getMenuProducts()).hasSize(1);
                softly.assertThat(savedMenu.getMenuProducts().get(0).getSeq()).isNotNull();
                softly.assertThat(savedMenu.getMenuProducts().get(0).getMenuId()).isEqualTo(savedMenu.getId());
                softly.assertThat(savedMenu.getMenuProducts().get(0).getProductId()).isEqualTo(product.getId());
                softly.assertThat(savedMenu.getMenuProducts().get(0).getQuantity()).isOne();
            });
        }

        @Test
        void 가격이_NULL이면_예외를_던진다() {
            //given
            MenuGroup menuGroup = menuGroupRepository.save(메뉴_그룹("일식"));
            Product product = productRepository.save(상품("텐동", BigDecimal.valueOf(11000)));
            MenuProduct menuProduct = 메뉴_상품(null, product.getId(), 1);
            Menu menu = 메뉴("텐동", null, menuGroup.getId(), List.of(menuProduct));

            //when, then
            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @ParameterizedTest
        @ValueSource(longs = {Integer.MIN_VALUE, -1})
        void 가격이_0보다_작으면_예외를_던진다(long price) {
            //given
            MenuGroup menuGroup = menuGroupRepository.save(메뉴_그룹("일식"));
            Product product = productRepository.save(상품("텐동", BigDecimal.valueOf(11000)));
            MenuProduct menuProduct = 메뉴_상품(null, product.getId(), 1);
            Menu menu = 메뉴("텐동", BigDecimal.valueOf(price), menuGroup.getId(), List.of(menuProduct));

            //when, then
            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 존재하지_않는_메뉴_그룹이라면_예외를_던진다() {
            //given
            Product product = productRepository.save(상품("텐동", BigDecimal.valueOf(11000)));
            MenuProduct menuProduct = 메뉴_상품(null, product.getId(), 1);
            Menu menu = 메뉴("텐동", BigDecimal.valueOf(11000), -1L, List.of(menuProduct));

            //when, then
            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 존재하지_않은_상품이라면_예외를_던진다() {
            //given
            MenuGroup menuGroup = menuGroupRepository.save(메뉴_그룹("일식"));
            MenuProduct menuProduct = 메뉴_상품(null, -1L, 1);
            Menu menu = 메뉴("텐동", BigDecimal.valueOf(11000), menuGroup.getId(), List.of(menuProduct));

            //when, then
            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 총_상품_가격보다_메뉴_가격이_크다면_예외를_던진다() {
            //given
            MenuGroup menuGroup = menuGroupRepository.save(메뉴_그룹("일식"));
            Product product = productRepository.save(상품("텐동", BigDecimal.valueOf(11000)));
            MenuProduct menuProduct = 메뉴_상품(null, product.getId(), 1);
            Menu menu = 메뉴("텐동", BigDecimal.valueOf(12000), menuGroup.getId(), List.of(menuProduct));

            //when, then
            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class 메뉴_목록_조회_시 {

        @Test
        void 모든_메뉴_목록을_조회한다() {
            //given
            MenuGroup menuGroup = menuGroupRepository.save(메뉴_그룹("일식"));
            Product product = productRepository.save(상품("텐동", BigDecimal.valueOf(11000)));
            MenuProduct menuProduct = 메뉴_상품(null, product.getId(), 1);
            Menu menuA = 메뉴("기본 텐동", BigDecimal.valueOf(11000), menuGroup.getId(), List.of(menuProduct));
            Menu menuB = 메뉴("할인 텐동", BigDecimal.valueOf(10000), menuGroup.getId(), List.of(menuProduct));
            Menu savedMenuA = menuService.create(menuA);
            Menu savedMenuB = menuService.create(menuB);

            //when
            List<Menu> menus = menuService.list();

            //then
            assertThat(menus).usingRecursiveComparison().isEqualTo(List.of(savedMenuA, savedMenuB));
        }

        @Test
        void 메뉴가_존재하지_않으면_목록이_비어있다() {
            //given, when
            List<Menu> menus = menuService.list();

            //then
            assertThat(menus).isEmpty();
        }
    }
}
