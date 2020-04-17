package com.example.library.util;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

import com.example.library.domain.Book;
import com.example.library.domain.BookRent;
import com.example.library.domain.Library;
import com.example.library.domain.User;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.servlet.view.document.AbstractXlsView;


public class LibraryGenerator extends ExcelGenerator  {
    public LibraryGenerator(){}

    @Override
    public  ByteArrayInputStream  toExcel(Library library)  {
        try(Workbook workbook = new XSSFWorkbook()){
            Sheet sheet = workbook.createSheet("library");
            String[] columns = {"Id", "Name", "Address"};

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
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return new ByteArrayInputStream(outputStream.toByteArray());
        }
        catch(IOException ex){
            ex.printStackTrace();
            return null;
        }

    }

    @Override
    public ByteArrayInputStream toExcel(Book book)  {
        try(Workbook workbook = new XSSFWorkbook()){
            Sheet sheet = workbook.createSheet("book");
            String[] columns= new String[]{"BookID", "Name", "Author", "Year", "Description"};
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.BLUE.getIndex());

            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFont(headerFont);

            // Row for Header
            Row headerRow = sheet.createRow(0);

            // Header Book
            for (int col = 0; col < columns.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(columns[col]);
                cell.setCellStyle(headerCellStyle);
            }
            int rowIdx = 1;
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(book.getId());
            row.createCell(1).setCellValue(book.getName());
            row.createCell(2).setCellValue(book.getAuthor());
            row.createCell(3).setCellValue(book.getYear());
            row.createCell(4).setCellValue(book.getDescription());
            sheet.createRow(rowIdx++);
            columns= new String[]{"LibraryID", "Name", "Address"};
            headerRow = sheet.createRow(rowIdx++);
            // Header of books
            for (int col = 0; col < columns.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(columns[col]);
                cell.setCellStyle(headerCellStyle);
            }
            for(Library library : book.getLibraries() ){
                row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(library.getId());
                row.createCell(1).setCellValue(library.getName());
                row.createCell(2).setCellValue(library.getAddress());
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return new ByteArrayInputStream(outputStream.toByteArray());
        }
        catch(IOException ex){
            ex.printStackTrace();
            return null;
        }
    }


    @Override
    public ByteArrayInputStream toExcel(User user)  {
        try(Workbook workbook = new XSSFWorkbook()){
            Sheet sheet = workbook.createSheet("user");
            String[] columns= new String[]{"UserID", "Name", "Login", "Email"};
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.BLUE.getIndex());

            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFont(headerFont);

            // Row for Header
            Row headerRow = sheet.createRow(0);

            // Header Book
            for (int col = 0; col < columns.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(columns[col]);
                cell.setCellStyle(headerCellStyle);
            }
            int rowIdx = 1;
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(user.getId());
            row.createCell(1).setCellValue(user.getName());
            row.createCell(2).setCellValue(user.getLogin());
            row.createCell(3).setCellValue(user.getEmail());
            sheet.createRow(rowIdx++);
            columns= new String[]{"LibraryID", "Name", "Address","BookIds"};
            headerRow = sheet.createRow(rowIdx++);
            // Header of books
            for (int col = 0; col < columns.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(columns[col]);
                cell.setCellStyle(headerCellStyle);
            }
            for(BookRent bookRent : user.getBookRentSet() ){
                row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(bookRent.getLibrary().getId());
                row.createCell(1).setCellValue(bookRent.getLibrary().getName());
                row.createCell(2).setCellValue(bookRent.getLibrary().getAddress());
                row.createCell(3).setCellValue(bookRent.getBooks().stream().map(x->x.getId()).collect(Collectors.toSet()).toString());
            }
            sheet.createRow(rowIdx++);
            columns= new String[]{"BookID", "Name", "Author", "Year", "Description"};
            headerRow = sheet.createRow(rowIdx++);
            // Header of books
            for (int col = 0; col < columns.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(columns[col]);
                cell.setCellStyle(headerCellStyle);
            }
            for(BookRent bookRent : user.getBookRentSet()){
                for(Book book : bookRent.getBooks()) {
                    row = sheet.createRow(rowIdx++);
                    row.createCell(0).setCellValue(book.getId());
                    row.createCell(1).setCellValue(book.getName());
                    row.createCell(2).setCellValue(book.getAuthor());
                    row.createCell(3).setCellValue(book.getYear());
                    row.createCell(4).setCellValue(book.getDescription());
                }
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return new ByteArrayInputStream(outputStream.toByteArray());
        }
        catch(IOException ex){
            ex.printStackTrace();
            return null;
        }
    }

}
