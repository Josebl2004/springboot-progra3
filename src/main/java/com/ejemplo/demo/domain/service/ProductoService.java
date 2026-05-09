package com.ejemplo.demo.domain.service;

import com.ejemplo.demo.api.dto.ProductoRequest;
import com.ejemplo.demo.api.dto.ProductoResponse;
import com.ejemplo.demo.domain.model.Categoria;
import com.ejemplo.demo.domain.model.Producto;
import com.ejemplo.demo.persistence.CategoriaRepository;
import com.ejemplo.demo.persistence.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import com.ejemplo.demo.api.exception.ResourceNotFoundException;

@Service
public class ProductoService {

    private final ProductoRepository productoRepo;
    private final CategoriaRepository categoriaRepo;

    public ProductoService(
            ProductoRepository productoRepo,
            CategoriaRepository categoriaRepo
    ) {
        this.productoRepo = productoRepo;
        this.categoriaRepo = categoriaRepo;
    }

    @Transactional(readOnly = true)
    public List<ProductoResponse> listarTodos() {
        return productoRepo.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProductoResponse buscarPorId(Long id) {
        Producto producto = productoRepo.findById(id)
        		.orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));

        return toResponse(producto);
    }

    @Transactional
    public ProductoResponse crear(ProductoRequest request) {

        Categoria categoria = categoriaRepo.findById(request.categoriaId())
                .orElseThrow(() -> new RuntimeException("Categoria no encontrada"));

        Producto producto = new Producto();

        producto.setNombre(request.nombre());
        producto.setDescripcion(request.descripcion());
        producto.setPrecio(request.precio());
        producto.setStock(request.stock());
        producto.setEstado(request.estado() != null ? request.estado() : true);
        producto.setCategoria(categoria);

        return toResponse(productoRepo.save(producto));
    }

    @Transactional
    public ProductoResponse actualizar(Long id, ProductoRequest request) {

        Producto producto = productoRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        Categoria categoria = categoriaRepo.findById(request.categoriaId())
                .orElseThrow(() -> new RuntimeException("Categoria no encontrada"));

        producto.setNombre(request.nombre());
        producto.setDescripcion(request.descripcion());
        producto.setPrecio(request.precio());
        producto.setStock(request.stock());
        producto.setEstado(request.estado());
        producto.setCategoria(categoria);

        return toResponse(productoRepo.save(producto));
    }

    @Transactional
    public void eliminar(Long id) {

        if (!productoRepo.existsById(id)) {
            throw new RuntimeException("Producto no encontrado");
        }

        productoRepo.deleteById(id);
    }

    private ProductoResponse toResponse(Producto producto) {

        return new ProductoResponse(
                producto.getId(),
                producto.getNombre(),
                producto.getDescripcion(),
                producto.getPrecio(),
                producto.getStock(),
                producto.getEstado(),
                producto.getCategoria().getId(),
                producto.getCategoria().getNombre()
        );
    }
}
