package kitchenpos.application;

import com.sun.tools.javac.util.List;
import kitchenpos.dao.MenuGroupRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.vo.product.ProductRequest;
import kitchenpos.vo.product.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@SpringBootTest
@Sql({"/h2-truncate.sql"})
class MenuServiceTest {

    @Autowired
    private MenuService menuService;

    @Autowired
    private ProductService productService;

    @Autowired
    private MenuGroupService menuGroupService;
    @Autowired
    private MenuGroupRepository menuGroupRepository;

    private ProductResponse savedProduct;
    private MenuGroup savedMenuGroup;
    private MenuProduct savedMenuProduct;

    @BeforeEach
    void setup() {
        ProductRequest productRequest = new ProductRequest("치킨", BigDecimal.valueOf(10000L));
        savedProduct = productService.create(productRequest);

        savedMenuProduct = new MenuProduct();
        savedMenuProduct.setProductId(savedProduct.getId());
        savedMenuProduct.setQuantity(2L);

        MenuGroup menuGroup = new MenuGroup("한식");

        savedMenuGroup = menuGroupRepository.save(menuGroup);

    }

    @Test
    @DisplayName("메뉴 상품 등록에 성공한다.")
    void succeedInRegisteringMenu() {
        //given
        Menu menu = new Menu();
        menu.setName("두마리치킨");
        menu.setPrice(BigDecimal.valueOf(20000L));
        menu.setMenuGroupId(savedMenuGroup.getId());
        menu.setMenuProducts(List.of(savedMenuProduct));

        //when
        Menu savedMenu = menuService.create(menu);

        //then
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
        Menu menu = new Menu();
        menu.setName("두마리치킨");
        menu.setPrice(BigDecimal.valueOf(-100L));
        menu.setMenuGroupId(savedMenuGroup.getId());
        menu.setMenuProducts(List.of(savedMenuProduct));

        //when & then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 가격이 상품가격 x 수량을 초과할 경우 예외가 발생한다.")
    void failToRegisterMenuWithUnMatchedPrice() {
        // given
        Menu menu = new Menu();
        menu.setName("두마리치킨");
        menu.setPrice(BigDecimal.valueOf(30000L));
        menu.setMenuGroupId(savedMenuGroup.getId());
        menu.setMenuProducts(List.of(savedMenuProduct));

        // when & then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴가 속한 메뉴그룹이 등록되지 않았을 경우 예외가 발생한다.")
    void failToRegisterMenuWithWrongMenuGroup() {
        // given
        Menu menu = new Menu();
        menu.setName("두마리치킨");
        menu.setPrice(BigDecimal.valueOf(20000L));
        menu.setMenuProducts(List.of(savedMenuProduct));

        Long unsavedMenuGroupId = 10000L;
        menu.setMenuGroupId(unsavedMenuGroupId);

        // when & then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 상품 내 속하는 상품이 등록되지 않았을 경우 예외가 발생한다.")
    void failToRegisterMenuWithWrongProduct() {
        // given
        Menu menu = new Menu();
        menu.setName("두마리치킨");
        menu.setPrice(BigDecimal.valueOf(20000L));
        menu.setMenuGroupId(savedMenuGroup.getId());

        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setQuantity(1l);
        menu.setMenuProducts(List.of(menuProduct));

        // when & then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 목록을 조회할 수 있다.")
    void succeedInSearchingMenuList() {
        // given
        Menu menuA = new Menu();
        menuA.setName("두마리치킨");
        menuA.setPrice(BigDecimal.valueOf(20000L));
        menuA.setMenuGroupId(savedMenuGroup.getId());
        menuA.setMenuProducts(List.of(savedMenuProduct));

        Menu menuB = new Menu();
        menuB.setName("핫두마리치킨");
        menuB.setPrice(BigDecimal.valueOf(20000L));
        menuB.setMenuGroupId(savedMenuGroup.getId());
        menuB.setMenuProducts(List.of(savedMenuProduct));

        // when
        Menu savedMenuA = menuService.create(menuA);
        Menu savedMenuB = menuService.create(menuB);

        // then
        assertThat(menuService.list()).hasSize(2);
    }
}
