package kitchenpos.configuration;  
  
import org.springframework.stereotype.Component;  
  
import java.lang.annotation.ElementType;  
import java.lang.annotation.Retention;  
import java.lang.annotation.RetentionPolicy;  
import java.lang.annotation.Target;  
  
@Target(value = ElementType.TYPE)  
@Retention(value = RetentionPolicy.RUNTIME)  
@Component  
public @interface Validator {  
}
