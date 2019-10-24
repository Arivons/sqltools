package com.sql.tool.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author:liyupeng
 * @Date:2019/10/24 17:32
 */
@Slf4j
public class InsertSqlUtil {
    private  static Integer INIT_SQL_VAlue_INDEX=1;

    void contextLoads() {
        //ExcelReader reader = ExcelUtil.getReader(FileUtil.file(""));

        //通过sheet名获取
        ExcelReader reader = ExcelUtil.getReader(FileUtil.file("d://test.xlsx"), "Sheet1");
        // List<T> all = reader.readAll(T.class);
        List<List<Object>> readAll = reader.read();
        System.out.println(readAll);

    }

    /**
     * 读取excel中的数据
     * @param path
     * @param sheetName
     * @return
     */
    public static List<List<Object>> readExcel(String path,String sheetName){
        ExcelReader reader = ExcelUtil.getReader(FileUtil.file(path), sheetName);
        return reader.read();
    }

    /*
     *配置excel中列与数据库中的字段对应关系
     * 例如 excel中第1列的值设置到id中
     * 1:id
     * @param map
     * @return insert into table (fild1,fild2...) values
     */
    public static StringBuffer InsertSqlPrefix(List<String> sqlFilds, String tableName){
        if(sqlFilds.isEmpty()){
            throw new RuntimeException("sql字段找不到");
        }
        if (StringUtils.isEmpty(tableName)){
            throw new RuntimeException("数据库表名找不到");
        }
        StringBuffer sqlPrefix = new StringBuffer("insert into ");
        sqlPrefix.append(tableName);
        sqlPrefix.append(" (");
        for (String fild : sqlFilds) {
            sqlPrefix.append(fild+",");
        }
        sqlPrefix.deleteCharAt(sqlPrefix.length()-1);
        sqlPrefix.append(") values ");
        return sqlPrefix;
    }

    /**
     * 生成sql语句
     * @param sortSqlFilds
     * @param fileAbsolutePath
     * @param SheetName
     * @param tableName
     * @return
     */
    public static List<String>  generateSqlByExcelNum(List<String> sortSqlFilds,String fileAbsolutePath,String SheetName,String tableName){
        List<String> sqls = new ArrayList<>();
        StringBuffer initSqlPrefix = InsertSqlPrefix(sortSqlFilds,tableName);
        initSqlPrefix.append(" (");
        List<List<Object>> lists = readExcel(fileAbsolutePath,SheetName);
        for (List<Object> excelDatas : lists) {
            StringBuffer sqlPre = new StringBuffer(initSqlPrefix);
            while (INIT_SQL_VAlue_INDEX<excelDatas.size()){
                sqlPre.append("\""+excelDatas.get(INIT_SQL_VAlue_INDEX++)+"\""+",");
            }
            sqlPre.deleteCharAt(sqlPre.length()-1);
            sqlPre.append(");");
            INIT_SQL_VAlue_INDEX=1;
            sqls.add(sqlPre.toString());
        }
        return  sqls;
    }
    /**
     * 将list按行写入到txt文件中
     * @param strings
     * @param path
     * @throws Exception
     */
    public static void writeFileContext(List<String>  strings, String path) throws Exception {
        File file = new File(path);
        //如果没有文件就创建
        if (!file.isFile()) {
            file.createNewFile();
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter(path));
        for (String l:strings){
            writer.write(l + "\r\n");
        }
        writer.close();
    }

    /**
     * 输出
     * @param excelObj
     * @param fileAbsolutePath
     * @param SheetName
     * @param tableName
     * @param outFilePath
     * @throws Exception
     */
    public static void outSQLToFile(Object excelObj,String fileAbsolutePath,String SheetName,String tableName,String outFilePath) throws Exception {
        List<String> sqlFilds = sqlFilds(excelObj);
        List<String> sqllist = generateSqlByExcelNum(sqlFilds, fileAbsolutePath, SheetName, tableName);
        writeFileContext(sqllist,outFilePath);
    }

    /**
     * 获取对象中的属性名集合
     * @param clazz
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static List<String> sqlFilds(Object clazz) throws IllegalAccessException, InstantiationException {
        List<String> filds = new ArrayList<>();
        Field[] declaredFields = clazz.getClass().getDeclaredFields();
        List<Field> fields = Arrays.asList(declaredFields);
        for (Field field : fields) {
            field.setAccessible(true);
            filds.add(field.getName());
        }
        return filds;
    }
    public static void main(String[] args) throws InstantiationException, IllegalAccessException {
        try {
            outSQLToFile(new LawyerOfficePo(),"d://test.xlsx", "Sheet1", "user","d://testsql.text");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
