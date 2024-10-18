package ies.jandula.incidencia.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FiltroBusqueda {

    private String numeroAula;
    private String correoDocente;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private String descripcionIncidencia;
    private String estadoIncidencia;
    private String comentario;

}



