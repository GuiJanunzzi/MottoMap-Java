package br.com.fiap.mottomap.dto;

import br.com.fiap.mottomap.model.Area;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
@Schema(description = "Entidade que representa uma posição do patio no sistema da Mottu")
public class PosicaoPatioRequestDto {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único da posição", example = "1", readOnly = true)
    private Long id;
    
    @NotBlank(message = "A identificação é obrigátoria")
    @Size(min = 2, message = "A identificação deve ter no minimo 2 caracteres")
    @Schema(description = "Identificação da posição", example = "A1", required = true)
    private String identificacao;

    @NotNull(message = "O numero da fila é obrigátorio")
    @Positive(message = "O numero não pode ser zero ou negativo")
    @Schema(description = "Numero da coluna da posição", example = "1", required = true)
    private int numeroLinha;

    @NotNull(message = "O numero da coluna é obrigátorio")
    @Positive(message = "O numero não pode ser zero ou negativo")
    @Schema(description = "Numero da linha da posição", example = "1", required = true)
    private int numeroColuna;

    @NotNull(message = "A area é obrigatória")
    @Enumerated(EnumType.STRING)
    @Schema(description = "Área da posição", example = "PRONTAS", required = true)
    private Area area;

    @NotNull(message = "O campo 'ocupado' deve ser informado")
    @Schema(description = "Indica se a posição está ocupada (true) ou não (false)", example = "false", required = true)
    private Boolean ocupado;

    @NotNull(message = "O ID da filial é obrigatório")
    @Positive
    @Schema(description = "ID da filial a qual a posição pertence", example = "1", required = true)
    private Long filial;
}
