import java.util.List;

/**
 * 从excel中读出的对象
 */
public class ExcelVo {
    /*表名*/
    private String tableName;
    /*字段名称（只读取最下面一层）*/
    private List<Object> fieldName;
    /*字段值*/
    private String fieldValue;
    /*开始时间*/
    private String beginTime;
    /*结束时间*/
    private String endTime;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<Object> getFieldName() {
        return fieldName;
    }

    public void setFieldName(List<Object> fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
