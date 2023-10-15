package kitchenpos.application;

import kitchenpos.application.dto.MenuRequest;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.domain.ProductRepository;
import kitchenpos.fake.InMemoryMenuDao;
import kitchenpos.fake.InMemoryMenuGroupDao;
import kitchenpos.fake.InMemoryMenuProductDao;
import kitchenpos.fake.InMemoryProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static kitchenpos.application.dto.MenuRequest.MenuProductRequest;
import static kitchenpos.fixture.MenuFixture.menu;
import static kitchenpos.fixture.MenuFixture.menuRequest;
import static kitchenpos.fixture.MenuGroupFixture.menuGroup;
import static kitchenpos.fixture.MenuProductFixture.menuProduct;
import static kitchenpos.fixture.ProductFixture.product;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MenuServiceTest {

    private MenuGroupDao menuGroupDao;
    private MenuDao menuDao;
    private MenuProductDao menuProductDao;
    private ProductRepository productRepository;
    private MenuService menuService;
    private MenuGroup savedMenuGroup;
    private MenuProduct savedMenuProduct;

    @BeforeEach
    void before() {
        menuGroupDao = new InMemoryMenuGroupDao();
        menuDao = new InMemoryMenuDao();
        menuProductDao = new InMemoryMenuProductDao();
        productRepository = new InMemoryProductRepository();
        menuService = new MenuService(menuDao, menuGroupDao, menuProductDao, productRepository);
        savedMenuGroup = menuGroupDao.save(new MenuGroup("메뉴 그룹"));
        savedMenuProduct = menuProductDao.save(new MenuProduct(1L, 1L, 10));
    }

    @Test
    void 메뉴를_생성한다() {
        // Given
        Product product = productRepository.save(new Product("chicken", BigDecimal.valueOf(1_000)));
        MenuGroup menuGroup = menuGroupDao.save(new MenuGroup("메뉴 그룹"));
        MenuRequest request = menuRequest("메뉴", 10_000L, menuGroup.getId(), List.of(new MenuProductRequest(product.getId(), 10L)));

        // When
        Menu createdMenu = menuService.create(request);

        // Then
        assertThat(createdMenu.getId()).isNotNull();
    }

    @Test
    void 메뉴_가격이_0보다_작으면_예외를_던진다() {
        // given
        MenuRequest request = menuRequest("메뉴 이름", -1L, savedMenuGroup.getId(), List.of());

        // expect
        assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴_그룹_아이디에_해당하는_메뉴_그룹이_없는_경우_예외를_던진다() {
        // given
        MenuRequest request = menuRequest("메뉴 이름", 1000L, Long.MAX_VALUE, List.of());

        // expect
        assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 가격이_실제_메뉴_상품들의_총_가격보다_크면_예외를_던진다() {
        // given
        MenuRequest request = menuRequest("메뉴 이름", 2001L, savedMenuGroup.getId(), List.of());

        // expect
        assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 전체_메뉴를_조회할_수_있다() {
        // given
        MenuGroup menuGroup = menuGroupDao.save(menuGroup("chicken"));
        Product product = productRepository.save(product("fried chicken", 10000L));

        Menu menu1 = menuDao.save(menu("fried chicken", 10000L, menuGroup.getId(), List.of()));
        MenuProduct menuProduct1 = menuProductDao.save(menuProduct(menu1.getId(), product.getId(), 1L));
        menu1.changeMenuProducts(List.of(menuProduct1));

        Menu menu2 = menuDao.save(menu("spicy chicken", 20000L, menuGroup.getId(), List.of()));
        MenuProduct menuProduct2 = menuProductDao.save(menuProduct(menu2.getId(), product.getId(), 1L));
        menu2.changeMenuProducts(List.of(menuProduct2));

        // when
        List<Menu> result = menuService.list();

        // then
        assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(List.of(menu1, menu2));
    }
}
