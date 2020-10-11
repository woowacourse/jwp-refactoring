package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.domain.MenuGroup;
import kitchenpos.dto.MenuGroupCreateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql("/deleteAll.sql")
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @BeforeEach
    void setUp() {
    }

    @DisplayName("메뉴 그룹 추가")
    @Test
    void create() {
        MenuGroupCreateRequest request = MenuGroupCreateRequest.builder()
            .name("반반메뉴")
            .build();

        MenuGroup savedMenuGroup = menuGroupService.create(request);

        assertThat(savedMenuGroup.getId()).isNotNull();
    }

    @DisplayName("메뉴 그룹 전체 조회")
    @Test
    void list() {
        MenuGroupCreateRequest request1 = MenuGroupCreateRequest.builder()
            .name("반반메뉴")
            .build();
        MenuGroupCreateRequest request2 = MenuGroupCreateRequest.builder()
            .name("강정메뉴")
            .build();
        menuGroupService.create(request1);
        menuGroupService.create(request2);

        List<MenuGroup> list = menuGroupService.list();

        assertThat(list).hasSize(2);
    }
}