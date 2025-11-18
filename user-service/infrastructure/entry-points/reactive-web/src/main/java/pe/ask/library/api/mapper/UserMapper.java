package pe.ask.library.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import pe.ask.library.api.dto.request.RegisterRequest;
import pe.ask.library.api.dto.response.RegisterResponse;
import pe.ask.library.model.user.User;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    User registerRequestToEntity(RegisterRequest registerRequest);
    RegisterResponse toRegisterResponse(User user);
}
