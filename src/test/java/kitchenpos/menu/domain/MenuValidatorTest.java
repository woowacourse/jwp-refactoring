package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kitchenpos.menu.dto.application.MenuProductDto;
import kitchenpos.menu.repository.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.repository.ProductRepository;

@SpringBootTest
class MenuValidatorTest {

    @Autowired
    private MenuValidator menuValidator;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Test
    @DisplayName("예외사항이 존재하지 않는 경우 예외가 발생하지 않는다.")
    void menu() {
        assertDoesNotThrow(() -> menuValidator.validateCreateMenu(
            "name",
            new BigDecimal(1000),
            createAndSaveMenuGroup().getId(),
            new ArrayList<MenuProductDto>() {{
                add(new MenuProductDto(createAndSaveProduct().getId(), 1L));
            }}
        ));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("name이 비어있을 경우 예외가 발생한다.")
    void emptyName(String name) {
        assertThatThrownBy(() -> menuValidator.validateCreateMenu(
            name,
            new BigDecimal(1000),
            createAndSaveMenuGroup().getId(),
            new ArrayList<MenuProductDto>() {{
                add(new MenuProductDto(createAndSaveProduct().getId(), 1L));
            }}
        )).isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("메뉴의 이름은 비어있을 수 없습니다.");
    }

    @Test
    @DisplayName("price가 null일 경우 예외가 발생한다.")
    void nullPrice() {
        assertThatThrownBy(() -> menuValidator.validateCreateMenu(
            "name",
            null,
            createAndSaveMenuGroup().getId(),
            new ArrayList<MenuProductDto>() {{
                add(new MenuProductDto(createAndSaveProduct().getId(), 1L));
            }}
        )).isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("메뉴의 가격은 비어있을 수 없습니다.");
    }

    @Test
    @DisplayName("price가 0원 미만일 경우 예외가 발생한다.")
    void negativePrice() {
        assertThatThrownBy(() -> menuValidator.validateCreateMenu(
            "name",
            new BigDecimal(-1),
            createAndSaveMenuGroup().getId(),
            new ArrayList<MenuProductDto>() {{
                add(new MenuProductDto(createAndSaveProduct().getId(), 1L));
            }}
        )).isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("메뉴의 가격은 0원 미만일 수 없습니다.");
    }

    @Test
    @DisplayName("존재하지 않는 menu group id인 경우 예외가 발생한다.")
    void invalidMenuGroupId() {
        assertThatThrownBy(() -> menuValidator.validateCreateMenu(
            "name",
            new BigDecimal(1000),
            0L,
            new ArrayList<MenuProductDto>() {{
                add(new MenuProductDto(createAndSaveProduct().getId(), 1L));
            }}
        ))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("존재하지 않는 메뉴그룹입니다.");
    }

    @Test
    @DisplayName("존재하지 않는 menu product id인 경우 예외가 발생한다.")
    void invalidMenuProductId() {
        assertThatThrownBy(() -> menuValidator.validateCreateMenu(
            "name",
            new BigDecimal(1000),
            createAndSaveMenuGroup().getId(),
            new ArrayList<MenuProductDto>() {{
                add(new MenuProductDto(0L, 1L));
            }}
        ))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("존재하지 않는 제품입니다.");
    }

    private Product createAndSaveProduct() {
        Product product = new Product("product", new BigDecimal(1000));
        return productRepository.save(product);
    }

    private MenuGroup createAndSaveMenuGroup() {
        MenuGroup menuGroup = new MenuGroup("menuGroup");
        return menuGroupRepository.save(menuGroup);
    }

}
