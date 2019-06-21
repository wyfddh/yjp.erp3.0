//package com.yjp.erp.util;
//
//import com.alibaba.fastjson.JSON;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class ScriptTemplatUtil {
//    public static void main(String [] args){
//
//        Map parameters=null;
//
//        //当前eeca 触发的实体
//        String originEntityName="";
//        //当前eeca 触发的实体明细
//        String originEntityDetailName="";
//        //目标实体的实体名
//        String destEntityName="";
//        //目标实体的明细
//        String destEntityDetailName="";
//        //映射类型 one/many
//        Integer mappingType=0;
//        //映射规则
//        List mappingRules= (List) JSON.parse("");
//        String billId="";
//
//
////        //当前eeca 触发的实体
////        def originEntityName=${originEntity}
////        //当前eeca 触发的实体明细
////        def originEntityDetailName=${originEntityDetail}
////        //目标实体的实体名
////        def destEntityName=${destEntity}
////        //目标实体的明细
////        def destEntityDetailName=${destEntityDetail}
////        //映射类型 one/many
////        def mappingType=${mappingType}
////        //映射规则
////        List mappingRules= (List) JSON.class.parse(${mappingRules})
//
//
//        if(null!=originEntityDetailName){
//            if(mappingType==0){
//                List detailList=ec.entity.find(destEntityDetailName).condition("billId",parameters.billId);
//                if(null!=detailList && detailList.size()>0){
//                    def originDetail=detailList.get(0)
//
//                    def targetDetail=ec.entity.makeValue(originEntityDetailName)
//                    for(String rule : mappingRules){
//
//                        Map ruleMap= (Map) JSON.class.parse(rule)
//
//                        //选出明细的字段 给 下推单据 的明细设置字段
//                        if(ruleMap.get("originField").contains("item.")){
//                            String fieldValue=getValueByOperator(ruleMap.get("operator"),originDetail.get(ruleMap.get("originField")))
//                            targetDetail.set(entry.key.replace("item.",fieldValue),fieldValue)
//                        }
//                    }
//
//
//                }
//                      <#--((Map.Entry<String, String>)stringMap.entrySet().toArray()[0]).getValue()-->
//            }
//        }
//
//
//        def downEntity= ec.entity.makeValue(${currentEntityName})
//        def downEntityDeatil = ec.entity.makeValue(${destEntity}")
//
//        def downFkValue
//
//        downEntity.setFields(context, true, null, null)
//
//        ${relatedMainFields}
//
//        downEntity.setSequencedIdPrimary()
//        ${fillDownFkValue}
//        downEntity.create()
//
//        def upEntityDetailList = ec.entity.find(${upEntityDetailName}).condition("billId", id).list()
//        def size = upEntityDetailList.size()
//        for (def i = 0; i < size; i++) {
//            ${relatedDetailFields}
//            downEntityDetail.setSequencedIdPrimary()
//            ${setDetailDownFkValue}
//            ${downEntityDetail.create()}
//        }
//
//        static String getValueByOperator(String operator,Object value){
//            String result
//            switch (operator){
//                case "equals":
//                    result=(String)value
//                    break;
//                case "plus":
//                    break;
//                default:
//                    println("单下推或回写中发现不支持的operator类型:"+operator)
//            }
//        }
//    }
//}
