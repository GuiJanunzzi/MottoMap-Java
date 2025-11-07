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

import br.com.fiap.mottomap.dto.ProblemaRequestDto;
import br.com.fiap.mottomap.filter.ProblemaFilter;
import br.com.fiap.mottomap.model.Moto;
import br.com.fiap.mottomap.model.Problema;
import br.com.fiap.mottomap.model.Usuario;
import br.com.fiap.mottomap.repository.MotoRepository;
import br.com.fiap.mottomap.repository.ProblemaRepository;
import br.com.fiap.mottomap.repository.UsuarioRepository;
import br.com.fiap.mottomap.service.NotificationService;
import br.com.fiap.mottomap.specification.ProblemaSpecification;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/problema")
@CrossOrigin
@Tag(name = "Problema", description = "API para gerenciamento de problema das motos no sistema da Mottu")
public class ProblemaController {
    
    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private ProblemaRepository repository;

    @Autowired
    private MotoRepository motoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private NotificationService notificationService;

    //----- Documentação Swagger -----
    @Operation(
        summary = "Listar todos os problemas",
        description = "Retorna uma lista com todos os problemas cadastrados no sistema",
        responses = {
                @ApiResponse(responseCode = "200", description = "Lista de problemas retornada com sucesso")
        }
    )
    //----- Documentação Swagger -----
    @GetMapping
    @Cacheable("problema")
    public Page<Problema> index(ProblemaFilter filter, @PageableDefault(size = 5, sort = "id", direction = Direction.DESC) Pageable pageable){

        var specification = ProblemaSpecification.withFilter(filter);
        return repository.findAll(specification, pageable);
    }

    //----- Documentação Swagger -----
    @Operation(
        summary = "Cadastrar Problema",
        description = "Coleta os dados para adicionar um problema no sistema",
        responses = {
                @ApiResponse(responseCode = "201", description = "Problema cadastrado com sucesso"),
                @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
        }
    )
    //----- Documentação Swagger -----
    @PostMapping
    @CacheEvict(value = "problema", allEntries = true)
    public ResponseEntity<Problema> create(@RequestBody @Valid ProblemaRequestDto dto){
        log.info("Cadastrando Problema");

        Moto moto = motoRepository.findById(dto.getIdMoto())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Moto não encontrada"));
        
        Usuario usuario = usuarioRepository.findById(dto.getIdUsuario())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário não encontrado"));

        Problema problema = Problema.builder()
            .tipoProblema(dto.getTipoProblema())
            .descricao(dto.getDescricao())
            .dataRegistro(dto.getDataRegistro())
            .resolvido(dto.getResolvido())
            .moto(moto)
            .usuario(usuario)
            .build();

        Problema problemaSalvo = repository.save(problema);

        String titulo = "Novo Problema Reportado!";
        String corpo = "A moto " + problemaSalvo.getMoto().getPlaca() + " reportou um problema: " + problemaSalvo.getDescricao();
        notificationService.sendNotificationToAll(titulo, corpo);

        return ResponseEntity.status(201).body(problemaSalvo);
    }

    //----- Documentação Swagger -----
    @Operation(
        summary = "Buscar Problema por ID",
        description = "Retorna os dados de um problema com base no ID fornecido",
        responses = {
                @ApiResponse(responseCode = "200", description = "Problema encontrado"),
                @ApiResponse(responseCode = "404", description = "Problema não encontrado")
        }
    )
    //----- Documentação Swagger -----
    @GetMapping({"/{id}"})
    public Problema get(@PathVariable Long id){
        log.info("Buscando problema por ID: " + id);
        return getProblema(id);
    }

    //----- Documentação Swagger -----
    @Operation(
        summary = "Atualizar Problema",
        description = "Atualiza os dados de um problema existente com base no ID fornecido",
        responses = {
                @ApiResponse(responseCode = "200", description = "Problema atualizado com sucesso"),
                @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
                @ApiResponse(responseCode = "404", description = "Problema não encontrado")
        }
    )
    //----- Documentação Swagger -----
    @PutMapping({"/{id}"})
    @CacheEvict(value = "problema", allEntries = true)
    public Problema update(@PathVariable Long id, @RequestBody @Valid ProblemaRequestDto dto){
        log.info("Atualizando problema " + dto.toString());

        Moto moto = motoRepository.findById(dto.getIdMoto())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Moto não encontrada"));
        
        Usuario usuario = usuarioRepository.findById(dto.getIdUsuario())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário não encontrado"));

        Problema problema = Problema.builder()
            .tipoProblema(dto.getTipoProblema())
            .descricao(dto.getDescricao())
            .dataRegistro(dto.getDataRegistro())
            .resolvido(dto.getResolvido())
            .moto(moto)
            .usuario(usuario)
            .build();

        getProblema(id);
        problema.setId(id);
        repository.save(problema);

        return problema;
    }

    //----- Documentação Swagger -----
    @Operation(
        summary = "Deletar Problema",
        description = "Remove um problema existente com base no ID fornecido",
        responses = {
                @ApiResponse(responseCode = "204", description = "Problema removido com sucesso"),
                @ApiResponse(responseCode = "404", description = "Problema não encontrado")
        }
    )
    //----- Documentação Swagger -----
    @DeleteMapping({"/{id}"})
    @CacheEvict(value = "problema", allEntries = true)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id){
        log.info("Apagando Problema ID: " + id);
        repository.delete(getProblema(id));
    }
    
    private Problema getProblema(Long id) {
        return repository
        .findById(id)
        .orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND)
        );
    }  
}
