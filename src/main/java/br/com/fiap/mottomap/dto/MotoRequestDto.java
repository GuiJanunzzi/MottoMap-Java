package br.com.fiap.mottomap.dto;

import br.com.fiap.mottomap.model.ModeloMoto;
import br.com.fiap.mottomap.model.StatusMoto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "MotoRequest", description = "DTO para cadastro e atualização de motos")
public class MotoRequestDto {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único da moto (gerado automaticamente)", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotBlank(message = "A placa é obrigatória")
    @Size(min = 7, max = 7, message = "A placa deve conter exatamente 7 caracteres.")
    @Pattern(regexp = "^[A-Z]{3}[0-9]{1}[A-Z0-9]{1}[0-9]{2}$|^[A-Z]{3}[0-9]{4}$", message = "Formato da placa inválido. Exemplo: ABC1D23")
    @Schema(description = "Placa da moto (padrão Mercosul ou antigo)", example = "ABC1D23", pattern = "^[A-Z]{3}[0-9]{1}[A-Z0-9]{1}[0-9]{2}$|^[A-Z]{3}[0-9]{4}$")
    private String placa;

    @NotBlank(message = "O chassi é obrigatório")
    @Size(min = 17, max = 17, message = "O chassi deve conter exatamente 17 caracteres.")
    @Pattern(regexp = "^[A-HJ-NPR-Z0-9]{17}$", message = "O chassi possui caracteres inválidos.")
    @Schema(description = "Número do chassi (17 caracteres alfanuméricos)", example = "9BWZZZ377VT004251", pattern = "^[A-HJ-NPR-Z0-9]{17}$")
    private String chassi;

    @NotNull(message = "O modelo é obrigatório.")
    @Enumerated(EnumType.STRING)
    @Schema(description = "Modelo da moto", example = "POP_110I", implementation = ModeloMoto.class)
    private ModeloMoto modeloMoto;

    @NotNull(message = "O ano é obrigatório")
    @Min(value = 1990, message = "O ano deve ser apartir de 1990")
    @Max(value = 2100, message = "O ano deve ser até 2100")
    @Schema(description = "Ano de fabricação (1990-2100)", example = "2023", minimum = "1990", maximum = "2100")
    private Integer ano;

    @NotNull(message = "O status é obrigatório.")
    @Enumerated(EnumType.STRING)
    @Schema(description = "Status atual da moto", example = "ATIVA", implementation = StatusMoto.class)
    private StatusMoto statusMoto;
    
    @NotNull(message = "O ID da filial é obrigatório")
    @Positive(message = "O ID não pode ser zero ou negativo")
    @Schema(description = "ID da filial onde a moto está alocada", example = "1", minimum = "1")
    private Long filial;
}
