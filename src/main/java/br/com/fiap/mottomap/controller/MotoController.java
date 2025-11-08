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

import br.com.fiap.mottomap.dto.MotoRequestDto;
import br.com.fiap.mottomap.filter.MotoFilter;
import br.com.fiap.mottomap.model.Filial;
import br.com.fiap.mottomap.model.Moto;
import br.com.fiap.mottomap.repository.FilialRepository;
import br.com.fiap.mottomap.repository.MotoRepository;
import br.com.fiap.mottomap.specification.MotoSpecification;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import br.com.fiap.mottomap.model.Problema; 
import br.com.fiap.mottomap.repository.ProblemaRepository; 
import java.util.List; 

@RestController
@RequestMapping("/moto")
@CrossOrigin
@Tag(name = "Moto", description = "API para gerenciamento de motos da frota Mottu")
public class MotoController {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private MotoRepository repository;

    @Autowired
    private FilialRepository filialRepository;

    @Autowired
    private ProblemaRepository problemaRepository;

    //----- Documentação Swagger -----
    @Operation(summary = "Listar motos",
        description = "Retorna uma lista paginada de motos com filtros opcionais",
        parameters = {
            @Parameter(name = "placa", description = "Filtro por placa (busca parcial)", example = "ABC"),
            @Parameter(name = "chassi", description = "Filtro por chassi (exato)", example = "9BWZZZ377VT004251"),
            @Parameter(name = "modeloMoto", description = "Filtro por modelo", example = "POP_110I"),
            @Parameter(name = "statusMoto", description = "Filtro por status", example = "ATIVA"),
            @Parameter(name = "page", description = "Número da página", example = "0"),
            @Parameter(name = "size", description = "Tamanho da página", example = "5"),
            @Parameter(name = "sort", description = "Ordenação (campo,direção)", example = "placa,asc")
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "Lista de motos retornada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
        })
    //----- Documentação Swagger -----    
    @GetMapping
    @Cacheable("moto")
    public Page<Moto> index(MotoFilter filter, @PageableDefault(size = 5, sort = "id", direction = Direction.DESC) Pageable pageable){

        var specification = MotoSpecification.withFilter(filter);
        return repository.findAll(specification, pageable);
    }

    //----- Documentação Swagger -----
    @Operation(summary = "Cadastrar nova moto",
        description = "Registra uma nova moto na frota da Mottu",
        responses = {
            @ApiResponse(responseCode = "201", description = "Moto cadastrada com sucesso",
                       content = @Content(schema = @Schema(implementation = Moto.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
            @ApiResponse(responseCode = "404", description = "Filial não encontrada")
        })
    //----- Documentação Swagger -----
    @PostMapping
    @CacheEvict(value = "moto", allEntries = true)
    public ResponseEntity<Moto> create(@RequestBody @Valid MotoRequestDto dto){
        log.info("Cadastrando Moto");

        Filial filial = filialRepository.findById(dto.getFilial())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Filial não encontrada"));

        Moto moto = Moto.builder()
            .placa(dto.getPlaca())
            .placa(dto.getPlaca())
            .chassi(dto.getChassi())
            .modeloMoto(dto.getModeloMoto())
            .ano(dto.getAno())
            .statusMoto(dto.getStatusMoto())
            .filial(filial)
            .build();

        repository.save(moto);
        return ResponseEntity.status(201).body(moto);
    }

    //----- Documentação Swagger -----
    @Operation(summary = "Buscar moto por ID",
        description = "Recupera os dados completos de uma moto específica",
        responses = {
            @ApiResponse(responseCode = "200", description = "Dados da moto retornados",
                       content = @Content(schema = @Schema(implementation = Moto.class))),
            @ApiResponse(responseCode = "404", description = "Moto não encontrada")
        })
    //----- Documentação Swagger -----
    @GetMapping({"/{id}"})
    public Moto get(@PathVariable Long id){
        log.info("Buscando moto por ID: " + id);
        return getMoto(id);
    }

    @Operation(summary = "Listar problemas de uma moto específica")
    @GetMapping("/{id}/problemas")
    public List<Problema> getProblemasDaMoto(@PathVariable Long id) {
        log.info("Buscando problemas para a moto ID: " + id);
        getMoto(id); 
        return problemaRepository.findByMotoId(id);
    }

    //----- Documentação Swagger -----
    @Operation(summary = "Atualizar moto",
        description = "Atualiza os dados de uma moto existente",
        responses = {
            @ApiResponse(responseCode = "200", description = "Moto atualizada com sucesso",
                       content = @Content(schema = @Schema(implementation = Moto.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou filial não encontrada"),
            @ApiResponse(responseCode = "404", description = "Moto não encontrada")
        })
    //----- Documentação Swagger -----
    @PutMapping({"/{id}"})
    @CacheEvict(value = "moto", allEntries = true)
    public Moto update(@PathVariable Long id, @RequestBody @Valid MotoRequestDto dto){
        log.info("Atualizando usuário " + dto.toString());

        Filial filial = filialRepository.findById(dto.getFilial())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Filial não encontrada"));

        Moto moto = Moto.builder()
            .placa(dto.getPlaca())
            .placa(dto.getPlaca())
            .chassi(dto.getChassi())
            .modeloMoto(dto.getModeloMoto())
            .ano(dto.getAno())
            .statusMoto(dto.getStatusMoto())
            .filial(filial)
            .build();

        getMoto(id);
        moto.setId(id);
        repository.save(moto);

        return moto;
    }

    //----- Documentação Swagger -----
    @Operation(summary = "Remover moto",
        description = "Exclui uma moto do sistema",
        responses = {
            @ApiResponse(responseCode = "204", description = "Moto removida com sucesso"),
            @ApiResponse(responseCode = "404", description = "Moto não encontrada")
        })
    //----- Documentação Swagger -----
    @DeleteMapping({"/{id}"})
    @CacheEvict(value = "moto", allEntries = true)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id){
        log.info("Apagando moto ID: " + id);
        repository.delete(getMoto(id));
    }
    
    private Moto getMoto(Long id) {
        return repository
        .findById(id)
        .orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND)
        );
    }    
}
