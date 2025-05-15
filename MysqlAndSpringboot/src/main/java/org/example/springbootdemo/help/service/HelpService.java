package org.example.springbootdemo.help.service;

import org.example.springbootdemo.help.model.Help;
import org.example.springbootdemo.help.repository.HelpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HelpService {

    @Autowired
    private HelpRepository helpRepository;

    public List<Help> getAllHelp() {
        return helpRepository.findAll();
    }

    public Optional<Help> getHelpById(Long id) {
        return helpRepository.findById(id);
    }

    public Help createHelp(Help Help) {
        return helpRepository.save(Help);
    }

    public void deleteHelp(Long id) {
        helpRepository.deleteById(id);
    }
}