package kitchenpos.support.extension;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

public class DataDefaultAddExtension implements BeforeEachCallback {

    @Override
    @Transactional
    public void beforeEach(ExtensionContext context) {
        ApplicationContext applicationContext = SpringExtension.getApplicationContext(context);
        JdbcTemplate jdbcTemplate = applicationContext.getBean(JdbcTemplate.class);
        try {
            URL resource = DataDefaultAddExtension.class.getClassLoader().getResource("db/migration/V2__Insert_default_data.sql");
            validateUrl(resource);
            File file = new File(resource.getFile());
            String sql = Files.readString(file.toPath());

            jdbcTemplate.execute(sql);
        } catch (Exception exception) {
            throw new RuntimeException("Error with Database Default Add");
        }
    }

    private void validateUrl(final URL resource) {
        if (resource == null) {
            throw new RuntimeException("Error with Database Default Add Script Not Found");
        }
    }
}
