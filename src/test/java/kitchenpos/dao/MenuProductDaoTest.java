package kitchenpos.dao;

import kitchenpos.CustomParameterizedTest;
import kitchenpos.domain.MenuProduct;
import kitchenpos.fixture.MenuFixture;
import kitchenpos.fixture.MenuProductFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.aggregator.AggregateWith;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.aggregator.ArgumentsAggregationException;
import org.junit.jupiter.params.aggregator.ArgumentsAggregator;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("MenuProductDao 테스트")
@SpringBootTest
@Transactional
class MenuProductDaoTest {
    @Autowired
    private MenuProductDao menuProductDao;

    private static Stream<Arguments> saveFailureWhenDbLimit() {
        return Stream.of(
                Arguments.of(0L, MenuProductFixture.후라이드치킨_후라이드치킨.getProductId()),
                Arguments.of(null, MenuProductFixture.후라이드치킨_후라이드치킨.getProductId()),
                Arguments.of(MenuProductFixture.후라이드치킨_후라이드치킨.getMenuId(), 0L),
                Arguments.of(MenuProductFixture.후라이드치킨_후라이드치킨.getMenuId(), null)
        );
    }

    private static Stream<Arguments> save() {
        return Stream.of(
                Arguments.of(MenuProductFixture.후라이드치킨_후라이드치킨.getMenuId(),
                        MenuProductFixture.후라이드치킨_후라이드치킨_NO_KEY.getProductId(),
                        MenuProductFixture.후라이드치킨_후라이드치킨_NO_KEY.getQuantity()
                ),
                Arguments.of(MenuProductFixture.후라이드치킨_후라이드치킨.getMenuId(),
                        MenuProductFixture.후라이드치킨_후라이드치킨_NO_KEY.getProductId(),
                        MenuProductFixture.후라이드치킨_후라이드치킨_NO_KEY.getQuantity()
                )
        );
    }

    @DisplayName("메뉴상품 저장 - 성공")
    @CustomParameterizedTest
    @MethodSource
    void save(@AggregateWith(MenuProductAggregator.class) MenuProduct menuProduct) {
        //given
        //when
        final MenuProduct actual = menuProductDao.save(menuProduct);
        //then
        assertThat(actual.getSeq()).isNotNull();
        assertThat(actual.getMenuId()).isEqualTo(menuProduct.getMenuId());
        assertThat(actual.getProductId()).isEqualTo(menuProduct.getProductId());
    }

    @DisplayName("메뉴상품 저장 - 실패 - DB 제약사항")
    @CustomParameterizedTest
    @MethodSource("saveFailureWhenDbLimit")
    void saveFailureWhenDbLimit(Long menuId, Long productId) {
        //given
        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.setMenuId(menuId);
        menuProduct.setMenuId(productId);
        //when
        //then
        assertThatThrownBy(() -> menuProductDao.save(menuProduct))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @DisplayName("메뉴상품 조회 - 성공 -  id 기반 조히")
    @Test
    void findById() {
        //given
        //when
        final Optional<MenuProduct> actual = menuProductDao.findById(MenuProductFixture.후라이드치킨_후라이드치킨.getSeq());
        //then
        assertThat(actual).isNotEmpty();
        assertThat(actual.get().getMenuId()).isEqualTo(MenuProductFixture.후라이드치킨_후라이드치킨.getMenuId());
        assertThat(actual.get().getProductId()).isEqualTo(MenuProductFixture.후라이드치킨_후라이드치킨.getProductId());
    }

    @DisplayName("메뉴상품 조회 - 성공 -  저장된 Id가 없을 때")
    @Test
    void findByIdWhenNotFound() {
        //given
        //when
        final Optional<MenuProduct> actual = menuProductDao.findById(0L);
        //then
        assertThat(actual).isEmpty();
    }

    @DisplayName("메뉴상품 조회 - 성공 -  전체 메뉴상품 조회")
    @Test
    void findAll() {
        //given
        //when
        final List<MenuProduct> menuProducts = menuProductDao.findAll();
        //then
        assertThat(menuProducts).extracting(MenuProduct::getMenuId)
                .containsAnyElementsOf(MenuProductFixture.menuProductIds());
    }

    @DisplayName("메뉴상품 전체 조회 - 성공 - menuId 기반 조회")
    @Test
    void findAllByMenuId() {
        //given
        //when
        final List<MenuProduct> actual = menuProductDao.findAllByMenuId(MenuFixture.후라이드치킨.getId());
        //then
        assertThat(actual).extracting(MenuProduct::getMenuId)
                .contains(MenuFixture.후라이드치킨.getId());
    }

    private static class MenuProductAggregator implements ArgumentsAggregator {
        @Override
        public Object aggregateArguments(ArgumentsAccessor accessor, ParameterContext context) throws ArgumentsAggregationException {
            final MenuProduct menuProduct = new MenuProduct();
            menuProduct.setMenuId(accessor.getLong(0));
            menuProduct.setProductId(accessor.getLong(1));
            menuProduct.setQuantity(accessor.getLong(2));
            return menuProduct;
        }
    }
}