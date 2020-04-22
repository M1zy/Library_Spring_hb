package com.example.library.util;
import com.example.library.domain.*;
import org.apache.poi.ss.usermodel.*;
import java.io.ByteArrayInputStream;
import java.util.*;
import java.util.stream.Collectors;

public abstract class ExcelGenerator<T extends Essence>{
    public abstract ByteArrayInputStream toExcel(T essence);

    protected static final List<String> bookHeader= Arrays.asList("BookID", "Name", "Author", "Year", "Description");

    protected static final List<String> libraryHeader= Arrays.asList("LibraryID", "Name", "Address");

    protected static final List<String> userHeader= Arrays.asList("UserID", "Name", "Login", "Email","LibraryID","BookIDS");

    static Integer headerToExcel(Workbook workbook, List<String> header, Sheet sheet, Integer step){
        Row headerRow = sheet.createRow(step++);
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.BLUE.getIndex());
        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);
        for (int col = 0; col < header.size(); col++) {
            Cell cell = headerRow.createCell(col);
            cell.setCellValue(header.get(col));
            cell.setCellStyle(headerCellStyle);
        }
        return step;
    }

    static Integer booksToExcel(Workbook workbook, Sheet sheet, Set<Book> bookSet, Integer step){
        step=headerToExcel(workbook,bookHeader,sheet,step);
        Row row = sheet.createRow(step++);
        for (Book book : bookSet) {
            row = sheet.createRow(step++);
            row.createCell(0).setCellValue(book.getId());
            row.createCell(1).setCellValue(book.getName());
            row.createCell(2).setCellValue(book.getAuthor());
            row.createCell(3).setCellValue(book.getYear());
            row.createCell(4).setCellValue(book.getDescription());
        }
        sheet.createRow(step++);
        return step;
    }

    static Integer librariesToExcel(Workbook workbook, Sheet sheet, Set<Library> librarySet, Integer step){
        step=headerToExcel(workbook,libraryHeader,sheet,step);
        Row row = sheet.createRow(step++);
        for(Library library : librarySet ){
            row = sheet.createRow(step++);
            row.createCell(0).setCellValue(library.getId());
            row.createCell(1).setCellValue(library.getName());
            row.createCell(2).setCellValue(library.getAddress());
        }
        sheet.createRow(step++);
        return step;
    }

    static Integer rentsToExcel(Workbook workbook, Sheet sheet, Set<BookRent> bookRentSet, Integer step){
        step=headerToExcel(workbook,userHeader,sheet,step);
        Row row = sheet.createRow(step++);
        for (BookRent bookRent : bookRentSet) {
            row = sheet.createRow(step++);
            row.createCell(0).setCellValue(bookRent.getUser().getId());
            row.createCell(1).setCellValue(bookRent.getUser().getName());
            row.createCell(2).setCellValue(bookRent.getUser().getLogin());
            row.createCell(3).setCellValue(bookRent.getUser().getEmail());
            row.createCell(4).setCellValue(bookRent.getLibrary().getId());
            row.createCell(5).setCellValue(bookRent.getBooks().stream().map(x -> x.getId()).collect(Collectors.toSet()).toString());
        }
        sheet.createRow(step++);
        return step;
    }
}
