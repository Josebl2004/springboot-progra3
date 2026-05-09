package com.ejemplo.demo.domain.service;

import com.ejemplo.demo.api.dto.CategoriaRequest;
import com.ejemplo.demo.api.dto.CategoriaResponse;
import com.ejemplo.demo.domain.model.Categoria;
import com.ejemplo.demo.persistence.CategoriaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import com.ejemplo.demo.api.exception.ResourceNotFoundException;

@Service
public class CategoriaService {

    private final CategoriaRepository repo;

    public CategoriaService(CategoriaRepository repo) {
        this.repo = repo;
    }

    @Transactional(readOnly = true)
    public List<CategoriaResponse> listarTodas() {
        return repo.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public CategoriaResponse buscarPorId(Long id) {
        Categoria categoria = repo.findById(id)
        		.orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada"));
        return toResponse(categoria);
    }

    @Transactional
    public CategoriaResponse crear(CategoriaRequest request) {
        Categoria categoria = new Categoria();
        categoria.setNombre(request.nombre());
        categoria.setDescripcion(request.descripcion());
        categoria.setEstado(request.estado() != null ? request.estado() : true);

        return toResponse(repo.save(categoria));
    }

    @Transactional
    public CategoriaResponse actualizar(Long id, CategoriaRequest request) {
        Categoria categoria = repo.findById(id)
        		.orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada"));

        categoria.setNombre(request.nombre());
        categoria.setDescripcion(request.descripcion());
        categoria.setEstado(request.estado() != null ? request.estado() : categoria.getEstado());

        return toResponse(repo.save(categoria));
    }

    @Transactional
    public void eliminar(Long id) {
        if (!repo.existsById(id)) {
        	throw new ResourceNotFoundException("Categoría no encontrada");
        }
        repo.deleteById(id);
    }

    private CategoriaResponse toResponse(Categoria categoria) {
        return new CategoriaResponse(
                categoria.getId(),
                categoria.getNombre(),
                categoria.getDescripcion(),
                categoria.getEstado()
        );
    }
}
