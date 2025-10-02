package br.com.fiap.mottomap.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import br.com.fiap.mottomap.filter.PosicaoPatioFilter;
import br.com.fiap.mottomap.model.PosicaoPatio;
import jakarta.persistence.criteria.Predicate;

public class PosicaoPatioSpecification {
    public static Specification<PosicaoPatio> withFilter( PosicaoPatioFilter filter ){
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(filter.identificacao() != null){
                predicates.add(cb.like(cb.lower(root.get("identificacao")), "%" + filter.identificacao().toLowerCase() + "%"));
            }

            if(filter.area() != null){
                predicates.add(cb.equal(root.get("area"), filter.area()));
            }

            if(filter.ocupado() != null){
                predicates.add(cb.equal(root.get("ocupado"), filter.ocupado()));
            }

            if(filter.filialId() != null) {
                predicates.add(cb.equal(root.get("filial").get("id"), filter.filialId()));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
