package com.example.library.util;
import java.io.*;
import java.util.List;

import com.example.library.domain.*;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class LibraryGenerator extends ExcelGenerator<Library> {
    public LibraryGenerator(){}
    @Override
    public ByteArrayInputStream toExcel(Library library) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("library");
            int rowIdx = 0;
            rowIdx = headerToExcel(workbook, libraryHeader, sheet, rowIdx);
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(library.getId());
            row.createCell(1).setCellValue(library.getName());
            row.createCell(2).setCellValue(library.getAddress());
            rowIdx = booksToExcel(workbook, sheet, library.getBooks(), rowIdx);
            rentsToExcel(workbook, sheet, library.getBookRentSet(), rowIdx);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return new ByteArrayInputStream(outputStream.toByteArray());
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

        public static ByteArrayInputStream toExcel(List<Library> libraries) {
            try (Workbook workbook = new XSSFWorkbook()) {
                Sheet sheet = workbook.createSheet("libraries");
                int rowIdx = 0;
                for (Library library:
                     libraries) {
                    rowIdx = headerToExcel(workbook, libraryHeader, sheet, rowIdx);
                    Row row = sheet.createRow(rowIdx++);
                    row.createCell(0).setCellValue(library.getId());
                    row.createCell(1).setCellValue(library.getName());
                    row.createCell(2).setCellValue(library.getAddress());
                    rowIdx = booksToExcel(workbook, sheet, library.getBooks(), rowIdx);
                    rowIdx=rentsToExcel(workbook, sheet, library.getBookRentSet(), rowIdx);
                    sheet.createRow(rowIdx++);
                }
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    workbook.write(outputStream);
                    return new ByteArrayInputStream(outputStream.toByteArray());
                } catch (IOException ex) {
                    ex.printStackTrace();
                    return null;
                }
        }

}
