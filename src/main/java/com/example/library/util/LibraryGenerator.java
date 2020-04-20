package com.example.library.util;

import java.io.*;
import java.util.Set;
import java.util.stream.Collectors;

import com.example.library.domain.*;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class LibraryGenerator extends ExcelGenerator<Library> {
    public LibraryGenerator(){}





    @Override
    public ByteArrayInputStream toExcel(Library library){
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("library");
            String[] columns = {"Id", "Name", "Address"};
            int rowIdx = 1;
            rowIdx=headerToExcel(workbook,columns,sheet,rowIdx);

            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(library.getId());
            row.createCell(1).setCellValue(library.getName());
            row.createCell(2).setCellValue(library.getAddress());

            rowIdx=booksToExcel(workbook,sheet,library.getBooks(),rowIdx);

            rentsToExcel(workbook,sheet,library.getBookRentSet(),rowIdx);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return new ByteArrayInputStream(outputStream.toByteArray());
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }







}
