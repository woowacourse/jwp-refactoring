package kitchenpos.application.menu;

import kitchenpos.application.MenuService;
import kitchenpos.config.ApplicationTestConfig;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.domain.vo.Name;
import kitchenpos.domain.vo.Price;
import kitchenpos.domain.vo.Quantity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

class MenuQueryServiceTest extends ApplicationTestConfig {

    private MenuService menuService;

    @BeforeEach
    void setUp() {
        menuService = new MenuService(
                menuRepository,
                menuGroupRepository,
                productRepository
        );
    }

    @DisplayName("[SUCCESS] 전체 메뉴 목록을 조회한다.")
    @Test
    void success_findAll() {
        // given
        final MenuGroup savedMenuGroup = menuGroupRepository.save(new MenuGroup(new Name("테스트용 메뉴 그룹명")));

        final List<MenuProduct> unsavedMenuProducts = new ArrayList<>(List.of(
                MenuProduct.ofWithoutMenu(productRepository.save(new Product(new Name("테스트용 상품명"), new Price("10000"))), new Quantity(10)),
                MenuProduct.ofWithoutMenu(productRepository.save(new Product(new Name("테스트용 상품명"), new Price("10000"))), new Quantity(10)),
                MenuProduct.ofWithoutMenu(productRepository.save(new Product(new Name("테스트용 상품명"), new Price("10000"))), new Quantity(10)),
                MenuProduct.ofWithoutMenu(productRepository.save(new Product(new Name("테스트용 상품명"), new Price("10000"))), new Quantity(10)),
                MenuProduct.ofWithoutMenu(productRepository.save(new Product(new Name("테스트용 상품명"), new Price("10000"))), new Quantity(10))
        ));

        final Menu menu = Menu.ofEmptyMenuProducts(
                new Name("테스트용 메뉴명"),
                new Price("0"),
                savedMenuGroup
        );
        menu.addMenuProducts(unsavedMenuProducts);

        // when
        final Menu expected = menuRepository.save(menu);
        final List<Menu> actual = menuService.list();

        // then
        assertSoftly(softly -> {
            softly.assertThat(actual).hasSize(1);
            final Menu actualMenu = actual.get(0);

            softly.assertThat(actualMenu.getId()).isEqualTo(expected.getId());
            softly.assertThat(actualMenu.getName()).isEqualTo(expected.getName());
            softly.assertThat(actualMenu.getPrice()).isEqualTo(expected.getPrice());
            softly.assertThat(actualMenu.getMenuGroup()).isEqualTo(expected.getMenuGroup());
            softly.assertThat(actualMenu.getMenuProducts()).isEqualTo(expected.getMenuProducts());
        });
    }
}
