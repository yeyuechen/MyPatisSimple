package com.zz.pail.sqlSession;

import com.zz.pail.config.Function;
import com.zz.pail.config.MapperBean;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

public class PailsMapperProxy implements InvocationHandler{

    private PailsSqlsession mySqlsession;

    private PailsConfiguration myConfiguration;

    public PailsMapperProxy(PailsConfiguration myConfiguration, PailsSqlsession mySqlsession) {
        this.myConfiguration=myConfiguration;
        this.mySqlsession=mySqlsession;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        MapperBean readMapper = myConfiguration.readMapper("UserMapper.xml");
        //是否是xml文件对应的接口
        if(!method.getDeclaringClass().getName().equals(readMapper.getInterfaceName())){
            return null;
        }
        List<Function> list = readMapper.getList();
        if(null != list || 0 != list.size()){
            for (Function function : list) {
                //id是否和接口方法名一样
                if(method.getName().equals(function.getFuncName())){
                    return mySqlsession.selectOne(function.getSql(), String.valueOf(args[0]));
                }
            }
        }
        return null;
    }
}