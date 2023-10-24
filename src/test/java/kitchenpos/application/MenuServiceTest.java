package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.application.exception.MenuGroupNotFoundException;
import kitchenpos.config.IntegrationTest;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.exception.InvalidPriceException;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.domain.menu.repository.MenuRepository;
import kitchenpos.domain.product.repository.ProductRepository;
import kitchenpos.ui.dto.request.CreateMenuProductRequest;
import kitchenpos.ui.dto.request.CreateMenuRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
@SuppressWarnings("NonAsciiCharacters")
class MenuServiceTest {

    @Autowired
    MenuGroupRepository menuGroupRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    MenuRepository menuRepository;

    @Autowired
    MenuService menuService;

    @Test
    void create_메서드는_menu를_전달하면_menu를_저장하고_반환한다() {
        // given
        final MenuGroup persistMenuGroup = menuGroupRepository.save(new MenuGroup("메뉴 그룹"));
        final Product persistProduct = productRepository.save(new Product("상품", BigDecimal.TEN));
        final CreateMenuRequest request = new CreateMenuRequest(
                "메뉴",
                BigDecimal.TEN,
                persistMenuGroup.getId(),
                List.of(new CreateMenuProductRequest(persistProduct.getId(), 1L)));

        // when
        final Menu actual = menuService.create(request);

        assertAll(
                () -> assertThat(actual.getId()).isPositive(),
                () -> assertThat(actual.getName()).isEqualTo(request.getName())
        );
    }

    @ParameterizedTest(name = "price가 {0}원일 때 예외가 발생한다.")
    @ValueSource(strings = {"-1", "-2", "-3"})
    void create_메서드는_menu의_price가_음수라면_예외가_발생한다(final String invalidPrice) {
        // given
        final MenuGroup persistMenuGroup = menuGroupRepository.save(new MenuGroup("메뉴 그룹"));
        final Product persistProduct = productRepository.save(new Product("상품", BigDecimal.TEN));
        final CreateMenuRequest invalidRequest = new CreateMenuRequest(
                "메뉴",
                new BigDecimal(invalidPrice),
                persistMenuGroup.getId(),
                List.of(new CreateMenuProductRequest(persistProduct.getId(), 1L)));

        // when & then
        assertThatThrownBy(() -> menuService.create(invalidRequest))
                .isInstanceOf(InvalidPriceException.class);
    }

    @Test
    void create_메서드는_menu의_price가_null이라면_예외가_발생한다() {
        // given
        final MenuGroup persistMenuGroup = menuGroupRepository.save(new MenuGroup("메뉴 그룹"));
        final Product persistProduct = productRepository.save(new Product("상품", BigDecimal.TEN));
        final CreateMenuRequest invalidRequest = new CreateMenuRequest(
                "메뉴",
                null,
                persistMenuGroup.getId(),
                List.of(new CreateMenuProductRequest(persistProduct.getId(), 1L)));

        // when & then
        assertThatThrownBy(() -> menuService.create(invalidRequest))
                .isInstanceOf(InvalidPriceException.class);
    }

    @Test
    void create_메서드는_menu의_menuGroupId가_존재하지_않는다면_예외가_발생한다() {
        // given
        final Product persistProduct = productRepository.save(new Product("상품", BigDecimal.TEN));
        final CreateMenuRequest invalidRequest = new CreateMenuRequest(
                "메뉴",
                BigDecimal.TEN,
                -999L,
                List.of(new CreateMenuProductRequest(persistProduct.getId(), 1L)));

        // when & then
        assertThatThrownBy(() -> menuService.create(invalidRequest))
                .isInstanceOf(MenuGroupNotFoundException.class);
    }

    @Test
    void list_메서드는_등록한_모든_menu를_반환한다() {
        // given
        final MenuGroup persistMenuGroup = menuGroupRepository.save(new MenuGroup("메뉴 그룹"));
        final Product persistProduct = productRepository.save(new Product("상품", BigDecimal.TEN));
        final MenuProduct menuProduct = new MenuProduct(persistProduct, 1);
        final Menu menu = Menu.of("메뉴", BigDecimal.TEN, List.of(menuProduct), persistMenuGroup);
        final Menu expected = menuRepository.save(menu);

        // when
        final List<Menu> actual = menuService.list();

        // then
        assertAll(
                () -> assertThat(actual).hasSize(1),
                () -> assertThat(actual.get(0).getId()).isEqualTo(expected.getId()),
                () -> assertThat(actual.get(0).getName()).isEqualTo(expected.getName())
        );
    }
}
