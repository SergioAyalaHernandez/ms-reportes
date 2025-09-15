package co.com.pragma.dynamodb.mapper;


import co.com.pragma.dynamodb.ReporteEntity;
import co.com.pragma.model.Reporte;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.LocalDate;

@Mapper(componentModel = "spring")
public interface ReporteMapper {

  @Mapping(source = "fecha", target = "fecha", qualifiedByName = "stringToLocalDate")
  Reporte toDomain(ReporteEntity entity);

  @Mapping(source = "fecha", target = "fecha", qualifiedByName = "localDateToString")
  ReporteEntity toEntity(Reporte domain);

  @Named("stringToLocalDate")
  static LocalDate stringToLocalDate(String fecha) {
    return fecha != null ? LocalDate.parse(fecha) : null;
  }

  @Named("localDateToString")
  static String localDateToString(LocalDate fecha) {
    return fecha != null ? fecha.toString() : null;
  }
}
