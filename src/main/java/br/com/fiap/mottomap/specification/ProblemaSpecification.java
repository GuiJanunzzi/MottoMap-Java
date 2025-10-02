package br.com.fiap.mottomap.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import br.com.fiap.mottomap.filter.ProblemaFilter;
import br.com.fiap.mottomap.model.Problema;
import jakarta.persistence.criteria.Predicate;

public class ProblemaSpecification {
    public static Specification<Problema> withFilter( ProblemaFilter filter ){
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(filter.tipoProblema() != null){
                predicates.add(cb.equal(root.get("tipoProblema"), filter.tipoProblema()));
            }

            if (filter.dataInicio() != null && filter.dataFim() != null) {
                predicates.add(cb.between(root.get("dataRegistro"), filter.dataInicio(), filter.dataFim()));
            }

            if (filter.dataInicio() != null && filter.dataFim() == null) {
                predicates.add(cb.equal(root.get("dataRegistro"), filter.dataInicio()));
            }
            
            if (filter.dataFim() != null && filter.dataInicio() == null) {
                predicates.add(cb.equal(root.get("dataRegistro"), filter.dataFim()));
            }

            if(filter.resolvido() != null){
                predicates.add(cb.equal(root.get("resolvido"), filter.resolvido()));
            }

            if(filter.motoId() != null) {
                predicates.add(cb.equal(root.get("moto").get("id"), filter.motoId()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
