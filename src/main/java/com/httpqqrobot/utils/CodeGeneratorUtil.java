package com.httpqqrobot.utils;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.util.HashMap;
import java.util.Map;

public class CodeGeneratorUtil {

    static String ip = "xxxx";
    static String username = "xxxx";
    static String password = "xxxx";
    static String author = "xxxx";
    static String tableName = "xxxx";

    public static void main(String[] args) {
        //获取代码生成器的对象
        AutoGenerator autoGenerator = new AutoGenerator();

        //设置数据库相关配置
        DataSourceConfig dataSource = new DataSourceConfig();
        dataSource.setDriverName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://" + ip + ":3306/httpqqrobot?useUnicode=true&characterEncoding=utf8&useSSL=false&allowPublicKeyRetrieval=true");
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        //赋值数据库相关配置
        autoGenerator.setDataSource(dataSource);

        //设置全局配置
        GlobalConfig globalConfig = new GlobalConfig();
        //设置生成完毕后是否打开生成代码所在的目录
        globalConfig.setOpen(false);
        //设置作者
        globalConfig.setAuthor(author);
        //设置是否覆盖原始生成的文件
        globalConfig.setFileOverride(true);
        //设置Id生成策略
        globalConfig.setIdType(IdType.ASSIGN_ID);
        //赋值全局配置
        autoGenerator.setGlobalConfig(globalConfig);

        //设置包相关配置
        PackageConfig packageConfig = new PackageConfig();
        Map<String, String> pathInfo = new HashMap<>();
        //设置生成的包名
        packageConfig.setParent("com.httpqqrobot");
        //设置controller的包生成位置
//        pathInfo.put("controller_path", System.getProperty("user.dir") + "/src/main/java/com/httpqqrobot/controller");
        //设置service的包生成位置
        pathInfo.put("service_path", System.getProperty("user.dir") + "/src/main/java/com/httpqqrobot/service");
        //设置service.impl的包生成位置
        pathInfo.put("service_impl_path", System.getProperty("user.dir") + "/src/main/java/com/httpqqrobot/service/impl");
        //设置mapper的包生成位置
        pathInfo.put("mapper_path", System.getProperty("user.dir") + "/src/main/java/com/httpqqrobot/mapper");
        //设置mapper.xml的包生成位置
        pathInfo.put("xml_path", System.getProperty("user.dir") + "/src/main/resources/mapper");
        //设置entity的包生成位置
        pathInfo.put("entity_path", System.getProperty("user.dir") + "/src/main/java/com/httpqqrobot/entity");
        //赋值包名相关配置
        packageConfig.setPathInfo(pathInfo);
        autoGenerator.setPackageInfo(packageConfig);

        //设置策略配置
        StrategyConfig strategyConfig = new StrategyConfig();
        //设置当前参与生成的表名，参数为可变参数
        strategyConfig.setInclude(tableName);
        //数据库表名的下划线转换为驼峰
        strategyConfig.setNaming(NamingStrategy.underline_to_camel);
        //数据库表的列名下划线转换为驼峰
        strategyConfig.setColumnNaming(NamingStrategy.underline_to_camel);
        //设置是否启用lombok
        strategyConfig.setEntityLombokModel(true);
        //赋值策略配置
        autoGenerator.setStrategy(strategyConfig);

        //执行生成操作
        autoGenerator.execute();
    }
}
