package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import kitchenpos.menugroup.dao.MenuGroupDao;
import kitchenpos.product.dao.ProductDao;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.product.domain.Product;
import kitchenpos.menu.dto.MenuCreateRequest;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.application.MenuService;
import org.assertj.core.data.Percentage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
@SuppressWarnings("NonAsciiCharacters")
class MenuServiceTest {

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private ProductDao productDao;

    @Test
    void 메뉴를_생성할_수_있다() {
        // given
        final MenuCreateRequest request = 후라이드_세트_메뉴_생성_요청(BigDecimal.valueOf(19000));

        // when
        MenuResponse actual = menuService.create(request);

        // then
        assertAll(
                () -> assertThat(actual.getName()).isEqualTo(request.getName()),
                () -> assertThat(actual.getPrice()).isCloseTo(request.getPrice(), Percentage.withPercentage(0)),
                () -> assertThat(actual.getMenuGroupId()).isEqualTo(request.getMenuGroupId()),
                () -> assertThat(actual.getMenuProducts()).extracting("menuId", "productId", "quantity")
                        .containsExactly(tuple(actual.getId(), request.getMenuProducts().get(0).getProductId(),
                                request.getMenuProducts().get(0).getQuantity()))
        );
    }

    @Test
    void 메뉴_가격이_null인_경우_메뉴를_생성할_수_없다() {
        // given
        final MenuCreateRequest request = 후라이드_세트_메뉴_생성_요청(null);

        // when & then
        assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴_가격이_0보다_작은_경우_메뉴를_생성할_수_없다() {
        // given
        final MenuCreateRequest request = 후라이드_세트_메뉴_생성_요청(BigDecimal.valueOf(-1));

        // when & then
        assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴_그룹이_존재하지_않는_경우_메뉴를_생성할_수_없다() {
        // given
        final MenuCreateRequest nonMenuGroupRequest = 메뉴_그룹이_존재하지않는_메뉴_생성_요청();

        // when & then
        assertThatThrownBy(() -> menuService.create(nonMenuGroupRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴의_가격이_상품_개별_가격의_총합보다_크다면_메뉴를_생성할_수_없다() {
        // given
        final MenuCreateRequest request = 후라이드_세트_메뉴_생성_요청(BigDecimal.valueOf(20001));

        // when & then
        assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴_목록을_조회한다() {
        // given
        menuService.create(후라이드_세트_메뉴_생성_요청(BigDecimal.valueOf(19000)));

        // when
        List<MenuResponse> actual = menuService.list();

        // then
        assertThat(actual).hasSizeGreaterThanOrEqualTo(1);
    }

    public MenuCreateRequest 후라이드_세트_메뉴_생성_요청(BigDecimal price) {
        return new MenuCreateRequest(
                "후라이드+후라이드",
                price,
                추천메뉴_그룹().getId(),
                Collections.singletonList(후라이드_세트_메뉴_상품_생성_요청())
        );
    }

    public MenuCreateRequest 메뉴_그룹이_존재하지않는_메뉴_생성_요청() {
        return new MenuCreateRequest(
                "후라이드+후라이드",
                BigDecimal.valueOf(19000),
                -1,
                Collections.singletonList(후라이드_세트_메뉴_상품_생성_요청())
        );
    }

    public MenuGroup 추천메뉴_그룹() {
        return menuGroupDao.save(new MenuGroup("추천메뉴"));
    }

    public MenuProductRequest 후라이드_세트_메뉴_상품_생성_요청() {
        return new MenuProductRequest(후라이드().getId(), 2);
    }

    public Product 후라이드() {
        return productDao.save(new Product("후라이드", BigDecimal.valueOf(10000)));
    }
}
