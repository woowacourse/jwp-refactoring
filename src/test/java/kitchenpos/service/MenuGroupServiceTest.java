package kitchenpos.service;

import kitchenpos.application.MenuGroupService;
import kitchenpos.domain.MenuGroup;
import kitchenpos.dto.MenuGroupCreateRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Sql({"classpath:/truncate.sql", "classpath:/set_up.sql"})
public class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @Test
    void create() {
        MenuGroupCreateRequest menuGroupCreateRequest = new MenuGroupCreateRequest("우테코 단체");

        MenuGroup menuGroup = menuGroupService.create(menuGroupCreateRequest);

        assertThat(menuGroup.getName()).isEqualTo("우테코 단체");
    }

    @Test
    void create_nameNull() {
        MenuGroupCreateRequest menuGroupCreateRequest = new MenuGroupCreateRequest(null);

        assertThatThrownBy(() -> menuGroupService.create(menuGroupCreateRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void list() {
        List<MenuGroup> menuGroups = menuGroupService.list();

        assertThat(menuGroups.size()).isEqualTo(3);
    }
}
