package kitchenpos.integration.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;

@Component
public class MenuGroupApi {

    @Autowired
    private MockMvc mockMvc;

}
