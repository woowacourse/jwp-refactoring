package kitchenpos.menu.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import exception.EntityNotFoundException;
import exception.InvalidPriceException;
import java.math.BigDecimal;
import java.util.List;
import kitchenpos.application.MenuService;
import kitchenpos.menu.dao.InMemoryMenuDao;
import kitchenpos.menugroup.InMemoryMenuGroupDao;
import kitchenpos.menugroup.MenuGroupFactory;
import kitchenpos.menuproduct.InMemoryMenuProductDao;
import kitchenpos.product.ProductFactory;
import kitchenpos.product.dao.InMemoryProductDao;
import kitchenpos.repository.MenuDao;
import kitchenpos.repository.MenuGroupDao;
import kitchenpos.repository.MenuProductDao;
import kitchenpos.repository.ProductDao;
import kitchenpos.ui.request.MenuCreateRequest;
import kitchenpos.ui.request.MenuProductCreateRequest;
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
            final var menuService = new MenuService(fakeMenuDao, fakeMenuGroupDao, fakeProductDao);

            final var menuProductCreateRequest = new MenuProductCreateRequest(product.getId(), 1L);
            final var menuWithInvalidPrice = new MenuCreateRequest("메뉴화 된 후라이드 치킨", BigDecimal.valueOf(1100), menuGroup.getId(), List.of(menuProductCreateRequest));

            // when
            final ThrowingCallable throwingCallable = () -> menuService.create(menuWithInvalidPrice);

            // then
            assertThatThrownBy(throwingCallable).isInstanceOf(InvalidPriceException.class);
        }

        @Test
        void 메뉴_그룹이_존재하지_않는다면_예외가_발생한다() {
            // given
            final var menuService = new MenuService(fakeMenuDao, fakeMenuGroupDao, fakeProductDao);
            final var menuWithInvalidMenuGroupId = new MenuCreateRequest("후라이드치킨", BigDecimal.valueOf(1000), Long.MAX_VALUE, List.of());

            // when
            final ThrowingCallable throwingCallable = () -> menuService.create(menuWithInvalidMenuGroupId);

            // then
            assertThatThrownBy(throwingCallable).isInstanceOf(EntityNotFoundException.class);
        }

        @Test
        void 메뉴_상품_목록에_있는_상품이_등록된_상품이_아니라면_예외가_발생한다() {
            // given
            final var menuGroup = fakeMenuGroupDao.save(MenuGroupFactory.createMenuGroupOf("메뉴 그룹"));

            final var menuService = new MenuService(fakeMenuDao, fakeMenuGroupDao, fakeProductDao);

            final var menuProductCreateRequest = new MenuProductCreateRequest(Long.MAX_VALUE, 1L);
            final var menuWithInvalidProductId = new MenuCreateRequest("메뉴화 된 후라이드 치킨", BigDecimal.valueOf(1000), menuGroup.getId(), List.of(menuProductCreateRequest));

            // when
            final ThrowingCallable throwingCallable = () -> menuService.create(menuWithInvalidProductId);

            // then
            assertThatThrownBy(throwingCallable).isInstanceOf(EntityNotFoundException.class);
        }

        @Test
        void 메뉴가_정상적으로_등록된다() {
            // given
            final var menuGroup = fakeMenuGroupDao.save(MenuGroupFactory.createMenuGroupOf("메뉴 그룹"));
            final var product = fakeProductDao.save(ProductFactory.createProductOf("후라이드치킨", BigDecimal.valueOf(1000)));
            final var menuProduct = new MenuProductCreateRequest(product.getId(), 1L);

            final var menuService = new MenuService(fakeMenuDao, fakeMenuGroupDao, fakeProductDao);
            final var menu = new MenuCreateRequest("메뉴화 된 후라이드 치킨", BigDecimal.valueOf(1000), menuGroup.getId(), List.of(menuProduct));

            // when
            final var savedMenu = menuService.create(menu);

            // then
            assertThat(savedMenu).isNotNull();
        }
    }
}
