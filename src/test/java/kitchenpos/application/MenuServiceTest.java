package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.mock;
import static org.mockito.BDDMockito.when;

import java.math.BigDecimal;
import java.util.Optional;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.MenuFactory;
import kitchenpos.domain.ProductFactory;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

@SuppressWarnings("NonAsciiCharacters")
class MenuServiceTest {

    private final MenuDao mockMenuDao = mock(MenuDao.class);
    private final MenuGroupDao mockMenuGroupDao = mock(MenuGroupDao.class);
    private final MenuProductDao mockMenuProductDao = mock(MenuProductDao.class);
    private final ProductDao mockProductDao = mock(ProductDao.class);

    @Nested
    class 메뉴_등록시 {

        @ParameterizedTest
        @NullAndEmptySource
        void 이름이_공백이나_비어있다면_예외가_발생한다(String nullOrEmptyName) {
            // given
            final var menuService = new MenuService(mockMenuDao, mockMenuGroupDao, mockMenuProductDao, mockProductDao);
            final var menuWithInvalidName = MenuFactory.createMenuOf(nullOrEmptyName, BigDecimal.valueOf(1000), 1L);
            final var menuProduct = MenuFactory.createMenuProductOf(1L, 1L);
            menuWithInvalidName.getMenuProducts().add(menuProduct);

            when(mockMenuGroupDao.existsById(1L)).thenReturn(true);
            when(mockProductDao.findById(1L)).thenReturn(Optional.of(ProductFactory.createProductOf(1L, "후라이드치킨", BigDecimal.valueOf(1000))));
            when(mockMenuDao.save(menuWithInvalidName)).thenReturn(menuWithInvalidName);
            when(mockMenuProductDao.save(menuProduct)).thenReturn(menuProduct);

            // when
            final ThrowingCallable throwingCallable = () -> menuService.create(menuWithInvalidName);

            // then
            assertThatThrownBy(throwingCallable).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 이름이_255자_이내가_아니라면_예외가_발생한다() {
            // given
            final var menuService = new MenuService(mockMenuDao, mockMenuGroupDao, mockMenuProductDao, mockProductDao);
            final var invalidName = "름".repeat(256);
            final var menuWithInvalidName = MenuFactory.createMenuOf(invalidName, BigDecimal.valueOf(1000), 1L);
            final var menuProduct = MenuFactory.createMenuProductOf(1L, 1L);
            menuWithInvalidName.getMenuProducts().add(menuProduct);

            when(mockMenuGroupDao.existsById(1L)).thenReturn(true);
            when(mockProductDao.findById(1L)).thenReturn(Optional.of(ProductFactory.createProductOf(1L, "후라이드치킨", BigDecimal.valueOf(1000))));
            when(mockMenuDao.save(menuWithInvalidName)).thenReturn(menuWithInvalidName);
            when(mockMenuProductDao.save(menuProduct)).thenReturn(menuProduct);

            // when
            final ThrowingCallable throwingCallable = () -> menuService.create(menuWithInvalidName);

            // then
            assertThatThrownBy(throwingCallable).isInstanceOf(IllegalArgumentException.class);
        }

        @ParameterizedTest
        @NullSource
        @ValueSource(strings = {"-1", "12345123451234512345"})
        void 가격이_null이나_올바르지_않은_범위라면_예외가_발생한다(BigDecimal nullPrice) {
            // given
            final var menuService = new MenuService(mockMenuDao, mockMenuGroupDao, mockMenuProductDao, mockProductDao);
            final var menuWithNullPrice = MenuFactory.createMenuOf("후라이드치킨", nullPrice, 1L);

            when(mockMenuGroupDao.existsById(1L)).thenReturn(true);

            // when
            final ThrowingCallable throwingCallable = () -> menuService.create(menuWithNullPrice);

            // then
            assertThatThrownBy(throwingCallable).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 가격이_메뉴_상품_목록의_상품_가격_총_합보다_크다면_예외가_발생한다() {
            // given
            final var menuService = new MenuService(mockMenuDao, mockMenuGroupDao, mockMenuProductDao, mockProductDao);
            final var menuWithInvalidPrice = MenuFactory.createMenuOf("메뉴화 된 후라이드 치킨", BigDecimal.valueOf(1100), 1L);
            final var menuProduct = MenuFactory.createMenuProductOf(1L, 1L);
            menuWithInvalidPrice.getMenuProducts().add(menuProduct);

            when(mockMenuGroupDao.existsById(1L)).thenReturn(true);
            when(mockProductDao.findById(1L)).thenReturn(Optional.of(ProductFactory.createProductOf(1L, "후라이드치킨", BigDecimal.valueOf(1000))));

            // when
            final ThrowingCallable throwingCallable = () -> menuService.create(menuWithInvalidPrice);

            // then
            assertThatThrownBy(throwingCallable).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 메뉴_그룹이_존재하지_않는다면_예외가_발생한다() {
            // given
            final var menuService = new MenuService(mockMenuDao, mockMenuGroupDao, mockMenuProductDao, mockProductDao);
            final var menuWithInvalidMenuGroupId = MenuFactory.createMenuOf("후라이드치킨", BigDecimal.valueOf(1000), 1L);

            when(mockMenuGroupDao.existsById(1L)).thenReturn(false);

            // when
            final ThrowingCallable throwingCallable = () -> menuService.create(menuWithInvalidMenuGroupId);

            // then
            assertThatThrownBy(throwingCallable).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 메뉴_상품_목록에_있는_상품이_등록된_상품이_아니라면_예외가_발생한다() {
            // given
            final var menuService = new MenuService(mockMenuDao, mockMenuGroupDao, mockMenuProductDao, mockProductDao);
            final var menuWithInvalidMenuProductId = MenuFactory.createMenuOf("후라이드치킨", BigDecimal.valueOf(1000), 1L);
            final var menuProduct = MenuFactory.createMenuProductOf(1L, 1L);
            menuWithInvalidMenuProductId.getMenuProducts().add(menuProduct);

            when(mockMenuGroupDao.existsById(1L)).thenReturn(true);
            when(mockProductDao.findById(1L)).thenReturn(Optional.empty());

            // when
            final ThrowingCallable throwingCallable = () -> menuService.create(menuWithInvalidMenuProductId);

            // then
            assertThatThrownBy(throwingCallable).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 메뉴_상품_목록의_상품_수량이_0_이라면_예외가_발생한다() {
            // given
            final var menuService = new MenuService(mockMenuDao, mockMenuGroupDao, mockMenuProductDao, mockProductDao);
            final var menuWithInvalidMenuProductQuantity = MenuFactory.createMenuOf("후라이드치킨", BigDecimal.valueOf(1000), 1L);
            final var menuProduct = MenuFactory.createMenuProductOf(1L, 0L);
            menuWithInvalidMenuProductQuantity.getMenuProducts().add(menuProduct);

            when(mockMenuGroupDao.existsById(1L)).thenReturn(true);
            when(mockProductDao.findById(1L)).thenReturn(Optional.of(ProductFactory.createProductOf(1L, "후라이드치킨", BigDecimal.valueOf(1000))));
            when(mockMenuDao.save(menuWithInvalidMenuProductQuantity)).thenReturn(menuWithInvalidMenuProductQuantity);
            when(mockMenuProductDao.save(menuProduct)).thenReturn(menuProduct);

            // when
            final ThrowingCallable throwingCallable = () -> menuService.create(menuWithInvalidMenuProductQuantity);

            // then
            assertThatThrownBy(throwingCallable).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 메뉴가_정상적으로_등록된다() {
            // given
            final var menuService = new MenuService(mockMenuDao, mockMenuGroupDao, mockMenuProductDao, mockProductDao);
            final var menu = MenuFactory.createMenuOf("메뉴화 된 후라이드 치킨", BigDecimal.valueOf(1000), 1L);
            final var menuProduct = MenuFactory.createMenuProductOf(1L, 1L);
            menu.getMenuProducts().add(menuProduct);

            when(mockMenuGroupDao.existsById(1L)).thenReturn(true);
            when(mockProductDao.findById(1L)).thenReturn(Optional.of(ProductFactory.createProductOf(1L, "후라이드치킨", BigDecimal.valueOf(1000))));
            when(mockMenuDao.save(menu)).thenReturn(menu);
            when(mockMenuProductDao.save(menuProduct)).thenReturn(menuProduct);

            // when
            final var savedMenu = menuService.create(menu);

            // then
            assertThat(savedMenu).isNotNull();
        }
    }
}
