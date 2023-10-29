package kitchenpos.menu.application;

import kitchenpos.config.ApplicationTestConfig;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.Product;
import kitchenpos.common.vo.Name;
import kitchenpos.common.vo.Price;
import kitchenpos.common.vo.Quantity;
import kitchenpos.menu.application.response.MenuResponse;
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
                MenuProduct.withoutMenu(productRepository.save(new Product(new Name("테스트용 상품명"), Price.from("10000"))), new Quantity(10)),
                MenuProduct.withoutMenu(productRepository.save(new Product(new Name("테스트용 상품명"), Price.from("10000"))), new Quantity(10)),
                MenuProduct.withoutMenu(productRepository.save(new Product(new Name("테스트용 상품명"), Price.from("10000"))), new Quantity(10)),
                MenuProduct.withoutMenu(productRepository.save(new Product(new Name("테스트용 상품명"), Price.from("10000"))), new Quantity(10)),
                MenuProduct.withoutMenu(productRepository.save(new Product(new Name("테스트용 상품명"), Price.from("10000"))), new Quantity(10))
        ));

        final Menu menu = Menu.withEmptyMenuProducts(
                new Name("테스트용 메뉴명"),
                Price.from("0"),
                savedMenuGroup
        );
        menu.addMenuProducts(unsavedMenuProducts);

        // when
        final Menu savedMenu = menuRepository.save(menu);
        final MenuResponse expected = MenuResponse.from(savedMenu);
        final List<MenuResponse> actual = menuService.list();

        // then
        assertSoftly(softly -> {
            softly.assertThat(actual).hasSize(1);
            final MenuResponse actualMenu = actual.get(0);

            softly.assertThat(actualMenu.getId()).isEqualTo(expected.getId());
            softly.assertThat(actualMenu.getName()).isEqualTo(expected.getName());
            softly.assertThat(actualMenu.getPrice()).isEqualTo(expected.getPrice());
            softly.assertThat(actualMenu.getMenuGroup())
                    .usingRecursiveComparison()
                    .isEqualTo(expected.getMenuGroup());
            softly.assertThat(actualMenu.getMenuProducts())
                    .usingRecursiveComparison()
                    .isEqualTo(expected.getMenuProducts());
        });
    }
}
