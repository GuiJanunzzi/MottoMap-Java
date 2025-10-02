package br.com.fiap.mottomap.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Entidade que representa um moto no sistema da Mottu")
public class Moto {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único do usuário", example = "1", readOnly = true)
    private Long id;

    @NotBlank(message = "A placa é obrigatória")
    @Size(min = 7, max = 7, message = "A placa deve conter exatamente 7 caracteres.")
    @Pattern(regexp = "^[A-Z]{3}[0-9]{1}[A-Z0-9]{1}[0-9]{2}$|^[A-Z]{3}[0-9]{4}$", message = "Formato da placa inválido. Exemplo: ABC1D23")
    @Schema(description = "Placa da moto, com o padrão antigo ou mercosul", example = "DFA9A89", required = true)
    private String placa;

    @NotBlank(message = "O chassi é obrigatório")
    @Size(min = 17, max = 17, message = "O chassi deve conter exatamente 17 caracteres.")
    @Pattern(regexp = "^[A-HJ-NPR-Z0-9]{17}$", message = "O chassi possui caracteres inválidos.")
    @Schema(description = "Chassi da moto", example = "9C2JC4110GR123456", required = true)
    private String chassi;

    @NotNull(message = "O modelo é obrigatório.")
    @Enumerated(EnumType.STRING)
    @Schema(description = "Modelo da moto", example = "POP_110I", required = true)
    private ModeloMoto modeloMoto;

    @NotNull(message = "O ano é obrigatório")
    @Min(value = 1990, message = "O ano deve ser apartir de 1990")
    @Max(value = 2100, message = "O ano deve ser até 2100")
    @Schema(description = "Ano de fabricação/modelo da moto", example = "2025", required = true)
    private Integer ano;

    @NotNull(message = "O status é obrigatório.")
    @Enumerated(EnumType.STRING)
    @Schema(description = "Status da moto(ATIVA/INATIVA)", example = "ATIVA", required = true)
    private StatusMoto statusMoto;

    @ManyToOne
    private Filial filial;

    @OneToMany(mappedBy = "moto", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Problema> problemas;
}
