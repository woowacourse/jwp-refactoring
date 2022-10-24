package kitchenpos.application;

import static kitchenpos.fixture.MenuFixture.메뉴_생성;
import static kitchenpos.fixture.MenuGroupFixture.메뉴_그룹_생성;
import static kitchenpos.fixture.MenuProductFixture.메뉴_상품_생성;
import static kitchenpos.fixture.ProductFixture.상품_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.application.support.IntegrationTest;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
public class MenuServiceTest {

    @Autowired
    private MenuService sut;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private MenuProductDao menuProductDao;

    @Autowired
    private ProductDao productDao;

    @Nested
    @DisplayName("메뉴 등록")
    class CreateTest {

        @DisplayName("정상적인 경우 메뉴를 등록할 수 있다.")
        @Test
        void createMenu() {
            final MenuGroup 떡잎_유치원 = menuGroupDao.save(메뉴_그룹_생성("떡잎 유치원"));
            final Product 짱구 = productDao.save(상품_생성("짱구", 100));
            final MenuProduct 짱구_메뉴_상품 = 메뉴_상품_생성(짱구.getId(), 5L);

            final Menu menu = new Menu();
            menu.setName("해바라기반");
            menu.setPrice(BigDecimal.valueOf(500));
            menu.setMenuGroupId(떡잎_유치원.getId());
            menu.setMenuProducts(List.of(짱구_메뉴_상품));

            final Menu actual = sut.create(menu);

            assertAll(
                    () -> assertThat(actual.getId()).isNotNull(),
                    () -> assertThat(actual.getPrice()).isEqualTo(menu.getPrice()),
                    () -> assertThat(actual.getMenuGroupId()).isEqualTo(menu.getMenuGroupId()),
                    () -> assertThat(actual.getMenuProducts()).hasSize(1)
            );
        }

        @DisplayName("메뉴 가격이 없는 경우 등록할 수 없다.")
        @Test
        void createMenuWithNullPrice() {
            final MenuGroup 떡잎_유치원 = menuGroupDao.save(메뉴_그룹_생성("떡잎 유치원"));
            final Product 짱구 = productDao.save(상품_생성("짱구", 100));
            final MenuProduct 짱구_메뉴_상품 = 메뉴_상품_생성(짱구.getId(), 5L);

            final Menu 해바라기반 = 메뉴_생성("해바라기반", null, 떡잎_유치원.getId(), List.of(짱구_메뉴_상품));

            assertThatThrownBy(() -> sut.create(해바라기반))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("메뉴 가격이 0원보다 적은 경우 등록할 수 없다.")
        @Test
        void createMenuWithPriceLessThanZero() {
            final MenuGroup 떡잎_유치원 = menuGroupDao.save(메뉴_그룹_생성("떡잎 유치원"));
            final Product 짱구 = productDao.save(상품_생성("짱구", 100));
            final MenuProduct 짱구_메뉴_상품 = 메뉴_상품_생성(짱구.getId(), 5L);

            final Menu 해바라기반 = 메뉴_생성("해바라기반", -1, 떡잎_유치원.getId(), List.of(짱구_메뉴_상품));

            assertThatThrownBy(() -> sut.create(해바라기반))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("존재하지 않는 메뉴 그룹에 해당 메뉴가 속한 경우 등록할 수 없다.")
        @Test
        void createMenuWithNotExistMenuGroup() {
            final Long 존재하지_않는_메뉴_그룹_ID = 1L;
            final Product 짱구 = productDao.save(상품_생성("짱구", 100));
            final MenuProduct 짱구_메뉴_상품 = 메뉴_상품_생성(짱구.getId(), 5L);

            final Menu 해바라기반 = 메뉴_생성("해바라기반", 500, 존재하지_않는_메뉴_그룹_ID, List.of(짱구_메뉴_상품));

            assertThatThrownBy(() -> sut.create(해바라기반))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("존재하지 않는 메뉴 상품이 해당 메뉴에 속한 경우 등록할 수 없다.")
        @Test
        void createMenuWithNotExistMenuProduct() {
            final MenuGroup 떡잎_유치원 = menuGroupDao.save(메뉴_그룹_생성("떡잎 유치원"));
            final Product 짱구 = productDao.save(상품_생성("짱구", 100));
            final Long 존재하지_않는_상품_ID = 짱구.getId() + 1;
            final MenuProduct 짱구_메뉴_상품 = 메뉴_상품_생성(존재하지_않는_상품_ID, 5L);

            final Menu 해바라기반 = 메뉴_생성("해바라기반", 500, 떡잎_유치원.getId(), List.of(짱구_메뉴_상품));

            assertThatThrownBy(() -> sut.create(해바라기반))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("메뉴의 가격이 해당 메뉴에 속한 메뉴 상품들의 가격의 총합보다 크면 등록할 수 없다.")
        @Test
        void createMenuWithIncorrectPrice() {
            final MenuGroup 떡잎_유치원 = menuGroupDao.save(메뉴_그룹_생성("떡잎 유치원"));
            final Product 짱구 = productDao.save(상품_생성("짱구", 100));
            final MenuProduct 짱구_메뉴_상품 = 메뉴_상품_생성(짱구.getId(), 5L);

            final Menu 해바라기반 = 메뉴_생성("해바라기반", 500 + 1, 떡잎_유치원.getId(), List.of(짱구_메뉴_상품));

            assertThatThrownBy(() -> sut.create(해바라기반))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("메뉴 목록을 조회할 수 있다.")
    @Test
    void getMenus() {
        final MenuGroup 떡잎_유치원 = menuGroupDao.save(메뉴_그룹_생성("떡잎 유치원"));
        final Product 짱구 = productDao.save(상품_생성("짱구", 100));
        final MenuProduct 짱구_메뉴_상품 = 메뉴_상품_생성(짱구.getId(), 5L);

        final Product 짱아 = productDao.save(상품_생성("짱아", 100));
        final MenuProduct 짱아_메뉴_상품 = 메뉴_상품_생성(짱아.getId(), 3L);

        final Menu 해바라기반 = 메뉴_생성("해바라기반", 500, 떡잎_유치원.getId(), List.of(짱구_메뉴_상품, 짱아_메뉴_상품));

        menuDao.save(해바라기반);

        final Product 치타 = productDao.save(상품_생성("치타", 100));
        final MenuProduct 치타_메뉴_상품 = 메뉴_상품_생성(치타.getId(), 1L);

        final Menu 장미반 = 메뉴_생성("장미반", 10, 떡잎_유치원.getId(), List.of(치타_메뉴_상품));

        menuDao.save(장미반);

        assertThat(sut.list())
                .hasSize(2)
                .extracting("name", "price", "menuGroupId")
                .containsExactly(
                        tuple(해바라기반.getName(), 해바라기반.getPrice(), 해바라기반.getMenuGroupId()),
                        tuple(장미반.getName(), 장미반.getPrice(), 장미반.getMenuGroupId())
                );
    }
}
