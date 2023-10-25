package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import kitchenpos.application.response.MenuGroupResponse;
import kitchenpos.domain.product.Name;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.context.annotation.Import;

@DataJdbcTest
@Import({MenuGroupService.class})
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @Test
    @DisplayName("메뉴 그룹의 이름을 제공하여 메뉴 그룹을 저장할 수 있다.")
    void givenName() {
        final MenuGroupResponse savedMenuGroup = menuGroupService.create(new Name("메뉴메뉴~!"));

        assertThat(savedMenuGroup).isNotNull();
    }
}
