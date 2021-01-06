package kitchenpos.menu.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class MenuServiceTest {

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private ProductRepository productDao;

    @Autowired
    private MenuService menuService;

    @DisplayName("create: 메뉴 생성 테스트")
    @Test
    void createTest() {
        final Product product = productDao.save(new Product("후라이드", BigDecimal.valueOf(8000)));
        final MenuProductRequest menuProduct = new MenuProductRequest(product.getId(), 2L);
        final MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("세트 메뉴"));
        final MenuRequest menu = new MenuRequest("후라이드+후라이드", BigDecimal.valueOf(16000), menuGroup.getId(),
                Collections.singletonList(menuProduct));

        final MenuResponse actual = menuService.create(menu);

        assertAll(
                () -> assertThat(actual.getName()).isEqualTo(menu.getName()),
                () -> assertThat(actual.getPrice()).isEqualByComparingTo(menu.getPrice()),
                () -> assertThat(actual.getMenuGroupId()).isEqualTo(menu.getMenuGroupId()),
                () -> assertThat(actual.getMenuProducts()).hasSize(1)
        );
    }

    @DisplayName("create: 해당 메뉴 그룹이 DB에 없는 메뉴 그룹일 경우 예외처리")
    @Test
    void createTestByPriceSmallThanZero() {
        final Product product = productDao.save(new Product("후라이드", BigDecimal.valueOf(8000)));
        final MenuProductRequest menuProduct = new MenuProductRequest(product.getId(), 2L);
        final MenuRequest menu = new MenuRequest("후라이드+후라이드", BigDecimal.valueOf(19000), 1L,
                Collections.singletonList(menuProduct));

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("해당 메뉴그룹을 찾을수 없습니다.");
    }

    @DisplayName("create: 해당 메뉴 그룹이 DB에 없는 메뉴 그룹일 경우 예외처리")
    @Test
    void createTestByMenuGroupNotExist() {
        final Product product = productDao.save(new Product("후라이드", BigDecimal.valueOf(8000)));
        final MenuProductRequest menuProduct = new MenuProductRequest(product.getId(), 2L);
        final MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("세트 메뉴"));
        final MenuRequest menu = new MenuRequest("후라이드+후라이드", BigDecimal.valueOf(-1), menuGroup.getId(),
                Collections.singletonList(menuProduct));

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("금액이 0미만일수 없습니다.");
    }

    @DisplayName("create: 메뉴상품의 가격 총합이 0보다 작으면 예외처리")
    @Test
    void createTestByProductPriceSmallThanZero() {
        final Product product = productDao.save(new Product("후라이드", BigDecimal.valueOf(8000)));
        final MenuProductRequest menuProduct = new MenuProductRequest(product.getId(), -1L);
        final MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("세트 메뉴"));
        final MenuRequest menu = new MenuRequest("후라이드+후라이드", BigDecimal.valueOf(-1), menuGroup.getId(),
                Collections.singletonList(menuProduct));

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("list: 전체 메뉴 그룹 목록 조회 테스트")
    @Test
    void listTest() {
        final Product product = productDao.save(new Product("후라이드", BigDecimal.valueOf(8000)));
        final MenuProduct menuProduct = new MenuProduct(product, 2L);
        final MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("세트 메뉴"));
        final Menu menu = menuRepository.save(new Menu("후라이드+후라이드", BigDecimal.valueOf(16000), menuGroup,
                Collections.singletonList(menuProduct)));

        final List<MenuResponse> menus = menuService.list();

        assertAll(
                () -> assertThat(menus).hasSize(1),
                () -> assertThat(menus.get(0).getName()).isEqualTo(menu.getName())
        );
    }
}
