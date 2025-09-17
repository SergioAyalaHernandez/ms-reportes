package co.com.pragma.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ReporteResumenScheduler {
  private Integer montoTotal;
  private Integer numeroDeCreditos;
  private String email;
}
