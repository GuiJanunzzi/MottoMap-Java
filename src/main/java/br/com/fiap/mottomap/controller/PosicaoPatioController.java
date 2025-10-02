package br.com.fiap.mottomap.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import br.com.fiap.mottomap.dto.PosicaoPatioRequestDto;
import br.com.fiap.mottomap.filter.PosicaoPatioFilter;
import br.com.fiap.mottomap.model.Filial;
import br.com.fiap.mottomap.model.PosicaoPatio;
import br.com.fiap.mottomap.repository.FilialRepository;
import br.com.fiap.mottomap.repository.PosicaoPatioRepository;
import br.com.fiap.mottomap.specification.PosicaoPatioSpecification;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/posicao-patio")
@CrossOrigin
@Tag(name = "Posição Patio", description = "API para gerenciamento de posições do patio no sistema da Mottu")
public class PosicaoPatioController {
    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private PosicaoPatioRepository repository;

    @Autowired
    private FilialRepository filialRepository;

    //----- Documentação Swagger -----
    @Operation(
        summary = "Listar todas as Posições do patio",
        description = "Retorna uma lista com todas as posições cadastradas no sistema",
        responses = {
                @ApiResponse(responseCode = "200", description = "Lista de posições retornada com sucesso")
        }
    )
    //----- Documentação Swagger -----
    @GetMapping
    @Cacheable("posicao")
    public Page<PosicaoPatio> index(PosicaoPatioFilter filter, @PageableDefault(size = 5, sort = "id", direction = Direction.DESC) Pageable pageable){

        var specification = PosicaoPatioSpecification.withFilter(filter);
        return repository.findAll(specification, pageable);
    }

    //----- Documentação Swagger -----
    @Operation(
        summary = "Cadastrar Posição do Patio",
        description = "Coleta os dados para adicionar uma posição no sistema",
        responses = {
                @ApiResponse(responseCode = "201", description = "Posição cadastrada com sucesso"),
                @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
        }
    )
    //----- Documentação Swagger -----
    @PostMapping
    @CacheEvict(value = "posicao", allEntries = true)
    public ResponseEntity<PosicaoPatio> create(@RequestBody @Valid PosicaoPatioRequestDto dto){
        log.info("Cadastrando Posição");

        Filial filial = filialRepository.findById(dto.getFilial())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Filial não encontrada"));

        PosicaoPatio posicaoPatio = PosicaoPatio.builder()
            .identificacao(dto.getIdentificacao())
            .numeroLinha(dto.getNumeroLinha())
            .numeroColuna(dto.getNumeroColuna())
            .area(dto.getArea())
            .ocupado(dto.getOcupado())
            .filial(filial)
            .build();

        repository.save(posicaoPatio);
        return ResponseEntity.status(201).body(posicaoPatio);
    }

    //----- Documentação Swagger -----
    @Operation(
        summary = "Buscar Posição por ID",
        description = "Retorna os dados de uma posição no pátio com base no ID fornecido",
        responses = {
                @ApiResponse(responseCode = "200", description = "Posição encontrada"),
                @ApiResponse(responseCode = "404", description = "Posição não encontrada")
        }
    )
    //----- Documentação Swagger -----
    @GetMapping({"/{id}"})
    public PosicaoPatio get(@PathVariable Long id){
        log.info("Buscando posição do patio por ID: " + id);
        return getPosicaoPatio(id);
    }

    //----- Documentação Swagger -----
    @Operation(
        summary = "Atualizar Posição",
        description = "Atualiza os dados de uma posição existente com base no ID fornecido",
        responses = {
                @ApiResponse(responseCode = "200", description = "Posição atualizada com sucesso"),
                @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
                @ApiResponse(responseCode = "404", description = "Posição não encontrada")
        }
    )
    //----- Documentação Swagger -----
    @PutMapping({"/{id}"})
    @CacheEvict(value = "posicao", allEntries = true)
    public PosicaoPatio update(@PathVariable Long id, @RequestBody @Valid PosicaoPatioRequestDto dto){
        log.info("Atualizando posicao " + dto.toString());

        Filial filial = filialRepository.findById(dto.getFilial())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Filial não encontrada"));

        PosicaoPatio posicaoPatio = PosicaoPatio.builder()
            .identificacao(dto.getIdentificacao())
            .numeroLinha(dto.getNumeroLinha())
            .numeroColuna(dto.getNumeroColuna())
            .area(dto.getArea())
            .ocupado(dto.getOcupado())
            .filial(filial)
            .build();

        getPosicaoPatio(id);
        posicaoPatio.setId(id);
        repository.save(posicaoPatio);

        return posicaoPatio;
    }

    //----- Documentação Swagger -----
    @Operation(
        summary = "Deletar Posição do Patio",
        description = "Remove uma posição existente com base no ID fornecido",
        responses = {
                @ApiResponse(responseCode = "204", description = "Posição removida com sucesso"),
                @ApiResponse(responseCode = "404", description = "Posição não encontrada")
        }
    )
    //----- Documentação Swagger -----
    @DeleteMapping({"/{id}"})
    @CacheEvict(value = "posicao", allEntries = true)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id){
        log.info("Apagando Posição ID: " + id);
        repository.delete(getPosicaoPatio(id));
    }
    
    private PosicaoPatio getPosicaoPatio(Long id) {
        return repository
        .findById(id)
        .orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND)
        );
    }    
}
