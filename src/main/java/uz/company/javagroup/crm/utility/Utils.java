package uz.isd.javagroup.grandcrm.utility;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import uz.isd.javagroup.grandcrm.entity.directories.Ticket;
import uz.isd.javagroup.grandcrm.entity.modules.*;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;
import uz.isd.javagroup.grandcrm.repository.modules.WarehouseRequestRepository;
import uz.isd.javagroup.grandcrm.services.modules.WarehouseProductService;
import uz.isd.javagroup.grandcrm.services.modules.WarehouseUserService;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;

import static uz.isd.javagroup.grandcrm.entity.modules.WarehouseProduct.Action.*;
import static uz.isd.javagroup.grandcrm.entity.modules.WarehouseRequest.DocumentStatus.READY;
import static uz.isd.javagroup.grandcrm.entity.modules.WarehouseRequest.DocumentStatus.WAITING;

public class Utils {

    public static void generateRegNumber(WarehouseRequest warehouseRequest, WarehouseRequestRepository warehouseRequestRepository) throws RecordNotFoundException {
        Long warehouseId = warehouseRequest.getFromWarehouse().getId();
        if (warehouseId == null) throw new RecordNotFoundException("Invalid id");
        Long count = warehouseRequestRepository.byFromWarehouseIdAndStatus2(WAITING, READY, warehouseId);
        if (warehouseRequest.getRegNumber() == null) {
            Calendar calendar = Calendar.getInstance();
            String number = "Re" + warehouseId + "-" + String.format("%07d", ++count) + "-" + calendar.get(Calendar.YEAR);
            warehouseRequest.setRegNumber(number);
        }
    }

    public static void generateRegNumber(WarehouseProduct warehouseProduct, WarehouseProductService warehouseProductService) throws RecordNotFoundException {
        Long warehouseId = null;
        Long count;
        WarehouseProduct.Action action = warehouseProduct.getAction();
        if (action.equals(KIRIM) || action.equals(QAYTARISH)) {
            warehouseId = warehouseProduct.getToWarehouse().getId();
            if (warehouseId == null) throw new RecordNotFoundException("Invalid id");
            count = warehouseProductService.findByToWarehouseIdAndAction(warehouseId, action);
        } else if (action.equals(KOCHIRISH) || action.equals(SPISAT) || action.equals(CHIQIM)) {
            warehouseId = warehouseProduct.getFromWarehouse().getId();
            if (warehouseId == null) throw new RecordNotFoundException("Invalid id");
            count = warehouseProductService.findByFromWarehouseIdAndAction(warehouseId, action);
        } else count = 0L;

        if (warehouseProduct.getRegNumber() == null) {
            Calendar calendar = Calendar.getInstance();
            String number = String.format("%07d", ++count) + "-" + calendar.get(Calendar.YEAR);
            if (action == KIRIM) {
                number = "In" + warehouseId + "-" + number;
            } else if (action == CHIQIM) {
                number = "Ex" + warehouseId + "-" + number;
            } else if (action == KOCHIRISH) {
                number = "Tr" + warehouseId + "-" + number;
            } else if (action == SPISAT) {
                number = "Wr" + warehouseId + "-" + number;
            } else if (action == QAYTARISH) {
                number = "Re" + warehouseId + "-" + number;
            }
            warehouseProduct.setRegNumber(number);
        }
    }

    public static void generateRegNumber(WarehouseProductDto warehouseProduct, WarehouseProductService warehouseProductService) throws RecordNotFoundException {
        Long warehouseId = null;
        Long count;
        WarehouseProduct.Action action = warehouseProduct.getAction();
        if (action.equals(KIRIM) || action.equals(QAYTARISH)) {
            warehouseId = warehouseProduct.getToWarehouseId();
            if (warehouseId == null) throw new RecordNotFoundException("Invalid id");
            count = warehouseProductService.findByToWarehouseIdAndAction(warehouseId, action);
        } else if (action.equals(KOCHIRISH) || action.equals(SPISAT) || action.equals(CHIQIM)) {
            warehouseId = warehouseProduct.getFromWarehouseId();
            if (warehouseId == null) throw new RecordNotFoundException("Invalid id");
            count = warehouseProductService.findByFromWarehouseIdAndAction(warehouseId, action);
        } else count = 0L;

        if (warehouseProduct.getRegNumber() == null) {
            Calendar calendar = Calendar.getInstance();
            String number = String.format("%07d", ++count) + "-" + calendar.get(Calendar.YEAR);
            if (action == KIRIM) {
                number = "In" + warehouseId + "-" + number;
            } else if (action == CHIQIM) {
                number = "Ex" + warehouseId + "-" + number;
            } else if (action == KOCHIRISH) {
                number = "Tr" + warehouseId + "-" + number;
            } else if (action == SPISAT) {
                number = "Wr" + warehouseId + "-" + number;
            } else if (action == QAYTARISH) {
                number = "Re" + warehouseId + "-" + number;
            }
            warehouseProduct.setRegNumber(number);
        }
    }

    public static void generateRegNumber(WarehouseExpense warehouseExpense) {
        if (warehouseExpense.getId() != null) {
            Calendar calendar = Calendar.getInstance();
            String number = String.format("%07d", warehouseExpense.getId()) + "-" + calendar.get(Calendar.YEAR);
//            if (document.getAction() == WarehouseExpense.Action.KIRIM)
            number = "WExp-" + number;
            warehouseExpense.setRegNumber(number);
        }
    }

