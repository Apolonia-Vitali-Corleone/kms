package kms.common;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


public abstract class BaseController<S extends IService<T>, T> {

    protected final S service;

    protected BaseController(S service) {
        this.service = service;
    }

    @GetMapping
    public List<T> list() {
        return service.list();
    }

    @GetMapping("/{id}")
    public T getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    public boolean save(@RequestBody T entity) {
        return service.save(entity);
    }

    @PostMapping("/batch")
    public boolean saveBatch(@RequestBody java.util.List<T> list) {
        return service.saveBatch(list);
    }

    @PutMapping
    public boolean update(@RequestBody T entity) {
        return service.updateById(entity);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.removeById(id);
    }

}

