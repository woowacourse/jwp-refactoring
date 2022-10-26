package kitchenpos.documentation;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;

import io.restassured.config.EncoderConfig;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.config.RestAssuredMockMvcConfig;
import io.restassured.module.mockmvc.specification.MockMvcRequestSpecification;
import kitchenpos.application.old.JdbcMenuGroupService;
import kitchenpos.application.old.JdbcMenuService;
import kitchenpos.application.old.JdbcOrderService;
import kitchenpos.application.old.JdbcProductService;
import kitchenpos.application.old.JdbcTableGroupService;
import kitchenpos.application.old.JdbcTableService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@WebMvcTest
@ExtendWith(RestDocumentationExtension.class)
public class DocumentationTest {
    protected MockMvcRequestSpecification docsGiven;
    @MockBean
    protected JdbcMenuGroupService menuGroupService;
    @MockBean
    protected JdbcMenuService menuService;
    @MockBean
    protected JdbcOrderService orderService;
    @MockBean
    protected JdbcProductService productService;
    @MockBean
    protected JdbcTableGroupService tableGroupService;
    @MockBean
    protected JdbcTableService tableService;

    @BeforeEach
    void setDocsGiven(final WebApplicationContext webApplicationContext,
                      final RestDocumentationContextProvider restDocumentation) {
        RestAssuredMockMvc.config = new RestAssuredMockMvcConfig()
                .encoderConfig(new EncoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));

        docsGiven = RestAssuredMockMvc
                .given()
                .mockMvc(MockMvcBuilders.webAppContextSetup(webApplicationContext)
                        .apply(documentationConfiguration(restDocumentation)
                                .operationPreprocessors()
                                .withRequestDefaults(prettyPrint())
                                .withResponseDefaults(prettyPrint()))
                        .build()).log().all();
    }
}