    public static void generateRegNumber(Ticket ticket) {
        if (ticket.getId() != null) {
            Calendar calendar = Calendar.getInstance();
            String number = String.format("%05d", ticket.getId()) + "-" + calendar.get(Calendar.YEAR);
            number = "" + number;
            ticket.setRegNumber(number);
        }
    }

    public static boolean isEmpty(String value) {
        if (value == null) return true;
        return value.trim().length() == 0;
    }

    public static boolean isEmpty(Object value) {
        if (value == null) return true;
        if (value instanceof String)
            return ((String) value).trim().length() == 0;
        if (value instanceof Collection)
            return ((Collection) value).isEmpty();
        if (value instanceof Map)
            return ((Map) value).isEmpty();
        if (value instanceof Object[]) {
//        if(value.getClass().isArray()){
            return ((Object[]) value).length == 0;
        }
        return false;
    }

    public static void reverse(final Object[] array) {
        if (array == null || array.length == 0) {
            return;
        }
        Object tmp;
        for (int i = 0; i < array.length / 2; i++) {
            tmp = array[i];
            array[i] = array[array.length - i - 1];
            array[array.length - i - 1] = tmp;
        }
    }

    public List<Company> ownCompanies(Long userId, WarehouseUserService warehouseUserService) throws RecordNotFoundException {
        if (userId == null) throw new RecordNotFoundException("Please, provide User ID");
        List<Company> companies = warehouseUserService.companiesByUserId(userId);
        //TODO: companies add all children
        if (companies == null || companies.size() == 0)
            return new ArrayList<>();
        else return companies;
    }

    public static List<Warehouse> ownWarehouses(Long userId, WarehouseUserService warehouseUserService) throws RecordNotFoundException {
        if (userId == null) throw new RecordNotFoundException("Please, provide User ID");
        List<Warehouse> warehouses = warehouseUserService.warehousesByUserId(userId);
        //TODO: warehouses add all children
        if (warehouses == null || warehouses.size() == 0)
            return new ArrayList<>();
        else return warehouses;
    }

    public static List<Warehouse> ownProductions(Long userId, WarehouseUserService warehouseUserService) throws RecordNotFoundException {
        if (userId == null) throw new RecordNotFoundException("Please, provide User ID");
        List<Warehouse> warehouses = warehouseUserService.productionsByUserId(userId);
        //TODO: warehouses add all children
        if (warehouses == null || warehouses.size() == 0)
            return new ArrayList<>();
        else return warehouses;
    }

    public static List<Long> ownWarehouseIds(Long userId, WarehouseUserService warehouseUserService) throws RecordNotFoundException {
        if (userId == null) throw new RecordNotFoundException("Please, provide User ID");
        List<Long> warehouseIds = warehouseUserService.warehouseIdsByUserId(userId);
        //TODO: warehouses add all children
        if (warehouseIds == null || warehouseIds.size() == 0)
            return new ArrayList<>();
        else return warehouseIds;
    }

    public static List<Map<Object, Object>> parseExcelFile(File file) throws Throwable {
        FileInputStream inputStream = new FileInputStream(file);
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        XSSFSheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rowIterator = sheet.iterator();
        List<Object> header = new ArrayList<>();
        List<Map<Object, Object>> finish = new ArrayList<>();
        long counter = -1L;
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();
            counter++;
            Map<Object, Object> getExcel = null;
            int length = 0;
            if (counter > 0) {
                length = header.size();
                getExcel = new LinkedHashMap<>();
            }
            int sizeCounter = 0;

            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                CellType cellType = cell.getCellTypeEnum();
                switch (cellType) {
                    case _NONE:
                        if (counter == 0)
                            header.add(cell.getStringCellValue());
                        if (counter > 0)
                            getExcel.put(header.get(sizeCounter), "");
                        break;
                    case BOOLEAN:
                        if (counter == 0)
                            header.add(cell.getBooleanCellValue());
                        if (counter > 0)
                            getExcel.put(header.get(sizeCounter), cell.getBooleanCellValue());
                        break;
                    case BLANK:
                        if (counter == 0)
                            header.add(" ");
                        if (counter > 0)
                            getExcel.put(header.get(sizeCounter), " ");
                        break;
                    case FORMULA:
                        if (counter == 0)
                            header.add(cell.getCellFormula());
                        if (counter > 0)
                            getExcel.put(header.get(sizeCounter), cell.getCellFormula());
                        FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
                        if (counter == 0)
                            header.add(evaluator.evaluate(cell).getNumberValue());
                        if (counter > 0)
                            getExcel.put(header.get(sizeCounter), cell.getCellFormula());
                        break;
                    case NUMERIC:
                        if (counter == 0)
                            header.add(cell.getNumericCellValue());
                        if (counter > 0)
                            getExcel.put(header.get(sizeCounter), cell.getNumericCellValue());
                        break;
                    case STRING:
                        if (counter == 0)
                            header.add(cell.getStringCellValue());
                        if (counter > 0)
                            getExcel.put(header.get(sizeCounter), cell.getStringCellValue());
                        break;
                    case ERROR:
                        if (counter == 0)
                            header.add(cell.getStringCellValue());
                        if (counter > 0)
                            getExcel.put(header.get(sizeCounter), "!");
                        break;
                }
                if (length > sizeCounter) {
                    sizeCounter++;
                }
            }
            if (counter > 0) {
                finish.add(getExcel);
            }
        }
        inputStream.close();
        return finish;
    }
}
