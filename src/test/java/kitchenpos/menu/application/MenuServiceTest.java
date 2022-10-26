package kitchenpos.menu.application;

import static kitchenpos.DomainFixture.뿌링클;
import static kitchenpos.DomainFixture.세트_메뉴;
import static kitchenpos.DomainFixture.치즈볼;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.menu.repository.MenuDao;
import kitchenpos.menu.repository.MenuGroupDao;
import kitchenpos.menu.repository.ProductDao;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.Name;
import kitchenpos.menu.domain.Price;
import kitchenpos.menu.domain.Product;
import kitchenpos.menu.domain.Quantity;
import kitchenpos.common.exception.CustomErrorCode;
import kitchenpos.common.exception.NotFoundException;
import kitchenpos.menu.ui.dto.MenuCreateRequest;
import kitchenpos.menu.ui.dto.MenuProductCreateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@Sql(scripts = "classpath:truncate.sql")
class MenuServiceTest {

    private MenuDao menuDao;
    private MenuGroupDao menuGroupDao;
    private ProductDao productDao;
    private MenuService menuService;

    private Product productA;
    private Product productB;
    private Long menuGroupId;

    @Autowired
    public MenuServiceTest(MenuDao menuDao, MenuGroupDao menuGroupDao, ProductDao productDao) {
        this.menuDao = menuDao;
        this.menuGroupDao = menuGroupDao;
        this.productDao = productDao;
        this.menuService = new MenuService(menuDao, menuGroupDao, productDao);
    }

    @BeforeEach
    void setUp() {
        productA = productDao.save(뿌링클);
        productB = productDao.save(치즈볼);
        menuGroupId = menuGroupDao.save(세트_메뉴).getId();
    }

    @Test
    void 메뉴를_생성하고_결과를_반환한다() {
        final var request = new MenuCreateRequest("뿌링클+치즈볼", 23_000, menuGroupId,
                List.of(new MenuProductCreateRequest(productA.getId(), 1),
                        new MenuProductCreateRequest(productB.getId(), 1))
        );

        // when
        final var created = menuService.create(request);

        // then
        assertAll(
                () -> assertThat(created.getId()).isNotNull(),
                () -> assertThat(created.getName()).isEqualTo(request.getName()),
                () -> assertThat(created.getPrice().intValue()).isEqualTo(request.getPrice()),
                () -> assertThat(created.getMenuGroupId()).isEqualTo(menuGroupId),
                () -> assertThat(created.getProducts()).hasSize(2)
        );
    }

    @Test
    void 메뉴_가격이_없는_경우_예외를_던진다() {
        final var request = new MenuCreateRequest("뿌링클과 치즈볼", null, menuGroupId,
                List.of(new MenuProductCreateRequest(productA.getId(), 1),
                        new MenuProductCreateRequest(productB.getId(), 1))
        );

        // when & then
        assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void 존재하지_않는_메뉴_그룹인_경우_예외를_던진다() {
        final var request = new MenuCreateRequest("뿌링클과 치즈볼", 23_000, 0L,
                List.of(new MenuProductCreateRequest(productA.getId(), 1),
                        new MenuProductCreateRequest(productB.getId(), 1))
        );

        // when & then
        assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(NotFoundException.class)
                .extracting("errorCode")
                .isEqualTo(CustomErrorCode.MENU_GROUP_NOT_FOUND_ERROR);
    }

    @Test
    void 존재하지_않는_상품이_있는_경우_예외를_던진다() {
        final var request = new MenuCreateRequest("뿌링클과 치즈볼", 23_000, menuGroupId,
                List.of(new MenuProductCreateRequest(0L, 1),
                        new MenuProductCreateRequest(productB.getId(), 1))
        );

        // when & then
        assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(NotFoundException.class)
                .extracting("errorCode")
                .isEqualTo(CustomErrorCode.PRODUCT_NOT_FOUND_ERROR);
    }

    @Test
    void 메뉴_목록을_조회한다() {
        // given
        final var productA = productDao.save(뿌링클);
        final var productB = productDao.save(치즈볼);
        final var menuGroupId = menuGroupDao.save(세트_메뉴).getId();

        Menu menuA = new Menu(new Name("뿌링클과 치즈볼"), Price.valueOf(23_000), menuGroupId,
                List.of(new MenuProduct(productA, new Quantity(1)), new MenuProduct(productB, new Quantity(1))));
        Menu menuB = new Menu(new Name("뿌뿌링클"), Price.valueOf(35_000), menuGroupId,
                List.of(new MenuProduct(productA, new Quantity(2))));
        menuDao.save(menuA);
        menuDao.save(menuB);

        // when
        List<Menu> foundMenus = menuService.list();

        // then
        assertThat(foundMenus).hasSize(2);
    }
}
