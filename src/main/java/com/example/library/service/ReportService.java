package com.example.library.service;
import com.example.library.domain.Book;
import com.example.library.domain.Report;
import com.example.library.repos.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ReportService {
    @Autowired
    ReportRepository reportRepository;

    public void save(Report report){
        reportRepository.save(report);
    }

    public List<Report> listAll() {
        return (List<Report>) reportRepository.findAll();
    }

    public Report get(Long id) {
        return reportRepository.findById(id).get();
    }

}
