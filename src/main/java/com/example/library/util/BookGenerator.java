package com.example.library.util;
import com.example.library.domain.Book;
import com.example.library.domain.Report;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;

public class BookGenerator extends ExcelGenerator<Book> {
    @Override
    public ByteArrayInputStream toExcel(Book book)  {
        try(Workbook workbook = new XSSFWorkbook()){
            Sheet sheet = workbook.createSheet("book");
            int rowIdx = 0;
            rowIdx=headerToExcel(workbook,bookHeader,sheet,rowIdx);
            Row row = sheet.createRow(rowIdx++);
            fillRow(book,row);
            sheet.createRow(rowIdx++);
            librariesToExcel(workbook,sheet,book.getLibraries(),rowIdx);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            outputStream.close();
            return new ByteArrayInputStream(outputStream.toByteArray());
        }
        catch(IOException ex){
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public Report report(Book book) {
        Report report = new Report();
        try {
            ByteArrayInputStream in = toExcel(book);
            String date = dateFormat(new Date());
            StringBuilder name = new StringBuilder();
            name.append("Book_");
            name.append(book.getName());
            name.append("_Author_");
            name.append(book.getAuthor());
            name.append("_report_");
            name.append(date);
            report = new Report(name.toString(),
                    byteArrayInputStreamToFile(in, name.toString()), date);
        }
        catch (Exception e){
            report.setStatus(e.getMessage());
        }
        finally {
            return report;
        }
    }
}
