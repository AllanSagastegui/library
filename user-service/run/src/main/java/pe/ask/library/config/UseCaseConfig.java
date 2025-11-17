package pe.ask.library.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import pe.ask.library.usecase.utils.UseCase;

@Configuration
@ComponentScan(
        basePackages = {
                "pe.ask.library.usecase"
        },
        includeFilters = @ComponentScan.Filter(
                type = FilterType.ANNOTATION,
                value = UseCase.class
        ),
        useDefaultFilters = false
)
public class UseCaseConfig {
}
