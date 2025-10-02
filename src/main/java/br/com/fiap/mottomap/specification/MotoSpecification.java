package br.com.fiap.mottomap.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import br.com.fiap.mottomap.filter.MotoFilter;
import br.com.fiap.mottomap.model.Moto;
import jakarta.persistence.criteria.Predicate;

public class MotoSpecification {
    public static Specification<Moto> withFilter( MotoFilter filter ){
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(filter.placa() != null){
                predicates.add(cb.like(cb.lower(root.get("placa")), "%" + filter.placa().toLowerCase() + "%"));
            }

            if(filter.chassi() != null){
                predicates.add(cb.equal(root.get("chassi"), filter.chassi()));
            }

            if(filter.modeloMoto() != null){
                predicates.add(cb.equal(root.get("modeloMoto"), filter.modeloMoto()));
            }

            if(filter.statusMoto() != null){
                predicates.add(cb.equal(root.get("statusMoto"), filter.statusMoto()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
