package com.marotech.skillhub.components.processor;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class JsonPub {
    private String title;
    private String source;
    private String category;
    private LocalDate publication_date;
    private List<Worker> workers = new ArrayList<>();
    private List<Citation> citations = new ArrayList<>();
    @Getter
    @Setter
    public class Worker{
        private String worker;
    }
    @Getter
    @Setter
    public class Citation{
        private String citation;
    }
}
