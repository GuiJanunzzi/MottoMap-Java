package br.com.fiap.mottomap.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*; // Mude os imports para jakarta.persistence.*
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List; // Importe a List

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Entidade que representa uma filial da Mottu")
public class Filial {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único da filial", example = "1", readOnly = true)
    private Long id;

    @NotBlank(message = "O nome da filial é obrigatório")
    @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres")
    @Schema(description = "Nome da filial", example = "Mottu São Paulo", required = true)
    private String nome;

    @NotBlank(message = "O endereço é obrigatório")
    @Size(max = 200, message = "O endereço deve ter até 200 caracteres")
    @Schema(description = "Endereço completo da filial", example = "Av. Paulista, 1000", required = true)
    private String endereco;

    @NotBlank(message = "A cidade é obrigatória")
    @Size(max = 50, message = "A cidade deve ter até 50 caracteres")
    @Schema(description = "Cidade onde a filial está localizada", example = "São Paulo", required = true)
    private String cidade;

    @NotBlank(message = "A sigla do estado é obrigatória")
    @Pattern(regexp = "[A-Z]{2}", message = "A sigla do estado deve ter 2 letras maiúsculas (ex: SP, RJ)")
    @Schema(description = "Sigla do estado (UF)", example = "SP", required = true)
    private String siglaEstado;

    @NotNull(message = "O número de colunas é obrigatório")
    @Min(value = 1, message = "O pátio deve ter pelo menos 1 coluna")
    @Max(value = 200, message = "O número máximo de colunas é 200")
    @Schema(description = "Número de colunas do pátio", example = "10", required = true)
    private Integer numeroColuna;

    @NotNull(message = "O número de linhas é obrigatório")
    @Min(value = 1, message = "O pátio deve ter pelo menos 1 linha")
    @Max(value = 200, message = "O número máximo de linhas é 200")
    @Schema(description = "Número de linhas do pátio", example = "5", required = true)
    private Integer numeroLinha;

    @NotNull(message = "A capacidade máxima é obrigatória")
    @Positive(message = "A capacidade deve ser maior que zero")
    @Schema(description = "Capacidade máxima de motos no pátio", example = "150", required = true)
    private Integer capacidadeMaxima;

    @OneToMany(mappedBy = "filial", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore // Evita loops infinitos ao serializar para JSON
    private List<Moto> motos;

    @OneToMany(mappedBy = "filial", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<PosicaoPatio> posicoesPatio;

    @OneToMany(mappedBy = "filial")
    @JsonIgnore
    private List<Usuario> usuarios; // IMPORTANTE: Sem cascade para não apagar usuários!
}