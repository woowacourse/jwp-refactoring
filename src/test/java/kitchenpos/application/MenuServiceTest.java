package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuRequest;
import kitchenpos.dto.MenuResponse;
import kitchenpos.fixture.ProductFixture;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuProductRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static kitchenpos.fixture.MenuFixture.createMenuWithMenuGroup;
import static kitchenpos.fixture.MenuGroupFixture.createMenuGroupWitId;
import static kitchenpos.fixture.MenuGroupFixture.createMenuGroupWithoutId;
import static kitchenpos.fixture.MenuProductFixture.createMenuProductWithMenuAndProduct;
import static kitchenpos.fixture.MenuProductFixture.createMenuProductWithProduct;
import static kitchenpos.fixture.ProductFixture.createProductWithId;
import static kitchenpos.fixture.ProductFixture.createProductWithPrice;
import static kitchenpos.utils.TestUtils.findById;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@SpringBootTest
class MenuServiceTest {
    private static final String MENU_NAME = "후라이드";

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MenuProductRepository menuProductRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private ProductRepository productRepository;

    @DisplayName("Menu 등록 성공")
    @Test
    void create() {
        BigDecimal price = new BigDecimal(19000);
        MenuGroup savedMenuGroup = menuGroupRepository.save(createMenuGroupWithoutId());
        Product savedProduct = productRepository.save(createProductWithPrice(price));
        MenuRequest menuRequest = createMenuRequest(savedMenuGroup, savedProduct);

        MenuResponse actual = menuService.create(menuRequest);
        Menu foundMenu = findById(menuRepository, actual.getId());
        Long actualMenuProductId = actual.getMenuProductResponses().get(0).getSeq();
        MenuProduct foundMenuProduct = findById(menuProductRepository, actualMenuProductId);
        MenuResponse expected = MenuResponse.of(foundMenu, Arrays.asList(foundMenuProduct));

        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }


    @DisplayName("Menu GruopId가 DB에 존재하지 않는 경우 예외 테스트")
    @Test
    void createNotExistMenuGroupId() {
        MenuRequest menuRequest = createMenuRequest(createMenuGroupWitId(1L), createProductWithId(2L));

        assertThatIllegalArgumentException()
                .isThrownBy(() -> menuService.create(menuRequest))
                .withMessage("존재하지 않는 Menu Group Id 입니다.");
    }

    @DisplayName("Menu 전체조회 테스트")
    @Test
    void list() {
        MenuGroup savedMenuGroup = menuGroupRepository.save(createMenuGroupWithoutId());
        Menu savedMenu = menuRepository.save(createMenuWithMenuGroup(savedMenuGroup));
        Product savedProduct = productRepository.save(ProductFixture.createProductWithoutId());
        MenuProduct menuProduct = createMenuProductWithMenuAndProduct(savedMenu, savedProduct);
        MenuProduct savedMenuProduct = menuProductRepository.save(menuProduct);

        List<MenuResponse> actual = menuService.list();
        MenuResponse expected = MenuResponse.of(savedMenu, Arrays.asList(savedMenuProduct));

        assertThat(actual.get(0))
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    private MenuRequest createMenuRequest(MenuGroup menuGroup, Product product) {
        return new MenuRequest(MENU_NAME, product.getPrice(), menuGroup.getId(),
                Arrays.asList(MenuProductRequest.from(createMenuProductWithProduct(product))));
    }

    @AfterEach
    void tearDown() {
        menuProductRepository.deleteAll();
        menuRepository.deleteAll();
        menuGroupRepository.deleteAll();
        productRepository.deleteAll();
    }
}