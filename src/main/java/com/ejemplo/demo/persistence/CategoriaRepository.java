package com.ejemplo.demo.persistence;

import com.ejemplo.demo.domain.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository  extends JpaRepository<Categoria, Long>{

}
