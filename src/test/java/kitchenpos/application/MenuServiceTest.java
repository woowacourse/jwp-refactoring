package kitchenpos.application;

import kitchenpos.domain.Product;
import kitchenpos.domain.ProductRepository;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuGroupRepository;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.MenuProductRepository;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.ui.dto.MenuProductDto;
import kitchenpos.ui.dto.MenuRequest;
import kitchenpos.ui.dto.MenuResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

import static kitchenpos.Fixture.Fixture.menuFixture;
import static kitchenpos.Fixture.Fixture.menuGroupFixture;
import static kitchenpos.Fixture.Fixture.menuProductFixture;
import static kitchenpos.Fixture.Fixture.productFixture;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.assertj.core.groups.Tuple.tuple;

@SuppressWarnings("NonAsciiCharacters")
@ServiceTest
class MenuServiceTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuProductRepository menuProductRepository;

    private MenuGroup menuGroup;
    private Product product;
    private MenuProductDto menuProductDto;

    @BeforeEach
    void setUp() {
        this.menuGroup = menuGroupRepository.save(menuGroupFixture("메뉴 그룹1"));
        this.product = productRepository.save(productFixture("상품1", new BigDecimal(10000)));
        this.menuProductDto = new MenuProductDto(1L, 1L, product.getId(), 4L);
    }

    @Nested
    class 몌뉴등록 {
        @Test
        void 메뉴를_등록한다() {
            MenuGroup menuGroup = menuGroupRepository.save(menuGroupFixture("메뉴 그룹1"));
            Product product = productRepository.save(productFixture("productName", BigDecimal.valueOf(10000L)));
            MenuProductDto menuProductDto = new MenuProductDto(1L, product.getId(), product.getId(), 3L);

            MenuRequest menuRequest = new MenuRequest("메뉴", new BigDecimal("30000.00"), menuGroup.getId(), List.of(menuProductDto));
            MenuResponse savedMenu = menuService.create(menuRequest);

            assertSoftly(softly -> {
                softly.assertThat(savedMenu.getId()).isNotNull();
                softly.assertThat(savedMenu.getPrice()).isEqualByComparingTo(menuRequest.getPrice());
                softly.assertThat(savedMenu).usingRecursiveComparison()
                        .ignoringFields("id", "menuProducts")
                        .isEqualTo(menuRequest);
                softly.assertThat(savedMenu.getMenuProducts()).hasSize(1)
                        .extracting("seq")
                        .containsOnly(menuProductDto.getSeq());
            });
        }

        @Test
        void 메뉴_가격이_0원_미만이면_등록할_수_없다() {
            MenuRequest menuRequest = new MenuRequest("메뉴", new BigDecimal(-1), menuGroup.getId(), List.of(menuProductDto));

            assertThatThrownBy(() -> menuService.create(menuRequest))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("메뉴 가격은 0 이상이어야 합니다.");
        }

        @Test
        void 포함될_메뉴_그룹이_존재하지_않으면_등록할_수_없다() {
            long notExistMenuGroupId = 100000L;
            MenuRequest menuRequest = new MenuRequest("메뉴", new BigDecimal("30000.00"), notExistMenuGroupId, List.of(menuProductDto));

            assertThatThrownBy(() -> menuService.create(menuRequest))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("존재하지 않는 메뉴그룹입니다. 메뉴를 등록할 수 없습니다.");
        }

        @Test
        void 메뉴_상품이_존재하지_않으면_등록할_수_없다() {
            long notExistMenuId = Long.MIN_VALUE;
            MenuProductDto notExistMenuProduct = new MenuProductDto(null, notExistMenuId, 1L, 2L);
            MenuRequest menuRequest = new MenuRequest("메뉴", new BigDecimal("30000.00"), menuGroup.getId(), List.of(notExistMenuProduct));

            assertThatThrownBy(() -> menuService.create(menuRequest))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("존재하지 않는 메뉴상품입니다. 메뉴를 등록할 수 없습니다.");
        }

        @Test
        void 메뉴의_가격이_메뉴_상품들의_가격_합보다_비싸면_등록할_수_없다() {
            Product product1 = productFixture("상품2", new BigDecimal(10001));
            MenuProductDto menuProductDto2 = new MenuProductDto(null, product1.getId(), 1L, 1L);
            MenuRequest menuRequest = new MenuRequest("메뉴", new BigDecimal("50000.00"), menuGroup.getId(), List.of(menuProductDto, menuProductDto2));

            assertThatThrownBy(() -> menuService.create(menuRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    void 메뉴의_목록을_조회할_수_있다() {
        Menu menu1 = menuRepository.save(new Menu("메뉴1", new BigDecimal("20000.00"), menuGroup));
        MenuProduct menuProduct = menuProductRepository.save(menuProductFixture(menu1, product, 4));
        menu1.addMenuProducts(List.of(menuProduct));
        Menu menu2 = menuRepository.save(menuFixture("메뉴2", new BigDecimal("30000.00"), menuGroup, List.of(menuProduct)));

        List<MenuResponse> menuList = menuService.list();

        assertThat(menuList).hasSize(2)
                .extracting("id", "name")
                .containsOnly(tuple(menu1.getId(), menu1.getName()),
                        tuple(menu2.getId(), menu2.getName()));
    }
}
