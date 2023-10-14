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
        savedMenuProduct = menuProductDao.save(new MenuProduct());
    }

    @Test
    void 메뉴를_생성한다() {
        // Given
        Product product = productRepository.save(new Product("chicken", BigDecimal.valueOf(1_000)));
        MenuProductRequest menuProduct = new MenuProductRequest(product.getId(), 10L);
        MenuGroup menuGroup = menuGroupDao.save(new MenuGroup("메뉴 그룹"));
        MenuRequest menu = new MenuRequest("메뉴", BigDecimal.valueOf(10_000), menuGroup.getId(), List.of(menuProduct));

        // When
        Menu createdMenu = menuService.create(menu);

        // Then
        assertThat(createdMenu.getId()).isNotNull();
    }

    @Test
    void 메뉴_가격이_0보다_작으면_예외를_던진다() {
        // given
        MenuRequest menu = new MenuRequest("메뉴 이름", BigDecimal.valueOf(-1), savedMenuGroup.getId(), List.of());

        // expect
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴_그룹_아이디에_해당하는_메뉴_그룹이_없는_경우_예외를_던진다() {
        // given
        MenuRequest menu = new MenuRequest("메뉴 이름", BigDecimal.valueOf(1000), Long.MAX_VALUE, List.of());

        // expect
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 가격이_실제_메뉴_상품들의_총_가격보다_크면_예외를_던진다() {
        // given
        MenuRequest menu = new MenuRequest("메뉴 이름", BigDecimal.valueOf(2001), savedMenuGroup.getId(), List.of());

        // expect
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 전체_메뉴를_조회할_수_있다() {
        // given
        Product product = productRepository.save(new Product("chicken", BigDecimal.valueOf(1_000)));
        MenuProductRequest menuProduct = new MenuProductRequest(product.getId(), 10L);
        MenuGroup menuGroup = menuGroupDao.save(new MenuGroup("메뉴 그룹"));
        MenuRequest menu = new MenuRequest("메뉴", BigDecimal.valueOf(10_000), menuGroup.getId(), List.of(menuProduct));

        menuService.create(menu);

        // when
        List<Menu> menus = menuService.list();

        // then
        assertThat(menus).hasSize(1);
    }
}
