package kitchenpos.domain;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.common.ServiceTest;

@ServiceTest
class MenuCreateServiceTest {
    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuCreateService menuCreateService;

    @DisplayName("유효한 메뉴를 생성한다.")
    @Test
    void createMenu() {
        MenuGroup savedGroup = menuGroupRepository.save(new MenuGroup("test_group"));

        Product savedProduct1 = productRepository.save(new Product("test1", BigDecimal.valueOf(1_000L)));
        Product savedProduct2 = productRepository.save(new Product("test2", BigDecimal.valueOf(1_000L)));

        Menu actual = menuCreateService.createMenu("test", BigDecimal.valueOf(3_000L), savedGroup.getId(),
            Arrays.asList(new MenuProductCreateInfo(savedProduct1.getId(), 2L),
                new MenuProductCreateInfo(savedProduct2.getId(), 1L)));

        assertThat(actual).isNotNull();
    }

    @DisplayName("메뉴에 등록할 제품이 없는 경우 예외 처리한다.")
    @Test
    void createMenuWithNotExistProduct() {
        MenuGroup savedGroup = menuGroupRepository.save(new MenuGroup("test_group"));

        assertThatThrownBy(() -> menuCreateService.createMenu("test", BigDecimal.valueOf(3_000L), savedGroup.getId(),
            Collections.singletonList(new MenuProductCreateInfo(1L, 2L))))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴에 등록할 메뉴 그룹이 없는 경우 예외 처리한다.")
    @Test
    void createMenuWithNotExistMenuGroup() {
        Product savedProduct1 = productRepository.save(new Product("test1", BigDecimal.valueOf(1_000L)));
        Product savedProduct2 = productRepository.save(new Product("test2", BigDecimal.valueOf(1_000L)));

        assertThatThrownBy(() -> menuCreateService.createMenu("test", BigDecimal.valueOf(3_000L), 1L,
            Arrays.asList(new MenuProductCreateInfo(savedProduct1.getId(), 2L),
                new MenuProductCreateInfo(savedProduct2.getId(), 1L))))
            .isInstanceOf(IllegalArgumentException.class);
    }
}