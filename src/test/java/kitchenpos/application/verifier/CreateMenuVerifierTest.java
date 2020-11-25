package kitchenpos.application.verifier;

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

import kitchenpos.domain.model.menu.Menu;
import kitchenpos.domain.model.menugroup.MenuGroupRepository;
import kitchenpos.domain.model.product.Product;
import kitchenpos.domain.model.product.ProductRepository;

@ExtendWith(MockitoExtension.class)
class CreateMenuVerifierTest {
    @Mock
    private MenuGroupRepository menuGroupRepository;
    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private CreateMenuVerifier createMenuVerifier;

    @DisplayName("메뉴 생성")
    @TestFactory
    Stream<DynamicTest> toMenu() {
        return Stream.of(
                dynamicTest("유효성 통과.", this::validateSuccess),
                dynamicTest("요청한 메뉴의 메뉴 그룹이 존재하지 않을때 IllegalArgumentException 발생",
                        this::noMenuGroup),
                dynamicTest("상품이 존재하지 않을때 IllegalArgumentException 발생", this::noProduct),
                dynamicTest("메뉴의 가격이 상품의 가격의 총 합보다 클때 IllegalArgumentException 발생",
                        this::bigMenuPrice)
        );
    }

    private void validateSuccess() {
        Menu menu = createMenu();
        Product product = new Product(1L, "강정치킨", BigDecimal.valueOf(17_000L));

        given(menuGroupRepository.existsById(menu.getMenuGroupId())).willReturn(true);
        given(productRepository.findById(menu.getMenuProducts().get(0).getProductId())).willReturn(
                Optional.of(product));

        assertDoesNotThrow(
                () -> createMenuVerifier.toMenu(MENU_REQUEST.getName(), MENU_REQUEST.getPrice(),
                        MENU_REQUEST.getMenuGroupId(), MENU_REQUEST.getMenuProducts()));
    }

    private void noMenuGroup() {
        Menu menu = createMenu();

        given(menuGroupRepository.existsById(menu.getMenuGroupId())).willReturn(false);

        throwIllegalArgumentException();
    }

    private void noProduct() {
        Menu menu = createMenu();
        given(menuGroupRepository.existsById(menu.getMenuGroupId())).willReturn(true);
        given(productRepository.findById(menu.getMenuProducts().get(0).getProductId()))
                .willReturn(Optional.empty());

        throwIllegalArgumentException();
    }

    private void bigMenuPrice() {
        Menu menu = createMenu();
        Product product = new Product(1L, "강정치킨", BigDecimal.valueOf(8_400L));

        given(menuGroupRepository.existsById(menu.getMenuGroupId())).willReturn(true);
        given(productRepository.findById(menu.getMenuProducts().get(0).getProductId()))
                .willReturn(Optional.of(product));

        throwIllegalArgumentException();
    }

    private Menu createMenu() {
        return new Menu(null, MENU_REQUEST.getName(), MENU_REQUEST.getPrice(),
                MENU_REQUEST.getMenuGroupId(), MENU_REQUEST.getMenuProducts());
    }

    private void throwIllegalArgumentException() {
        assertThatIllegalArgumentException().isThrownBy(
                () -> createMenuVerifier.toMenu(MENU_REQUEST.getName(), MENU_REQUEST.getPrice(),
                        MENU_REQUEST.getMenuGroupId(), MENU_REQUEST.getMenuProducts()));
    }
}
