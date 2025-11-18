package pe.ask.library.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import pe.ask.library.api.dto.response.LoginResponse;
import pe.ask.library.model.token.Token;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TokenMapper {
    LoginResponse toLoginResponse(Token token);
}
