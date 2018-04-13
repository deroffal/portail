package fr.deroffal.portail.authentification.mapping;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import fr.deroffal.portail.authentification.dto.UserDto;
import fr.deroffal.portail.authentification.entity.UserEntity;

@Mapper(componentModel = "spring")
public abstract class UserMapper {

	@Autowired
	private PasswordEncoder passwordEncoder;

	public abstract UserDto toDto(final UserEntity user);

	@Mapping(target = "password", ignore = true)
	@Mapping(target = "roles", ignore = true)
	public abstract UserEntity toEntityAndEncorePassword(final UserDto user);

	@AfterMapping
	void afterDtoToEntity(final UserDto userIn, @MappingTarget final UserEntity userOut) {
		userOut.setPassword(passwordEncoder.encode(userIn.getPassword()));
	}
}
