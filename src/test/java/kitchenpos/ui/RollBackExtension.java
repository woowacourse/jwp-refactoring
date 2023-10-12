package kitchenpos.ui;

import static java.nio.file.Files.readAllBytes;

import java.util.Arrays;
import java.util.stream.Collectors;
import javax.sql.DataSource;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.ResourceUtils;

public class RollBackExtension implements AfterEachCallback {

    @Override
    public void afterEach(final ExtensionContext context) throws Exception {
        final var file = ResourceUtils.getFile("classpath:truncate.sql");
        final var sqls = new String(readAllBytes(file.toPath()));

        final var parsedSqls = Arrays.stream(sqls.split(";"))
                                     .map(String::trim)
                                     .filter(sql -> !sql.isEmpty())
                                     .collect(Collectors.toList());

        try (final var connection = SpringExtension.getApplicationContext(context)
                                                   .getBean("dataSource", DataSource.class)
                                                   .getConnection();) {

            connection.setAutoCommit(false);
            for (final var sql : parsedSqls) {
                try (final var preparedStatement = connection.prepareStatement(sql + ";")) {
                    preparedStatement.execute();
                }
            }
            connection.commit();
        }
    }
}
