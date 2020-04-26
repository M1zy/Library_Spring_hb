package com.example.library.domain;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.Entity;
import java.io.File;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Report extends Essence {
    private File report;

    private String date;

    private String status="OK";

    public Report(String name,File report,String date) {
        super(name);
        this.report=report;
        this.date=date;
    }

}
