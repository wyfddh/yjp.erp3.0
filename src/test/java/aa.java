public class aa {
    String aa="def requestContext = ec.web.parameters\n" +
            "def entityFind=ec.entity.find(\"yjp.SKU\").condition(\"cityId\",requestContext.cityId).list()\n" +
            "ec.web.sendJsonResponse(entityFind)";
//    public static void main(String[] args){
//        JSON.parseObject()
//    }
}
