package br.com.fiap.mottomap.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import br.com.fiap.mottomap.model.Filial;

public interface FilialRepository extends JpaRepository<Filial, Long>, JpaSpecificationExecutor<Filial> {

    
} 
