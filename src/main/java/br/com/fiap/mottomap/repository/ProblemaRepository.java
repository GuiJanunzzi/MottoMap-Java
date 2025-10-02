package br.com.fiap.mottomap.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import br.com.fiap.mottomap.model.Problema;

public interface ProblemaRepository extends JpaRepository<Problema, Long>, JpaSpecificationExecutor<Problema>{
    
}
