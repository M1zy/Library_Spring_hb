package com.example.library.util;

import com.example.library.domain.Book;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class BookGenerator extends ExcelGenerator<Book> {
    @Override
    public ByteArrayInputStream toExcel(Book book)  {
        try(Workbook workbook = new XSSFWorkbook()){
            Sheet sheet = workbook.createSheet("book");
            String[] columns= new String[]{"BookID", "Name", "Author", "Year", "Description"};
            int rowIdx = 1;
            rowIdx=headerToExcel(workbook,columns,sheet,rowIdx);
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(book.getId());
            row.createCell(1).setCellValue(book.getName());
            row.createCell(2).setCellValue(book.getAuthor());
            row.createCell(3).setCellValue(book.getYear());
            row.createCell(4).setCellValue(book.getDescription());
            sheet.createRow(rowIdx++);
            librariesToExcel(workbook,sheet,book.getLibraries(),rowIdx);
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
