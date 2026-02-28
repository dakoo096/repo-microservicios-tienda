package com.tpFinalTodoCode.ms_productos.repository;

import com.tpFinalTodoCode.ms_productos.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IProductoRepository extends JpaRepository<Producto,Long>{
}
