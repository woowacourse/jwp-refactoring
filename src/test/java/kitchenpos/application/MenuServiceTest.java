package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import kitchenpos.domain.Menu;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class MenuServiceTest {

    @Autowired
    private MenuService menuService;

    @Nested
    class create_메소드는 {

        @Nested
        class 가격이_null이_입력될_경우 {

            private final Menu menu = new Menu("파닭", null, 1L, new ArrayList<>());

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> menuService.create(menu))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("가격은 양의 정수만 들어올 수 있습니다.");
            }
        }
    }
}
