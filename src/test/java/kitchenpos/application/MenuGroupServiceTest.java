package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.dao.MenuGroupRepository;
import kitchenpos.domain.MenuGroup;
import kitchenpos.dto.MenuGroupCreationRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
@Sql(value = "/initialization.sql")
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @DisplayName("메뉴 그룹을 저장할 수 있다.")
    @Test
    void createSuccessTest() {
        //given
        MenuGroupCreationRequest request = new MenuGroupCreationRequest("TestMenuGroup");

        //when
        MenuGroup savedMenuGroup = menuGroupService.create(request);

        //then
        MenuGroup findMenuGroup = menuGroupRepository.findById(savedMenuGroup.getId()).get();

        assertThat(savedMenuGroup)
                .usingRecursiveComparison()
                .isEqualTo(findMenuGroup);
    }

    @DisplayName("메뉴 그릅을 조회할 수 있다.")
    @Test
    void listSuccessTest() {
        //given
        MenuGroup menuGroup1 = MenuGroup.from("TestMenuGroup1");
        MenuGroup menuGroup2 = MenuGroup.from("TestMenuGroup2");

        menuGroupRepository.save(menuGroup1);
        menuGroupRepository.save(menuGroup2);

        //when
        List<MenuGroup> findMenuGroups = menuGroupService.list();

        //then
        assertThat(findMenuGroups).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(List.of(menuGroup1, menuGroup2));
    }

}
