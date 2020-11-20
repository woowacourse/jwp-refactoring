package kitchenpos.ui;

import static kitchenpos.utils.TestObjects.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

import kitchenpos.domain.MenuGroup;

@SuppressWarnings("NonAsciiCharacters")
class MenuGroupRestControllerTest extends ControllerTest {

    @DisplayName("create: 이름을 body message에 포함해 제품 등록을 요청시 , 메뉴 그룹을 생성 후 생성 성공 시 201 응답을 반환한다.")
    @Test
    void createNewMenuGroup() throws Exception {
        MenuGroup 단품메뉴 = createMenuGroup("단품메뉴");

        String 메뉴그룹추가_API_URL = "/api/menu-groups/";
        ResultActions resultActions = create(메뉴그룹추가_API_URL, 단품메뉴);

        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is("단품메뉴")));
    }

    @DisplayName("list: 메뉴 그룹의 목록 요청시, 전체 목록을 body message로 가지고 있는 status code 200 응답을 반환한다.")
    @Test
    void list() throws Exception {
        final String 메뉴그룹전체조회_API_URL = "/api/menu-groups/";
        final ResultActions resultActions = findList(메뉴그룹전체조회_API_URL);

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }
}