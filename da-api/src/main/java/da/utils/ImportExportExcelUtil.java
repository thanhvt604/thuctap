package com.globits.da.utils;

import com.globits.core.dto.DepartmentDto;
import com.globits.da.domain.Employee;
import com.globits.da.dto.EmployeeDto;
import com.globits.da.repository.EmployeeRepository;
import com.globits.da.support.Response;
import com.globits.da.support.StatusMessage;
import com.globits.da.support.handle.EmployeeHandle;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;


@Component
public class ImportExportExcelUtil {
    @Autowired
    private EmployeeHandle employeeHandle;
    @Autowired
    private EmployeeRepository employeeRepository;

    private static Hashtable<String, Integer> hashStaffColumnConfig = new Hashtable<String, Integer>();
    private static Hashtable<String, Integer> hashDepartmentColumnConfig = new Hashtable<String, Integer>();
    private static DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private static DecimalFormat numberFormatter = new DecimalFormat("######################");
    private static Hashtable<String, String> hashColumnPropertyConfig = new Hashtable<String, String>();


    private static final String[] HEADERS = {"Code", "Name", "Email", "Phone", "Age", "Province Id", "District Id", "Commune Id"};
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private List<Employee> employees;
    static String SHEET = "Employee";

    public ImportExportExcelUtil() {
    }

    public ImportExportExcelUtil(List<Employee> employees) {
        this.employees = employees;
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet(SHEET);
    }

    private void writeHeaderRow() {
        Row row = sheet.createRow(0);
        Cell cell;
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setColor(XSSFFont.SS_SUB);
        font.setFamily(10);
        font.setFontHeight(14);
        font.setBold(true);
        for (int i = 0; i < HEADERS.length; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(style);
            cell.setCellValue(HEADERS[i]);
            createCell(row, i, HEADERS[i], style);
        }
    }


    public void createCell(Row row, int columnIndex, Object object, CellStyle style) {
        sheet.autoSizeColumn(columnIndex);
        Cell cell = row.createCell(columnIndex);
        if (object instanceof Integer) {
            cell.setCellValue((Integer) object);
        } else if (object instanceof String) {
            cell.setCellValue((String) object);
        } else if (object instanceof Boolean) {
            cell.setCellValue((Boolean) object);
        }
        cell.setCellStyle(style);
    }

