package pe.ask.library.port.out.kafka;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface KafkaSender {
    String topic();
}
