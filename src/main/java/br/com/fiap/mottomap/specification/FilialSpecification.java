package br.com.fiap.mottomap.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import br.com.fiap.mottomap.filter.FilialFilter;
import br.com.fiap.mottomap.model.Filial;
import jakarta.persistence.criteria.Predicate;

public class FilialSpecification {
    public static Specification<Filial> withFilter( FilialFilter filter ){
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(filter.nome() != null){
                predicates.add(cb.like(cb.lower(root.get("nome")), "%" + filter.nome().toLowerCase() + "%"));
            }

            if(filter.cidade() != null){
                predicates.add(cb.equal(root.get("cidade"), filter.cidade()));
            }

            if(filter.siglaEstado() != null){
                predicates.add(cb.equal(root.get("siglaEstado"), filter.siglaEstado()));
            }

            if (filter.capacidadeMin() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("capacidadeMaxima"), filter.capacidadeMin()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
