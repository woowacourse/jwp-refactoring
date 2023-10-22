package kitchenpos.integration;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import kitchenpos.ui.dto.MenuGroupCreateRequest;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
public class MenuGroupControllerV1IntegrationTest extends V1IntegrationTest {

    @Nested
    class create {

        @Test
        void 성공하면_201() throws Exception {
            // given
            var request = new MenuGroupCreateRequest("주류");

            // when & then
            메뉴_그룹_생성(request)
                .andExpect(status().isCreated());
        }
    }
}
