package kitchenpos.application;

import static kitchenpos.KitchenPosFixtures.까르보치킨;
import static kitchenpos.KitchenPosFixtures.짜장치킨;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MenuServiceTest extends ServiceTest {
    @Autowired
    private MenuService menuService;
    @Autowired
    private MenuGroupDao menuGroupDao;
    @Autowired
    private ProductDao productDao;
    private MenuGroup menuGroup;
    private Product productA;
    private Product productB;

    @BeforeEach
    void setUpForMenu() {
        menuGroup = menuGroupDao.save(new MenuGroup("뼈 한 마리"));
        productA = productDao.save(까르보치킨);
        productB = productDao.save(짜장치킨);
    }

    @DisplayName("메뉴를 생성할 수 있다")
    @Test
    void create() {
        // given
        final var menuRequest = new Menu(
                "치토스치킨",
                new BigDecimal(18000),
                menuGroup.getId(),
                List.of(
                        new MenuProduct()
                )
        );

        // when
        final var menu = menuService.create(menuRequest);

        // then
        assertAll(
                () -> assertThat(menu.getId()).isEqualTo(1L),
                () -> assertThat(menu.getName()).isEqualTo("치토스치킨")
        );
    }

    @Test
    void list() {
    }
}
