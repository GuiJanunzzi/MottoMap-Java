package br.com.fiap.mottomap.controller;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import br.com.fiap.mottomap.filter.FilialFilter;
import br.com.fiap.mottomap.model.Filial;
import br.com.fiap.mottomap.model.PosicaoPatio;
import br.com.fiap.mottomap.repository.FilialRepository;
import br.com.fiap.mottomap.repository.PosicaoPatioRepository;
import br.com.fiap.mottomap.service.FilialService;
import br.com.fiap.mottomap.specification.FilialSpecification;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/filial")
@CrossOrigin
@Tag(name = "Filial")
public class FilialController {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private FilialRepository repository;

    @Autowired
    private PosicaoPatioRepository posicaoPatioRepository;

    @Autowired
    private FilialService filialService;

    @GetMapping
    public Page<Filial> index(FilialFilter filter, @PageableDefault(size = 5, sort = "id", direction = Direction.DESC) Pageable pageable){
        var specification = FilialSpecification.withFilter(filter);
        return repository.findAll(specification, pageable);
    }

    @PostMapping
    public ResponseEntity<Filial> create(@RequestBody @Valid Filial filial){
        log.info("Cadastrando nova Filial e gerando vagas automaticamente");
        Filial savedFilial = filialService.createFilialAndGenerateSpots(filial);
        return ResponseEntity.status(201).body(savedFilial);
    }

    @GetMapping("/{id}")
    public Filial get(@PathVariable Long id){
        log.info("Buscando filial por ID: " + id);
        return getFilial(id);
    }

    @Operation(summary = "Buscar Posições do Pátio por Filial")
    @GetMapping("/{id}/posicoes")
    public List<PosicaoPatio> getPosicoesDaFilial(@PathVariable Long id) {
        log.info("Buscando posições para a filial com ID: " + id);
        getFilial(id);
        return posicaoPatioRepository.findByFilialId(id);
    }

    @PutMapping("/{id}")
    public Filial update(@PathVariable Long id, @RequestBody @Valid Filial filial){
        log.info("Atualizando filial " + filial.toString());
        getFilial(id);
        filial.setId(id);
        repository.save(filial);
        return filial;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id){
        filialService.delete(id);
    }

    private Filial getFilial(Long id) {
        return repository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Filial não encontrada"));
    }
}