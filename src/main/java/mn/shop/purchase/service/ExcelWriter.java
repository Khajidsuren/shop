package mn.shop.purchase.service;

import mn.shop.purchase.model.OrderView;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class ExcelWriter {

    private static String getStringValue(Object value) {
        return (value != null) ? value.toString() : "";
    }

    public static byte[] writeToExcel(List<OrderView> objects) throws IOException {
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Захилагын түүх");

            Row headerRow = sheet.createRow(0);
            String[] headers = {"ID", "Утасны дугаар", "Захиалсан бүтээгдэхүүн", "Үнийн дүн", "Хаяг", "Тайлбар", "Имэйл хаяг",
                    "Фэйсбүүк", "Захиалгын төлөв", "Үүсгэсэн огноо", "Гүйлгээний утга"};

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            int rowNum = 1;
            for (OrderView object : objects) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(getStringValue(object.getId()));
                row.createCell(1).setCellValue(getStringValue(object.getPhoneNumber()));
                row.createCell(2).setCellValue(getStringValue(object.getOrderedProducts()));
                row.createCell(3).setCellValue(getStringValue(object.getPrice()));
                row.createCell(4).setCellValue(getStringValue(object.getAddress()));
                row.createCell(5).setCellValue(getStringValue(object.getComment()));
                row.createCell(6).setCellValue(getStringValue(object.getEmail()));
                row.createCell(7).setCellValue(getStringValue(object.getFb()));
                row.createCell(8).setCellValue(getStringValue(object.getOrderState()));
                row.createCell(9).setCellValue(getStringValue(object.getCreatedAt()));
                row.createCell(10).setCellValue(getStringValue(object.getTransactionInfo()));
            }

            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }

}
