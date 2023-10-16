package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.dao.fakedao.InMemoryMenuDao;
import kitchenpos.dao.fakedao.InMemoryMenuGroupDao;
import kitchenpos.dao.fakedao.InMemoryMenuProductDao;
import kitchenpos.dao.fakedao.InMemoryProductDao;
import kitchenpos.domain.MenuFactory;
import kitchenpos.domain.MenuGroupFactory;
import kitchenpos.domain.ProductFactory;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class MenuServiceTest {

    private MenuDao fakeMenuDao;
    private MenuGroupDao fakeMenuGroupDao;
    private MenuProductDao fakeMenuProductDao;
    private ProductDao fakeProductDao;

    @BeforeEach
    void setUp() {
        fakeMenuDao = new InMemoryMenuDao();
        fakeMenuGroupDao = new InMemoryMenuGroupDao();
        fakeMenuProductDao = new InMemoryMenuProductDao();
        fakeProductDao = new InMemoryProductDao();
    }

    @Nested
    class 메뉴_등록시 {

        @Test
        void 가격이_메뉴_상품_목록의_상품_가격_총_합보다_크다면_예외가_발생한다() {
            // given
            final var menuGroup = fakeMenuGroupDao.save(MenuGroupFactory.createMenuGroupOf("메뉴 그룹"));
            final var product = fakeProductDao.save(ProductFactory.createProductOf("후라이드치킨", BigDecimal.valueOf(1000)));
            final var menuProduct = MenuFactory.createMenuProductOf(product.getId(), 1L);

            final var menuService = new MenuService(fakeMenuDao, fakeMenuGroupDao, fakeMenuProductDao, fakeProductDao);
            final var menuWithInvalidPrice = MenuFactory.createMenuOf("메뉴화 된 후라이드 치킨", BigDecimal.valueOf(1100), menuGroup.getId());
            menuWithInvalidPrice.getMenuProducts().add(menuProduct);

            // when
            final ThrowingCallable throwingCallable = () -> menuService.create(menuWithInvalidPrice);

            // then
            assertThatThrownBy(throwingCallable).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 메뉴_그룹이_존재하지_않는다면_예외가_발생한다() {
            // given
            final var menuService = new MenuService(fakeMenuDao, fakeMenuGroupDao, fakeMenuProductDao, fakeProductDao);
            final var menuWithInvalidMenuGroupId = MenuFactory.createMenuOf("후라이드치킨", BigDecimal.valueOf(1000), Long.MAX_VALUE);

            // when
            final ThrowingCallable throwingCallable = () -> menuService.create(menuWithInvalidMenuGroupId);

            // then
            assertThatThrownBy(throwingCallable).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 메뉴_상품_목록에_있는_상품이_등록된_상품이_아니라면_예외가_발생한다() {
            // given
            final var menuGroup = fakeMenuGroupDao.save(MenuGroupFactory.createMenuGroupOf("메뉴 그룹"));
            final var menuProduct = MenuFactory.createMenuProductOf(Long.MAX_VALUE, 1L);

            final var menuService = new MenuService(fakeMenuDao, fakeMenuGroupDao, fakeMenuProductDao, fakeProductDao);
            final var menuWithInvalidProductId = MenuFactory.createMenuOf("후라이드치킨", BigDecimal.valueOf(1000), menuGroup.getId());
            menuWithInvalidProductId.getMenuProducts().add(menuProduct);

            // when
            final ThrowingCallable throwingCallable = () -> menuService.create(menuWithInvalidProductId);

            // then
            assertThatThrownBy(throwingCallable).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 메뉴가_정상적으로_등록된다() {
            // given
            final var menuGroup = fakeMenuGroupDao.save(MenuGroupFactory.createMenuGroupOf("메뉴 그룹"));
            final var product = fakeProductDao.save(ProductFactory.createProductOf("후라이드치킨", BigDecimal.valueOf(1000)));
            final var menuProduct = MenuFactory.createMenuProductOf(product.getId(), 1L);

            final var menuService = new MenuService(fakeMenuDao, fakeMenuGroupDao, fakeMenuProductDao, fakeProductDao);
            final var menu = MenuFactory.createMenuOf("메뉴화 된 후라이드 치킨", BigDecimal.valueOf(1000), menuGroup.getId());

            menu.getMenuProducts().add(menuProduct);

            // when
            final var savedMenu = menuService.create(menu);

            // then
            assertThat(savedMenu).isNotNull();
        }
    }
}
