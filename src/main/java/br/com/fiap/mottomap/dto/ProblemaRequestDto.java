package br.com.fiap.mottomap.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.fiap.mottomap.model.TipoProblema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Entidade que representa os problemas das motos no sistema da Mottu")
public class ProblemaRequestDto {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único do usuário", example = "1", readOnly = true)
    private Long id;

    @NotNull(message = "O tipo do problema é obrigatório")
    @Enumerated(EnumType.STRING)
    @Schema(description = "Tipo do problema da moto", example = "MECANICO", required = true)
    private TipoProblema tipoProblema;

    @NotBlank(message = "A descrição é obrigatória")
    @Size(min = 10, max = 255, message = "A descrição deve ter entre 10 e 255 caracteres")
    @Schema(description = "Descrição do problema", example = "MECANICO", required = true)
    private String descricao;

    @NotNull(message = "A data é obrigatória")
    @Schema(description = "Data do registro do problema", example = "17/05/2025 10:45:33", required = true)
    private LocalDate dataRegistro;

    @NotNull(message = "O status de resolução é obrigatório")
    @Schema(description = "Indica se o probelma foi resolvido (true) ou não (false)", example = "false", required = true)
    private Boolean resolvido;

    @NotNull(message = "O ID da moto é obrigatório")
    @Positive(message = "O ID não pode ser Zero ou Negativo")
    @Schema(description = "ID da moto que o problema foi relatado", example = "1", required = true)
    private Long idMoto;

    @NotNull(message = "O ID do usuário é obrigatório")
    @Positive(message = "O ID não pode ser Zero ou Negativo")
    @Schema(description = "ID do usuário que cadastrou o problema", example = "1", required = true)
    private Long idUsuario;
}
