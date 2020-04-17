package com.example.library.util;

import com.example.library.domain.Book;
import com.example.library.domain.Library;
import com.example.library.domain.User;
import org.apache.poi.ss.formula.functions.T;
import org.reflections.ReflectionUtils;

import javax.swing.text.html.parser.Entity;
import java.io.ByteArrayInputStream;

public abstract class ExcelGenerator {
    public abstract<T extends Library> ByteArrayInputStream toExcel(T obj);
    public abstract<T extends Book> ByteArrayInputStream toExcel(T obj);
    public abstract<T extends User> ByteArrayInputStream toExcel(T obj);
}