    private void writeDataLines(List<Employee> employees) {
        Row row;
        int rowCount = 1;

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);
        for (Employee employee : employees) {
            int columnCount = 0;
            row = sheet.createRow(rowCount++);
            createCell(row, columnCount++, employee.getCode(), style);
            createCell(row, columnCount++, employee.getName(), style);
            createCell(row, columnCount++, employee.getPhoneNumber(), style);
            createCell(row, columnCount++, employee.getEmail(), style);
            createCell(row, columnCount++, employee.getAge(), style);
            createCell(row, columnCount++, employee.getProvince().getId().toString(), style);
            createCell(row, columnCount++, employee.getDistrict().getId().toString(), style);
            createCell(row, columnCount, employee.getCommune().getId().toString(), style);
        }
    }

    public Response<List<String>> importExcel(MultipartFile file) {

        List<String> errorList = new ArrayList<>();
        StatusMessage statusMessage;
        try {
            workbook = new XSSFWorkbook(file.getInputStream());

        } catch (IOException e) {
            return new Response<>(StatusMessage.CAN_N0T_ACCESS_EXCEL_FILE);
        }
        Sheet sheet = workbook.getSheet(SHEET);
        Iterator<Row> rows = sheet.iterator();

        int rowNumber = 0;
        while (rows.hasNext()) {
            Row currentRow = rows.next();
            if (rowNumber == 0) {
                rowNumber++;
                continue;
            }
            Iterator<Cell> cellsInRow = currentRow.iterator();
            EmployeeDto employeeDto = new EmployeeDto();

            int cellIdx = 0;
            ColumnEmployeeIndex columnEmployeeIndex = ColumnEmployeeIndex.values()[cellIdx];

            while (cellsInRow.hasNext()) {
                Cell currentCell = cellsInRow.next();
                try {
                    switch (columnEmployeeIndex) {
                        case CODE:
                            employeeDto.setCode(currentCell.getStringCellValue());
                            break;

                        case NAME:
                            employeeDto.setName(currentCell.getStringCellValue());
                            break;

                        case EMAIL:
                            employeeDto.setEmail(currentCell.getStringCellValue());
                            break;

                        case PHONE_NUMBER:
                            employeeDto.setPhoneNumber(currentCell.getStringCellValue());
                            break;

                        case AGE:
                            employeeDto.setAge((int) currentCell.getNumericCellValue());
                            break;

                        case PROVINCE:
                            employeeDto.getProvinceDto().setId(UUID.fromString(currentCell.getStringCellValue()));
                            break;

                        case DISTRICT:
                            employeeDto.getDistrictDto().setId(UUID.fromString(currentCell.getStringCellValue()));
                            break;

                        case COMMUNE:
                            employeeDto.getCommuneDto().setId(UUID.fromString(currentCell.getStringCellValue()));
                            break;

                        default:
                            break;
                    }
                } catch (Exception e) {
                    errorList.add("Wrong insert from row " + rowNumber + " column " + cellIdx);
                    cellIdx++;
                    continue;
                }
                cellIdx++;

            }
            statusMessage = employeeHandle.employeeValidate(employeeDto, false);
            if (StatusMessage.SUCCESS != statusMessage) {
                errorList.add("Wrong insert from row " + rowNumber + " error " + statusMessage);
            } else {
                employeeRepository.save(employeeHandle.toEntity(null, employeeDto, false));
            }
            rowNumber++;
        }
        return new Response<List<String>>(errorList, StatusMessage.SUCCESS);
    }


    public StatusMessage generateExcelFile(HttpServletResponse response) {
        writeHeaderRow();
        writeDataLines(employees);
        try {
            ServletOutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            return StatusMessage.SUCCESS;
        } catch (IOException e) {
            return StatusMessage.CAN_NOT_GENERATE_EXCEL_FILE;
        } finally {
            if (null != workbook) {
                try {
                    workbook.close();
                } catch (Exception e) {
                    return StatusMessage.CAN_NOT_GENERATE_EXCEL_FILE;
                }
            }
        }
    }


    private static void scanStaffColumnExcelIndex(Sheet datatypeSheet, int scanRowIndex) {
        Row row = datatypeSheet.getRow(scanRowIndex);
        int numberCell = row.getPhysicalNumberOfCells();

        hashColumnPropertyConfig.put("staffCode".toLowerCase(), "staffCode");
        hashColumnPropertyConfig.put("firstName".toLowerCase(), "firstName");
        hashColumnPropertyConfig.put("lastName".toLowerCase(), "lastName");
        hashColumnPropertyConfig.put("displayName".toLowerCase(), "displayName");
        hashColumnPropertyConfig.put("birthdate".toLowerCase(), "birthdate");
        hashColumnPropertyConfig.put("birthdateMale".toLowerCase(), "birthdateMale");
        hashColumnPropertyConfig.put("birthdateFeMale".toLowerCase(), "birthdateFeMale");
        hashColumnPropertyConfig.put("gender".toLowerCase(), "gender");
        hashColumnPropertyConfig.put("address".toLowerCase(), "address");// Cái này cần xem lại
        hashColumnPropertyConfig.put("userName".toLowerCase(), "userName");
        hashColumnPropertyConfig.put("password".toLowerCase(), "password");
        hashColumnPropertyConfig.put("email".toLowerCase(), "email");
        hashColumnPropertyConfig.put("BirthPlace".toLowerCase(), "BirthPlace");

        hashColumnPropertyConfig.put("departmentCode".toLowerCase(), "departmentCode");
        hashColumnPropertyConfig.put("MaNgach".toLowerCase(), "MaNgach");
        hashColumnPropertyConfig.put("IDCard".toLowerCase(), "IDCard");

        for (int i = 0; i < numberCell; i++) {
            Cell cell = row.getCell(i);
            if (cell != null && cell.getCellTypeEnum() == CellType.STRING) {
                String cellValue = cell.getStringCellValue();
                if (cellValue != null && cellValue.length() > 0) {
                    cellValue = cellValue.toLowerCase().trim();
                    String propertyName = hashColumnPropertyConfig.get(cellValue);
                    if (propertyName != null) {
                        hashStaffColumnConfig.put(propertyName, i);
                    }
                }
            }
        }
    }

    public static List<DepartmentDto> getListDepartmentFromInputStream(InputStream is) {
        try {

            List<DepartmentDto> ret = new ArrayList<DepartmentDto>();
            // FileInputStream excelFile = new FileInputStream(new File(filePath));
            // Workbook workbook = new XSSFWorkbook(excelFile);
            @SuppressWarnings("resource")
            Workbook workbook = new XSSFWorkbook(is);
            Sheet datatypeSheet = workbook.getSheetAt(0);
            // Iterator<Row> iterator = datatypeSheet.iterator();
            int rowIndex = 4;

            hashDepartmentColumnConfig.put("code", 0);

            hashDepartmentColumnConfig.put("name", 1);

            int num = datatypeSheet.getLastRowNum();
            while (rowIndex <= num) {
                Row currentRow = datatypeSheet.getRow(rowIndex);
                Cell currentCell ;
                if (currentRow != null) {
                    DepartmentDto department = new DepartmentDto();
                    Integer index = hashDepartmentColumnConfig.get("code");
                    if (index != null) {
                        currentCell = currentRow.getCell(index);// code
                        if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                            double value = currentCell.getNumericCellValue();
                            String code = numberFormatter.format(value);
                            department.setCode(code);
                        } else if (currentCell != null && currentCell.getStringCellValue() != null) {
                            String code = currentCell.getStringCellValue();
                            department.setCode(code);
                        }
                    }
                    index = hashDepartmentColumnConfig.get("name");
                    if (index != null) {
                        currentCell = currentRow.getCell(index);// name
                        if (currentCell != null && currentCell.getStringCellValue() != null) {
                            String name = currentCell.getStringCellValue();
                            department.setName(name);
                        }
                    }
                    ret.add(department);
                }
                rowIndex++;
            }
            return ret;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] agrs) {
//		try {
//			
//				FileInputStream fileIn = new FileInputStream(new File("C:\\Projects\\Globits\\Education\\globits-ecosystem\\hr\\hr-app\\Document\\DanhSachNhanSuDHTL.xlsx"));
//				List lst = getListStaffFromInputStream(fileIn);
//				System.out.println(lst.size());
//			}catch (Exception ex) {
//					ex.printStackTrace();
//			}

    }
}
