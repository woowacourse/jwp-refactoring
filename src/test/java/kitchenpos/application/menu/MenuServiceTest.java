package kitchenpos.application.menu;

import com.sun.tools.javac.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menuGroup.domain.MenuGroup;
import kitchenpos.product.domain.Product;
import kitchenpos.menu.application.dto.MenuProductRequest;
import kitchenpos.menu.application.dto.MenuRequest;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menuGroup.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuProductRepository;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.product.domain.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@Transactional
@SpringBootTest
@Sql({"/h2-truncate.sql"})
class MenuServiceTest {

    @Autowired
    private MenuService menuService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MenuProductRepository menuProductRepository;
    @Autowired
    private MenuGroupRepository menuGroupRepository;

    private Product savedProduct;
    private MenuGroup savedMenuGroup;
    private Menu savedMenu;

    @BeforeEach
    void setup() {
        Product product = new Product("치킨", BigDecimal.valueOf(10000L));
        savedProduct = productRepository.save(product);

        MenuGroup menuGroup = new MenuGroup("한식");
        savedMenuGroup = menuGroupRepository.save(menuGroup);

        MenuProductRequest menuProductRequest = new MenuProductRequest(savedProduct.getId(), 2L);
        MenuRequest menuRequest = new MenuRequest("두마리치킨", BigDecimal.valueOf(20000L), savedMenuGroup.getId(), List.of(menuProductRequest));
        savedMenu = menuService.create(menuRequest);
    }

    @Test
    @DisplayName("메뉴 상품 등록에 성공한다.")
    void succeedInRegisteringMenu() {
        //given & when & then
        assertSoftly(softly -> {
            softly.assertThat(savedMenu.getId()).isNotNull();
            softly.assertThat(savedMenu.getName()).isEqualTo("두마리치킨");
            softly.assertThat(savedMenu.getMenuGroupId()).isEqualTo(savedMenuGroup.getId());
            softly.assertThat(savedMenu.getMenuProducts()).hasSize(1);
        });
    }

    @Test
    @DisplayName("메뉴 가격이 0미만일 경우 예외가 발생한다.")
    void failToRegisterMenuWithWrongPrice() {
        //given
        MenuRequest menuRequest = new MenuRequest("두마리치킨", BigDecimal.valueOf(-100L), savedMenuGroup.getId(), List.of(new MenuProductRequest(savedProduct.getId(), 2L)));

        //when & then
        assertThatThrownBy(() -> menuService.create(menuRequest)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 가격이 상품가격 x 수량을 초과할 경우 예외가 발생한다.")
    void failToRegisterMenuWithUnMatchedPrice() {
        // given
        MenuRequest menuRequest = new MenuRequest("두마리치킨", BigDecimal.valueOf(30000L), savedMenuGroup.getId(), List.of(new MenuProductRequest(savedProduct.getId(), 2L)));

        // when & then
        assertThatThrownBy(() -> menuService.create(menuRequest)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴가 속한 메뉴그룹이 등록되지 않았을 경우 예외가 발생한다.")
    void failToRegisterMenuWithWrongMenuGroup() {
        // given
        Long unsavedGroupId = 1000L;
        MenuRequest menuRequest = new MenuRequest("두마리치킨", BigDecimal.valueOf(20000L), unsavedGroupId, List.of(new MenuProductRequest(savedProduct.getId(), 2L)));

        // when & then
        assertThatThrownBy(() -> menuService.create(menuRequest)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 상품 내 속하는 상품이 등록되지 않았을 경우 예외가 발생한다.")
    void failToRegisterMenuWithWrongProduct() {
        // given
        Long unsavedProductId = 1000L;
        MenuProductRequest menuProductRequest = new MenuProductRequest(unsavedProductId, 2L);
        MenuRequest menuRequest = new MenuRequest("두마리치킨", BigDecimal.valueOf(20000L), savedMenuGroup.getId(), List.of(menuProductRequest));

        // when & then
        assertThatThrownBy(() -> menuService.create(menuRequest)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 목록을 조회할 수 있다.")
    void succeedInSearchingMenuList() {
        // given
        MenuProductRequest menuProductRequestA = new MenuProductRequest(savedProduct.getId(), 2L);
        MenuRequest menuRequestA = new MenuRequest("두마리치킨", BigDecimal.valueOf(20000L), savedMenuGroup.getId(), List.of(menuProductRequestA));

        MenuProductRequest menuProductRequestB = new MenuProductRequest(savedProduct.getId(), 2L);
        MenuRequest menuRequestB = new MenuRequest("두마리치킨", BigDecimal.valueOf(20000L), savedMenuGroup.getId(), List.of(menuProductRequestB));


        // when
        menuService.create(menuRequestA);
        menuService.create(menuRequestB);

        // then
        assertThat(menuService.list()).hasSize(3);
    }
}
