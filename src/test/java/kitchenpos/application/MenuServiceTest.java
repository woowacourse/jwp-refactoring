package kitchenpos.application;

import kitchenpos.application.dto.MenuRequest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuGroupRepository;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Product;
import kitchenpos.domain.ProductRepository;
import kitchenpos.support.ServiceTest;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

import static kitchenpos.fixture.MenuFixture.menu;
import static kitchenpos.fixture.MenuFixture.menuRequest;
import static kitchenpos.fixture.MenuGroupFixture.menuGroup;
import static kitchenpos.fixture.MenuProductFixture.menuProduct;
import static kitchenpos.fixture.ProductFixture.product;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@ServiceTest
class MenuServiceTest {
    @Autowired
    private MenuGroupRepository menuGroupRepository;
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private MenuService menuService;

    @Test
    void 메뉴를_생성한다() {
        // Given
        Product product = productRepository.save(new Product("chicken", BigDecimal.valueOf(1_000)));
        MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("메뉴 그룹"));
        MenuRequest request = menuRequest("메뉴", 10_000L, menuGroup.getId(), List.of(menuProduct(product, 10, null)));

        // When
        Menu createdMenu = menuService.create(request);

        // Then
        assertThat(createdMenu.getId()).isNotNull();
    }

    @Test
    void 메뉴_가격이_0보다_작으면_예외를_던진다() {
        // given
        MenuGroup savedMenuGroup = menuGroupRepository.save(menuGroup("메뉴 그룹"));
        MenuRequest request = menuRequest("메뉴 이름", -1L, savedMenuGroup.getId(), List.of());

        // expect
        assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("가격은 0원 이상이여야합니다");
    }

    @Test
    void 메뉴의_가격이_메뉴_상품들의_금액의_합보다_큰_경우_예외를_던진다() {
        // given
        MenuGroup menuGroup = menuGroupRepository.save(menuGroup("pizza"));
        Product product = productRepository.save(product("cheese pizza", 10000L));
        MenuProduct menuProduct = menuProduct(product, 1L, null);
        MenuRequest request = menuRequest("cheese pizza", 10001L, menuGroup.getId(), List.of(menuProduct));

        // expect
        assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("가격의 합이 맞지 않습니다");
    }

    @Test
    void 메뉴_그룹_아이디에_해당하는_메뉴_그룹이_없는_경우_예외를_던진다() {
        // given
        MenuRequest request = menuRequest("메뉴 이름", 1000L, Long.MAX_VALUE, List.of());

        // expect
        assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴 그룹이 존재 해야합니다");
    }

    @Test
    void 전체_메뉴를_조회할_수_있다() {
        // given
        MenuGroup menuGroup = menuGroupRepository.save(menuGroup("chicken"));

        Menu menu1 = menuRepository.save(menu("fried chicken", 10000L, menuGroup.getId(), List.of()));
        Menu menu2 = menuRepository.save(menu("spicy chicken", 20000L, menuGroup.getId(), List.of()));

        // when
        List<Menu> result = menuService.list();

        // then
        assertThat(result)
                .usingRecursiveComparison()
                .withComparatorForType(BigDecimal::compareTo, BigDecimal.class)
                .isEqualTo(List.of(menu1, menu2));
    }
}
