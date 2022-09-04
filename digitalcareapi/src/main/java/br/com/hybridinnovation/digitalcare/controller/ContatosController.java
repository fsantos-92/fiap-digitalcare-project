package br.com.hybridinnovation.digitalcare.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.SpringVersion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.hybridinnovation.digitalcare.model.Contatos;
import br.com.hybridinnovation.digitalcare.service.ContatosService;


@RestController
@RequestMapping("/api/contatos")
public class ContatosController {
    @Autowired
    private ContatosService service;

    @GetMapping
    public Page<Contatos> index( @PageableDefault(size = 10, sort = "paciente") Pageable pageable){
        return service.listAll(pageable);
    }

    @PostMapping
    public ResponseEntity<Contatos> create(@RequestBody @Valid Contatos task){
        service.save(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(task);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> destroy(@PathVariable Long id){
        Optional<Contatos> optional = service.getById(id);

        if (optional.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        service.deleteById(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

    @GetMapping("{id}")
    public ResponseEntity<Contatos> show(@PathVariable Long id){
        return ResponseEntity.of( service.getById(id) );   
    }

    @PutMapping("{id}")
    public ResponseEntity<Contatos> update(@PathVariable Long id, @RequestBody @Valid Contatos newContatos){
        Optional<Contatos> optional = service.getById(id);

        if (optional.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        var task = optional.get();
        BeanUtils.copyProperties(newContatos, task);
        task.setId(id);

        service.save(task);

        return ResponseEntity.ok(task);
    }
}
