
import com.sun.deploy.util.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.util.StringUtil;
import org.dom4j.DocumentException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 读取excel
 */
public class ExcelReader1 {
    public static List<Map<Object, List<ExcelVo>>> readExcel(InputStream is, String xmlName) throws DocumentException, IOException {
        List dataList=new LinkedList();
        ExcelVo excelVo;
        Map returnMap;
        List returnList=new LinkedList();
        Workbook workbook = null;
        workbook = WorkbookFactory.create(is);
        Sheet sheet = null;
        List<Map<String, String>> xmlElements = XmlUtil.parseXml(xmlName);
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            returnMap=new LinkedHashMap();
            sheet = workbook.getSheetAt(i);
            Map<String, String> element = xmlElements.get(i);
            String sheetNumber = element.get("sheet");
            String tableName = element.get("tableName");
            Integer dateRow = Integer.valueOf(element.get("dateRow"));
            Integer startRow = Integer.valueOf(element.get("startRow"));
            Integer endRow = Integer.valueOf(element.get("endRow"));
            Integer startCol = Integer.valueOf(element.get("startCol"));
            Integer endCol = Integer.valueOf(element.get("endCol"));
            Boolean hasRowHeader = Boolean.parseBoolean(element.get("hasRowHeader"));
            excelVo=new ExcelVo();
            excelVo.setTableName(tableName);
            for(int j=startRow-1;j<endRow-1;j++)
            {
                Row row = sheet.getRow(j);
                String[] startAndEndDate= getDateString(formatCell(sheet.getRow(dateRow-1).getCell(0)),"([\\s0-9\\s])*年([\\s0-9\\s])*月([\\s0-9\\s])*日");
                 //如果从数据开始行为null或者第3列为null(这个有待考量)， 则默认这一行数据无效
                excelVo.setBeginTime(startAndEndDate[0]);
                excelVo.setEndTime(startAndEndDate[1]);
                if(row==null){
                    continue;
                }
                for(int k=startCol-1;k<endCol-1;k++)
                {
                    Cell cell=row.getCell(k);
                    excelVo.setFieldName(null);
                    System.out.print("value==="+row.getCell(k));
                    excelVo.setFieldValue(String.valueOf(Integer.valueOf(formatCell(row.getCell(k)))));
                    dataList.add(excelVo);
                }
            }
            returnMap.put(sheetNumber,dataList);
            returnList.add(returnMap);

        }
        return returnList;
    }

    /**
     * 将从excel表中读出来的数据类型进行数据转换
     * 默认为0
     */
    private static String formatCell(Cell cell) {
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case NUMERIC: {
                return (String.valueOf(cell.getNumericCellValue())).trim();
            }
            case STRING:
                return (cell.getStringCellValue()).trim();

            case BLANK:
                return "0";
        }
        return "";
    }


    private static String[] getDateString(String oneRow, String regex) {
        String[] dateString = new String[2];
        String startAndEndDate = oneRow.substring(0, oneRow.indexOf("填"));
        //匹配日期格式  年  月 日
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(oneRow);
        int i = 0;
        //遍历字符串中共几个匹配  年  月   日
        while (matcher.find() && i <=1) {
                dateString[i] = matcher.group().replaceAll(" +","");
                i++;
        }
        return dateString;
    }

    public static void main(String[] args) throws IOException, DocumentException {
        FileInputStream fileInputStream=new FileInputStream(new File("C:\\Users\\Administrator\\Desktop\\116FA000.xls"));
        ExcelReader1.readExcel(fileInputStream,"test");
    }
}
