package com.example.library.util;
import com.example.library.domain.User;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class UserGenerator extends ExcelGenerator<User> {
    @Override
    public ByteArrayInputStream toExcel(User user)  {
        try(Workbook workbook = new XSSFWorkbook()){
            Sheet sheet = workbook.createSheet("user");
            int rowIdx = 1;
            rowIdx=headerToExcel(workbook,userHeader,sheet,rowIdx);
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(user.getId());
            row.createCell(1).setCellValue(user.getName());
            row.createCell(2).setCellValue(user.getLogin());
            row.createCell(3).setCellValue(user.getEmail());
            sheet.createRow(rowIdx++);
            rentsToExcel(workbook,sheet,user.getBookRentSet(),rowIdx);
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
