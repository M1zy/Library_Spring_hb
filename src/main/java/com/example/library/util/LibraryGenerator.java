package com.example.library.util;
import java.io.*;
import java.util.Date;
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
            fillRow(library,row);
            rowIdx = booksToExcel(workbook, sheet, library.getBooks(), rowIdx);
            rentsToExcel(workbook, sheet, library.getCartSet(), rowIdx);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            outputStream.close();
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
                    fillRow(library,row);
                    rowIdx = booksToExcel(workbook, sheet, library.getBooks(), rowIdx);
                    rowIdx = rentsToExcel(workbook, sheet, library.getCartSet(), rowIdx);
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

    @Override
    public Report report(Library library) {
        Report report = new Report();
        try {
        ByteArrayInputStream in = toExcel(library);
        String date = dateFormat(new Date());
        StringBuilder name = new StringBuilder();
        name.append("Library_");
        name.append(library.getName());
        name.append("_address_");
        name.append(library.getAddress());
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

    public Report report(List<Library> libraries) {
        Report report = new Report();
        try {
        String date = dateFormat(new Date());
        StringBuilder name = new StringBuilder();
        name.append("Libraries_report_");
        name.append(date);
        report = new Report(name.toString(),
                byteArrayInputStreamToFile(toExcel(libraries), name.toString()), date);
        }
        catch (Exception e){
            report.setStatus(e.getMessage());
        }
        finally {
            return report;
        }
    }
}
