import java.sql.Connection;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Project name(项目名称)：JDBC框架之queryForScalar方法
 * Package(包名): PACKAGE_NAME
 * Class(类名): JDBCTemplate
 * Author(作者）: mao
 * Author QQ：1296193245
 * GitHub：https://github.com/maomao124/
 * Date(创建日期)： 2022/1/23
 * Time(创建时间)： 19:14
 * Version(版本): 1.0
 * Description(描述)： 无
 */

public class JDBCTemplate
{
    /**
     * JDBC框架 update方法
     *
     * @param sql  sql更新语句,执行此PreparedStatement对象中的SQL语句，该语句必须是SQL数据操作语言(DML)语句，
     *             例如INSERT、UPDATE或DELETE；或不返回任何内容的SQL语句，例如DDL语句。
     * @param objs sql中的问号占位符，数量和顺序一定要一致
     * @return 影响行数
     */
    public static int update(String sql, Object... objs)
    {
        //影响行数,初始值为0
        int result = 0;
        //连接对象
        Connection connection = null;
        //预编译执行者对象
        PreparedStatement preparedStatement = null;
        try
        {
            //获取连接对象(Druid连接池)
            connection = Druid.getConnection();
            //或者(自定义数据库连接池)：
            //connection = connectionPool.getConnection();
            //或者直接获取(自定义JDBC工具类)：
            //connection = JDBC.getConnection();

            //预编译sql，返回执行者对象
            preparedStatement = connection.prepareStatement(sql);
            //获取参数的源信息对象
            ParameterMetaData parameterMetaData = preparedStatement.getParameterMetaData();
            //获取参数个数
            int count = parameterMetaData.getParameterCount();
            //判断参数是否一致，如果不一致，异常抛出
            if (objs.length != count)
            {
                throw new RuntimeException("update方法中参数个数不一致!");
            }
            //为问号占位符赋值
            for (int i = 0; i < count; i++)
            {
                preparedStatement.setObject(i + 1, objs[i]);
            }
            //执行sql语句
            result = preparedStatement.executeUpdate();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            //释放资源
            Druid.close(connection, preparedStatement);
            //或者：
            //JDBC.close(connection, preparedStatement);
        }
        //返回结果
        return result;
    }


    /**
     * 查询方法，此方法用于将一条记录封装成一个自定义对象并返回，如果resultSetHandler中参数传人的是Student.class,
     * 则返回的是一个Student类的对象
     *
     * @param sql              sql查询语句，建议通过主键查找，如果查询的sql语句有多条记录，则返回第一条记录
     *                         sql语句实例：select * from table where primaryKey=?
     * @param resultSetHandler ResultSetHandler<T>对象，使用：new BeanHandler<>(自定义对象.class)
     * @param objs             sql中的问号占位符，数量和顺序一定要一致
     * @param <T>              泛型，自定义对象
     * @return 返回自定义对象
     */
    public static <T> T queryForObject(String sql, ResultSetHandler<T> resultSetHandler, Object... objs)
    {
        //定义泛型变量
        T object = null;
        //连接对象
        Connection connection = null;
        //预编译执行者对象
        PreparedStatement preparedStatement = null;
        //结果集对象
        ResultSet resultSet = null;
        try
        {
            //获取连接对象(Druid连接池)
            connection = Druid.getConnection();
            //或者(自定义数据库连接池)：
            //connection = connectionPool.getConnection();
            //或者直接获取(自定义JDBC工具类)：
            //connection = JDBC.getConnection();

            //预编译sql，返回执行者对象
            preparedStatement = connection.prepareStatement(sql);
            //获取参数的源信息对象
            ParameterMetaData parameterMetaData = preparedStatement.getParameterMetaData();
            //获取参数个数
            int count = parameterMetaData.getParameterCount();
            //判断参数是否一致，如果不一致，异常抛出
            if (objs.length != count)
            {
                throw new RuntimeException("queryForObject方法中参数个数不一致!");
            }
            //为问号占位符赋值
            for (int i = 0; i < count; i++)
            {
                preparedStatement.setObject(i + 1, objs[i]);
            }
            //执行sql语句,返回结果集
            resultSet = preparedStatement.executeQuery();
            //通过beanHandler类中的handler方法对结果集进行处理
            object = resultSetHandler.handler(resultSet);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            //释放资源
            Druid.close(connection, preparedStatement, resultSet);
            //或者：
            //JDBC.close(connection, preparedStatement);
        }
        //返回结果
        return object;
    }


    /**
     * 查询方法，此方法用于将多条记录封装成一个集合对象并返回，如果resultSetHandler中参数传人的是Student.class,
     * 则返回的是一个装有Student类的对象的List集合
     *
     * @param sql              sql查询语句，sql语句实例：select * from table
     * @param resultSetHandler ResultSetHandler<T>对象，使用：new BeanListHandler<>(自定义对象.class)
     * @param objs             sql中的问号占位符，数量和顺序一定要一致
     * @param <T>              泛型，自定义对象
     * @return List集合
     */
    public static <T> List<T> queryForList(String sql, ResultSetHandler<T> resultSetHandler, Object... objs)
    {
        //定义泛型集合
        List<T> list = new ArrayList<>();
        //连接对象
        Connection connection = null;
        //预编译执行者对象
        PreparedStatement preparedStatement = null;
        //结果集对象
        ResultSet resultSet = null;
        try
        {
            //获取连接对象(Druid连接池)
            connection = Druid.getConnection();
            //或者(自定义数据库连接池)：
            //connection = connectionPool.getConnection();
            //或者直接获取(自定义JDBC工具类)：
            //connection = JDBC.getConnection();

            //预编译sql，返回执行者对象
            preparedStatement = connection.prepareStatement(sql);
            //获取参数的源信息对象
            ParameterMetaData parameterMetaData = preparedStatement.getParameterMetaData();
            //获取参数个数
            int count = parameterMetaData.getParameterCount();
            //判断参数是否一致，如果不一致，异常抛出
            if (objs.length != count)
            {
                throw new RuntimeException("queryForList方法中参数个数不一致!");
            }
            //为问号占位符赋值
            for (int i = 0; i < count; i++)
            {
                preparedStatement.setObject(i + 1, objs[i]);
            }
            //执行sql语句,返回结果集
            resultSet = preparedStatement.executeQuery();
            //通过BeanListHandler类中的handler方法对结果集进行处理
            list = resultSetHandler.handler(resultSet);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            //释放资源
            Druid.close(connection, preparedStatement, resultSet);
            //或者：
            //JDBC.close(connection, preparedStatement);
        }
        //返回结果
        return list;
    }


    /**
     * 查询方法，用于将聚合函数的查询结果进行返回，注意：结果集中应该要只有一行和一列数据(一个数据),
     * 如果有多行或者多列的数据，将始终返回第一行第一列的数据，如果第一行第一列的数据不能转换为Long型，则返回null
     *
     * @param sql              预编译的sql查询语句，sql语句实例：select count(primaryKey) from table
     * @param resultSetHandler ResultSetHandler<T>对象，使用：new ScalarHandler<>(自定义对象.class)
     * @param objs             sql中的问号占位符，数量和顺序一定要一致
     * @return Long型的对象
     */
    public static Long queryForScalar(String sql, ResultSetHandler<Long> resultSetHandler, Object... objs)
    {
        //定义Long类型的变量
        Long value = null;
        //连接对象
        Connection connection = null;
        //预编译执行者对象
        PreparedStatement preparedStatement = null;
        //结果集对象
        ResultSet resultSet = null;
        try
        {
            //获取连接对象(Druid连接池)
            connection = Druid.getConnection();
            //或者(自定义数据库连接池)：
            //connection = connectionPool.getConnection();
            //或者直接获取(自定义JDBC工具类)：
            //connection = JDBC.getConnection();

            //预编译sql，返回执行者对象
            preparedStatement = connection.prepareStatement(sql);
            //获取参数的源信息对象
            ParameterMetaData parameterMetaData = preparedStatement.getParameterMetaData();
            //获取参数个数
            int count = parameterMetaData.getParameterCount();
            //判断参数是否一致，如果不一致，异常抛出
            if (objs.length != count)
            {
                throw new RuntimeException("queryForScalar方法中参数个数不一致!");
            }
            //为问号占位符赋值
            for (int i = 0; i < count; i++)
            {
                preparedStatement.setObject(i + 1, objs[i]);
            }
            //执行sql语句,返回结果集
            resultSet = preparedStatement.executeQuery();
            //通过ScalarHandler类中的handler方法对结果集进行处理
            value = resultSetHandler.handler(resultSet);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            //释放资源
            Druid.close(connection, preparedStatement, resultSet);
            //或者：
            //JDBC.close(connection, preparedStatement);
        }
        //返回结果
        return value;
    }
}
