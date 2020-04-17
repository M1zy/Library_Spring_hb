package com.example.library.util;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

import com.example.library.domain.Book;
import com.example.library.domain.BookRent;
import com.example.library.domain.Library;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class ExcelGenerator {

    public static void libraryToExcel(Library library) throws IOException {
        String[] columns = {"Id", "Name", "Address"};
        try(
                Workbook workbook = new XSSFWorkbook();
                ByteArrayOutputStream out = new ByteArrayOutputStream();
        ){
            CreationHelper createHelper = workbook.getCreationHelper();

            Sheet sheet = workbook.createSheet("library");

            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.BLUE.getIndex());

            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFont(headerFont);

            // Row for Header
            Row headerRow = sheet.createRow(0);

            // Header
            for (int col = 0; col < columns.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(columns[col]);
                cell.setCellStyle(headerCellStyle);
            }
            int rowIdx = 1;
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(library.getId());
            row.createCell(1).setCellValue(library.getName());
            row.createCell(2).setCellValue(library.getAddress());
            sheet.createRow(rowIdx++);
             columns= new String[]{"BookID", "Name", "Author", "Year", "Description"};
            headerRow = sheet.createRow(rowIdx++);
            // Header of books
            for (int col = 0; col < columns.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(columns[col]);
                cell.setCellStyle(headerCellStyle);
            }
            for(Book book : library.getBooks() ){
                 row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(book.getId());
                row.createCell(1).setCellValue(book.getName());
                row.createCell(2).setCellValue(book.getAuthor());
                row.createCell(3).setCellValue(book.getYear());
                row.createCell(4).setCellValue(book.getDescription());
            }
            sheet.createRow(rowIdx++);
            columns= new String[]{"UserID", "Name", "Login", "Email", "BookIDS"};
            headerRow = sheet.createRow(rowIdx++);
            // Header of book rents
            for (int col = 0; col < columns.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(columns[col]);
                cell.setCellStyle(headerCellStyle);
            }
            for(BookRent bookRent : library.getBookRentSet() ){
                row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(bookRent.getUser().getId());
                row.createCell(1).setCellValue(bookRent.getUser().getName());
                row.createCell(2).setCellValue(bookRent.getUser().getLogin());
                row.createCell(3).setCellValue(bookRent.getUser().getEmail());
                row.createCell(4).setCellValue(bookRent.getBooks().stream().map(x->x.getId()).collect(Collectors.toSet()).toString());
            }

            File currDir = new File(".");
            String path = currDir.getAbsolutePath();
            String fileLocation = path.substring(0, path.length() - 1) + "library.xlsx";
            FileOutputStream outputStream = new FileOutputStream(fileLocation);
            workbook.write(outputStream);
        }
    }
}
