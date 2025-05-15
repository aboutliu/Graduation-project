package org.example.springbootdemo.virtual.service;

import org.example.springbootdemo.virtual.model.Virtual;
import org.example.springbootdemo.virtual.repository.VirtualRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VirtualService {

    @Autowired
    private VirtualRepository virtualRepository;

    public List<Virtual> getAllvirtual() {
        return virtualRepository.findAll();
    }

    public Optional<Virtual> getvirtualById(Long id) {
        return virtualRepository.findById(id);
    }

    public Virtual createvirtual(Virtual virtual) {
        return virtualRepository.save(virtual);
    }

    public void deletevirtual(Long id) {
        virtualRepository.deleteById(id);
    }
}