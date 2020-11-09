package kitchenpos.domain.model.menu;

import static kitchenpos.fixture.RequestFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.*;
import static org.mockito.BDDMockito.*;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.domain.model.menugroup.MenuGroupRepository;
import kitchenpos.domain.model.product.Product;
import kitchenpos.domain.model.product.ProductRepository;

@ExtendWith(MockitoExtension.class)
class MenuTest {
    @Mock
    private MenuGroupRepository menuGroupRepository;
    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private MenuCreateService menuCreateService;

    @DisplayName("메뉴 생성")
    @TestFactory
    Stream<DynamicTest> create() {
        return Stream.of(
                dynamicTest("유효성 통과.", this::validateSuccess),
                dynamicTest("메뉴 상품이 존재해야 한다.", this::noMenuGroup),
                dynamicTest("상품이 존재해야 한다.", this::noProduct),
                dynamicTest("메뉴의 가격은 상품의 가격의 총 합보다 작아야 한다.", this::bigMenuPrice)
        );
    }

    private void validateSuccess() {
        Menu menu = MENU_REQUEST.toEntity();
        Product product = new Product(1L, "강정치킨", BigDecimal.valueOf(17_000L));

        given(menuGroupRepository.existsById(menu.getMenuGroupId())).willReturn(true);
        given(productRepository.findById(menu.getMenuProducts().get(0).getProductId())).willReturn(
                Optional.of(product));

        assertDoesNotThrow(() -> menu.create(menuCreateService));
    }

    private void noMenuGroup() {
        Menu menu = MENU_REQUEST.toEntity();

        given(menuGroupRepository.existsById(menu.getMenuGroupId())).willReturn(false);

        assertThatIllegalArgumentException().isThrownBy(
                () -> menu.create(menuCreateService));
    }

    private void noProduct() {
        Menu menu = MENU_REQUEST.toEntity();
        given(menuGroupRepository.existsById(menu.getMenuGroupId())).willReturn(true);
        given(productRepository.findById(menu.getMenuProducts().get(0).getProductId()))
                .willReturn(Optional.empty());

        assertThatIllegalArgumentException().isThrownBy(
                () -> menu.create(menuCreateService));
    }

    private void bigMenuPrice() {
        Menu menu = MENU_REQUEST.toEntity();
        Product product = new Product(1L, "강정치킨", BigDecimal.valueOf(8_400L));

        given(menuGroupRepository.existsById(menu.getMenuGroupId())).willReturn(true);
        given(productRepository.findById(menu.getMenuProducts().get(0).getProductId()))
                .willReturn(Optional.of(product));

        assertThatIllegalArgumentException().isThrownBy(
                () -> menu.create(menuCreateService));
    }
}