package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.dto.MenuGroupRequest;
import kitchenpos.dto.MenuGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

@SpringBootTest
@Sql(value = "/deleteAll.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹 추가")
    @Test
    void create() {
        MenuGroupRequest request = MenuGroupRequest.builder()
            .name("반반메뉴")
            .build();

        MenuGroupResponse savedMenuGroup = menuGroupService.create(request);

        assertThat(savedMenuGroup.getId()).isNotNull();
    }

    @DisplayName("메뉴 그룹 전체 조회")
    @Test
    void list() {
        MenuGroupRequest request1 = MenuGroupRequest.builder()
            .name("반반메뉴")
            .build();
        MenuGroupRequest request2 = MenuGroupRequest.builder()
            .name("강정메뉴")
            .build();
        menuGroupService.create(request1);
        menuGroupService.create(request2);

        List<MenuGroupResponse> list = menuGroupService.list();

        assertThat(list).hasSize(2);
    }
}