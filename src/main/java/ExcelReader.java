//import jdk.internal.org.xml.sax.SAXException;
//import org.apache.poi.hssf.usermodel.HSSFSheet;
//import org.apache.poi.hssf.usermodel.HSSFWorkbook;
//import org.apache.poi.poifs.filesystem.POIFSFileSystem;
//import org.w3c.dom.Document;
//
//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.DocumentBuilderFactory;
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.ArrayList;
//import java.util.List;
//
//public class ExcelReader {
//
//    private Document doc=null;
//
//    //读取Xml
//    public void  xmlGet(String fileName){
//        DocumentBuilderFactory bdf = DocumentBuilderFactory.newInstance();
//        DocumentBuilder bd = null;
//        try {
//            bd=bdf.newDocumentBuilder();
//            doc = bd.parse(this.getClass().getClassLoader().getResourceAsStream(fileName));
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }catch(Exception e){
//            e.printStackTrace();
//        }
//    }
//    //判断当前前台要导入的Excel的配置在哪个Excel中
//    public  Element checkXml(int sheet){
//        Element listEle=null;
//        NodeList lie = doc.getElementsByTagName("dataList");
//        for(int i=0;i<lie.getLength();i++){
//            Element ele = (Element)lie.item(i);
//            String xmlTb=ele.getAttribute("sheet");
//            if(sheet==Integer.parseInt(xmlTb)){
//                listEle=ele;
//            }
//        }
//        return  listEle;
//    }
//    /*//**上传Excel文件*//*
//	public String uploadExcel(MultipartFile[] files){
//		//获取文件的上传路径，如果路径文件不存在就创建
//		String[] pathStr = new String[files.length];
//		String filePath = DataXml.getInputExcelPath(request());
//		File file = new File(filePath);
//		if (!file.exists()){
//			file.mkdirs();
//		}
//		Date dat=new Date();
//		SimpleDateFormat spl=new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
//		String  nowName=spl.format(dat);
//		//将文件上传，获取到上传后的名称，然后在下面读取该文件内容
//		String uploadName = "";
//		if(files!=null && files.length>0){
//			int i = 0;
//			for(MultipartFile mfile : files){
//	        	if(!mfile.isEmpty()){
//	        		uploadName =nowName +"_"+ mfile.getOriginalFilename();
//	        		File tfile = new File(filePath + uploadName);
//	                try {
//	                    FileUtils.copyInputStreamToFile(mfile.getInputStream(), tfile);
//	                } catch (Exception e) {
//	                    e.printStackTrace();
//	                }
//	                pathStr[i] = uploadName; i++;
//	            }
//	        }
//		}
//		return uploadName;
//	}*/
//
//    //1.配置xml文件
//    //2.读取xml文件位置,
//    //2.1 根据xml配置读出关起始行以及结束行，并读取从第几列到第几列的数据
//    //3.读出那一行的数据与数据库某个字段是对应的
//
//    public String  inPutExcel(InputStream is, String type, String xmlPath) {
//        int a = 0;
//        int i =0;
//        int j = 0;
//        try{
//            //根据type找到对应的表
//            xmlGet("report-xml/"+type);
//            //String uploadName=uploadExcel(files);
//            POIFSFileSystem poifs;
//            //excel工作簿,支持97
//            HSSFWorkbook workBook=new HSSFWorkbook(is);
//            //excel中的sheet
//            HSSFSheet sheet=null;
//            List<String> allSql=new ArrayList<String>();
//            StringBuffer columns=new StringBuffer();
//            StringBuffer values=new StringBuffer();
//            for ( j = 0; j < workBook.getNumberOfSheets(); j++) {
//                sheet = workBook.getSheetAt(j);//配置文件中的sheet页名称。
//                Element ele=checkXml(j);
//                String tbName=ele.getAttribute("tbName");//配置文件中的表名
//                String dateRow=ele.getAttribute("dateRow");//配置文件中的数据行
//                String tbCol=ele.getTextContent().trim();//配置文件中字段拼成的字符串
//                String  cols[]=tbCol.split(",");		 //配置文件中字段拼成的字符串按照指定格式切割为列名和类型。
//                String start=ele.getAttribute("dataStart"),end=ele.getAttribute("dataEnd");//配置文件中的数据开始和结束行。
//                String startCol=ele.getAttribute("startCol"),endCol=ele.getAttribute("endcol");//配置文件中开始和结束列
//                String colspan=ele.getAttribute("clospan");//配置文件中，哪个位置的单元格进行了合并。
//                int s=Integer.parseInt(start),e=Integer.parseInt(end);//开始行
//                int ss=Integer.parseInt(startCol),ee=Integer.parseInt(endCol);//开始列以及结束列
//
//                //从dataRow（从0行，0列开始计算）开始获取第个单元格的值
//                //统计期间：  年     月     日  至      年     月     日   填表时间：    年    月
//                String dateString=sheet.getRow(Integer.parseInt(dateRow)).getCell(0).toString();
//                //取字符串开始然后到‘填’字位置
//                //  年     月     日  至      年     月     日
//                dateString=dateString.substring(0,dateString.indexOf("填"));
//                //匹配日期格式          年     月     日
//                Pattern pattern = Pattern.compile("([\\s0-9\\s])*年([\\s0-9\\s])*月([\\s0-9\\s])*日");
//                Matcher matcher = pattern.matcher(dateString);
//                int dateCount=0;
//                //遍历字符串中共几个匹配          年     月     日
//                while(matcher.find()){
//                    //每个匹配的去除所有空格后为年月日
//                    if("年月日".equals( matcher.group().replaceAll(" ",""))){
//                        values.append("sysdate,");
//                        values.append("sysdate,");
//                        columns.append("Begin_count,");
//                        columns.append("End_count,");
//                        break;
//                    }else{
//                        String date1=matcher.group().replaceAll(" ", "");
//                        date1=date1.replaceAll("年", "-");
//                        date1=date1.replaceAll("月", "-");
//                        date1=date1.replaceAll("日", "");
//                        values.append("to_date('"+date1+"','YYYY-MM-DD'),");
//                        if(dateCount==0){
//                            columns.append("Begin_count,");
//                            dateCount++;
//                        }else{
//                            columns.append("End_count,");
//                        }
//                    }
//                }
//
//                for(a=s;a<sheet.getPhysicalNumberOfRows();a++){
//                    HSSFRow row = sheet.getRow(a);
//                    //如果从数据开始行为null或者第3列为null(这个有待考量)， 则默认这一行数据无效
//                    if(row==null || row.getCell(2).toString().equals("")){
//                        break;
//                    }
//                    int colSpanCount=0;
//                    int sjcd=0;
//                    for(i=ss;i<ee;i++){
//                        if(!colspan.isEmpty()&& colspan.indexOf("h"+i+"h")!=-1){
//                            colSpanCount++;
//                            continue;   //如果当前单元格属于合并格,就跳过当前单元格
//                        }
//                        String colData=row.getCell(i).toString().isEmpty()?"0":row.getCell(i).toString();
//                        String col= cols[sjcd];
//                        String colType[]=col.split(":");
//                        columns.append(colType[0]+",");
//                        if("1".equals(colType[1].trim())){
//                            values.append(colData+",");
//                        }else if("2".equals(colType[1].trim())){
//                            values.append("'"+colData+"',");
//                        }else if("5".equals(colType[1].trim())){
//                            values.append(colData+",");
//                        }
//
//                        sjcd++;
//                    }
//
//                    columns.append("create_date,");
//                    values.append("sysdate,");
//                    columns.append("Create_by,");
//                    values.append("'"+getUserInfo().getUserName()+"',");
//                    columns.append("imp_status,");
//                    values.append("'IMP',");
//                    columns.append("guid");
//                    values.append("'"+StringUtil.getUUID()+"'");
//                    String sql=" insert  into  "+tbName+" ("+columns.toString()+") values("+values.toString()+")";
//                    columns=new StringBuffer();
//                    values=new StringBuffer();
//                    System.out.println(sql);
//                    allSql.add(sql);
//                }
//
//            }
//            zdxtModelService.forSqlList(allSql, null, null);
//        }catch(Exception e){
//            System.out.println("--------------------------------一条完美的开始分割线------------------------------------------");
//            System.out.println("错误出现在第"+j+" 个sheet第"+a+"行第"+i+"个单元格");
//            System.out.println("--------------------------------一条完美的结束分割线------------------------------------------");
//            e.printStackTrace();
//            return "导入失败,错误出现在第"+(j+1)+" 个sheet第"+(a+1)+"行第"+(i+1)+"个单元格";
//        }
//        return "导入成功";
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//    public static void main(String[] args) {
//        System.out.println("主程序main===我开始运行啦");
//
//    }
//}
