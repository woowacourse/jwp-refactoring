package kitchenpos.application;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.Product;
import kitchenpos.dto.CreateMenuRequest;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class MenuServiceTest {
    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private ProductRepository productRepository;

    private MenuGroup menuGroup;
    private MenuProduct menuProduct;

    @BeforeEach
    private void setUp() {
        this.menuGroup = menuGroupRepository.save(new MenuGroup("피자"));

        Product product = new Product("감자", BigDecimal.valueOf(4000));
        Product savedProduct = productRepository.save(product);

        this.menuProduct = new MenuProduct(1L, savedProduct, 1L);
    }

    @DisplayName("Menu 생성을 확인한다.")
    @Test
    void createTest() {
        CreateMenuRequest request = new CreateMenuRequest("감자_피자", 1000L, menuGroup, Collections.singletonList(menuProduct));
        Menu result = menuService.create(request);

        Menu savedMenu = menuRepository.findById(result.getId()).get();
        assertThat(savedMenu.getName()).isEqualTo(result.getName());
    }

    @DisplayName("생성 시 menu group를 보유해야 한다.")
    @Test
    void createExceptionTest_noGroupId() {
        CreateMenuRequest request = new CreateMenuRequest("감자_피자", 1000L, null, Collections.singletonList(menuProduct));
        assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(NullPointerException.class);
    }

    @DisplayName("price가 제품의 sum보다 크면 예외가 발생한다.")
    @Test
    void createSumExceptionTest() {
        CreateMenuRequest request = new CreateMenuRequest("감자_피자", 10000L, null, Collections.singletonList(menuProduct));
        assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(NullPointerException.class);
    }

    @DisplayName("생성 시 menu group의 product는 저장된 상태여야 한다.")
    @Test
    void name() {
        Product notSavedProduct = new Product("감자", BigDecimal.valueOf(4000));
        this.menuProduct = new MenuProduct(1L, notSavedProduct, 1L);

        CreateMenuRequest request = new CreateMenuRequest("감자_피자", 1000L, menuGroup, Collections.singletonList(menuProduct));
        assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(NullPointerException.class);
    }
}