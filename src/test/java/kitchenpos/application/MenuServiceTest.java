package kitchenpos.application;

import static kitchenpos.common.fixtures.MenuGroupFixtures.루나세트_이름;
import static kitchenpos.common.fixtures.ProductFixtures.야채곱창_가격;
import static kitchenpos.common.fixtures.ProductFixtures.야채곱창_이름;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.sun.tools.javac.util.List;
import kitchenpos.common.builder.MenuBuilder;
import kitchenpos.common.builder.MenuGroupBuilder;
import kitchenpos.common.builder.MenuProductBuilder;
import kitchenpos.common.builder.ProductBuilder;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MenuServiceTest {

    @Autowired
    private MenuService menuService;

    @Autowired
    private ProductService productService;

    @Autowired
    private MenuGroupService menuGroupService;

    private Product 야채곱창;
    private MenuGroup 루나세트;

    @BeforeEach
    void setUp() {
        야채곱창 = new ProductBuilder()
                .name(야채곱창_이름)
                .price(야채곱창_가격)
                .build();

        루나세트 = new MenuGroupBuilder()
                .name(루나세트_이름)
                .build();
    }

    @DisplayName("메뉴를 등록한다.")
    @Test
    void 메뉴를_등록한다() {
        // given
        Product 저장된_야채곱창 = productService.create(야채곱창);
        MenuGroup 저장된_루나세트 = menuGroupService.create(루나세트);

        MenuProduct 루나_야채곱창 = new MenuProductBuilder()
                .productId(저장된_야채곱창.getId())
                .quantity(1)
                .build();

        Menu 야채곱창_메뉴 = new MenuBuilder().name(야채곱창_이름)
                .price(야채곱창_가격)
                .menuGroupId(저장된_루나세트.getId())
                .menuProducts(List.of(루나_야채곱창))
                .build();

        // when
        Menu actual = menuService.create(야채곱창_메뉴);

        // then
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getName()).isEqualTo(야채곱창_이름),
                () -> assertThat(actual.getPrice()).isEqualTo(야채곱창_가격)
        );
    }
}
